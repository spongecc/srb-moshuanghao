# srb-moshuanghao

### 2021-08-26 积分等级的增删改查

### 2021-08-30 EasyExcel导入和导出数据字典，页面折叠展示数据
    - 导入数据不能使用PO（Persistent Object）类，PO类是与操作数据库相关的类,它跟持久层（通常是关系型数据库）的数据结构形成一一对应的映射关系，需要新创建一个 DTO（Data Transfer Object）类，针对用户数据与服务层的数据传输对象
    - 导入数据需要定义类继承AnalysisEventListener，实现invoke()和doAfterAllAnalysed()
    - Element UI在显示页面折叠数据时需要给一个boolean属性，有子节点时，表示可展开