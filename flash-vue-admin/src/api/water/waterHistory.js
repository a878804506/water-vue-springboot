import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/water/waterhistory/list',
    method: 'get',
    params
  })
}

