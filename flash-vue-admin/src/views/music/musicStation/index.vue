<template>
  <div class="app-container">
    <div class="block">
      <el-form :inline="true" :model="listQuery">
        <el-form-item label="来源平台">
          <el-select v-model="listQuery.platform" size="medium" style="width: 150px">
            <el-option
              v-for="item in platformDatas"
              :key="item.id"
              :label="item.nameCn"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关键字">
          <el-input v-model="listQuery.keyword" size="medium" placeholder="关键字" type="text"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" size="mini" icon="el-icon-search" @click.native="search">{{ $t('button.search') }}
          </el-button>
          <el-button type="primary" size="mini" icon="el-icon-refresh" @click.native="reset">{{ $t('button.reset') }}
          </el-button>
          <el-button type="primary" size="mini" icon="el-icon-service" @click.native="getMusicById">试听</el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click.native="remove">{{ $t('button.delete') }}
          </el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <audio ref="audio" :src="musicEntity.src" controls="controls"></audio>
        </el-form-item>
      </el-form>

    </div>

    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="歌曲名称">
        <template slot-scope="scope">
          {{scope.row.name}}
        </template>
      </el-table-column>
      <el-table-column label="歌手">
        <template slot-scope="scope">
          {{scope.row.singers}}
        </template>
      </el-table-column>
      <el-table-column label="图片地址">
        <template slot-scope="scope">
          <el-image :src="scope.row.picUrl" style="width: 60px;height:60px;border-radius: 100px" fit="fit"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="音乐来源平台">
        <template slot-scope="scope">
          {{platformDatas[scope.row.platformId-1].nameCn}}
        </template>
      </el-table-column>
      <el-table-column label="有高品质音乐?">
        <template slot-scope="scope">
          {{scope.row.hasHQ}}
        </template>
      </el-table-column>
      <el-table-column label="有无损音乐?">
        <template slot-scope="scope">
          {{scope.row.hasSQ}}
        </template>
      </el-table-column>
      <el-table-column label="有MV?">
        <template slot-scope="scope">
          {{scope.row.hasMV}}
        </template>
      </el-table-column>
      <el-table-column label="有专辑?">
        <template slot-scope="scope">
          {{scope.row.hasAlbum}}
        </template>
      </el-table-column>
      <el-table-column label="专辑id">
        <template slot-scope="scope">
          {{scope.row.albumId}}
        </template>
      </el-table-column>
      <el-table-column label="专辑名称">
        <template slot-scope="scope">
          {{scope.row.albumName}}
        </template>
      </el-table-column>
      <el-table-column label="音乐类型">
        <template slot-scope="scope">
          <span v-if="scope.row.musicType === 1">标准品质</span>
          <span v-if="scope.row.musicType === 2">高品质</span>
          <span v-if="scope.row.musicType === 3">无损品质</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="音乐播放地址">
        <template slot-scope="scope">
          {{scope.row.musicUrl}}
        </template>
      </el-table-column>-->
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

<script src="./musicStation.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>

