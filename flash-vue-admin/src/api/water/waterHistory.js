import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/water/waterhistory/list',
    method: 'get',
    params
  })
}

export function waterCancel(params) {
  return request({
    url: '/water/waterhistory/waterCancel',
    method: 'post',
    params
  })
}
