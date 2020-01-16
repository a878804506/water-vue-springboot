import {remove, getList, save} from '@/api/water/waterCustomer'

export default {
  data() {
    return {
      customerStatus: [{
        value: '1',
        label: '正常'
      }, {
        value: '0',
        label: '报停'
      }
      ],
      customerDelete: [{
        value: '1',
        label: '正常'
      }, {
        value: '0',
        label: '删除'
      }
      ],
      formVisible: false,
      formTitle: '添加客户信息表',
      isAdd: true,
      form: {
        name: '',
        price: '',
        address: '',
        starttime: '',
        status: '',
        id: ''
      },
      rules: {
        name: [
          {required: true, message: '请输入客户姓名', trigger: 'blur'},
          {min: 2, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        price: [
          {required: true, message: '请输入定价', trigger: 'blur'}
        ],
        address: [
          {required: true, message: '请输入客户地址', trigger: 'blur'}
        ],
        starttime: [
          {required: true, message: '请输入开户时间', trigger: 'blur'}
        ],
        status: [
          {required: true, message: '请选择', trigger: 'blur'}
        ]
      },
      listQuery: {
        page: 1,
        limit: 10,
        id: undefined,
        name: undefined,
        status: undefined,
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

    // 表单验证
    rules() {
      return {
        // cfgName: [
        //   { required: true, message: this.$t('config.name') + this.$t('common.isRequired'), trigger: 'blur' },
        //   { min: 3, max: 2000, message: this.$t('config.name') + this.$t('config.lengthValidation'), trigger: 'blur' }
        // ]
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
      this.fetchData()
    },
    reset() {
      this.listQuery.name = ''
      this.listQuery.status = ''
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
    resetForm() {
      this.form = {
        name: '',
        price: '',
        address: '',
        starttime: '',
        status: '',
        id: ''
      }
    },
    add() {
      this.resetForm()
      this.formTitle = '添加客户信息表'
      this.formVisible = true
      /**
       * 默认正常
       */
      this.form.status = '1'
      this.isAdd = true
    },
    save() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          var form = this.form
          save({
            name: this.form.name,
            price: this.form.price,
            address: this.form.address,
            starttime: this.form.starttime,
            status: this.form.status,
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
          console.log('失败')
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
        this.form.status = this.selRow.status + ''
        this.formTitle = '编辑客户信息表'
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
    }

  }
}
