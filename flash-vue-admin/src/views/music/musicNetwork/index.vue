<template>
  <div class="myBakImg">
    <div class="myMusicDiv">
      <aplayer autoplay
               :music="music"
               :list="musicList"
               :shuffle="shuffle"
               listMaxHeight="500px"
               preload="auto"
               ref="aplayer"
               :token="token"
               :serverAddress="serverAddress"
               :stationModel="stationModel"
      ></aplayer>
    </div>
    <div class="myDiv">
      <div class="myRow">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="来源平台">
            <el-select v-model="listQuery.platform" size="medium" style="width: 120px">
              <el-option
                v-for="item in platformDatas"
                :key="item.id"
                :label="item.nameCn"
                :value="item.nameEn"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="关键字">
            <el-input v-model="listQuery.keyword" size="medium" placeholder="关键字" type="text"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="success" size="mini" icon="el-icon-search" @click.native="searchBtn">{{ $t('button.search') }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="myRow" v-for="item in songsList" @click="addMusic(item)">
        <div class="myPic">
          <el-image :src="item.picUrl" fit="fit"></el-image>
        </div>
        <div class="myInfo">
          <div class="mySong">
            {{item.name}} - 《{{item.albumName}}》
          </div>
          <div class="mySinger">
            <span>{{item.singers}}</span>
            <el-tag effect="dark" type="warning" size="mini" v-show="item.hasSQ">SQ</el-tag>
            <el-tag effect="dark" type="success" size="mini" v-show="item.hasHQ">HQ</el-tag>
          </div>
        </div>
        <div class="myBorder"></div>
      </div>
    </div>
    <el-dialog
      :visible.sync="listLoading"
      width="12%"
      top="40vh"
      custom-class="myDialog">
      <i class="el-icon-loading"></i><span> 加载中。。</span>
    </el-dialog>
  </div>
</template>

<script src="./musicNetwork.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>
<style>
  .myDialog{
    background-color: #5e6165;
  }
  .myDialog span{
    color: #f7f8fa;
  }
  .myDialog i{
    color: #f7f8fa;
  }
</style>
<style rel="stylesheet/scss" lang="scss" scoped>
  .myBakImg {
    width: 100%;
    background-color: #f7f8fa;
    background-size: cover;
    background-image: url(http://qzonestyle.gtimg.cn/qzone/qzactStatics/imgs/20171122191532_f2975b.jpg);
    background-repeat: no-repeat;
    background-position: 50%;
    background-attachment: fixed;
  }
  .myDiv{
    min-height: 900px;
  }
  .myRow {
    width: 50%;
    margin: 0 auto 0 auto;
    padding-top: 18px;
    padding-left: 55px;
  }
  .myPic {
    width: 15%;
    float: left;
    height: 80px;
  }
  .el-image {
    width: 80px;
    height: 80px;
    border-radius: 100px;
  }
  .myInfo {
    width: 100%;
    height: 80px;
  }
  .mySong {
    padding-top: 18px;
    padding-bottom: 10px;
  }
  .mySinger {

  }
  .myBorder {
    position: relative;
    margin-top: 10px;
    width: 100%;
    height: 1px;
    background-color: green;
    text-align: center;
    font-size: 16px;
    color: rgba(101, 101, 101, 1);
  }
  .myMusicDiv {
    float: left;
    width: 25%;
    position: fixed;
    z-index: 999;
  }
</style>
