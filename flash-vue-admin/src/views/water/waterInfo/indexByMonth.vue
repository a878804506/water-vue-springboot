<template>
  <div class="app-container">
    <div class="block">
      <el-form :inline="true" :model="listQuery">
        <el-form-item label="月份">
          <el-select v-model="listQuery.month" size="medium">
            <el-option
              v-for="item in month"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="success" size="mini" icon="el-icon-search" @click.native="search">{{ $t('button.search') }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" v-loading="listLoading" border
              :cell-style="{fontSize:'20px',color:'red',fontWeight:'900'}">
      <el-table-column prop="waterCustomersCount" label="全镇总用户数" align="center"></el-table-column>
      <el-table-column prop="statusCount" label="报停用户数" align="center"></el-table-column>
      <el-table-column prop="shouldCustomerCount" label="应录入用户数" align="center"></el-table-column>
      <el-table-column prop="practicalCustomerCount" label="实际录入用户数" align="center"></el-table-column>
      <el-table-column prop="noPracticalCustomerCount" label="未录入用户数" align="center"></el-table-column>
      <el-table-column prop="waterCount" label="实际录入总用水量(m³)" align="center"></el-table-column>
      <el-table-column prop="waterCost" label="实际录入总水费(￥)" align="center"></el-table-column>
    </el-table>
    <br>
    <br>
    <br>

    <el-table :data="practicalCustomerWaterInfo" v-loading="listLoading" border height="600" >
      <el-table-column label="当前所选月份未录入账单的客户起止码信息" align="center" >
        <el-table-column prop="waterCustomer.name" label="客户姓名" align="center"></el-table-column>
        <el-table-column prop="one" label="一月" align="center"></el-table-column>
        <el-table-column prop="two" label="二月" align="center"></el-table-column>
        <el-table-column prop="three" label="三月" align="center"></el-table-column>
        <el-table-column prop="four" label="四月" align="center"></el-table-column>
        <el-table-column prop="five" label="五月" align="center"></el-table-column>
        <el-table-column prop="six" label="六月" align="center"></el-table-column>

        <el-table-column prop="seven" label="七月" align="center"></el-table-column>
        <el-table-column prop="eight" label="八月" align="center"></el-table-column>
        <el-table-column prop="nine" label="九月" align="center"></el-table-column>
        <el-table-column prop="ten" label="十月" align="center"></el-table-column>
        <el-table-column prop="eleven" label="十一月" align="center"></el-table-column>
        <el-table-column prop="twelve" label="十二月" align="center"></el-table-column>
      </el-table-column>
    </el-table>
  </div>
</template>

<script src="./waterInfoByMonth.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>
