package cn.enilu.flash.bean.entity.water;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 客户起止码表
 */
@Data
@Entity(name="t_water_watermeter")
@Table(appliesTo = "t_water_watermeter",comment = "客户起止码表")
public class WaterMeter extends BaseEntity {
    @Column(name="cid",columnDefinition = "INT COMMENT '客户编号'")
    private Integer cid;
    @Column(name="cname",columnDefinition = "VARCHAR(20) COMMENT '客户姓名'")
    private String cname;
    @Column(name="one",columnDefinition = "DOUBLE COMMENT '一月'")
    private Integer one;
    @Column(name="two",columnDefinition = "DOUBLE COMMENT '二月'")
    private Integer two;
    @Column(name="three",columnDefinition = "DOUBLE COMMENT '三月'")
    private Integer three;
    @Column(name="four",columnDefinition = "DOUBLE COMMENT '四月'")
    private Integer four;
    @Column(name="five",columnDefinition = "DOUBLE COMMENT '五月'")
    private Integer five;
    @Column(name="six",columnDefinition = "DOUBLE COMMENT '六月'")
    private Integer six;
    @Column(name="seven",columnDefinition = "DOUBLE COMMENT '七月'")
    private Integer seven;
    @Column(name="eight",columnDefinition = "DOUBLE COMMENT '八月'")
    private Integer eight;
    @Column(name="nine",columnDefinition = "DOUBLE COMMENT '九月'")
    private Integer nine;
    @Column(name="ten",columnDefinition = "DOUBLE COMMENT '十月'")
    private Integer ten;
    @Column(name="eleven",columnDefinition = "DOUBLE COMMENT '十一月'")
    private Integer eleven;
    @Column(name="twelve",columnDefinition = "DOUBLE COMMENT '十二月'")
    private Integer twelve;
}
