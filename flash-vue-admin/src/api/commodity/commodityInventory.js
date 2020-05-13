import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/commodity/inventory/list',
    method: 'get',
    params
  })
}

export function inOrOutInventory(params) {
  return request({
    url: '/commodity/inventory/inOrOutInventory',
    method: 'post',
    params
  })
}
