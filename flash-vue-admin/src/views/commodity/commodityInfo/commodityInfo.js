import {remove, getList, save} from '@/api/commodity/commodityInfo'
import {getDictByPid} from '@/api/system/dict'

export default {
  data() {
    return {
      formVisible: false,
      formTitle: '添加商品',
      isAdd: true,
      form: {
        commodityNumber: '',
        commodityName: '',
        commodityOrigin: '',
        commodityType: '',
        commodityTradePrice: '',
        commoditySalesPrice: '',
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
      selRow: {},
      commodityTypes: []
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
        commodityNumber: [
          {required: true, message: '请输入商品编码', trigger: 'blur'},
          {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
        ],
        commodityName: [
          {required: true, message: '请输入商品名称', trigger: 'blur'},
          {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
        ],
        commodityOrigin: [
          {required: true, message: '请输入商品产地', trigger: 'blur'}
        ],
        commodityTradePrice: [
          {required: true, message: '请输入商品批发价', trigger: 'blur'}
        ],
        commoditySalesPrice: [
          {required: true, message: '请输入商品销售价', trigger: 'blur'}
        ],
        commodityType: [
          {required: true, message: '请输入商品类别', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.init()
    this.getDictListByPid()
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
    resetForm() {
      this.form = {
        commodityNumber: '',
        commodityName: '',
        commodityOrigin: '',
        commodityType: '',
        commodityTradePrice: '',
        commoditySalesPrice: '',
        id: ''
      }
    },
    add() {
      this.resetForm()
      this.formTitle = '添加商品'
      this.formVisible = true
      this.isAdd = true
    },
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          save({
            commodityNumber: this.form.commodityNumber,
            commodityName: this.form.commodityName,
            commodityOrigin: this.form.commodityOrigin,
            commodityType: this.form.commodityType,
            commodityTradePrice: this.form.commodityTradePrice,
            commoditySalesPrice: this.form.commoditySalesPrice,
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
    edit() {
      if (this.checkSel()) {
        this.isAdd = false
        this.form = this.selRow
        console.log(this.selRow)
        this.formTitle = '编辑商品'
        this.formVisible = true
      }
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
    },
    getDictListByPid() {
      getDictByPid({
        pid: 79
      }).then(response => {
        console.log(response.data)
        this.commodityTypes = response.data
      })
    }

  }
}
