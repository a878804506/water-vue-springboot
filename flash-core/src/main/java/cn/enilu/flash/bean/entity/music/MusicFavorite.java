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
 * @ClassName MusicFavorite
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-07-09 11:21
 **/
@Entity(name = "t_music_favorite")
@Table(appliesTo = "t_music_favorite", comment = "音乐收藏组")
@Data
@IdClass(MusicFavoritePK.class)
@EntityListeners(AuditingEntityListener.class)
public class MusicFavorite {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    @Column(name = "favorite_name", columnDefinition = "收藏名称'")
    private String favoriteName;
    @Id
    @Column(name = "favorite_user_id", columnDefinition = "收藏的用户'")
    private Long favoriteUserId;

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

        MusicFavorite that = (MusicFavorite) o;

        if (id != that.id) return false;
        if (favoriteName != null ? !favoriteName.equals(that.favoriteName) : that.favoriteName != null) return false;
        if (favoriteUserId != null ? !favoriteUserId.equals(that.favoriteUserId) : that.favoriteUserId != null)
            return false;
        if (createBy != null ? !createBy.equals(that.createBy) : that.createBy != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (modifyBy != null ? !modifyBy.equals(that.modifyBy) : that.modifyBy != null) return false;
        if (modifyTime != null ? !modifyTime.equals(that.modifyTime) : that.modifyTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (favoriteName != null ? favoriteName.hashCode() : 0);
        result = 31 * result + (favoriteUserId != null ? favoriteUserId.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (modifyBy != null ? modifyBy.hashCode() : 0);
        result = 31 * result + (modifyTime != null ? modifyTime.hashCode() : 0);
        return result;
    }
}
