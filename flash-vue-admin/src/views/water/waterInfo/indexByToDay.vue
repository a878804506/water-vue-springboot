<template>
  <div class="app-container">
    <div class="block">
    </div>

    <el-tabs tab-position="left" @tab-click="clickTab">
      <el-tab-pane v-for="(item,index) in tabs" :key="index" :label="item">
        <el-table :data="totalDatas" v-loading="listLoading" border
                  :cell-style="{fontSize:'20px',color:'red',fontWeight:'900'}">
          <el-table-column prop="toDayCount" label="今日录入总数" align="center"></el-table-column>
          <el-table-column prop="thisCount" label="本次查询客户数" align="center"></el-table-column>
          <el-table-column prop="water" label="本次查询客户总用水量(m³)" align="center"></el-table-column>
          <el-table-column prop="cost" label="本次查询客户总水费(元)" align="center"></el-table-column>
        </el-table>
        <br>
        <br>
        <el-table :data="customerDatas" v-loading="listLoading" border height="600">
          <el-table-column prop="cid" label="编号" align="center"></el-table-column>
          <el-table-column prop="cname" label="客户姓名" align="center"></el-table-column>
          <el-table-column prop="modifyTime" label="录入时间" align="center"></el-table-column>
          <el-table-column prop="count" label="用水量(m³)" align="center"></el-table-column>
          <el-table-column prop="cost" label="水费(元)" align="center"></el-table-column>
        </el-table>

      </el-tab-pane>
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
