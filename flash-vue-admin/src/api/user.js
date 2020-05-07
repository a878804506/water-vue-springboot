import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/account/login',
    method: 'post',
    params: {
      "username": data.username,
      "password": data.password
    }
  })
}

export function getInfo() {
  return request({
    url: '/account/info',
    method: 'get'
  })
}

export function logout(token) {
  return request({
    url: '/logout',
    method: 'post'
  })
}

export function updatePwd(params) {
  return request({
    url: '/account/updatePwd',
    method: 'post',
    params
  })
}

export function checkDingDingLogin(params) {
  return request({
    url: '/account/dingdingCallback',
    method: 'get',
    params
  })
}

export function bindSystemUser(params) {
  return request({
    url: '/account/bindSystemUser',
    method: 'post',
    params
  })
}
