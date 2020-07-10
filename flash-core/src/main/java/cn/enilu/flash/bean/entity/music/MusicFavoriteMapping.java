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
 * @ClassName MusicFavoriteMapping
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-07-09 15:34
 **/
@Entity(name = "t_music_favorite_mapping")
@Table(appliesTo = "t_music_favorite_mapping", comment = "音乐收藏组详情")
@Data
@IdClass(MusicFavoriteMappingPK.class)
@EntityListeners(AuditingEntityListener.class)
public class MusicFavoriteMapping {

    @Id
    @Column(name = "favorite_id", columnDefinition = "收藏id'")
    private Long favoriteId;
    @Id
    @Column(name = "music_station_id", columnDefinition = "收藏的音乐'")
    private String musicStationId;

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

        MusicFavoriteMapping that = (MusicFavoriteMapping) o;

        if (favoriteId != that.favoriteId) return false;
        if (musicStationId != null ? !musicStationId.equals(that.musicStationId) : that.musicStationId != null)
            return false;
        if (createBy != null ? !createBy.equals(that.createBy) : that.createBy != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (modifyBy != null ? !modifyBy.equals(that.modifyBy) : that.modifyBy != null) return false;
        if (modifyTime != null ? !modifyTime.equals(that.modifyTime) : that.modifyTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = favoriteId.intValue();
        result = 31 * result + (musicStationId != null ? musicStationId.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (modifyBy != null ? modifyBy.hashCode() : 0);
        result = 31 * result + (modifyTime != null ? modifyTime.hashCode() : 0);
        return result;
    }
}
