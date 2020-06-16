<template>
  <div class="app-container">
    <div class="block">
      <el-row>
        <el-col :span="24">
          <el-button type="success" size="mini" icon="el-icon-plus" @click.native="add">{{ $t('button.add') }}
          </el-button>

          <el-button type="primary" size="mini" icon="el-icon-search" @click.native="search">刷新</el-button>
        </el-col>
      </el-row>
    </div>

    <el-table :data="list" v-loading="listLoading" element-loading-text="Loading" border fit highlight-current-row
              @current-change="handleCurrentChange">
      <!--<el-table-column label="id">
        <template slot-scope="scope">
          {{scope.row.id}}
        </template>
      </el-table-column>-->
      <el-table-column label="名称">
        <template slot-scope="scope">
          {{scope.row.name}}
        </template>
      </el-table-column>
      <el-table-column label="原始pdf">
        <template slot-scope="scope">
          {{scope.row.oldPdf}}
        </template>
      </el-table-column>
      <el-table-column label="原始excel">
        <template slot-scope="scope">
          {{scope.row.oldExcel}}
        </template>
      </el-table-column>
      <el-table-column label="pdf开始页" width="90px">
        <template slot-scope="scope">
          {{scope.row.oldPdfStartPage}}
        </template>
      </el-table-column>
      <el-table-column label="pdf结束页" width="90px">
        <template slot-scope="scope">
          <span v-if="scope.row.oldPdfEndPage == ''">/</span>
          <span v-if="scope.row.oldPdfEndPage != ''">{{scope.row.oldPdfEndPage}}</span>
        </template>
      </el-table-column>
      <el-table-column label="excel开始行" width="100px">
        <template slot-scope="scope" >
          {{excelRows[scope.row.oldExcelStartRow].label}}
        </template>
      </el-table-column>
      <el-table-column label="excel开始列" width="100px">
        <template slot-scope="scope">
          {{excelColumns[scope.row.oldExcelStartColumn].label}}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="75px">
        <template slot-scope="scope">
          <span v-if="scope.row.pdfStatus == '0'">失败</span>
          <span v-if="scope.row.pdfStatus == '1'">成功</span>
          <span v-if="scope.row.pdfStatus == '2'">运行中</span>
          <span v-if="scope.row.pdfStatus == '3'">待运行</span>
        </template>
      </el-table-column>
      <el-table-column label="添加时间" width="150px">
        <template slot-scope="scope">
          {{scope.row.createTime}}
        </template>
      </el-table-column>
      <!--<el-table-column label="生成的文件">
        <template slot-scope="scope">
          <span v-if="scope.row.pdfStatus == '1'">{{scope.row.createPdf}}</span>
          <span v-if="scope.row.pdfStatus != '1'">/</span>
        </template>
      </el-table-column>-->
      <!--<el-table-column label="生成pdf的数量"  width="115px">
        <template slot-scope="scope">
          <span v-if="scope.row.pdfStatus == '1'">{{scope.row.createPdfCount}}</span>
          <span v-if="scope.row.pdfStatus != '1'">/</span>
        </template>
      </el-table-column>-->
      <el-table-column label="失败原因">
        <template slot-scope="scope">
          <span v-if="scope.row.pdfStatus == '0'">{{scope.row.failReason}}</span>
          <span v-if="scope.row.pdfStatus != '0'">/</span>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="80px">
        <template slot-scope="scope">
          <span v-if="scope.row.pdfStatus == '1'"><a style="color:#1890ff" @click="download(scope.row.id)">下载</a></span>
          <span v-if="scope.row.pdfStatus != '1'"></span>
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

    <el-dialog :visible.sync="formVisible" title="新增pdf转换任务" width="55%">
      <el-form ref="formData" :model="formData" :rules="rules" size="medium" label-width="100px">
        <el-row>
          <el-col :span="13">
            <el-form-item label-width="150px" label="任务名称" prop="name">
              <el-input v-model="formData.name" placeholder="请输入pdf解析任务名称" clearable :style="{width: '100%'}">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label-width="150px" label="pdf解析开始页" prop="oldPdfStartPage">
              <el-input-number v-model="formData.oldPdfStartPage" placeholder="请输入pdf解析开始页" :step='1'
                               step-strictly :min='1'></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label-width="150px" label="pdf解析结束页" prop="oldPdfEndPage">
              <el-input-number v-model="formData.oldPdfEndPage" placeholder="请输入pdf解析结束页" :step='1'
                               step-strictly :min='1'></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label-width="150px" label="excel开始行" prop="oldExcelStartRow">
              <el-select v-model="formData.oldExcelStartRow" size="medium">
                <el-option
                  v-for="item in excelRows"
                  :key="item.id"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label-width="150px" label="excel开始列" prop="oldExcelStartColumn">
              <el-select v-model="formData.oldExcelStartColumn" size="medium">
                <el-option
                  v-for="item in excelColumns"
                  :key="item.id"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label-width="150px" label="上传pdf文件" required>
              <el-upload :auto-upload="true" :before-upload="old_pdfBeforeUpload" accept=".pdf"
                         name="file" :http-request="uploadPdfFile" action="" :on-remove="removePdf"
                         ref="uploadPdf">
                <el-button size="small" type="primary" icon="el-icon-upload">点击上传PDF</el-button>
                <div slot="tip" class="el-upload__tip">只能上传不超过 300MB 的.pdf文件</div>
              </el-upload>
              <el-progress :percentage="progressPdf"></el-progress>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label-width="150px" label="上传excel文件" required>
              <el-upload :auto-upload="true" :before-upload="old_excelBeforeUpload" accept=".xls,.xlsx"
                         name="file" :http-request="uploadExcelFile" action="" :on-remove="removeExcel"
                         ref="uploadExcel">
                <el-button size="small" type="primary" icon="el-icon-upload">点击上传EXCEL</el-button>
                <div slot="tip" class="el-upload__tip">只能上传不超过 300MB 的.xls,.xlsx文件</div>
              </el-upload>
              <el-progress :percentage="progressExcel"></el-progress>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="">
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item>
          <el-button type="primary" @click="submitForm">{{ $t('button.submit') }}</el-button>
          <el-button @click="resetForm">{{ $t('button.cancel') }}</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script src="./pdfManagement.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>
<style>
  .el-upload__tip {
    line-height: 1.2;
  }
</style>

