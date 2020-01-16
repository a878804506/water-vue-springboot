<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  export default {
    name: 'App'
  }
</script>
<style>
  .current-row > td {
    background: #D2E4EE !important;
  }

  .el-table--border td {
    border-right: 0;
  }

  .el-form-item__label {
    font-size: 16px;
  }
</style>
