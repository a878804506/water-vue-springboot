package cn.enilu.flash.bean.entity.water;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.*;

/**
 * 客户起止码表
 */
@Data
@Entity(name = "t_water_watermeter")
@Table(appliesTo = "t_water_watermeter", comment = "客户起止码表")
public class WaterMeter extends BaseEntity {
    public WaterMeter() {
    }

    public WaterMeter(Integer cid) {
        this.cid = cid;
        this.one = 0d;
        this.two = 0d;
        this.three = 0d;
        this.four = 0d;
        this.five = 0d;
        this.six = 0d;
        this.seven = 0d;
        this.eight = 0d;
        this.nine = 0d;
        this.ten = 0d;
        this.eleven = 0d;
        this.twelve = 0d;
        this.thirteen = 0d;
    }

    /**
     * cascade 表的级联操作
     * referencedColumnName 参考列名，默认的情况下hi列表的主键
     * insertable 是否可以插入
     * updateable 是否可以更新
     * columnDefinition 列定义
     * foreignKey 外键
     */
    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name="cid",referencedColumnName = "id",insertable = false , updatable = false)
    private WaterCustomer waterCustomer;

    @Column(name = "cid", columnDefinition = "INT COMMENT '客户编号'")
    private Integer cid;
    @Column(name = "one", columnDefinition = "DOUBLE COMMENT '一月'")
    private Double one;
    @Column(name = "two", columnDefinition = "DOUBLE COMMENT '二月'")
    private Double two;
    @Column(name = "three", columnDefinition = "DOUBLE COMMENT '三月'")
    private Double three;
    @Column(name = "four", columnDefinition = "DOUBLE COMMENT '四月'")
    private Double four;
    @Column(name = "five", columnDefinition = "DOUBLE COMMENT '五月'")
    private Double five;
    @Column(name = "six", columnDefinition = "DOUBLE COMMENT '六月'")
    private Double six;
    @Column(name = "seven", columnDefinition = "DOUBLE COMMENT '七月'")
    private Double seven;
    @Column(name = "eight", columnDefinition = "DOUBLE COMMENT '八月'")
    private Double eight;
    @Column(name = "nine", columnDefinition = "DOUBLE COMMENT '九月'")
    private Double nine;
    @Column(name = "ten", columnDefinition = "DOUBLE COMMENT '十月'")
    private Double ten;
    @Column(name = "eleven", columnDefinition = "DOUBLE COMMENT '十一月'")
    private Double eleven;
    @Column(name = "twelve", columnDefinition = "DOUBLE COMMENT '十二月'")
    private Double twelve;
    /**
     * 里面的值还是12月份的止码，在表初始化的时候，会将twelve字段重置为0，然后将12月份的值存于此
     */
    @Column(name = "thirteen", columnDefinition = "DOUBLE COMMENT '十二月'")
    private Double thirteen;

}
