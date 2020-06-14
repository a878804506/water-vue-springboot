import request from '@/utils/request'
import qs from 'qs'

export function getList(params) {
  return request({
    url: '/pdf/management/list',
    method: 'get',
    params
  })
}

export function updaloadFile(data) {
  return request({
    url: '/pdf/management/updaloadFile',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: data
  })
}

export function save(data) {
  return request({
    url: '/pdf/management/',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset-UTF-8'
    },
    data: qs.stringify(data)
  })
}
