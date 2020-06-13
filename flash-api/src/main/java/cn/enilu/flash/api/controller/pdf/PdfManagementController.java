package cn.enilu.flash.api.controller.pdf;

import cn.enilu.flash.bean.entity.pdf.PdfManagement;
import cn.enilu.flash.service.pdf.PdfManagementService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.utils.factory.Page;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/pdf/management")
public class PdfManagementController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PdfManagementService pdfManagementService;

    @Value("${file.base.path}")
    private String fileBasePath;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list() {
        Page<PdfManagement> page = new PageFactory<PdfManagement>().defaultPage();
        page = pdfManagementService.queryPage(page);
        return Rets.success(page);
    }

    @RequestMapping(value = "/updaloadFile", method = RequestMethod.POST)
    @BussinessLog(value = "上传文件", key = "name", dict = CommonDict.class)
    public Object updaloadFile(@RequestParam("file") MultipartFile file) {
        if (null != file) {
            try {
                File sourceFile = new File(fileBasePath);
                if (!sourceFile.exists()) {
                    sourceFile.mkdirs();
                }
                String filename = file.getOriginalFilename();
                File uploadFile = new File(fileBasePath + filename);
                FileUtils.writeByteArrayToFile(uploadFile, file.getBytes());
                return Rets.success(filename + "上传成功");
            } catch (Exception e) {
                logger.error(e.getMessage());
                return Rets.failure("文件上传失败");
            }
        }
        return Rets.failure("操作失败");
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "保存pdf解析的信息", key = "name", dict = CommonDict.class)
    public Object save(@ModelAttribute PdfManagement pdfManagement) {
        File pdfFile = new File(fileBasePath + pdfManagement.getOldPdf());
        File excelFile = new File(fileBasePath + pdfManagement.getOldExcel());
        if (!pdfFile.exists()) {
            return Rets.failure(pdfManagement.getOldPdf() + "未发现文件,请重新添加");
        }
        if (!excelFile.exists()) {
            return Rets.failure(pdfManagement.getOldExcel() + "未发现文件,请重新添加");
        }
        if (null != pdfManagement.getOldPdfEndPage()) {
            if (pdfManagement.getOldPdfStartPage() > pdfManagement.getOldPdfEndPage()) {
                return Rets.failure("pdf起止页码逻辑错误！");
            }
        }

        if (pdfManagement.getId() == null) {
            // 待运行
            pdfManagement.setPdfStatus(3);
            pdfManagementService.insert(pdfManagement);

            return Rets.success("添加成功");
        }
        return Rets.failure("添加失败");
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @BussinessLog(value = "文件下载", key = "name", dict = CommonDict.class)
    public void download(@RequestParam Long id, HttpServletResponse response) {
        if (null == id) {
//            return Rets.failure("参数非法");
            return;
        }
        PdfManagement pdfManagement = pdfManagementService.get(id);
        if (null == pdfManagement) {
//            return Rets.failure("数据非法");
            return;
        }
        if(pdfManagement.getPdfStatus() != 1){
//            return Rets.failure("文件下载失败");
            return;
        }
        OutputStream os = null;
        InputStream is = null;
        try {
            //取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfManagement.getCreatePdf().getBytes("utf-8"), "iso-8859-1"));
            //读取流
            File f = new File(fileBasePath + pdfManagement.getId() + File.separator + pdfManagement.getCreatePdf());
            is = new FileInputStream(f);
            //复制
            byte[] buff = new byte[1024];
            int i = is.read(buff);
            while(i != -1){
                os.write(buff,0,buff.length);
                os.flush();
                i = is.read(buff);
            }

//            IOUtils.copy(is, response.getOutputStream());
//            response.getOutputStream().flush();
        } catch (IOException e) {
//            return Rets.failure("文件下载失败！" + e.getMessage());
            e.printStackTrace();
            return;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error(ExceptionUtils.getFullStackTrace(e));
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error(ExceptionUtils.getFullStackTrace(e));
            }
        }
//        return Rets.success(pdfManagement.getCreatePdf() + " 文件开始下载");
    }


	/*@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除pdf解析管理", key = "id",dict= CommonDict.class)
	public Object remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		pdfManagementService.delete(id);
		return Rets.success();
	}*/
}