import { remove, getList, getMusicById } from '@/api/music/musicStation'
import { getPlatformsList } from '@/api/music/musicSync'
import store from '@/store'
import { getFavoriteList } from '@/api/music/musicFavorite'
import { saveOrUpdateFavoriteMapping } from '@/api/music/musicFavoriteMapping'

export default {
  data() {
    return {
      formVisible: false,
      isAdd: true,
      listQuery: {
        page: 1,
        limit: 20,
        platform: undefined,
        keyword: ''
      },
      total: 0,
      list: null,
      listLoading: true,
      selRow: {},
      platformDatas: [],
      musicEntity: {
        src: ''
      },
      // 权限控制 是否显示删除歌曲的按钮
      showDeleteBtn: false,
      // 用户选择收藏到的组
      selectedFavorite: '',
      // 用户收藏组集合
      favoriteList: [],
      // 收藏/取消收藏按钮的悬停状态按钮
      visible: false
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  created() {
    this.init()
    this.getFavoriteList()
  },
  methods: {
    init() {
      this.getPlatformsList()
      this.fetchData()
      // 根据当前用户的权限 来判断是否显示 删除按钮
      store.getters.permissions.forEach((value, index) => {
        if ('/musicStationDelete' === value) {
          this.showDeleteBtn = true
          return
        }
      })
    },
    getPlatformsList() {
      getPlatformsList().then(response => {
        this.platformDatas = response.data
      })
    },
    fetchData() {
      this.listLoading = true
      getList(this.listQuery).then(response => {
        this.list = response.data.records
        this.listLoading = false
        this.total = response.data.total
      })
    },
    search() {
      this.fetchData()
    },
    // 试听
    getMusicById() {
      if (this.checkSel()) {
        getMusicById({id: this.selRow.id}).then(response => {
          this.musicEntity.src = response.data
          this.$refs.audio.load()
          let playPromise = this.$refs.audio.play()
          if (playPromise !== undefined) {
            playPromise.then(() => {
              this.$refs.audio.play()
            }).catch(() => {
              this.$refs.audio.play()
            })
          }
        })
      }
    },
    optFavorite(music) {
      this.visible = false
      console.log(music)
      let isFavorite = false
      if (music.userFavorite.isUserFavorite === false) {
        if (this.selectedFavorite === '') {
          this.$message({
            message: '请选择收藏列表',
            type: 'warning'
          })
          return
        }
        isFavorite = true
      }
      saveOrUpdateFavoriteMapping({
        favoriteId: this.selectedFavorite,
        musicStationId: music.id,
        isFavorite: isFavorite,
        page: this.listQuery.page,
        limit: this.listQuery.limit,
        platform: this.listQuery.platform,
        keyword: this.listQuery.keyword
      }).then(response => {
        if (response.code === 20000) {
          this.$message({
            message: response.msg,
            type: 'success'
          })
          this.list = response.data.records
        } else {
          this.$message({
            message: response.msg,
            type: 'warning'
          })
        }
      })
    },
    getFavoriteList() {
      getFavoriteList().then(response => {
        this.favoriteList = response.data
      })
    },
    reset() {
      this.listQuery.platform = ''
      this.listQuery.keyword = ''
      this.fetchData()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleClose() {

    },
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.fetchData()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.fetchData()
    },
    fetchPage(page) {
      this.listQuery.page = page
      this.fetchData()
    },
    changeSize(limit) {
      this.listQuery.limit = limit
      this.fetchData()
    },
    handleCurrentChange(currentRow, oldCurrentRow) {
      this.selRow = currentRow
    },
    checkSel() {
      if (this.selRow && this.selRow.id) {
        return true
      }
      this.$message({
        message: this.$t('common.mustSelectOne'),
        type: 'warning'
      })
      return false
    },
    remove() {
      if (this.checkSel()) {
        var id = this.selRow.id
        this.$confirm(this.$t('common.deleteConfirm'), this.$t('common.tooltip'), {
          confirmButtonText: this.$t('button.submit'),
          cancelButtonText: this.$t('button.cancel'),
          type: 'warning'
        }).then(() => {
          remove(id).then(response => {
            this.$message({
              message: this.$t('common.optionSuccess'),
              type: 'success'
            })
            this.fetchData()
          }).catch(err => {
            this.$notify.error({
              title: '错误',
              message: err
            })
          })
        }).catch(() => {
        })
      }
    }

  }
}
