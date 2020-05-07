<script>
export default {
  name: 'AuthRedirect',
  created() {
    // 钉钉扫码登陆
    this.$store.dispatch('user/checkDingDingLogin', this.$route.query).then(response => {
      if( response.isSystemUser ){
        this.$router.push({ path: '/' })
      }else{
        this.$router.push({ path: '/account/profile' })
      }
    }).catch((err) => {
      this.loading = false
    })
  },
  render: function (h) {
    return h() // avoid warning message
  }
}
</script>
