<template>
  <div class="drawer-container">
    <!--<div>
      <h3 class="drawer-title">Page style setting</h3>

      <div class="drawer-item">
        <span>Theme Color</span>
        <theme-picker style="float: right;height: 26px;margin: -3px 8px 0 0;" @change="themeChange"/>
      </div>

      <div class="drawer-item">
        <span>Open Tags-View</span>
        <el-switch v-model="tagsView" class="drawer-switch"/>
      </div>

      <div class="drawer-item">
        <span>Fixed Header</span>
        <el-switch v-model="fixedHeader" class="drawer-switch"/>
      </div>

      <div class="drawer-item">
        <span>Sidebar Logo</span>
        <el-switch v-model="sidebarLogo" class="drawer-switch"/>
      </div>

    </div>-->

    <div>
      <h2>我的收藏</h2>
      <div class="drawer-item">
        <a @click="change">变换歌曲列表</a>
      </div>
    </div>

    <div>
      <h2>站内音乐鉴赏</h2>
      <aplayer autoplay :music="music" :list="musicList"
               repeat="list"
               :shuffle="shuffle"
               listMaxHeight="600px"
               preload="auto"
               ref="aplayer"
               v-if="flushMusicList == true"
      ></aplayer>
    </div>
  </div>
</template>
<script>
import ThemePicker from '@/components/ThemePicker'
import aplayer from 'vue-aplayer'

export default {
  components: {
    ThemePicker,
    aplayer
  },
  data() {
    return {
      music: {

      },
      musicList: [

      ],
      flushMusicList: false,
      // 随机播放
      shuffle: true
    }
  },
  computed: {
    fixedHeader: {
      get() {
        return this.$store.state.settings.fixedHeader
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'fixedHeader',
          value: val
        })
      }
    },
    tagsView: {
      get() {
        return this.$store.state.settings.tagsView
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'tagsView',
          value: val
        })
      }
    },
    sidebarLogo: {
      get() {
        return this.$store.state.settings.sidebarLogo
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'sidebarLogo',
          value: val
        })
      }
    }
  },
  methods: {
    themeChange(val) {
      this.$store.dispatch('settings/changeSetting', {
        key: 'theme',
        value: val
      })
    },
    change() {
      this.musicList = [
        {
          title: "红尘来去一场梦",
          artist: "耳东",
          src: "http://staticfile.erdongchen.top/blog/music/红尘来去一场梦.mp3",
          pic: "http://staticfile.erdongchen.top/blog/music/红尘来去一场梦.jpg"
        },
        {
          title: "菊花台",
          artist: "耳东",
          src: "http://staticfile.erdongchen.top/blog/music/菊花台.mp3",
          pic: "http://staticfile.erdongchen.top/blog/music/菊花台.jpg"
        },
        {
          title: "明明就",
          artist: "耳东",
          src: "http://staticfile.erdongchen.top/blog/music/明明就.mp3",
          pic: "http://staticfile.erdongchen.top/blog/music/明明就.jpg"
        },
        {
          title: "你是风儿我是沙",
          artist: "耳东",
          src: "http://staticfile.erdongchen.top/blog/music/你是风儿我是沙.mp3",
          pic: "http://staticfile.erdongchen.top/blog/music/你是风儿我是沙.jpg"
        },
        {
          title: "偏偏喜欢你",
          artist: "耳东",
          src: "http://staticfile.erdongchen.top/blog/music/偏偏喜欢你.mp3",
          pic: "http://staticfile.erdongchen.top/blog/music/偏偏喜欢你.jpg"
        },
        {
          title: "千里之外",
          artist: "耳东",
          src: "http://staticfile.erdongchen.top/blog/music/千里之外.mp3",
          pic: "http://staticfile.erdongchen.top/blog/music/千里之外.jpg"
        },
        {
          title: "青花瓷",
          artist: "耳东",
          src: "http://staticfile.erdongchen.top/blog/music/青花瓷.mp3",
          pic: "http://staticfile.erdongchen.top/blog/music/青花瓷.jpg"
        }
      ]
      if(this.musicList.size != 0){
        this.music = this.musicList[0]
      }
      this.flushMusicList = false
      this.$nextTick(() => {
        this.flushMusicList = true
      })
      this.$
    }
  }
}
</script>
<style lang="scss" scoped>
  .drawer-container {
    padding: 24px;
    font-size: 14px;
    line-height: 1.5;
    word-wrap: break-word;

    .drawer-title {
      margin-bottom: 12px;
      color: rgba(0, 0, 0, .85);
      font-size: 14px;
      line-height: 22px;
    }

    .drawer-item {
      color: rgba(0, 0, 0, .65);
      font-size: 14px;
      padding: 12px 0;
    }

    .drawer-switch {
      float: right
    }
  }
</style>
