import request from '@/utils/request'

export function createWaterMonthlyPayment(params) {
  return request({
    url: '/water/monthlyPayment/createWaterMonthlyPayment',
    method: 'post',
    params
  })
}
