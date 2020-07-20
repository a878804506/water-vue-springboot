import request from '@/utils/request'

export function saveOrUpdateFavoriteMapping(params) {
  return request({
    url: '/music/favorite/mapping',
    method: 'post',
    params
  })
}
