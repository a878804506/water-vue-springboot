package cn.enilu.flash.service.task.job;

import cn.enilu.flash.bean.entity.pdf.PdfManagement;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.pdf.PdfManagementService;
import cn.enilu.flash.service.task.JobExecuter;
import cn.enilu.flash.utils.ExcelUtil;
import cn.enilu.flash.utils.ZipUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 解析pdf
 *
 * @ClassName AnalysisPdfJob
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-11 11:27
 **/
@Component
public class AnalysisPdfJob extends JobExecuter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PdfManagementService pdfManagementService;

    @Value("${file.base.path}")
    private String fileBasePath;

    @Override
    public void execute(Map<String, Object> dataMap) throws Exception {

        // 查找状态为待运行的pdf解析任务
        SearchFilter condition = SearchFilter.build("pdfStatus", SearchFilter.Operator.EQ, 3);
        List<PdfManagement> pdfManagementList = pdfManagementService.queryAll(condition);

        if (null != pdfManagementList && pdfManagementList.size() != 0) {
            for (PdfManagement pdfManagement : pdfManagementList) {
                AnalysisPdfTask analysisPdfTask = new AnalysisPdfTask(pdfManagement);
                Thread pdfTask = new Thread(analysisPdfTask);
                pdfTask.start();
            }
        } else {
            logger.info("AnalysisPdfJob : 没有需要解析pdf的任务");
        }
    }

    class AnalysisPdfTask implements Runnable {

        private PdfManagement pdfManagement;

        public AnalysisPdfTask(PdfManagement pdfManagement) {
            this.pdfManagement = pdfManagement;
        }

        @Override
        public void run() {
            pdfManagement.setPdfStatus(2);
            pdfManagementService.update(pdfManagement);

            File pdfFile = new File(fileBasePath + pdfManagement.getOldPdf());
            File excelFile = new File(fileBasePath + pdfManagement.getOldExcel());
            if (!pdfFile.exists() || !excelFile.exists()) {
                pdfManagement.setPdfStatus(0);
                pdfManagement.setFailReason("没有找到文件！");
                pdfManagementService.update(pdfManagement);
                return;
            }

            // 创建文件夹
            String pdfsPath = fileBasePath + pdfManagement.getId();
            File tempFile = new File(pdfsPath);
            try {
                FileUtils.moveFileToDirectory(pdfFile, tempFile, true);
                FileUtils.moveFileToDirectory(excelFile, tempFile, true);
            } catch (IOException e) {
                logger.error("文件移动失败");
                return;
            }

            //解析excel  批量命名
            Map<Integer, String> columns = ExcelUtil.readExcel(pdfsPath + File.separator + pdfManagement.getOldExcel(),
                    pdfManagement.getOldExcelStartColumn());
            if (columns.size() == 0) {
                pdfManagement.setPdfStatus(0);
                pdfManagement.setFailReason("没有读取到Excel指定行和列的数据！");
                pdfManagementService.update(pdfManagement);
                return;
            }
            // pdf拆分,并重命名
            List<File> srcFiles = splitPDF(pdfsPath, columns);
            // 在吧生成的pdfs打包 zip
            // 逻辑在此
            String zipFilePath = pdfsPath + File.separator + pdfManagement.getName() + ".zip";
            ZipUtil.toZip(srcFiles, zipFilePath);
            //更新任务状态
            pdfManagement.setPdfStatus(1);
            pdfManagement.setCreatePdf(pdfManagement.getName() + ".zip");
            pdfManagement.setCreatePdfCount(srcFiles.size());
            pdfManagementService.update(pdfManagement);
        }

        /**
         * 大的pdf拆分
         *
         * @param pdfsPath 生成的单页pdf存放位置
         * @return 返回生成的pdf的数量
         */
        public List<File> splitPDF(String pdfsPath, Map<Integer, String> columns) {
            //生成的pdf文件列表
            List<File> createPdfFIleList = new ArrayList<>();

            Document document = new Document();
            try {
                PdfReader inputPDF = new PdfReader(new FileInputStream(pdfsPath + File.separator + pdfManagement.getOldPdf()));
                int totalPages = inputPDF.getNumberOfPages();

                if (totalPages > 0) {
                    Integer startPage = pdfManagement.getOldPdfStartPage(); //用户设置的开始解析的页码
                    Integer endPage = pdfManagement.getOldPdfEndPage(); //用户设置的结束解析的页码
                    if (endPage == null) {
                        endPage = totalPages;
                        pdfManagement.setOldPdfEndPage(endPage);
                    } else if (endPage > totalPages) {
                        endPage = totalPages;
                        pdfManagement.setOldPdfEndPage(endPage);
                    }
                    // 循环拆分成每页一个pdf
                    Integer oldExcelStartRow = pdfManagement.getOldExcelStartRow();
                    for (int p = startPage; p <= endPage; p++) {
                        // Create a writer for the outputstream
                        int readRow = oldExcelStartRow + p - 1;
                        String newPdfName = StringUtils.isNotBlank(columns.get(readRow)) ? columns.get(readRow) : p + "";
                        OutputStream outputStream = new FileOutputStream(pdfsPath + File.separator + newPdfName + ".pdf");
                        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
                        document.open();
                        PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data
                        PdfImportedPage page;
                        document.newPage();
                        page = writer.getImportedPage(inputPDF, p);
                        cb.addTemplate(page, 0, 0);
                        outputStream.flush();
                        document.close();
                        outputStream.close();

                        File pdfFile = new File(pdfsPath + File.separator + newPdfName + ".pdf");
                        createPdfFIleList.add(pdfFile);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (document.isOpen())
                    document.close();
            }
            return createPdfFIleList;
        }
    }
}
