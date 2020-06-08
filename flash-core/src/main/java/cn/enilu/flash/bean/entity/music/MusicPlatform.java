package cn.enilu.flash.bean.entity.music;

import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.*;


/**
 * @ClassName MusicPlatform
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-06-03 21:27
 **/
@Entity(name = "t_music_platform")
@Table(appliesTo = "t_music_platform", comment = "音乐平台")
@Data
public class MusicPlatform {
    @Id
    @Column(name = "id", columnDefinition = "INT COMMENT 'id'")
    private int id;
    @Column(name = "nameCN", columnDefinition = "VARCHAR(50) COMMENT '中文'")
    private String nameCn;
    @Column(name = "nameEN", columnDefinition = "VARCHAR(50) COMMENT '英文'")
    private String nameEn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicPlatform that = (MusicPlatform) o;

        if (id != that.id) return false;
        if (nameCn != null ? !nameCn.equals(that.nameCn) : that.nameCn != null) return false;
        if (nameEn != null ? !nameEn.equals(that.nameEn) : that.nameEn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nameCn != null ? nameCn.hashCode() : 0);
        result = 31 * result + (nameEn != null ? nameEn.hashCode() : 0);
        return result;
    }
}
