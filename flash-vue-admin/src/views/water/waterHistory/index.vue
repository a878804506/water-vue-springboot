<template>
  <div class="app-container">
    <div class="block">
      <!-- 条件查询 开始 -->
      <el-row  :gutter="20">
        <el-col :span="3">
          <el-input v-model="listQuery.cname" size="mini" placeholder="请输入客户姓名"></el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="success" size="mini" icon="el-icon-search" @click.native="search">{{ $t('button.search') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-refresh" @click.native="reset">{{ $t('button.reset') }}</el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" :disabled="(selRow !== null && selRow.cancel === 1)" @click.native="waterCancel">{{ $t('button.waterCancel') }}</el-button>
        </el-col>
      </el-row>
      <br>
      <!-- 条件查询 结束 -->
    </div>

    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="客户编号">
        <template slot-scope="scope">
          {{scope.row.cid}}
        </template>
      </el-table-column>
      <el-table-column label="客户姓名">
        <template slot-scope="scope">
          {{scope.row.cname}}
        </template>
      </el-table-column>
      <el-table-column label="最后开票时间">
        <template slot-scope="scope">
          {{scope.row.modifyTime}}
        </template>
      </el-table-column>
      <el-table-column label="开票周期">
        <template slot-scope="scope">
          {{scope.row.remark}}
        </template>
      </el-table-column>
      <el-table-column label="开票类型">
        <template slot-scope="scope">
          {{scope.row.type === 1 ? '按月开票' : '包月'}}
        </template>
      </el-table-column>
      <el-table-column label="开票人">
        <template slot-scope="scope">
          {{scope.row.modifyName}}
        </template>
      </el-table-column>
      <el-table-column label="开票金额(元)">
        <template slot-scope="scope">
          {{scope.row.cost}}
        </template>
      </el-table-column>
      <el-table-column label="已作废？">
        <template slot-scope="scope">
          {{scope.row.cancel === 1 ? '是' : '否'}}
        </template>
      </el-table-column>
      <el-table-column label="作废原因">
        <template slot-scope="scope">
          {{scope.row.cancel === 1 ? scope.row.reason : '暂无'}}
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

    <!-- 表单 -->
    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="35%">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="20">
            <el-form-item label="作废原因" prop="reason">
              <el-input type="textarea" v-model="form.reason" placeholder="请输入作废原因" maxlength="50" show-word-limit rows="4"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="waterCancelSubmit">{{ $t('button.submit') }}</el-button>
          <el-button @click.native="formVisible = false;form.id = null;form.remark = null; form.reason = ''">{{ $t('button.cancel') }}</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>
  </div>
</template>

<script src="./waterHistory.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>
