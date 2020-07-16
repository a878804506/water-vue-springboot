import { getAllUrls } from '@/api/movie/movieUrl'

export default {
  data() {
    return {
      showHelp: false,
      urlList: [],
      // 选择的解析线路
      selectedLabel: '',
      // 用户输入的播放地址
      userUrl: '',
      // 拼接后的播放地址
      playUrl: ''
    }
  },
  computed: {

  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.fetchData()
    },
    fetchData() {
      getAllUrls(this.listQuery).then(response => {
        this.urlList = response.data
      })
    },
    playMovie() {
      if (this.selectedLabel === ''){
        this.$message.error('请选择解析线路')
        return
      }
      if (this.userUrl === ''){
        this.$message.error('请输入要播放的视频地址')
        return
      }
      this.playUrl = this.selectedLabel + this.userUrl
    }
  }
}
