package cn.enilu.flash.bean.entity.system;

import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * @ClassName SysUrl
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-07-27 17:48
 **/
@Entity(name = "t_sys_url")
@Table(appliesTo = "t_sys_url",comment = "url转义表")
@Data
public class SysUrl {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "symbol", columnDefinition = "符号'")
    private String symbol;
    @Column(name = "transformation", columnDefinition = "转义字符串'")
    private String transformation;
    @Column(name = "enabled", columnDefinition = "是否可用'")
    private Integer enabled;
    @Column(name = "remark", columnDefinition = "备注'")
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysUrl sysUrl = (SysUrl) o;
        if (id != sysUrl.id) return false;
        if (symbol != null ? !symbol.equals(sysUrl.symbol) : sysUrl.symbol != null) return false;
        if (transformation != null ? !transformation.equals(sysUrl.transformation) : sysUrl.transformation != null)
            return false;
        if (enabled != null ? !enabled.equals(sysUrl.enabled) : sysUrl.enabled != null) return false;
        if (remark != null ? !remark.equals(sysUrl.remark) : sysUrl.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        result = 31 * result + (transformation != null ? transformation.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
