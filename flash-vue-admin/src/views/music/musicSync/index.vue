<template>
  <div class="app-container">
    <div class="block">
      <el-form :inline="true" :model="listQuery">
        <el-form-item label="来源平台">
          <el-select v-model="listQuery.platform" size="medium">
            <el-option
              v-for="item in platformDatas"
              :key="item.id"
              :label="item.nameCn"
              :value="item.nameEn"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关键字">
          <el-input v-model="listQuery.keyword" size="medium" placeholder="关键字" type="text"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" size="mini" icon="el-icon-search" @click.native="searchSongs">{{ $t('button.search') }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="block">
      <el-form :inline="true" :model="listQuery">
        <el-form-item label="同步的音乐品质">
          <el-select v-model="listQuery.syncType" size="medium">
            <el-option
              v-for="item in syncType"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
          <el-button type="primary" size="mini" icon="el-icon-connection" @click.native="syncSongs">{{ $t('button.musicSync') }}</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="songsList" v-loading="listLoading"
              element-loading-text="Loading" border fit highlight-current-row
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="70"></el-table-column>
      <el-table-column label="歌曲名称" width="200%">
        <template slot-scope="scope">
          {{scope.row.name}}
        </template>
      </el-table-column>
      <el-table-column label="歌手" width="200%">
        <template slot-scope="scope">
          {{scope.row.singers}}
        </template>
      </el-table-column>
      <el-table-column label="图片地址" width="150%">
        <template slot-scope="scope">
          <el-image :src="scope.row.picUrl" style="width: 60px;height:60px;border-radius: 100px" fit="fit"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="高品质" width="150%">
        <template slot-scope="scope">
          {{scope.row.hasHQ}}
        </template>
      </el-table-column>
      <el-table-column label="无损音乐" width="150%">
        <template slot-scope="scope">
          {{scope.row.hasSQ}}
        </template>
      </el-table-column>
      <el-table-column label="是否有MV" width="150%">
        <template slot-scope="scope">
          {{scope.row.hasMV}}
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
      <el-table-column label="专辑名称" width="200%">
        <template slot-scope="scope">
          {{scope.row.albumName}}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[20]"
      :page-size="listQuery.pageSize"
      :total="total"
      @size-change="changeSize"
      @current-change="fetchPage"
      @prev-click="fetchPrev"
      @next-click="fetchNext">
    </el-pagination>
  </div>
</template>

<script src="./musicSync.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>

