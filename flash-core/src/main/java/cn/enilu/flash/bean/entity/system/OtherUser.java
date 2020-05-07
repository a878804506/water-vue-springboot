package cn.enilu.flash.bean.entity.system;

import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 第三方登录 用户表
 */
@Entity(name = "t_sys_other_user")
@Table(appliesTo = "t_sys_other_user",comment = "第三方登录账号")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OtherUser {

    @Id
    @Column(columnDefinition = "VARCHAR(100) COMMENT 'openId'")
    private String openid;
    @Column(columnDefinition = "VARCHAR(100) COMMENT 'unionid'")
    private String unionid;
    @Column(columnDefinition = "VARCHAR(100) COMMENT '第三方登录昵称'")
    private String nick;
    @Column(columnDefinition = "INT COMMENT '第三方登录类型'")
    private Integer type;

    @Column(columnDefinition = "INT COMMENT '自己系统用户id'")
    private Integer userid;

    @CreatedDate
    @Column(name = "create_time",columnDefinition="DATETIME COMMENT '创建时间/注册时间'",updatable = false)
    private Date createTime;
}
