package cn.enilu.flash.bean.entity.music;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName MusicSyncPK
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-05 15:02
 **/
@Data
public class MusicSyncPK implements Serializable {
    @Column(name = "platform")
    @Id
    private String platform;
    @Column(name = "name")
    @Id
    private String name;
    @Column(name = "singers")
    @Id
    private String singers;
    @Column(name = "has_hq")
    @Id
    private Boolean hasHQ;
    @Column(name = "has_sq")
    @Id
    private Boolean hasSQ;
    @Column(name = "has_mv")
    @Id
    private Boolean hasMV;
    @Column(name = "has_album")
    @Id
    private Boolean hasAlbum;
    @Column(name = "album_id")
    @Id
    private String albumId;
    @Column(name = "album_name")
    @Id
    private String albumName;
    @Column(name = "sync_type")
    @Id
    private String syncType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicSyncPK that = (MusicSyncPK) o;

        if (hasHQ != that.hasHQ) return false;
        if (hasSQ != that.hasSQ) return false;
        if (hasMV != that.hasMV) return false;
        if (hasAlbum != that.hasAlbum) return false;
        if (platform != null ? !platform.equals(that.platform) : that.platform != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (singers != null ? !singers.equals(that.singers) : that.singers != null) return false;
        if (albumId != null ? !albumId.equals(that.albumId) : that.albumId != null) return false;
        if (albumName != null ? !albumName.equals(that.albumName) : that.albumName != null) return false;
        if (syncType != null ? !syncType.equals(that.syncType) : that.syncType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = platform != null ? platform.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (singers != null ? singers.hashCode() : 0);
        result = 31 * result + (hasHQ != null ? hasHQ.hashCode() : 0);
        result = 31 * result + (hasSQ != null ? hasSQ.hashCode() : 0);
        result = 31 * result + (hasMV != null ? hasMV.hashCode() : 0);
        result = 31 * result + (hasAlbum != null ? hasAlbum.hashCode() : 0);
        result = 31 * result + (albumId != null ? albumId.hashCode() : 0);
        result = 31 * result + (albumName != null ? albumName.hashCode() : 0);
        result = 31 * result + (syncType != null ? syncType.hashCode() : 0);
        return result;
    }
}
