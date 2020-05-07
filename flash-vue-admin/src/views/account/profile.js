
export default {
  data() {
    return {
      activeName: 'profile',
      user:{},
      form: {
        account: '',
        password: '',
        rePassword: ''
      },
      formVisible: false,
      formTitle: '绑定本系统账号'
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      this.user = this.$store.state.user.profile
    },
    handleClick(tab, event) {
      this.$router.push({ path: '/account/'+tab.name})
    },
    bindUser() {
      this.formVisible = true
      this.form.account = ''
      this.form.password = ''
      this.form.rePassword = ''
    },
    saveBind() {
      console.log(this.form)
      if (this.form.account === '' || this.form.password === '' || this.form.rePassword === '') {
        this.$message({
          message: this.$t('common.isRequired'),
          type: 'warning'
        })
        return
      }
      if (this.form.password !== this.form.rePassword) {
        this.$message({
          message: this.$t('common.twoPasswordsAreInconsistent'),
          type: 'warning'
        })
        return
      }
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.$store.dispatch('user/bindSystemUser', this.form).then(() => {
            this.$message({
              message: this.$t('common.bindMessage'),
              type: 'success'
            })
            const that = this
            setTimeout(function() {
              that.$router.go(0)
            },2000)
          }).catch((err) => {

          })
        } else {
          return false
        }
      })
    }
  }
}
