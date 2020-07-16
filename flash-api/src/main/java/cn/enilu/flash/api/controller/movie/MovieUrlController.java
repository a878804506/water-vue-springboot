package cn.enilu.flash.api.controller.movie;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.dictmap.CommonDict;
import cn.enilu.flash.bean.entity.movie.MovieUrl;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.movie.MovieUrlService;
import cn.enilu.flash.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movie/url")
public class MovieUrlController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private MovieUrlService movieUrlService;

	//获取可用的url列表
	@RequestMapping(value = "/getAllUrls",method = RequestMethod.GET)
	public Object getAllUrls() {
		List<MovieUrl> movieUrls = movieUrlService.queryAll(
				SearchFilter.build("enabled", SearchFilter.Operator.EQ, 1),
				new Sort(Sort.Direction.ASC, "sort"));
		return Rets.success(movieUrls);
	}

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
		Page<MovieUrl> page = new PageFactory<MovieUrl>().defaultPage();
		// 手动排序 id正序
		page.setSort(new Sort(Sort.Direction.ASC,"sort"));
		page.addFilter(SearchFilter.build("enabled", SearchFilter.Operator.EQ, 1));
		page = movieUrlService.queryPage(page);
		return Rets.success(page);
	}

	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑免费视频解析url", key = "name",dict= CommonDict.class)
	public Object save(@ModelAttribute MovieUrl tMovieUrl){
		if(tMovieUrl.getId()==null){
			tMovieUrl.setEnabled(1);
			movieUrlService.insert(tMovieUrl);
		}else {
			movieUrlService.update(tMovieUrl);
		}
		return Rets.success();
	}

	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除免费视频解析url", key = "id",dict= CommonDict.class)
	public Object remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		MovieUrl movieUrl = movieUrlService.get(id);
		if(null == movieUrl){
			return Rets.failure("删除失败");
		}
		movieUrl.setEnabled(0);
		movieUrlService.update(movieUrl);
		return Rets.success();
	}
}