<template>
  <div class="app-container">
    <div class="block">
      <el-form :inline="true" :model="listQuery">
        <el-form-item label="月份">
          <el-select v-model="listQuery.month" size="medium" class="month_input" @change="getWaterInfoData">
            <el-option
              v-for="item in month"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户编号">
          <el-input v-model="listQuery.cid" size="medium" class="condition_input" placeholder="客户编号"
                    type="number"></el-input>
        </el-form-item>
        <el-form-item label="水表止码" :span="6">
          <el-input v-model="listQuery.meterCode" size="medium" class="condition_input" placeholder="水表止码"
                    type="number"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" size="mini" icon="el-icon-edit" @click.native="create"
                     :disabled="button.createButton">{{$t('button.waterCreateTable') }}
          </el-button>
          <el-button type="primary" size="mini" icon="el-icon-check" @click.native="downloadExcel"
                     :disabled="button.downloadButton">{{$t('button.waterDownloadExcel') }}
          </el-button>
        </el-form-item>
      </el-form>

      <el-form :inline="true">
        <el-form-item label="今日已经开票" :span="6">
          <span class="colorFont">{{toDayCount}}</span>
        </el-form-item>
        <el-form-item label="户" :span="6"></el-form-item>
        <el-form-item label="" :span="6">
        </el-form-item>
        <el-form-item label="当前所选月份已经开票总户数" :span="6">
          <span class="colorFont">{{toMonthCount}}</span>
        </el-form-item>
        <el-form-item label="户" :span="6"></el-form-item>
      </el-form>
    </div>

    <table v-loading="listLoading" align="center" valgin="center" width="80%" border="1" cellpadding="0" cellspacing="0"
           style="font-size: 18px">
      <tr>
        <td colspan="13" align="center" height="35">
          <span class="colorFont">{{waterBill.year}}</span>&nbsp;&nbsp;年&nbsp;&nbsp;
          <span class="colorFont">{{waterBill.month}}</span>&nbsp;&nbsp;月
        </td>
      </tr>
      <tr align="center">
        <td width="100" height="35">用户</td>
        <td width="120" colspan="2" class="colorFont">{{waterBill.cname}}</td>

        <td width="81">住址</td>
        <td width="63" colspan="8" class="colorFont">{{waterBill.address}}</td>
        <td width="63">备注</td>
      </tr>
      <tr align="center">
        <td rowspan="2">项目及类别</td>
        <td colspan="2" valign="middle" height="40">水表动态</td>
        <td rowspan="2">
          <p>用水量</p>
          <p>(立方米)</p>
        </td>
        <td rowspan="2">单价</td>
        <td colspan="7">金额</td>
        <td rowspan="5"></td>
      </tr>
      <tr align="center">
        <td>起码</td>
        <td>止码</td>
        <td>万</td>
        <td>千</td>
        <td>百</td>
        <td>十</td>
        <td>元</td>
        <td>角</td>
        <td>分</td>
      </tr>
      <tr align="center" height="35">
        <td>计量水费</td>
        <td class="colorFont">{{waterBill.firstNumber}}</td>
        <td class="colorFont">{{waterBill.lastNumber}}</td>
        <td class="colorFont">{{waterBill.waterCount}}</td>
        <td class="colorFont">{{waterBill.price}}</td>
        <td class="colorFont">{{waterBill.charMeterageCost[0]}}</td>
        <td class="colorFont">{{waterBill.charMeterageCost[1]}}</td>
        <td class="colorFont">{{waterBill.charMeterageCost[2]}}</td>
        <td class="colorFont">{{waterBill.charMeterageCost[3]}}</td>
        <td class="colorFont">{{waterBill.charMeterageCost[4]}}</td>
        <td class="colorFont">{{waterBill.charMeterageCost[6]}}</td>
        <td class="colorFont">{{waterBill.charMeterageCost[7]}}</td>
      </tr>
      <tr align="center" height="35">
        <td>容量水费</td>
        <td colspan="4"></td>
        <td class="colorFont">{{waterBill.charCapacityCost[0]}}</td>
        <td class="colorFont">{{waterBill.charCapacityCost[1]}}</td>
        <td class="colorFont">{{waterBill.charCapacityCost[2]}}</td>
        <td class="colorFont">{{waterBill.charCapacityCost[3]}}</td>
        <td class="colorFont">{{waterBill.charCapacityCost[4]}}</td>
        <td class="colorFont">{{waterBill.charCapacityCost[6]}}</td>
        <td class="colorFont">{{waterBill.charCapacityCost[7]}}</td>
      </tr>
      <tr align="center" height="35">
        <td>合计大写</td>
        <td colspan="4" class="colorFont">{{waterBill.capitalization}}</td>
        <td class="colorFont">{{waterBill.charWaterCost[0]}}</td>
        <td class="colorFont">{{waterBill.charWaterCost[1]}}</td>
        <td class="colorFont">{{waterBill.charWaterCost[2]}}</td>
        <td class="colorFont">{{waterBill.charWaterCost[3]}}</td>
        <td class="colorFont">{{waterBill.charWaterCost[4]}}</td>
        <td class="colorFont">{{waterBill.charWaterCost[6]}}</td>
        <td class="colorFont">{{waterBill.charWaterCost[7]}}</td>
      </tr>
      <tr height="35">
        <td border="0" align="center">合计：</td>
        <td>&nbsp;</td>
        <td colspan="2">复核：</td>
        <td colspan="6">收款：</td>
        <td colspan="3">开票：</td>
      </tr>
    </table>
  </div>
</template>

<script src="./waterInfo.js"></script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
  .condition_input {
    width: 110px;
  }

  .month_input {
    width: 90px;
  }

  .colorFont {
    color: red;
    font-weight: 900;
    font-size: 20px;
  }
</style>

