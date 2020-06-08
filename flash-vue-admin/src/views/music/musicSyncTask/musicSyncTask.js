import {getMusicSyncTask} from '@/api/music/musicSyncTask'

export default {
  data() {
    return {
      formVisible: false,
      listQuery: {
        page: 1,
        limit: 10,
        name: '',
        syncStatus: -1
      },
      total: 0,
      listLoading: false,
      selectedRow: [],
      list: null,
      syncStatusList: [
        {
          label: '请选择',
          value: -1
        },
        {
          label: '待同步',
          value: 0
        },
        {
          label: '同步成功',
          value: 1
        },
        {
          label: '同步失败',
          value: 2
        },
        {
          label: '无相应品质的音乐资源',
          value: 3
        },
        {
          label: '指定条件下没有找到该歌曲',
          value: 4
        }
      ]
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.getMusicSyncTask()
    },
    getMusicSyncTask() {
      this.listLoading = true
      getMusicSyncTask(this.listQuery).then(response => {
        this.listLoading = false
        this.total = response.data.total
        this.list = response.data.records
      })
    },
    fetchNext() {
      this.listQuery.page = this.listQuery.page + 1
      this.getMusicSyncTask()
    },
    fetchPrev() {
      this.listQuery.page = this.listQuery.page - 1
      this.getMusicSyncTask()
    },
    fetchPage(page) {
      this.listQuery.page = page
      this.getMusicSyncTask()
    },
    handleSelectionChange(selectedRow) {
      this.selectedRow = selectedRow
    },
    changeSize(limit) {
      this.listQuery.pageSize = limit
      this.getMusicSyncTask()
    },
    handleCurrentChange(currentRow, oldCurrentRow) {
      this.selRow = currentRow
    }
  }
}

