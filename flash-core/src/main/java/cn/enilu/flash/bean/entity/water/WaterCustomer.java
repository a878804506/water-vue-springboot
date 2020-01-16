package cn.enilu.flash.bean.entity.water;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 客户信息表
 */
@Entity(name="t_water_customer")
@Table(appliesTo = "t_water_customer",comment = "客户信息表")
@Data
public class WaterCustomer extends BaseEntity {
    @Column(name="name",columnDefinition = "VARCHAR(20) COMMENT '客户姓名'")
    private String name;
    @Column(name="price",columnDefinition = "double COMMENT '水费定价(￥)'")
    private Double price;
    @Column(name="address",columnDefinition = "VARCHAR(50) COMMENT '客户住址'")
    private String address;
    @Column(name="starttime",columnDefinition = "VARCHAR(50) COMMENT '开户时间'")
    private String starttime;
    @Column(name="status",columnDefinition = "INT COMMENT '客户状态（1：正常、0：报停）'")
    private Integer status;

}
