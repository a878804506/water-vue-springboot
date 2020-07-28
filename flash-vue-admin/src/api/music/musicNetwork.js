import request from '@/utils/request'

export function getMusicNetworkConfig() {
  return request({
    url: '/music/network/getMusicNetworkConfig',
    method: 'get'
  })
}
