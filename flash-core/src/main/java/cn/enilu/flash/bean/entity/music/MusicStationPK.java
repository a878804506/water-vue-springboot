package cn.enilu.flash.bean.entity.music;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName MusicStationPK
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-02 15:36
 **/
@Data
public class MusicStationPK implements Serializable {
    @Column(name = "id")
    @Id
    private String id;

    @Column(name = "platformId")
    @Id
    private int platformId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicStationPK that = (MusicStationPK) o;

        if (platformId != that.platformId) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + platformId;
        return result;
    }
}
