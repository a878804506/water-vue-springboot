package cn.enilu.flash.bean.entity.commodity;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 商品信息表
 * @ClassName CommodityInfo
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-05-12 18:04
 **/
@Entity(name="t_commodity_info")
@Table(appliesTo = "t_commodity_info",comment = "商品信息表")
@Data
public class CommodityInfo extends BaseEntity {

    @Column(name="commodity_number",columnDefinition = "VARCHAR(50) COMMENT '商品编码'")
    private String commodityNumber;

    @Column(name="commodity_name",columnDefinition = "VARCHAR(50) COMMENT '商品名称'")
    private String commodityName;

    @Column(name="commodity_origin",columnDefinition = "VARCHAR(50) COMMENT '商品产地'")
    private String commodityOrigin;

    @Column(name="commodity_type",columnDefinition = "int COMMENT '商品分类'")
    private String commodityType;

    @Column(name="commodity_trade_price",columnDefinition = "decimal COMMENT '批发价'")
    private Double commodityTradePrice;

    @Column(name="commodity_sales_price",columnDefinition = "decimal COMMENT '销售价'")
    private Double commoditySalesPrice;
}
