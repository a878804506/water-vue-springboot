<template>
  <div class="app-container">
    <div >
      <span class="controller">解析线路</span>
      <el-select v-model="selectedLabel" placeholder="请选择" style="width: 15%">
        <el-option v-for="url in urlList"
                   :key="url.id"
                   :label="url.urlName"
                   :value="url.url"
        ></el-option>
      </el-select>

      <span class="controller" style="margin-left: 25px">播放地址</span>
      <el-input v-model="userUrl" placeholder="请将播放地址粘贴于此" style="width: 40%"></el-input>

      <el-button type="success" icon="el-icon-view" style="margin-left: 25px" @click="playMovie">GO-点击开始解析</el-button>

      <el-button type="info" icon="el-icon-star-off"style="margin-left: 25px" @click="showHelp = true">使用帮助</el-button>
    </div>

    <!--http://jx.bwcxy.com/?v=https://www.iqiyi.com/v_19rr78nouw.html-->
    <div style="text-align: center; margin-top: 20px">
      <iframe :src="playUrl" width="80%" height="568px" frameborder="0" scrolling="auto"
              style="" webkitallowfullscreen="" mozallowfullscreen="" allowfullscreen=""></iframe>
    </div>

    <el-dialog
      title="使用说明"
      :visible.sync="showHelp"
      width="60%" center>
      <el-row class="myRow">
        <el-tag class="helpText">
          前言：
        </el-tag>
        <el-tag type="success" class="helpText">
          在这里理论上可以看到全网视频（优酷，爱奇艺等等）,包括vip视频
        </el-tag>
      </el-row>
      <el-row class="myRow">
        <el-tag class="helpText">
          解析线路：
        </el-tag>
        <el-tag type="success" class="helpText">
          请任意选择一个，这里选择的是一个免费的站外解析器，如果解析失败，请更换线路！
        </el-tag>
      </el-row>
      <el-row class="myRow">
        <el-tag class="helpText">
          播放地址：
        </el-tag>
        <el-tag type="success" class="helpText">
          这个输入框输入需要观看的播放地址，
          这个地址可以在各大视频网站上搜索进入到播放页面时把浏览器地址栏的网址粘贴于此就可以了
        </el-tag>
      </el-row>
      <el-row class="myRow">
        <el-tag class="helpText">
          播放地址示例：
        </el-tag>
        <el-tag type="danger" class="helpText">
          https://www.iqiyi.com/v_19rr78nouw.html
        </el-tag>
      </el-row>
      <el-row class="myRow">
        <el-button @click.native="showHelp = false" type="info" style="float: right;">我知道了</el-button>
      </el-row>
    </el-dialog>
  </div>
</template>

<script src="./moviePlay.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>

<style>
  .controller{
    font-weight: 800;
    font-size: 18px;
    color: #555;
    text-align: center;
  }

  .helpText{
    font-weight: 800;
    font-size: 14px;
  }

  .myRow{
    margin-bottom: 5px;
  }
</style>
