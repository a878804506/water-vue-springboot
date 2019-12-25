<template>
  <div class="app-container">
    <div class="block">
      <!-- 条件查询 开始 -->
      <el-row  :gutter="20">
        <el-col :span="3">
          <el-input v-model="listQuery.name" size="mini" placeholder="请输入客户姓名"></el-input>
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
          <el-button type="primary" size="mini" icon="el-icon-edit" @click.native="edit">{{ $t('button.edit') }}
          </el-button>
        </el-col>
      </el-row>
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
      <el-table-column label="一月">
        <template slot-scope="scope">
          {{scope.row.one}}
        </template>
      </el-table-column>
      <el-table-column label="二月">
        <template slot-scope="scope">
          {{scope.row.two}}
        </template>
      </el-table-column>
      <el-table-column label="三月">
        <template slot-scope="scope">
          {{scope.row.three}}
        </template>
      </el-table-column>
      <el-table-column label="四月">
        <template slot-scope="scope">
          {{scope.row.four}}
        </template>
      </el-table-column>
      <el-table-column label="五月">
        <template slot-scope="scope">
          {{scope.row.five}}
        </template>
      </el-table-column>
      <el-table-column label="六月">
        <template slot-scope="scope">
          {{scope.row.six}}
        </template>
      </el-table-column>
      <el-table-column label="七月">
        <template slot-scope="scope">
          {{scope.row.seven}}
        </template>
      </el-table-column>
      <el-table-column label="八月">
        <template slot-scope="scope">
          {{scope.row.eight}}
        </template>
      </el-table-column>
      <el-table-column label="九月">
        <template slot-scope="scope">
          {{scope.row.nine}}
        </template>
      </el-table-column>
      <el-table-column label="十月">
        <template slot-scope="scope">
          {{scope.row.ten}}
        </template>
      </el-table-column>
      <el-table-column label="十一月">
        <template slot-scope="scope">
          {{scope.row.eleven}}
        </template>
      </el-table-column>
      <el-table-column label="十二月">
        <template slot-scope="scope">
          {{scope.row.twelve}}
        </template>
      </el-table-column>
      <el-table-column label="是否可用">
        <template slot-scope="scope">
          {{scope.row.deleteName}}
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
          <el-col :span="8">
            <el-form-item label="一月">
              <el-input v-model="form.one" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="二月">
              <el-input v-model="form.two" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="三月">
              <el-input v-model="form.three" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="四月">
              <el-input v-model="form.four" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="五月">
              <el-input v-model="form.five" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="六月">
              <el-input v-model="form.six" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="七月">
              <el-input v-model="form.seven" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="八月">
              <el-input v-model="form.eight" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="九月">
              <el-input v-model="form.nine" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="十月">
              <el-input v-model="form.ten" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="十一月">
              <el-input v-model="form.eleven" minlength=1 type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="十二月">
              <el-input v-model="form.twelve" minlength=1 type="number"></el-input>
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

<script src="./waterMeter.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>
