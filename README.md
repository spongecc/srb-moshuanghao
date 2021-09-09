# srb-moshuanghao

### 2021-08-26 积分等级的增删改查

### 2021-08-30 EasyExcel导入和导出数据字典，页面折叠展示数据
    - 导入数据不能使用PO（Persistent Object）类，PO类是与操作数据库相关的类,它跟持久层（通常是关系型数据库）的数据结构形成一一对应的映射关系，需要新创建一个 DTO（Data Transfer Object）类，针对用户数据与服务层的数据传输对象
    - 导入数据需要定义类继承AnalysisEventListener，实现invoke()和doAfterAllAnalysed()
    - Element UI在显示页面折叠数据时需要给一个boolean属性，有子节点时，表示可展开
### 2021-09-03 完成用户注册和登录功能，用户注册时可发送验证码到阿里云短信
### 2021-09-06 对项目添加spring cloud组件，
    - nacos注册中心、sentinel流量控制、OpenFeign远程调用和被动熔断降级、
      gateway网关路径断言（负载均衡）和请求过滤
      
### 2021-09-07 前台提供用户绑定汇付宝接口
### 2021-09-08 前台提供用户申请借款额度接口
    - 根据dictCode查询对应的字典表的下拉内容
    - 根据图片类型上传图片到阿里云OSS存储服务器
### 2021-09-09 后台提供借款额度审核接口
