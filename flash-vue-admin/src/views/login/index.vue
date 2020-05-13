<template>
  <!--<div class="login-container" style=" background-image: url(/img/bg.jpg);">-->
  <div class="login-container" >
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form cyh-css" auto-complete="on"
             label-position="left">

      <div class="title-container">
        <h3 class="title">{{ $t('login.title') }}</h3>
        <lang-select class="set-language"/>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user"/>
        </span>
        <el-input
          v-model="loginForm.username"
          :placeholder="$t('login.username')"
          name="username"
          type="text"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password"/>
        </span>
        <el-input
          :type="pwdType"
          v-model="loginForm.password"
          :placeholder="$t('login.password')"
          name="password"
          auto-complete="on"
          @keyup.enter.native="handleLogin"/>
        <span class="show-pwd" @click="showPwd">
          <svg-icon icon-class="eye"/>
        </span>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
                 @click.native.prevent="handleLogin">{{ $t('login.logIn') }}
      </el-button>

      <div style="position:relative">
        <div class="tips">
          <span></span>
          <span></span>
        </div>
        <div class="tips">
          <span style="margin-right:18px;"></span>
          <span></span>
        </div>
        <el-button class="thirdparty-button" type="primary" @click="showDialog=true">
          {{ $t('login.otherLogin') }}
        </el-button>
      </div>
    </el-form>

    <el-dialog :title="$t('login.otherLoginTips')" :visible.sync="showDialog">
      <br>
      <social-sign />
    </el-dialog>

    <vue-particles
      color="#00bfff"
      :particleOpacity="0.7"
      :particlesNumber="60"
      shapeType="star"
      :particleSize="4"
      linesColor="#fff"
      :linesWidth="1"
      :lineLinked="true"
      :lineOpacity="0.4"
      :linesDistance="150"
      :moveSpeed="2"
      :hoverEffect="true"
      hoverMode="grab"
      :clickEffect="true"
      clickMode="push"
    >
    </vue-particles>
  </div>

</template>

<script src="./login.js"></script>
<style rel="stylesheet/scss" lang="scss" src="./login.scss"></style>

<style rel="stylesheet/scss" lang="scss" scoped>
  .cyh-css {
    position: absolute;
    left: 50%;
    top: 20%;
    margin-left: -250px;
    margin-top: -200px;
    z-index: 999;
  }
  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }
  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
</style>

<!--
https://www.jianshu.com/p/53199b842d25
color: String类型。默认'#dedede'。粒子颜色。
particleOpacity: Number类型。默认0.7。粒子透明度。
particlesNumber: Number类型。默认80。粒子数量。
shapeType: String类型。默认'circle'。可用的粒子外观类型有："circle","edge","triangle", "polygon","star"。
particleSize: Number类型。默认80。单个粒子大小。
linesColor: String类型。默认'#dedede'。线条颜色。
linesWidth: Number类型。默认1。线条宽度。
lineLinked: 布尔类型。默认true。连接线是否可用。
lineOpacity: Number类型。默认0.4。线条透明度。
linesDistance: Number类型。默认150。线条距离。
moveSpeed: Number类型。默认3。粒子运动速度。
hoverEffect: 布尔类型。默认true。是否有hover特效。
hoverMode: String类型。默认true。可用的hover模式有: "grab", "repulse", "bubble"。
clickEffect: 布尔类型。默认true。是否有click特效。
clickMode: String类型。默认true。可用的click模式有: "push", "remove", "repulse", "bubble"。
-->

