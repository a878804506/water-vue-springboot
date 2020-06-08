import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/water/watermeter/list',
    method: 'get',
    params
  })
}


export function save(params) {
  return request({
    url: '/water/watermeter',
    method: 'post',
    params
  })
}

export function remove(id) {
  return request({
    url: '/water/watermeter',
    method: 'delete',
    params: {
      id: id
    }
  })
}
