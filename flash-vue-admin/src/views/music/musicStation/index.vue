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
          <el-button type="danger" size="mini" icon="el-icon-delete" @click.native="remove" v-show="showDeleteBtn">{{$t('button.delete') }}
          </el-button>

          <el-button type="warning" size="mini" icon="el-icon-star-off" @click="showMoreSelect()" v-show="moreSelectBox">批量收藏</el-button>
          <el-button type="success" size="mini" icon="el-icon-check" @click="beforeAddFavoriteList()" v-show="!moreSelectBox">确认收藏</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <audio ref="audio" :src="musicEntity.src" controls="controls"></audio>
        </el-form-item>
      </el-form>

    </div>

    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="70" v-if="!moreSelectBox"></el-table-column>
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
      <el-table-column label="音乐品质">
        <template slot-scope="scope">
          <span v-if="scope.row.musicType === 1">标准品质</span>
          <span v-if="scope.row.musicType === 2">高品质</span>
          <span v-if="scope.row.musicType === 3">无损品质</span>
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
      <!--<el-table-column label="有高品质音乐?">
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
      </el-table-column>-->
      <el-table-column label="专辑名称">
        <template slot-scope="scope">
          {{scope.row.albumName}}
        </template>
      </el-table-column>

      <!--<el-table-column label="音乐播放地址">
        <template slot-scope="scope">
          {{scope.row.musicUrl}}
        </template>
      </el-table-column>-->

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button icon="el-icon-star-off" circle
                     type="danger" @click="delFavorite(scope.row,$event)" v-show="scope.row.userFavorite.isUserFavorite == true"></el-button>
          <el-button icon="el-icon-star-off" circle
                     type="" @click="beforeAddFavorite(scope.row)" v-show="scope.row.userFavorite.isUserFavorite == false"></el-button>
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
      title="收藏到..."
      :visible.sync="favoriteDialog"
      width="16%">
      <el-row>
        <el-col :span="12">
          <el-select v-model="selectedFavorite" size="medium" style="width: 150px" >
            <el-option
              v-for="item in favoriteList"
              :key="item.id"
              :label="item.favoriteName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row style="margin-top: 10px">
        <el-button type="primary" @click="addFavorite" v-show="showAddOrAddAll">收藏</el-button>
        <el-button type="primary" @click="addFavoriteList" v-show="!showAddOrAddAll">批量收藏</el-button>
        <el-button @click.native="favoriteDialog = false">取消</el-button>
      </el-row>
    </el-dialog>
  </div>
</template>

<script src="./musicStation.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>

