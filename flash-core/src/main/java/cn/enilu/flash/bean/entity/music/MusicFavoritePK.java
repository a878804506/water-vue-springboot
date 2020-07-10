package cn.enilu.flash.bean.entity.music;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName MusicFavoritePK
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-07-09 11:21
 **/
public class MusicFavoritePK implements Serializable {
    private Long id;
    private Long favoriteUserId;

    @Column(name = "id")
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "favorite_user_id")
    @Id
    public Long getFavoriteUserId() {
        return favoriteUserId;
    }

    public void setFavoriteUserId(Long favoriteUserId) {
        this.favoriteUserId = favoriteUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicFavoritePK that = (MusicFavoritePK) o;

        if (id != that.id) return false;
        if (favoriteUserId != null ? !favoriteUserId.equals(that.favoriteUserId) : that.favoriteUserId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (favoriteUserId != null ? favoriteUserId.hashCode() : 0);
        return result;
    }
}
