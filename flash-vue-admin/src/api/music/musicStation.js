import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/music/station/list',
    method: 'get',
    params
  })
}

export function getMusicById(params) {
  return request({
    url: '/music/station/getMusicById',
    method: 'get',
    params
  })
}

export function remove(id) {
  return request({
    url: '/music/station',
    method: 'delete',
    params: {
      id: id
    }
  })
}
