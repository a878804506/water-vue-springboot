<template>
  <div class="app-container waterStatistics">
    <div class="block">
      <el-form :inline="true" >
        <el-form-item label="统计区间" span="16">
          <el-date-picker
            v-model="serchDate"
            type="daterange"
            @change="changeDatePicker"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="success" size="mini" icon="el-icon-search" @click.native="search">{{ $t('button.search') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-check" @click.native="downloadExcel"  :disabled="downloadBtnDisabeld">{{$t('button.waterDownloadExcel') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-check" @click.native="downloadOriExcel"  :disabled="downloadBtnDisabeld">{{$t('button.waterDownloadOriExcel') }}</el-button>
        </el-form-item>
      </el-form>
      <div>
        已开票汇总金额为：<span class="statisticsInfo">{{waterInfoTotal}}</span>元，未开票的用户数为：<span class="statisticsInfo">{{waterCustomers.length}}</span>户
      </div>
    </div>

    <el-tabs class="tabs" v-model="activeName" type="card" @tab-click="tabClick">
      <el-tab-pane label="已开票" name="first">
        <el-table :data="waterInfos" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row>
          <el-table-column label="序号">
            <template slot-scope="scope">
              {{scope.$index + 1}}
            </template>
          </el-table-column>
          <el-table-column label="客户编号">
            <template slot-scope="scope">
              {{scope.row.id}}
            </template>
          </el-table-column>
          <el-table-column label="客户姓名">
            <template slot-scope="scope">
              {{scope.row.name}}
            </template>
          </el-table-column>
          <el-table-column label="开票类型">
            <template slot-scope="scope">
              {{scope.row.type === 1 ? '按月开票' : '包月'}}
            </template>
          </el-table-column>
          <el-table-column label="客户住址">
            <template slot-scope="scope">
              {{scope.row.address}}
            </template>
          </el-table-column>
          <el-table-column label="收费区间">
            <template slot-scope="scope">
              {{scope.row.remark}}
            </template>
          </el-table-column>
          <el-table-column label="开票金额">
            <template slot-scope="scope">
              {{scope.row.cost}}
            </template>
          </el-table-column>
          <el-table-column label="开票人">
            <template slot-scope="scope">
              {{scope.row.modifyName}}
            </template>
          </el-table-column>
          <el-table-column label="最后开票时间">
            <template slot-scope="scope">
              {{scope.row.modifyTime}}
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="未开票的用户" name="second">
        <el-table :data="waterCustomers" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row>
          <el-table-column label="序号">
            <template slot-scope="scope">
              {{scope.$index + 1}}
            </template>
          </el-table-column>
          <el-table-column label="客户编号">
            <template slot-scope="scope">
              {{scope.row.id}}
            </template>
          </el-table-column>
          <el-table-column label="客户姓名">
            <template slot-scope="scope">
              {{scope.row.name}}
            </template>
          </el-table-column>
          <el-table-column label="水费定价(￥)">
            <template slot-scope="scope">
              {{scope.row.price}}
            </template>
          </el-table-column>
          <el-table-column label="客户住址">
            <template slot-scope="scope">
              {{scope.row.address}}
            </template>
          </el-table-column>
          <el-table-column label="备注">
            <template slot-scope="scope">
              {{scope.row.starttime}}
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script src="./waterStatistics.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/waterStatistics.scss";
</style>
