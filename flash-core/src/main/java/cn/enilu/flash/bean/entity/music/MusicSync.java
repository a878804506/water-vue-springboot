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

/**
 * @ClassName MusicSync
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-05 15:01
 **/
@Entity(name = "t_music_sync")
@Table(appliesTo = "t_music_sync", comment = "音乐同步任务")
@Data
@IdClass(MusicSyncPK.class)
@EntityListeners(AuditingEntityListener.class)
public class MusicSync {

    @Column(name = "id", columnDefinition = "VARCHAR(100) COMMENT 'id'")
    private String id;

    @Id
    @Column(name = "platform", columnDefinition = "VARCHAR(100) COMMENT '歌曲来源平台'")
    private String platform;
    @Id
    @Column(name = "name", columnDefinition = "VARCHAR(100) COMMENT '歌曲名称'")
    private String name;
    @Id
    @Column(name = "singers", columnDefinition = "VARCHAR(100) COMMENT '歌手'")
    private String singers;
    @Id
    @Column(name = "has_hq")
    private Boolean hasHQ;
    @Id
    @Column(name = "has_sq")
    private Boolean hasSQ;
    @Id
    @Column(name = "has_mv")
    private Boolean hasMV;
    @Id
    @Column(name = "has_album")
    private Boolean hasAlbum;
    @Id
    @Column(name = "album_id", columnDefinition = "VARCHAR(100) COMMENT '专辑id'")
    private String albumId;
    @Id
    @Column(name = "album_name", columnDefinition = "VARCHAR(100) COMMENT '专辑名称'")
    private String albumName;
    @Id
    @Column(name = "sync_type", columnDefinition = "VARCHAR(100) COMMENT '同步音乐类型'")
    private String syncType;
    @Column(name = "sync_status", columnDefinition = "int COMMENT '音乐同步状态'")
    private int syncStatus;
    @Column(name = "keyword", columnDefinition = "VARCHAR(100) COMMENT '关键字'")
    private String keyword;
    @Column(name = "page", columnDefinition = "int COMMENT '页码'")
    private int page;
    @Column(name = "page_size", columnDefinition = "int COMMENT '一页条数'")
    private int pageSize;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicSync musicSync = (MusicSync) o;

        if (hasHQ != musicSync.hasHQ) return false;
        if (hasSQ != musicSync.hasSQ) return false;
        if (hasMV != musicSync.hasMV) return false;
        if (hasAlbum != musicSync.hasAlbum) return false;
        if (platform != null ? !platform.equals(musicSync.platform) : musicSync.platform != null) return false;
        if (name != null ? !name.equals(musicSync.name) : musicSync.name != null) return false;
        if (singers != null ? !singers.equals(musicSync.singers) : musicSync.singers != null) return false;
        if (albumId != null ? !albumId.equals(musicSync.albumId) : musicSync.albumId != null) return false;
        if (albumName != null ? !albumName.equals(musicSync.albumName) : musicSync.albumName != null) return false;

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
        result = 31 * result + syncStatus;
        result = 31 * result + (keyword != null ? keyword.hashCode() : 0);
        result = 31 * result + page;
        result = 31 * result + pageSize;
        return result;
    }
}
