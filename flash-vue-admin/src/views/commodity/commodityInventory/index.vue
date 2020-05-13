<template>
  <div class="app-container">
    <!-- 条件查询 开始 -->
    <el-row :gutter="20">
      <el-col :span="3">
        <el-input v-model="listQuery.cname" size="mini" placeholder="请输入商品名称"></el-input>
      </el-col>
      <el-col :span="6">
        <el-button type="success" size="mini" icon="el-icon-search" @click.native="search">{{ $t('button.search') }}
        </el-button>
        <el-button type="primary" size="mini" icon="el-icon-refresh" @click.native="reset">{{ $t('button.reset') }}
        </el-button>
      </el-col>
    </el-row>
    <br>
    <!-- 条件查询 结束 -->

    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="商品编码">
        <template slot-scope="scope">
          {{scope.row.commodityInfo.commodityNumber}}
        </template>
      </el-table-column>
      <el-table-column label="商品名称">
        <template slot-scope="scope">
          {{scope.row.commodityInfo.commodityName}}
        </template>
      </el-table-column>
      <el-table-column label="商品产地">
        <template slot-scope="scope">
          {{scope.row.commodityInfo.commodityOrigin}}
        </template>
      </el-table-column>
      <el-table-column label="商品分类">
        <template slot-scope="scope">
          {{scope.row.commodityTypeName}}
        </template>
      </el-table-column>
      <el-table-column label="商品库存量">
        <template slot-scope="scope">
          {{scope.row.commodityNum}}
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" size="mini" icon="el-icon-plus" @click.native="inInventory(scope.row)">{{
            $t('button.inInventory') }}
          </el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click.native="outInventory(scope.row)">{{
            $t('button.outInventory') }}
          </el-button>
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

    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="50%">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="商品名称">
              <span class="text">{{form.commodityName}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品现有库存量">
              <span  class="text">{{form.commodityNowNum}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="formTypeName" prop="commodityNum">
              <el-input v-model="form.commodityNum" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.commodityRemarks" minlength=1 type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="submitOrder">{{ $t('button.submit') }}</el-button>
          <el-button @click.native="formVisible = false">{{ $t('button.cancel') }}</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script src="./commodityInventory.js"></script>


<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";

  .text {
    font-size: 16px;
  }
</style>

