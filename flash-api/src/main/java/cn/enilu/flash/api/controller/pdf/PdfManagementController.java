package cn.enilu.flash.api.controller.pdf;

import cn.enilu.flash.bean.entity.pdf.PdfManagement;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.service.pdf.PdfManagementService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.utils.factory.Page;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

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
    @RequiresPermissions(value = {Permission.PDF_SAVE})
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

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @BussinessLog(value = "文件下载", key = "name", dict = CommonDict.class)
    @RequiresPermissions(value = {Permission.PDF_DOWNLOAD})
    public void download(@RequestParam Long id, HttpServletResponse response) {
        if (null == id) {
            return;
        }
        PdfManagement pdfManagement = pdfManagementService.get(id);
        if (null == pdfManagement) {
            return;
        }
        if (pdfManagement.getPdfStatus() != 1) {
            return;
        }
        OutputStream os = null;
        InputStream is = null;
        try {
            response.setCharacterEncoding("UTF-8");
            // 第一步：设置响应类型
            response.setContentType("application/force-download");// 应用程序强制下载
            // 第二读取文件
            InputStream in = new FileInputStream(fileBasePath + pdfManagement.getId() + File.separator + pdfManagement.getCreatePdf());
            // 设置响应头，对文件进行url编码
            String pdfFileName = URLEncoder.encode(pdfManagement.getCreatePdf(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + pdfFileName);
            response.setContentLength(in.available());
            // 第三步：老套路，开始copy
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (IOException e) {
                logger.error(ExceptionUtils.getFullStackTrace(e));
            }
        }
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