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
      <h2>我的收藏
        <el-button type="primary" icon="el-icon-plus" circle size="mini" @click="beforeFavorite('新建收藏组',true,'')"></el-button>
      </h2>

      <div class="drawer-item">
        <ul>
          <li v-for="( item, index ) in favoriteList">
            <a @click="changeMusicList(item.id)">{{ item.favoriteName }}</a>
            <el-link icon="el-icon-delete" type="danger" style="float: right" @click="deleteFavorite(item.id)">删除</el-link>
            <el-link icon="el-icon-edit"  type="warning" style="float: right" @click="beforeFavorite('修改收藏组',true,item)">修改</el-link>
          </li>
        </ul>
      </div>

      <el-dialog :title="title" :visible.sync="showFavoriteForm">
        <el-form >
          <el-form-item label="收藏组名称">
            <el-input type="text" maxlength="10" v-model="favorite.favoriteName"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="beforeFavorite('',false,'')">取 消</el-button>
          <el-button type="primary" @click="addFavorite">确 定</el-button>
        </div>
      </el-dialog>
    </div>

    <div>
      <h2>站内音乐鉴赏</h2>
      <aplayer autoplay
               :music="music"
               :list="musicList"
               repeat="list"
               :shuffle="shuffle"
               listMaxHeight="600px"
               preload="auto"
               ref="aplayer"
               v-if="flushMusicList == true"
               :token="token"
               :serverAddress="serverAddress"
      ></aplayer>
    </div>
  </div>

</template>

<script>
import { getToken } from '@/utils/auth'
import { getApiUrl } from '@/utils/utils'
import ThemePicker from '@/components/ThemePicker'
import { getFavoriteList, saveOrUpdateFavorite, deleteFavorite, getFavoriteMusicList, getAppMusicUrl } from '@/api/music/musicFavorite'
import Aplayer from 'diy-vue-aplayer'

export default {
  components: {
    ThemePicker,
    Aplayer
  },
  data() {
    return {
      music: {},
      musicList: [],
      flushMusicList: false,
      // 随机播放
      shuffle: true,
      // 项目token  音乐插件里面用
      token: getToken(),
      //项目地址  音乐插件里面用
      serverAddress: getApiUrl(),
      //用户收藏列表
      favoriteList: [],
      // 添加收藏的 dialog
      showFavoriteForm: false,
      favorite: {
        id: '',
        favoriteName: ''
      },
      // 卡片名称
      title: ''
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
  created() {
    this.getFavoriteList()
  },
  methods: {
    themeChange(val) {
      this.$store.dispatch('settings/changeSetting', {
        key: 'theme',
        value: val
      })
    },
    async changeMusicList(favoriteId) {
      await this.getFavoriteMusicList(favoriteId)
      if (this.musicList.length == 0) {
        this.$notify({
          title: '警告',
          message: '该收藏组暂无歌曲列表',
          type: 'warning',
          position: 'top-left',
          duration: 3000
        })
        return
      } else {
        this.music = this.musicList[0]
        this.music.src = await this.getAppMusicUrl(this.music.id)
        this.flushMusicList = true
        /*this.$nextTick(() => {
          this.flushMusicList = true
        })*/
      }
    },
    async getFavoriteMusicList(favoriteId) {
      await getFavoriteMusicList({ favoriteId: favoriteId }).then(response => {
        this.musicList = response.data
      })
    },
    async getAppMusicUrl(musicId) {
      let src = ''
      await getAppMusicUrl(
        {
          id: musicId,
          searchType: 0
        }).then(response => {
        src = response.data
      })
      return src
    },
    getFavoriteList() {
      getFavoriteList().then(response => {
        this.favoriteList = response.data
      })
    },
    addFavorite() {
      if (this.favorite.favoriteName === '' || this.favorite.favoriteName.length == 0) {
        this.$notify({
          title: '警告',
          message: '请输入收藏组名称',
          type: 'warning',
          position: 'top-left',
          duration: 3000
        })
        return
      }
      saveOrUpdateFavorite(this.favorite).then(response => {
        this.favoriteList = response.data
        this.showFavoriteForm = false
        this.$notify({
          title: '成功',
          message: '操作成功',
          type: 'success',
          position: 'top-left',
          duration: 3000
        })
      })
    },
    beforeFavorite(title, showFavoriteForm, item) {
      this.title = title
      this.showFavoriteForm = showFavoriteForm
      this.favorite.id = item.id
      this.favorite.favoriteName = item.favoriteName
    },
    deleteFavorite(id) {
      deleteFavorite({ id: id }).then(response => {
        this.favoriteList = response.data
        this.showFavoriteForm = false
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          position: 'top-left',
          duration: 3000
        })
      })
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
