package cn.enilu.flash.api;

import cn.enilu.flash.dao.BaseRepositoryFactoryBean;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * ApiApplication
 *
 * @author enilu
 * @version 2018/9/11 0011
 */
// 开启使用缓存
@EnableCaching
@SpringBootApplication
@NacosPropertySource(dataId = "onecloud",autoRefreshed = true)
@ComponentScan(basePackages = "cn.enilu.flash")
@EntityScan(basePackages="cn.enilu.flash.bean.entity")
@EnableJpaRepositories(basePackages = "cn.enilu.flash.dao", repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
// 开启审计功能 by 陈韵辉 20191230
@EnableJpaAuditing
public class ApiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class);
    }
}
