<template>
  <div class="app-container">
    <div class="user">
      <strong>{{ user.name }}</strong><br>
      <small>{{ user.dept }} &nbsp;&nbsp; {{ user.roles }}</small>
    </div>
    <el-row class="user-content" style="">
      <el-col :span="6" class="profile">
        <img class="user-avatar" src="@/assets/img/avatar.gif"><br>
        <p><span class="title"><i class="el-icon-phone"></i>&nbsp;&nbsp;{{ user.phone == ''? '暂无': user.phone }}</span></p>
        <p><span class="title"><i class="el-icon-message"></i>&nbsp;&nbsp;{{ user.email == ''? '暂无': user.email }}</span></p>
        <p><span class="title"><i class="el-icon-open"></i>&nbsp;&nbsp;{{ user.status == '1'?'启用' : '禁用' }}</span></p>
      </el-col>
      <el-col :span="18" style="padding-left:10px;">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="个人资料" name="profile"></el-tab-pane>
          <el-tab-pane label="最近活动" name="timeline"></el-tab-pane>
          <el-tab-pane label="修改密码" name="updatePwd"></el-tab-pane>
        </el-tabs>
        <el-form  label-width="80px">
          <h3>基本信息</h3>
          <el-form-item label="名称">
            <span>{{ user.name }}</span>
          </el-form-item>
          <el-form-item label="性别">
            <span> {{ user.sex = 1 ? '男' : '女' }}</span>
          </el-form-item>
          <el-form-item label="生日">
            <span> {{ user.birthday == ''? ' 暂无': user.birthday }}</span>
          </el-form-item>
          <h3>联系信息</h3>
          <el-form-item label="手机">
            <span>{{ user.phone == ''? ' 暂无': user.phone }}</span>
          </el-form-item>
          <el-form-item label="邮箱">
            <span> {{ user.email == ''? ' 暂无': user.email }}</span>
          </el-form-item>

          <h3 v-show="user.id == -2">重要信息</h3>
          <el-form-item label="" v-show="user.id == -2">
            <span>您现在还没绑定到云平台账号上，请先绑定账号！</span>
            <el-button type="primary" size="mini" icon="el-icon-plus"  @click.native="bindUser">绑定
            </el-button>
          </el-form-item>

        </el-form>
      </el-col>
    </el-row>

    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="30%">
      <el-form ref="form" :model="form" label-width="150px">
        <el-row>
          <el-col :span="30">
            <el-form-item label="账号" prop="account">
              <el-input v-model="form.account" ></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" type="password"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="rePassword">
              <el-input v-model="form.rePassword" type="password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="saveBind">{{ $t('button.submit') }}</el-button>
          <el-button @click.native="formVisible = false">{{ $t('button.cancel') }}</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script src="./profile.js"></script>


<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/common.scss";
</style>

