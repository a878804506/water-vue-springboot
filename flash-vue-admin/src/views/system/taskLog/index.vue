<template>
  <div class="app-container">
    <div class="block">
      <el-row>
        <el-col :span="24">
          <el-button icon="el-icon-back" size="mini" @click.native="back">{{ $t('button.back') }}</el-button>
        </el-col>
      </el-row>
    </div>
    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row>

      <el-table-column label="任务名" width="240">
        <template slot-scope="scope">
          {{scope.row.name}}
        </template>
      </el-table-column>
      <el-table-column label="执行时间" width="180">
        <template slot-scope="scope">
          {{scope.row.execAt}}
        </template>
      </el-table-column>
      <el-table-column label="执行结果" width="130">
        <template slot-scope="scope">
          {{scope.row.execSuccess === 1 ? '成功' : '失败'}}
        </template>
      </el-table-column>
      <el-table-column label="异常信息" width="530">
        <template slot-scope="scope">
          {{scope.row.jobExecption}}
        </template>
      </el-table-column>

      <el-table-column label="任务自定义消息">
        <template slot-scope="scope">
          {{scope.row.successLog}}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50, 100,500]"
      :page-size="listQuery.limit"
      :total="total"
      @size-change="changeSize"
      @current-change="fetchPage"
      @prev-click="fetchPrev"
      @next-click="fetchNext">
    </el-pagination>
  </div>
</template>

<script src="./log.js"></script>


<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>

