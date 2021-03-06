import {getList, save, updaloadFile} from '@/api/pdf/pdfManagement'
import request from '@/utils/request'
import {getApiUrl} from '@/utils/utils'
import {getToken} from '@/utils/auth'

export default {
  data() {
    return {
      formVisible: false,
      isAdd: true,
      listQuery: {
        page: 1,
        limit: 10,
        id: undefined
      },
      total: 0,
      list: null,
      listLoading: true,
      selRow: {},

      formData: {
        name: '',
        oldPdfStartPage: 1,
        oldPdfEndPage: undefined,
        oldExcelStartRow: 0,
        oldExcelStartColumn: 0,
        oldPdf: '',
        oldExcel: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请输入pdf解析名称',
          trigger: 'blur'
        }],
        oldPdfStartPage: [{
          required: true,
          message: '请输入pdf解析开始页',
          trigger: 'blur'
        }],
        oldPdfEndPage: [],
        oldExcelStartRow: [{
          required: true,
          message: '请输入excel开始行',
          trigger: 'blur'
        }],
        oldExcelStartColumn: [{
          required: true,
          message: '请输入excel开始列',
          trigger: 'blur'
        }]
      },
      excelColumns: [
        {
          label: 'A列',
          value: 0
        },
        {
          label: 'B列',
          value: 1
        },
        {
          label: 'C列',
          value: 2
        },
        {
          label: 'D列',
          value: 3
        },
        {
          label: 'E列',
          value: 4
        },
        {
          label: 'F列',
          value: 5
        },
        {
          label: 'G列',
          value: 6
        },
        {
          label: 'H列',
          value: 7
        },
        {
          label: 'I列',
          value: 8
        },
        {
          label: 'J列',
          value: 9
        },
        {
          label: 'K列',
          value: 10
        },
        {
          label: 'L列',
          value: 11
        },
        {
          label: 'M列',
          value: 12
        },
        {
          label: 'N列',
          value: 13
        },
        {
          label: 'O列',
          value: 14
        },
        {
          label: 'P列',
          value: 15
        },
        {
          label: 'Q列',
          value: 16
        },
        {
          label: 'R列',
          value: 17
        },
        {
          label: 'S列',
          value: 18
        },
        {
          label: 'T列',
          value: 19
        },
        {
          label: 'U列',
          value: 20
        },
        {
          label: 'V列',
          value: 21
        },
        {
          label: 'W列',
          value: 22
        },
        {
          label: 'X列',
          value: 23
        },
        {
          label: 'Y列',
          value: 24
        },
        {
          label: 'Z列',
          value: 25
        }
      ],
      excelRows: [
        {
          label: '第一行',
          value: 0
        },
        {
          label: '第二行',
          value: 1
        },
        {
          label: '第三行',
          value: 2
        },
        {
          label: '第四行',
          value: 3
        },
        {
          label: '第五行',
          value: 4
        },
        {
          label: '第六行',
          value: 5
        },
        {
          label: '第七行',
          value: 6
        },
        {
          label: '第八行',
          value: 7
        },
        {
          label: '第九行',
          value: 8
        },
        {
          label: '第十行',
          value: 9
        },
        {
          label: '第十一行',
          value: 10
        },
        {
          label: '第十二行',
          value: 11
        },
        {
          label: '第十三行',
          value: 12
        },
        {
          label: '第十四行',
          value: 13
        },
        {
          label: '第十五行',
          value: 14
        },
        {
          label: '第十六行',
          value: 15
        },
        {
          label: '第十七行',
          value: 16
        },
        {
          label: '第十八行',
          value: 17
        },
        {
          label: '第十九行',
          value: 18
        },
        {
          label: '第二十行',
          value: 19
        }
      ],
      progressPdf: 0,
      progressExcel: 0,
      downloadUrl: ''
    }
  },
  computed: {},
  created() {
    this.init()
  },
  methods: {
    init() {
      this.downloadUrl = getApiUrl() + '/pdf/management/download'
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
      this.listQuery.id = ''
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

    add() {
      this.resetForm()
      this.formVisible = true
    },
    resetForm() {
      this.formVisible = false
      this.formData.name = ''
      this.formData.oldPdfStartPage = 1
      this.formData.oldPdfEndPage = undefined
      this.formData.oldExcelStartRow = 0
      this.formData.oldExcelStartColumn = 0
      this.formData.oldPdf = ''
      this.formData.oldExcel = ''
      if (this.$refs['uploadPdf'] !== undefined) {
        this.$refs['uploadPdf'].clearFiles()
      }
      if (this.$refs['uploadExcel'] !== undefined) {
        this.$refs['uploadExcel'].clearFiles()
      }
    },
    old_pdfBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 300
      if (!isRightSize) {
        this.$message.error('文件大小超过 300MB')
        return false
      }
      let isAccept = new RegExp('.pdf').test(file.type)
      if (!isAccept) {
        this.$message.error('应该选择.pdf类型的文件')
        return false
      }
      if (this.formData.oldPdf != '') {
        this.$message.error('请先移除掉上一个pdf文件')
        return false
      }
      return true
    },
    old_excelBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 300
      if (!isRightSize) {
        this.$message.error('文件大小超过 300MB')
        return false
      }
      const xls = file.name.split('.')
      if (xls[1] !== 'xls' && xls[1] !== 'xlsx') {
        this.$message.error('应该选择.xls,.xlsx类型的文件')
        return false
      }
      if (this.formData.oldExcel != '') {
        this.$message.error('请先移除掉上一个excel文件')
        return false
      }
      return true
    },
    // 上传文件
    uploadPdfFile(file) {
      const formData = new FormData()
      formData.append('file', file.file)
      var config = {
        // 上传进度
        onUploadProgress: progressEvent => {
          // 百分比
          let num = progressEvent.loaded / progressEvent.total * 100 | 0
          // 进度条
          this.progressPdf = num
        }
      }
      request.defaults.timeout = 1000 * 60 * 60 * 2
      request.post('/pdf/management/updaloadFile', formData, config).then(response => {
        this.formData.oldPdf = file.file.name
        this.$message({
          message: response.data,
          type: 'success'
        })
        // 上传成功(打钩的小图标)
        file.onSuccess()
      })
    },
    // 上传文件
    uploadExcelFile(file) {
      const formData = new FormData()
      formData.append('file', file.file)
      var config = {
        // 上传进度
        onUploadProgress: progressEvent => {
          // 百分比
          let num = progressEvent.loaded / progressEvent.total * 100 | 0
          // 进度条
          this.progressExcel = num
        }
      }
      request.defaults.timeout = 1000 * 60 * 60 * 2
      request.post('/pdf/management/updaloadFile', formData, config).then(response => {
        this.formData.oldExcel = file.file.name
        this.$message({
          message: response.data,
          type: 'success'
        })
        // 上传成功(打钩的小图标)
        file.onSuccess()
      })
    },
    // 用户移除了上传的pdf文件
    removePdf(file, fileList) {
      console.log(file)
      console.log(fileList)
      if (fileList.length === 0) {
        this.formData.oldPdf = ''
        this.progressPdf = 0
      }
    },
    // 用户移除了上传的excel文件
    removeExcel(file, fileList) {
      if (fileList.length === 0) {
        this.formData.oldExcel = ''
        this.progressExcel = 0
      }
    },
    // 提交表单
    submitForm() {
      this.$refs['formData'].validate((valid) => {
        if (valid) {
          console.log(this.formData)
          if (this.formData.oldExcel === '' || this.formData.oldPdf === '') {
            this.$message({
              message: '请上传文件',
              type: 'warning'
            })
            return
          }
          save(this.formData).then(response => {
            this.$message({
              message: response.data,
              type: 'success'
            })
            this.resetForm()
            this.fetchData()
          })
        } else {
          return false
        }
      })
    },
    download(id) {
      window.location.href = this.downloadUrl + '?id=' + id + '&Authorization=' + getToken()
    }
  }
}
