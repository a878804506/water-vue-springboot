<template>
  <div class="app-container">
    <div class="block">
    </div>

    <el-tabs tab-position="left" style="height: 200px;">
      <el-tab-pane v-for="(item,index) in tabs" :key="index" :label="item">哈哈哈</el-tab-pane>
    </el-tabs>

    <br>
    <br>
    <br>

  </div>
</template>

<script src="./waterInfoByToDay.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>
