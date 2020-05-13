package cn.enilu.flash.dao.commodity;


import cn.enilu.flash.bean.entity.commodity.CommodityInventory;
import cn.enilu.flash.dao.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommodityInventoryRepository extends BaseRepository<CommodityInventory, Long> {

    @Modifying
    @Query(value = "delete from t_commodity_inventory where commodity_id = ?1", nativeQuery = true)
    void deleteByCommodityInfoId(Long id);
}

