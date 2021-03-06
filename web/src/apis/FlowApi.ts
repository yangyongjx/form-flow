import Api from './Api'

export default class FlowApi extends Api {
  //~ ------------------------------------------------------
  // 构造器
  constructor() {
    super()
  }

  //~ ------------------------------------------------------
  // 保存流程结构数据
  async saveFlow(flowData: any) {
    const res: any = await this.http.post('/api/flow/save', flowData)
    return super.extractData(res)
  }

  // 删除流程结构数据
  async deleteFlow(id: string) {
    const res: any = await this.http.delete('/api/flow/delete?id=' + id)
    return super.extractMsg(res)
  }

  // 查询流程列表
  async getFlowList(flowQuery: any) {
    const res: any = await this.http.post('/api/flow/getList', flowQuery)
    return super.extractData(res)
  }

  // 查询用户可以开始的流程
  async getByUser(userId: string) {
    const res: any = await this.http.get('/api/flow/getByUser?userId=' + userId)
    return super.extractData(res)
  }
}
