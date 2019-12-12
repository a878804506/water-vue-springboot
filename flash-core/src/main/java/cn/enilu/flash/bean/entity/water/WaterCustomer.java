package cn.enilu.flash.bean.entity.water;

import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 客户信息表
 */
@Data
@Entity(name="t_water_customer")
@Table(appliesTo = "t_water_customer",comment = "客户信息表")
public class WaterCustomer {
    @Column(name="customer_id",columnDefinition = "INT COMMENT '客户编号'")
    private Integer customerId;
    @Column(name="name",columnDefinition = "VARCHAR(20) COMMENT '客户姓名'")
    private String name;
    @Column(name="price",columnDefinition = "double COMMENT '水费定价'")
    private Double price;
    @Column(name="address",columnDefinition = "VARCHAR(50) COMMENT '客户住址'")
    private String address;
    @Column(name="starttime",columnDefinition = "VARCHAR(50) COMMENT '开户时间'")
    private String starttime;
    @Column(name="status",columnDefinition = "INT COMMENT '用户状态（1：正常、0：报停）'")
    private Integer status;
    @Column(name="delete",columnDefinition = "INT COMMENT '是否可用（1：正常、0：已删除）'")
    private Integer delete;
}
