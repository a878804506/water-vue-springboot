package cn.enilu.flash.bean.entity.music;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName MusicFavoriteMappingPK
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-07-09 15:34
 **/
public class MusicFavoriteMappingPK implements Serializable {

    private Long favoriteId;
    private String musicStationId;

    @Column(name = "favorite_id")
    @Id
    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    @Column(name = "music_station_id")
    @Id
    public String getMusicStationId() {
        return musicStationId;
    }

    public void setMusicStationId(String musicStationId) {
        this.musicStationId = musicStationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicFavoriteMappingPK that = (MusicFavoriteMappingPK) o;

        if (favoriteId != that.favoriteId) return false;
        if (musicStationId != null ? !musicStationId.equals(that.musicStationId) : that.musicStationId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = favoriteId.intValue();
        result = 31 * result + (musicStationId != null ? musicStationId.hashCode() : 0);
        return result;
    }
}
