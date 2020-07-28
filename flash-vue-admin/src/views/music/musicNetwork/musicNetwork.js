import { getMusicNetworkConfig } from '@/api/music/musicNetwork'
import { getPlatformsList } from '@/api/music/musicSync'
import Aplayer from 'vue-aplayer'

export default {
  components: {
    Aplayer
  },
  data() {
    return {
      formVisible: false,
      platformDatas: [],
      listQuery: {
        page: 1,
        pageSize: 20,
        platform: '',
        keyword: '热歌榜',
        syncType: '320'
      },
      // 网络获取的音乐列表
      songsList: [],
      // 用户添加的音乐列表
      musicList: [],
      // 对应用户添加音乐的id 做唯一判断
      musicIdList: {},
      listLoading: false,
      selectedRow: [],
      // 网络音乐api
      musicSearchUrl: '',
      musicSongInfoUrl: '',
      musicSongPlayerUrl: '',
      unlockCode: '',
      music: {
        title: 'none',
        artist: 'none',
        src: 'none',
        pic: 'none'
      }
    }
  },
  created() {
    this.init()
  },
  mounted() {
    // 添加滚动事件，检测滚动到页面底部
    window.addEventListener('scroll', this.scrollBottom)
  },
  methods: {
    async init() {
      // 音乐平台列表
      await this.getPlatformsList()
      // 获取网络音乐的配置信息
      await this.getMusicNetworkConfig()
      this.searchSongs()
    },
    searchBtn() {
      this.listQuery.page = 1
      this.songsList = []
      this.searchSongs()
    },
    searchSongs() {
      if (this.listQuery.platform === '')
        return
      this.listLoading = true
      let tempMusicSearchUrl = this.musicSearchUrl
      tempMusicSearchUrl = tempMusicSearchUrl.replace("%s", this.listQuery.platform)
      tempMusicSearchUrl = tempMusicSearchUrl.replace("%s", this.listQuery.keyword)
      tempMusicSearchUrl = tempMusicSearchUrl.replace("%s", this.listQuery.page)
      tempMusicSearchUrl = tempMusicSearchUrl.replace("%s", this.listQuery.pageSize)
      this.$axios.get(tempMusicSearchUrl).then(res => {
        this.songsList = this.songsList.concat(res.data.data.list)
        this.listLoading = false
      })
    },
    async addMusic(music) {
      let temp_id = encodeURIComponent(music.id)
      if (this.musicIdList[temp_id] === temp_id) {
        this.$message({
          message: '请勿重复添加',
          type: 'warning'
        })
        return
      }
      let song = {}
      song.title = music.name
      song.artist = music.singers
      song.pic = music.picUrl
      // 继续获取歌曲信息
      let songInfo = await this.musicSongInfoUrlFun(temp_id)
      if (songInfo.code !== 200) {
        this.$message({
          message: '网络错误，请重试！',
          type: 'warning'
        })
        return
      }
      let urlInfo = await this.musicSongPlayerUrlFun(encodeURIComponent(songInfo.data.id))
      if (urlInfo.code !== 200) {
        this.$message({
          message: '网络错误，请重试。',
          type: 'warning'
        })
        return
      }
      song.src = urlInfo.data[0]
      if (this.musicIdList[temp_id] === temp_id) {
        this.$message({
          message: '请勿重复添加',
          type: 'warning'
        })
        return
      }
      this.musicIdList[temp_id] = temp_id
      this.musicList.push(song)
    },
    scrollBottom() {
      //变量scrollTop是滚动条滚动时，距离顶部的距离
      var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      //变量windowHeight是可视区的高度
      var windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
      //变量scrollHeight是滚动条的总高度
      var scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
      //滚动条到底部的条件
      if (scrollTop + windowHeight == scrollHeight) {
        //写后台加载数据的函数
        console.log("距顶部" + scrollTop + "可视区高度" + windowHeight + "滚动条总高度" + scrollHeight);
        this.listQuery.page = this.listQuery.page + 1
        this.searchSongs()
      }
    },
    async getMusicNetworkConfig() {
      await getMusicNetworkConfig().then(response => {
        let {musicSearchUrl, musicSongInfoUrl, musicSongPlayerUrl, unlockCode} = response.data
        this.musicSearchUrl = musicSearchUrl
        this.musicSongInfoUrl = musicSongInfoUrl
        this.musicSongPlayerUrl = musicSongPlayerUrl
        this.unlockCode = unlockCode
      })
    },
    async getPlatformsList() {
      await getPlatformsList().then(response => {
        this.platformDatas = response.data
        this.listQuery.platform = this.platformDatas[0].nameEn
      })
    },
    async musicSongInfoUrlFun(musicId) {
      let result = ''
      let musicSongInfoUrl = this.musicSongInfoUrl
      musicSongInfoUrl = musicSongInfoUrl.replace("%s", this.listQuery.platform)
      musicSongInfoUrl = musicSongInfoUrl.replace("%s", musicId)
      await this.$axios.get(musicSongInfoUrl, {
        headers: {
          'unlockCode': this.unlockCode
        }
      }).then(res => {
        console.log(res)
        result = res.data
      })
      return result
    },
    async musicSongPlayerUrlFun(musicId) {
      let result = ''
      let musicSongPlayerUrl = this.musicSongPlayerUrl
      musicSongPlayerUrl = musicSongPlayerUrl.replace("%s", this.listQuery.platform)
      musicSongPlayerUrl = musicSongPlayerUrl.replace("%s", musicId)
      musicSongPlayerUrl = musicSongPlayerUrl.replace("%s", 320)
      await this.$axios.get(musicSongPlayerUrl, {
        headers: { 'unlockCode': this.unlockCode }
      }).then(res => {
        console.log(res)
        result = res.data
      })
      return result
    }
  }
}

