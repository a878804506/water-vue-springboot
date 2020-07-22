import request from '@/utils/request'
import qs from 'qs'

export function saveOrUpdateFavoriteMapping(params) {
  return request({
    url: '/music/favorite/mapping',
    method: 'post',
    params
  })
}

export function saveFavoriteMappingList(data) {
  return request({
    url: '/music/favorite/mapping/addList',
    method: 'post',
    data: qs.stringify(data)
  })
}
