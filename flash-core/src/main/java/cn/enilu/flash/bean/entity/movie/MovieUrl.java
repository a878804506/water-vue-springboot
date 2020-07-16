package cn.enilu.flash.bean.entity.movie;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.*;

/**
 * @ClassName MovieUrl
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-07-14 14:56
 **/
@Entity(name = "t_movie_url")
@Table(appliesTo = "t_movie_url", comment = "免费视频解析url")
@Data
public class MovieUrl extends BaseEntity {
    @Column(name = "url_name",columnDefinition = "VARCHAR(100) COMMENT 'url名称'")
    private String urlName;
    @Column(name = "url",columnDefinition = "VARCHAR(200) COMMENT '网络url'")
    private String url;
    @Column(name = "sort",columnDefinition = "INT COMMENT '排序'")
    private Integer sort;
    @Column(name = "enabled",columnDefinition = "INT COMMENT '0:删除，1:正常'")
    private Integer enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieUrl movieUrl = (MovieUrl) o;

        if (urlName != null ? !urlName.equals(movieUrl.urlName) : movieUrl.urlName != null) return false;
        if (url != null ? !url.equals(movieUrl.url) : movieUrl.url != null) return false;
        if (sort != null ? !sort.equals(movieUrl.sort) : movieUrl.sort != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + (urlName != null ? urlName.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        return result;
    }
}
