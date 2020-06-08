import request from '@/utils/request'

export function getMusicSyncTask(params) {
  return request({
    url: '/musicSyncTask/list',
    method: 'get',
    params
  })
}
