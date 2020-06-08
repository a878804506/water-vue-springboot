<template>
    <div class="app-container">
        <div class="block">
            <el-row>
                <el-col :span="24">
                    <el-button type="success" size="mini"  icon="el-icon-plus" @click.native="add">{{ $t('button.add') }}</el-button>
                    <el-button type="primary" size="mini"  icon="el-icon-edit" @click.native="edit">{{ $t('button.edit') }}</el-button>
                    <el-button type="danger" size="mini"  icon="el-icon-delete" @click.native="remove">{{ $t('button.delete') }}</el-button>
                </el-col>
            </el-row>
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
                    {{scope.row.picUrl}}
                </template>
            </el-table-column>
            <el-table-column label="是否有高品质音乐">
                <template slot-scope="scope">
                    {{scope.row.hasHq}}
                </template>
            </el-table-column>
            <el-table-column label="是否有无损音乐">
                <template slot-scope="scope">
                    {{scope.row.hasSq}}
                </template>
            </el-table-column>
            <el-table-column label="是否有MV">
                <template slot-scope="scope">
                    {{scope.row.hasMv}}
                </template>
            </el-table-column>
            <el-table-column label="是否有专辑">
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
            <el-table-column label="是否同步了标准品质音乐">
                <template slot-scope="scope">
                    {{scope.row.hasSync128}}
                </template>
            </el-table-column>
            <el-table-column label="同步的标准品质音乐播放地址">
                <template slot-scope="scope">
                    {{scope.row.quality128Url}}
                </template>
            </el-table-column>
            <el-table-column label="是否同步了高品质音乐">
                <template slot-scope="scope">
                    {{scope.row.hasSyncHq}}
                </template>
            </el-table-column>
            <el-table-column label="同步的高品质音乐播放地址">
                <template slot-scope="scope">
                    {{scope.row.qualityHqUrl}}
                </template>
            </el-table-column>
            <el-table-column label="是否同步了无损音乐">
                <template slot-scope="scope">
                    {{scope.row.hasSyncSq}}
                </template>
            </el-table-column>
            <el-table-column label="同步的无损音乐播放地址">
                <template slot-scope="scope">
                    {{scope.row.qualitySqUrl}}
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
                        <el-form-item label="歌曲名称"  >
                            <el-input v-model="form.name" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="歌手"  >
                            <el-input v-model="form.singers" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="图片地址"  >
                            <el-input v-model="form.picUrl" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否有高品质音乐"  >
                            <el-input v-model="form.hasHq" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否有无损音乐"  >
                            <el-input v-model="form.hasSq" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否有MV"  >
                            <el-input v-model="form.hasMv" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否有专辑"  >
                            <el-input v-model="form.hasAlbum" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="专辑id"  >
                            <el-input v-model="form.albumId" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="专辑名称"  >
                            <el-input v-model="form.albumName" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否同步了标准品质音乐"  >
                            <el-input v-model="form.hasSync128" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="同步的标准品质音乐播放地址"  >
                            <el-input v-model="form.quality128Url" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否同步了高品质音乐"  >
                            <el-input v-model="form.hasSyncHq" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="同步的高品质音乐播放地址"  >
                            <el-input v-model="form.qualityHqUrl" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否同步了无损音乐"  >
                            <el-input v-model="form.hasSyncSq" minlength=1></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="同步的无损音乐播放地址"  >
                            <el-input v-model="form.qualitySqUrl" minlength=1></el-input>
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

<script src="./musicStation.js"></script>


<style rel="stylesheet/scss" lang="scss" scoped>
    @import "src/styles/common.scss";
</style>

