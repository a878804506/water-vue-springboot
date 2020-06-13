package cn.enilu.flash.service.pdf;


import cn.enilu.flash.bean.entity.pdf.PdfManagement;
import cn.enilu.flash.dao.pdf.PdfManagementRepository;

import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PdfManagementService extends BaseService<PdfManagement,Long,PdfManagementRepository>  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PdfManagementRepository pdfManagementRepository;

}

