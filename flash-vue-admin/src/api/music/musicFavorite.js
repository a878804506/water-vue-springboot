import request from '@/utils/request'

export function getFavoriteList() {
  return request({
    url: '/music/favorite/getFavoriteList',
    method: 'get'
  })
}

export function saveOrUpdateFavorite(params) {
  return request({
    url: '/music/favorite',
    method: 'post',
    params
  })
}

export function deleteFavorite(params) {
  return request({
    url: '/music/favorite',
    method: 'delete',
    params
  })
}

export function getFavoriteMusicList(params) {
  return request({
    url: '/music/favorite/mapping/getFavoriteMusicList',
    method: 'get',
    params
  })
}

export function getAppMusicUrl(params) {
  return request({
    url: '/music/app/getAppMusicUrl',
    method: 'get',
    params
  })
}
