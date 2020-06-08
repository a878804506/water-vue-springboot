import {searchSongs, getPlatformsList, syncSongs} from '@/api/music/musicSync'

export default {
  data() {
    return {
      formVisible: false,
      platformDatas: [],
      syncType: [
        {
          label: '标准品质',
          value: '128'
        },
        {
          label: '高品质',
          value: '320'
        },
        {
          label: '无损音乐',
          value: 'flac'
        }
      ],
      listQuery: {
        page: 1,
        pageSize: 20,
        platform: '',
        keyword: '',
        syncType: '320'
      },
      total: 600,
      songsList: [],
      listLoading: false,
      selectedRow: []
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      getPlatformsList().then(response => {
        this.platformDatas = response.data
        this.listQuery.platform = this.platformDatas[0].nameEn
      })
    },
    searchSongs() {
      this.listLoading = true
      const datas = JSON.parse(JSON.stringify(this.listQuery))
      datas.page = datas.page - 1
      searchSongs(datas).then(response => {
        this.songsList = response.data.list
        console.log(this.listQuery)
      })
      this.listLoading = false
    },
    syncSongs() {
      if (this.selectedRow.length == 0) {
        this.$message({
          message: '未选中数据项',
          type: 'warning'
        })
        return
      }
      const datas = JSON.parse(JSON.stringify(this.listQuery))
      const page = datas.page - 1
      syncSongs({
        musicSync: JSON.stringify(this.selectedRow),
        platform: this.listQuery.platform,
        page: page,
        pageSize: this.listQuery.pageSize,
        keyword: this.listQuery.keyword,
        syncType: this.listQuery.syncType
      }).then(response => {
        this.$message({
          message: response.data,
          type: 'success'
        })
      })
    },
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.searchSongs()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.searchSongs()
    },
    fetchPage(page) {
      this.listQuery.page = page
      this.searchSongs()
    },
    handleSelectionChange(selectedRow) {
      this.selectedRow = selectedRow
    },
    changeSize(limit) {
      this.listQuery.pageSize = limit
      this.searchSongs()
    }
  }
}

