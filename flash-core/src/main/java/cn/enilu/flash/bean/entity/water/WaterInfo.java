package cn.enilu.flash.bean.entity.water;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName WaterWaterinfo 客户月水费详情
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-01-12 21:43
 **/
@Entity
@Table(name = "t_water_waterinfo")
@Data
@IdClass(WaterInfoPK.class)
/*
开启审计功能 by 陈韵辉 20191230
用于自动插入创建人创建时间 修改人 修改时间
 */
@EntityListeners(AuditingEntityListener.class)
public class WaterInfo {
    @Id
    @Column(name = "cid")
    private int cid;
    @Column(name = "cname")
    private String cname;
    @Column(name = "year")
    private int year;
    @Column(name = "month")
    private int month;
    @Column(name = "[count]")
    private double count;
    @Column(name = "cost")
    private double cost;

    // 1 普通开票，2 包月，默认为1
    @Column(name = "type")
    private int type;

    // 是否已经作废 1是，0否，默认0
    @Column(name = "cancel")
    private int cancel;

    @Column(name = "reason")
    private String reason;

    @Id
    @Column(name = "remark")
    private String remark;

    @CreatedDate
    @Column(name = "create_time",columnDefinition="DATETIME COMMENT '创建时间/注册时间'",updatable = false)
    private Date createTime;
    @Column(name = "create_by",columnDefinition="bigint COMMENT '创建人'",updatable = false)
    @CreatedBy
    private Long createBy;
    @LastModifiedDate
    @Column(name = "modify_time",columnDefinition="DATETIME COMMENT '最后更新时间'")
    private Date modifyTime;
    @LastModifiedBy
    @Column(name = "modify_by",columnDefinition="bigint COMMENT '最后更新人'")
    private Long modifyBy;

    @Transient
    private String modifyName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaterInfo that = (WaterInfo) o;

        if (cid != that.cid) return false;
        if (year != that.year) return false;
        if (month != that.month) return false;
        if (Double.compare(that.count, count) != 0) return false;
        if (Double.compare(that.cost, cost) != 0) return false;
        if (cname != null ? !cname.equals(that.cname) : that.cname != null) return false;
        if (createBy != null ? !createBy.equals(that.createBy) : that.createBy != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (modifyBy != null ? !modifyBy.equals(that.modifyBy) : that.modifyBy != null) return false;
        if (modifyTime != null ? !modifyTime.equals(that.modifyTime) : that.modifyTime != null) return false;

        if (type != that.type ) return false;
        if (cancel != that.cancel ) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = cid;
        result = 31 * result + (cname != null ? cname.hashCode() : 0);
        result = 31 * result + year;
        result = 31 * result + month;
        temp = Double.doubleToLongBits(count);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (modifyBy != null ? modifyBy.hashCode() : 0);
        result = 31 * result + (modifyTime != null ? modifyTime.hashCode() : 0);

        result = 31 * result + type;
        result = 31 * result + cancel;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }
}
