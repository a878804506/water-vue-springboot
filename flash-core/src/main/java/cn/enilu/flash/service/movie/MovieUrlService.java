package cn.enilu.flash.service.movie;


import cn.enilu.flash.bean.entity.movie.MovieUrl;
import cn.enilu.flash.dao.movie.MovieUrlRepository;

import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieUrlService extends BaseService<MovieUrl,Long,MovieUrlRepository>  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MovieUrlRepository movieUrlRepository;

}

