package cn.enilu.flash.dao.system;


import cn.enilu.flash.bean.entity.system.SysUrl;
import cn.enilu.flash.dao.BaseRepository;

import java.util.List;


public interface SysUrlRepository extends BaseRepository<SysUrl,Long>{

    List<SysUrl> findByEnabled(int i);
}

