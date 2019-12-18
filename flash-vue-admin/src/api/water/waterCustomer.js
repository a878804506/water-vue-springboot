import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/water/customer/list',
    method: 'get',
    params
  })
}

export function save(params) {
  return request({
    url: '/water/customer',
    method: 'post',
    params
  })
}

export function remove(id) {
  return request({
    url: '/water/customer',
    method: 'delete',
    params: {
      id: id
    }
  })
}
