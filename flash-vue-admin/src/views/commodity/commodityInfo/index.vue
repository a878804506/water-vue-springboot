<template>
  <div class="app-container">
    <div class="block">
      <!-- 条件查询 开始 -->
      <el-row  :gutter="20">
        <el-col :span="3">
          <el-input v-model="listQuery.cname" size="mini" placeholder="请输入商品名称"></el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="success" size="mini" icon="el-icon-search" @click.native="search">{{ $t('button.search') }}</el-button>
          <el-button type="primary" size="mini" icon="el-icon-refresh" @click.native="reset">{{ $t('button.reset') }}</el-button>
        </el-col>
      </el-row>
      <br>
      <!-- 条件查询 结束 -->

      <el-row>
        <el-col :span="24">
          <el-button type="success" size="mini" icon="el-icon-plus" @click.native="add">{{ $t('button.add') }}
          </el-button>
          <el-button type="primary" size="mini" icon="el-icon-edit" @click.native="edit">{{ $t('button.edit') }}
          </el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click.native="remove">{{ $t('button.delete') }}
          </el-button>
        </el-col>
      </el-row>
    </div>


    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="商品编码">
        <template slot-scope="scope">
          {{scope.row.commodityNumber}}
        </template>
      </el-table-column>
      <el-table-column label="商品名称">
        <template slot-scope="scope">
          {{scope.row.commodityName}}
        </template>
      </el-table-column>
      <el-table-column label="商品产地">
        <template slot-scope="scope">
          {{scope.row.commodityOrigin}}
        </template>
      </el-table-column>
      <el-table-column label="商品分类">
        <template slot-scope="scope">
          {{scope.row.commodityTypeName}}
        </template>
      </el-table-column>
      <el-table-column label="批发价(￥)">
        <template slot-scope="scope">
          {{scope.row.commodityTradePrice}}
        </template>
      </el-table-column>
      <el-table-column label="销售价(￥)">
        <template slot-scope="scope">
          {{scope.row.commoditySalesPrice}}
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
      width="70%">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品编码" prop="commodityNumber">
              <el-input v-model="form.commodityNumber" minlength=1></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品名称" prop="commodityName">
              <el-input v-model="form.commodityName" minlength=1></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品产地" prop="commodityOrigin">
              <el-input v-model="form.commodityOrigin" minlength=1></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="商品分类" prop="commodityType">
              <el-select v-model="form.commodityType" minlength=1 >
                <el-option
                  v-for="item in commodityTypes"
                  :key="item.num"
                  :label="item.name"
                  :value="item.num">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批发价(￥)" prop="commodityTradePrice">
              <el-input v-model="form.commodityTradePrice" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销售价(￥)" prop="commoditySalesPrice">
              <el-input v-model="form.commoditySalesPrice" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="save">{{ $t('button.submit') }}</el-button>
          <el-button @click.native="formVisible = false">{{ $t('button.cancel') }}</el-button>
        </el-form-item>

      </el-form>
    </el-dialog>
  </div>
</template>

<script src="./commodityInfo.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>

