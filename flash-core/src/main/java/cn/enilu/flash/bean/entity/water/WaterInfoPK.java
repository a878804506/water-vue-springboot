package cn.enilu.flash.bean.entity.water;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName TWaterWaterinfoPK
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-01-12 21:43
 **/
@Data
public class WaterInfoPK implements Serializable {
    @Column(name = "cid")
    @Id
    private int cid;
    @Column(name = "year")
    @Id
    private int year;
    @Column(name = "month")
    @Id
    private int month;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaterInfoPK that = (WaterInfoPK) o;

        if (cid != that.cid) return false;
        if (year != that.year) return false;
        if (month != that.month) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cid;
        result = 31 * result + year;
        result = 31 * result + month;
        return result;
    }
}
