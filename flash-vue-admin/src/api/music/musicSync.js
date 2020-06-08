import request from '@/utils/request'
import qs from 'qs'

export function searchSongs(params) {
  return request({
    url: '/musicSync/searchMusic',
    method: 'get',
    params
  })
}

export function getPlatformsList() {
  return request({
    url: '/musicSync/getPlatformsList',
    method: 'get'
  })
}

export function syncSongs(data) {
  return request({
    url: '/musicSync/syncSongs',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset-UTF-8'
    },
    data: qs.stringify(data)
  })
}
