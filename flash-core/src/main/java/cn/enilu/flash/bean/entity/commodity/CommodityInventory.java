package cn.enilu.flash.bean.entity.commodity;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @ClassName CommodityInventory
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-05-12 19:20
 **/
@Entity(name="t_commodity_inventory")
@Table(appliesTo = "t_commodity_inventory",comment = "商品库存")
@Data
public class CommodityInventory extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "commodity_id" , referencedColumnName = "id")
    private CommodityInfo commodityInfo;

    @Column(name="commodity_num",columnDefinition = "int COMMENT '商品库存量'")
    private Integer commodityNum;

    @Column(name="commodity_version",columnDefinition = "int COMMENT '版本号'")
    private Integer commodityVersion;
}
