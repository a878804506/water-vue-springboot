import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/commodity/info/list',
    method: 'get',
    params
  })
}

export function save(params) {
  return request({
    url: '/commodity/info',
    method: 'post',
    params
  })
}

export function remove(id) {
  return request({
    url: '/commodity/info',
    method: 'delete',
    params: {
      id: id
    }
  })
}
