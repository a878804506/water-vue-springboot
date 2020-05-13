package cn.enilu.flash.bean.entity.commodity;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @ClassName CommodityInOutLog
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-05-13 17:07
 **/
@Entity(name = "t_commodity_in_out_log")
@Table(appliesTo = "t_commodity_in_out_log", comment = "出入库记录")
@Data
public class CommodityInOutLog extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "commodity_id", referencedColumnName = "id")
    private CommodityInfo commodityInfo;

    @Column(name = "commodity_in_out_type", columnDefinition = "int COMMENT '商品出入库类型'")
    private Integer commodityInOutType;

    @Column(name = "commodity_in_out_num", columnDefinition = "int COMMENT '商品出入库数量'")
    private Integer commodityInOutNum;

    @Column(name = "commodity_trade_price", columnDefinition = "decimal COMMENT '商品出入库时批发价'")
    private Double commodityTradePrice;

    @Column(name = "commodity_sales_price", columnDefinition = "decimal COMMENT '商品出入库时销售价'")
    private Double commoditySalesPrice;

    @Column(name = "commodity_remarks", columnDefinition = "VARCHAR(255) COMMENT '商品出入库备注'")
    private String commodityRemarks;
}
