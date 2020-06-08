<template>
  <div class="app-container">

    <div class="block">
      <el-form :inline="true" :model="listQuery">

        <el-form-item label="歌曲名称">
          <el-input v-model="listQuery.name" size="medium" placeholder="歌曲名称" type="text"></el-input>
        </el-form-item>

        <el-form-item label="音乐同步状态">
          <el-select v-model="listQuery.syncStatus" size="medium">
            <el-option
              v-for="item in syncStatusList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>

          <el-form-item>
          <el-button type="success" size="mini" icon="el-icon-search" @click.native="getMusicSyncTask">{{ $t('button.search')
            }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="歌曲名称" width="240%">
        <template slot-scope="scope">
          {{scope.row.name}}
        </template>
      </el-table-column>
      <el-table-column label="歌手" width="240%">
        <template slot-scope="scope">
          {{scope.row.singers}}
        </template>
      </el-table-column>
      <el-table-column label="是否有专辑" width="150%">
        <template slot-scope="scope">
          {{scope.row.hasAlbum}}
        </template>
      </el-table-column>
      <el-table-column label="专辑id" width="200%">
        <template slot-scope="scope">
          {{scope.row.albumId}}
        </template>
      </el-table-column>
      <el-table-column label="专辑名称" width="240%">
        <template slot-scope="scope">
          {{scope.row.albumName}}
        </template>
      </el-table-column>
      <el-table-column label="同步音乐品质" width="150%">
        <template slot-scope="scope">
          <span v-if="scope.row.syncType == '128'">标准品质</span>
          <span v-if="scope.row.syncType == '320'">高品质</span>
          <span v-if="scope.row.syncType == 'flac'">无损品质</span>
        </template>
      </el-table-column>
      <el-table-column label="同步状态" width="200%">
        <template slot-scope="scope">
          <span v-if="scope.row.syncStatus == 0">待同步</span>
          <span v-if="scope.row.syncStatus == 1">同步成功</span>
          <span v-if="scope.row.syncStatus == 2">同步失败</span>
          <span v-if="scope.row.syncStatus == 3">无相应品质的音乐资源</span>
          <span v-if="scope.row.syncStatus == 4">指定条件下没有找到该歌曲</span>
        </template>
      </el-table-column>
      <el-table-column label="任务创建时间" width="200%">
        <template slot-scope="scope">
          {{scope.row.createTime}}
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

<script src="./musicSyncTask.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>

