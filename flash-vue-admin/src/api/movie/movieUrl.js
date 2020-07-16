import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/movie/url/list',
    method: 'get',
    params
  })
}

export function save(params) {
  return request({
    url: '/movie/url',
    method: 'post',
    params
  })
}

export function remove(id) {
  return request({
    url: '/movie/url',
    method: 'delete',
    params: {
      id: id
    }
  })
}

export function getAllUrls(params) {
  return request({
    url: '/movie/url/getAllUrls',
    method: 'get',
    params
  })
}
