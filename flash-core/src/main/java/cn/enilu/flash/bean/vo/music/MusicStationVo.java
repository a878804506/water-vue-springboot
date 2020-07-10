package cn.enilu.flash.bean.vo.music;

import lombok.Data;

/**
 * @ClassName MusicStationVo
 * @Description TODO
 * @Author 陈韵辉
 * @Date 2020-07-09 15:59
 **/
@Data
public class MusicStationVo {
    String id;
    String title;
    String artist;
    String src;
    String pic;

    public MusicStationVo(String id,String title,String artist,String src,String pic){
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.src = src;
        this.pic = pic;
    }
}
