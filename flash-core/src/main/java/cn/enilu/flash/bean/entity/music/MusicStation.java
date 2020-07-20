package cn.enilu.flash.bean.entity.music;

import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName MusicStation
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-02 15:35
 **/
@Entity(name = "t_music_station")
@Table(appliesTo = "t_music_station", comment = "站内音乐")
@Data
@IdClass(MusicStationPK.class)
@EntityListeners(AuditingEntityListener.class)
public class MusicStation {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(100) COMMENT 'id'")
    private String id;

    @Id
    @Column(name = "platform_id", columnDefinition = "INT COMMENT '平台id'")
    private int platformId;

    @Column(name = "name", columnDefinition = "VARCHAR(255) COMMENT '歌曲名称'")
    private String name;

    @Column(name = "singers", columnDefinition = "VARCHAR(100) COMMENT '歌手'")
    private String singers;

    @Column(name = "pic_url", columnDefinition = "VARCHAR(255) COMMENT '图片地址'")
    private String picUrl;

    @Column(name = "has_hq", columnDefinition = "INT COMMENT '是否有高品质音乐'")
    private Boolean hasHQ;

    @Column(name = "has_sq", columnDefinition = "INT COMMENT '是否有无损音乐'")
    private Boolean hasSQ;

    @Column(name = "has_mv", columnDefinition = "INT COMMENT '是否有MV'")
    private Boolean hasMV;

    @Column(name = "has_album", columnDefinition = "INT COMMENT '是否有专辑'")
    private Boolean hasAlbum;

    @Column(name = "album_id", columnDefinition = "VARCHAR(100) COMMENT '专辑id'")
    private String albumId;

    @Column(name = "album_name", columnDefinition = "VARCHAR(100) COMMENT '专辑名称'")
    private String albumName;

    @Column(name = "music_type", columnDefinition = "INT COMMENT '音乐类型'")
    private Integer musicType;

    @Column(name = "music_url", columnDefinition = "VARCHAR(255) COMMENT '音乐播放地址'")
    private String musicUrl;

    @CreatedDate
    @Column(name = "create_time",columnDefinition="DATETIME COMMENT '创建时间/注册时间'",updatable = false)
    private Date createTime;
    @Column(name = "create_by",columnDefinition="bigint COMMENT '创建人'",updatable = false)
    @CreatedBy
    private Long createBy;
    @LastModifiedDate
    @Column(name = "modify_time",columnDefinition="DATETIME COMMENT '最后更新时间'")
    private Date modifyTime;
    @LastModifiedBy
    @Column(name = "modify_by",columnDefinition="bigint COMMENT '最后更新人'")
    private Long modifyBy;

    // 搜索平台类型 站内、站外
    @Transient
    private int searchType;

    // 查询列表时 附带个人收藏信息情况
    @Transient
    private Map<String,Object> userFavorite;

    public MusicStation(String id, String name, String singers, String picUrl,
                        Boolean hasHQ, Boolean hasSQ, Boolean hasMV, Boolean hasAlbum,
                        String albumId, String albumName) {
        this.id = id;
        this.name = name;
        this.singers = singers;
        this.picUrl = picUrl;
        this.hasHQ = hasHQ;
        this.hasSQ = hasSQ;
        this.hasMV = hasMV;
        this.hasAlbum = hasAlbum;
        this.albumId = albumId;
        this.albumName = albumName;
    }

    public MusicStation(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicStation that = (MusicStation) o;

        if (platformId != that.platformId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (singers != null ? !singers.equals(that.singers) : that.singers != null) return false;
        if (hasHQ != null ? !hasHQ.equals(that.hasHQ) : that.hasHQ != null) return false;
        if (hasSQ != null ? !hasSQ.equals(that.hasSQ) : that.hasSQ != null) return false;
        if (hasMV != null ? !hasMV.equals(that.hasMV) : that.hasMV != null) return false;
        if (hasAlbum != null ? !hasAlbum.equals(that.hasAlbum) : that.hasAlbum != null) return false;
        if (albumId != null ? !albumId.equals(that.albumId) : that.albumId != null) return false;
        if (albumName != null ? !albumName.equals(that.albumName) : that.albumName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
//        int result = id != null ? id.hashCode() : 0;
        int result = 31 * 1 + platformId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (singers != null ? singers.hashCode() : 0);
        result = 31 * result + (picUrl != null ? picUrl.hashCode() : 0);
        result = 31 * result + (hasHQ != null ? hasHQ.hashCode() : 0);
        result = 31 * result + (hasSQ != null ? hasSQ.hashCode() : 0);
        result = 31 * result + (hasMV != null ? hasMV.hashCode() : 0);
        result = 31 * result + (hasAlbum != null ? hasAlbum.hashCode() : 0);
        result = 31 * result + (albumId != null ? albumId.hashCode() : 0);
        result = 31 * result + (albumName != null ? albumName.hashCode() : 0);
        return result;
    }
}
