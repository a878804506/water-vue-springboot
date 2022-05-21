import request from '@/utils/request'

export function getData(params) {
  return request({
    url: '/water/waterhistory/monthStatistics',
    method: 'get',
    params
  })
}

