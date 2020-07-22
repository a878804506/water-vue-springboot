import { remove, getList, getMusicById } from '@/api/music/musicStation'
import { getPlatformsList } from '@/api/music/musicSync'
import store from '@/store'
import { getFavoriteList } from '@/api/music/musicFavorite'
import { saveOrUpdateFavoriteMapping, saveFavoriteMappingList } from '@/api/music/musicFavoriteMapping'

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
      // 收藏到。。。弹出框
      favoriteDialog: false,
      // 待收藏音乐id (单个收藏)
      addFavoriteMusicId: '',

      selectedRow: [],
      // 表格多选框 显示与隐藏
      moreSelectBox: true,
      // 收藏、批量收藏的互斥按钮，提交收藏时显示的按钮不一样 true：显示收藏按钮，false：显示批量收藏按钮
      showAddOrAddAll: true
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
    delFavorite(music, e) {
      let btn = e.currentTarget
      btn.disabled = true
      this.$confirm('取消收藏？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        saveOrUpdateFavoriteMapping({
          favoriteId: this.selectedFavorite,
          musicStationId: music.id,
          isFavorite: false,
          page: this.listQuery.page,
          limit: this.listQuery.limit,
          platform: this.listQuery.platform,
          keyword: this.listQuery.keyword
        }).then(response => {
          this.$message({
            message: '已取消收藏',
            type: 'success'
          })
          this.list = response.data.records
          btn.disabled = false
        })
      }).catch(() => {
        btn.disabled = false
      })
    },
    beforeAddFavorite(music) {
      this.favoriteDialog = true
      this.showAddOrAddAll = true
      this.addFavoriteMusicId = music.id
    },
    addFavorite() {
      if (this.selectedFavorite === '') {
        this.$message({
          message: '请选择收藏列表',
          type: 'warning'
        })
        return
      }
      saveOrUpdateFavoriteMapping({
        favoriteId: this.selectedFavorite,
        musicStationId: this.addFavoriteMusicId,
        isFavorite: true,
        page: this.listQuery.page,
        limit: this.listQuery.limit,
        platform: this.listQuery.platform,
        keyword: this.listQuery.keyword
      }).then(response => {
        this.$message({
          message: '已收藏',
          type: 'success'
        })
        this.favoriteDialog = false
        this.list = response.data.records
      })
    },
    showMoreSelect() {
      this.moreSelectBox = false
    },
    beforeAddFavoriteList() {
      this.favoriteDialog = true
      this.showAddOrAddAll = false
    },
    addFavoriteList() {
      this.moreSelectBox = true
      this.favoriteDialog = false
      console.log(this.selectedRow)
      if (this.selectedRow.length == 0) {
        return
      }
      var ids = []
      this.selectedRow.forEach(item => {
        ids.push(item.id)
      })
      saveFavoriteMappingList({
        favoriteId: this.selectedFavorite,
        musicStationIds: JSON.stringify(ids),
        page: this.listQuery.page,
        limit: this.listQuery.limit,
        platform: this.listQuery.platform,
        keyword: this.listQuery.keyword
      }).then(response => {
        this.$message({
          message: '批量收藏成功',
          type: 'success'
        })
        this.list = response.data.records
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
    handleSelectionChange(selectedRow) {
      this.selectedRow = selectedRow
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
