import request from '@/utils/request'

export function createWaterInfo(params) {
  return request({
    url: '/water/info/createWaterInfo',
    method: 'post',
    params
  })
}

export function downloadExcel(params) {
  return request({
    url: '/water/info/downloadExcel',
    method: 'get',
    params
  })
}

export function checkMeterCode(params) {
  return request({
    url: '/water/info/checkMeterCode',
    method: 'get',
    params
  })
}

export function getWaterInfo(params) {
  return request({
    url: '/water/info/getWaterInfo',
    method: 'get',
    params
  })
}

export function getCustomersWaterCostByMonth(params) {
  return request({
    url: '/water/info/getCustomersWaterCostByMonth',
    method: 'get',
    params
  })
}
