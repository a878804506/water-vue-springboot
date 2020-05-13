import {getList, inOrOutInventory} from '@/api/commodity/commodityInventory'

export default {
  data() {
    return {
      formVisible: false,
      formTitle: '添加商品库存',
      formTypeName: '',
      form: {
        commodityName: '',
        commodityNowNum: '',
        commodityNum: '',
        commodityVersion: '',
        commodityRemarks: '',
        type: -1,
        id: ''
      },
      listQuery: {
        page: 1,
        limit: 20,
        id: undefined
      },
      total: 0,
      list: null,
      listLoading: true,
      selRow: {}
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
  computed: {

    //表单验证
    rules() {
      return {
        commodityNum: [
          {required: true, message: '请输入商品出入库数量', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.fetchData()
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
      alert("功能暂未实现")
    },
    reset() {
      alert("功能暂未实现")
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
    inInventory(row) {
      this.resetForm()
      this.form.commodityName = row.commodityInfo.commodityName
      this.form.commodityNowNum = row.commodityNum
      this.form.commodityVersion = row.commodityVersion
      this.form.id = row.id
      this.form.type = 1

      this.formTitle = '商品入库'
      this.formTypeName = '商品入库数量'
      this.formVisible = true
    },
    outInventory(row) {
      this.resetForm()
      this.form.commodityName = row.commodityInfo.commodityName
      this.form.commodityNowNum = row.commodityNum
      this.form.commodityVersion = row.commodityVersion
      this.form.id = row.id
      this.form.type = 2

      this.formTitle = '商品出库'
      this.formTypeName = '商品出库数量'
      this.formVisible = true
    },
    submitOrder() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          inOrOutInventory({
            commodityNum: this.form.commodityNum,
            commodityVersion: this.form.commodityVersion,
            commodityRemarks: this.form.commodityRemarks,
            type: this.form.type,
            id: this.form.id
          }).then(response => {
            this.$message({
              message: this.$t('common.optionSuccess'),
              type: 'success'
            })
            this.fetchData()
            this.formVisible = false
          })
        } else {
          return false
        }
      })
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
    resetForm() {
      this.form = {
        commodityName: '',
        commodityNowNum: '',
        commodityNum: '',
        commodityVersion: '',
        commodityRemarks: '',
        type: -1,
        id: ''
      }
    }
  }
}
