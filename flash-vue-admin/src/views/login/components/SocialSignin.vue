<template>
  <div class="social-signup-container">
    <div class="sign-btn" @click="dingidngHandleClick()">
      <span class="dingding-svg-container"><svg-icon icon-class="dingding" class="icon" /></span>
      钉钉
    </div>
  </div>
</template>

<script>

export default {
  name: 'SocialSignin',
  methods: {
    dingidngHandleClick() {
      const appid = 'dingoaodwhront5y4ogucf'
      const redirect_uri = encodeURIComponent(window.location.origin + '/auth-redirect')
      const url = 'https://oapi.dingtalk.com/connect/qrconnect?appid=' + appid + '&response_type=code&scope=snsapi_login&state=STATE&redirect_uri=' + redirect_uri
      window.location.href = url
    }
  }
}
</script>

<style lang="scss" scoped>
  .social-signup-container {
    margin: 20px 10px;
    .sign-btn {
      display: inline-block;
      cursor: pointer;
    }
    .icon {
      color: #fff;
      font-size: 35px;
      margin-top: 8px;
    }
    .dingding-svg-container {
      display: inline-block;
      width: 40px;
      height: 40px;
      line-height: 40px;
      text-align: center;
      padding-top: 1px;
      border-radius: 4px;
      margin-bottom: 20px;
      margin-right: 5px;
    }
  }
</style>
