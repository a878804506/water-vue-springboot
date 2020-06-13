package cn.enilu.flash.bean.entity.pdf;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

/**
 * @ClassName PdfManagement
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-08 21:40
 **/
@Entity(name = "t_pdf_management")
@Table(appliesTo = "t_pdf_management", comment = "pdf解析管理")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PdfManagement extends BaseEntity {

    @Column(name = "name", columnDefinition = "VARCHAR(100) COMMENT '名称'")
    private String name;

    @Column(name = "old_pdf", columnDefinition = "VARCHAR(100) COMMENT '原始pdf'")
    private String oldPdf;

    @Column(name = "old_excel", columnDefinition = "VARCHAR(100) COMMENT '原始excel'")
    private String oldExcel;

    @Column(name = "old_pdf_start_page", columnDefinition = "int COMMENT 'pdf解析开始页'")
    private Integer oldPdfStartPage;

    @Column(name = "old_pdf_end_page", columnDefinition = "int COMMENT 'pdf解析结束页'")
    private Integer oldPdfEndPage;

    @Column(name = "old_excel_start_row", columnDefinition = "int COMMENT 'excel解析开始行'")
    private Integer oldExcelStartRow;

    @Column(name = "old_excel_start_column", columnDefinition = "int COMMENT 'excel解析开始列'")
    private Integer oldExcelStartColumn;

    @Column(name = "create_pdf", columnDefinition = "VARCHAR(100) COMMENT '生成的文件'")
    private String createPdf;

    @Column(name = "create_pdf_count", columnDefinition = "int COMMENT '生成pdf的数量'")
    private Integer createPdfCount;

    @Column(name = "pdf_status", columnDefinition = "int COMMENT '状态'")
    private Integer pdfStatus;

    @Column(name = "fail_reason", columnDefinition = "VARCHAR(100)  COMMENT '失败原因'")
    private String failReason;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PdfManagement that = (PdfManagement) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (oldPdf != null ? !oldPdf.equals(that.oldPdf) : that.oldPdf != null) return false;
        if (oldExcel != null ? !oldExcel.equals(that.oldExcel) : that.oldExcel != null) return false;
        if (oldPdfStartPage != null ? !oldPdfStartPage.equals(that.oldPdfStartPage) : that.oldPdfStartPage != null)
            return false;
        if (oldPdfEndPage != null ? !oldPdfEndPage.equals(that.oldPdfEndPage) : that.oldPdfEndPage != null)
            return false;
        if (oldExcelStartRow != null ? !oldExcelStartRow.equals(that.oldExcelStartRow) : that.oldExcelStartRow != null)
            return false;
        if (oldExcelStartColumn != null ? !oldExcelStartColumn.equals(that.oldExcelStartColumn) : that.oldExcelStartColumn != null)
            return false;
        if (createPdf != null ? !createPdf.equals(that.createPdf) : that.createPdf != null)
            return false;
        if (createPdfCount != null ? !createPdfCount.equals(that.createPdfCount) : that.createPdfCount != null)
            return false;
        if (pdfStatus != null ? !pdfStatus.equals(that.pdfStatus) : that.pdfStatus != null) return false;
        if (failReason != null ? !failReason.equals(that.failReason) : that.failReason != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 31 * 1 + (name != null ? name.hashCode() : 0);
        result = 31 * result + (oldPdf != null ? oldPdf.hashCode() : 0);
        result = 31 * result + (oldExcel != null ? oldExcel.hashCode() : 0);
        result = 31 * result + (oldPdfStartPage != null ? oldPdfStartPage.hashCode() : 0);
        result = 31 * result + (oldPdfEndPage != null ? oldPdfEndPage.hashCode() : 0);
        result = 31 * result + (oldExcelStartRow != null ? oldExcelStartRow.hashCode() : 0);
        result = 31 * result + (oldExcelStartColumn != null ? oldExcelStartColumn.hashCode() : 0);
        result = 31 * result + (createPdf != null ? createPdf.hashCode() : 0);
        result = 31 * result + (createPdfCount != null ? createPdfCount.hashCode() : 0);
        result = 31 * result + (pdfStatus != null ? pdfStatus.hashCode() : 0);
        result = 31 * result + (failReason != null ? failReason.hashCode() : 0);
        return result;
    }
}
