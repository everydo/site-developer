.. contents::

对象的状态
===========================
每一个对象存在一组状态，存放在对象的context.stati属性中

modify: 发布流程

- modify.default	草稿
- modify.pending	待审
- modify.archived	发布/存档 (只读)

visible: 保密流程

- visible.default	普通
- visible.private	保密

folder:受控

- folder.default	普通文件夹
- folder.control	受控文件夹

flowsheet：流程

- flowsheet.active,	'活动', '流程单正在处理中'
- flowsheet.pending	暂停
- flowsheet.abandoned	废弃
- flowsheet.finished	完成

flowtask: 流程任务

- flowtask.active	活动
- flowtask.pending	暂停
- flowtask.abandoned	废弃
- flowtask.finished	完成


状态机IStateMachine
===========================

系统的大部分对象支持状态的状态：

- 每个对象包括一个或者多个状态
- 预先定义连接状态的transition，执行transition将导致状态发生变化
- transition的执行，是有权限控制的

我们先看看使用的示例：

- 不进行权限检查，直接发布某个文档:::

    IStateMachine(context).setState('modify.archived', do_check=False)

- 设置文件夹为受控:::
  
    IStateMachine(context).setState('folder.control', do_check=False)

上面使用了IStateMachine状态机接口，其包括的接口有：

getAllStates()	
   得到对象的所有状态	
getState(prefix)
   得到某个的状态	
setState(new_state, do_check=True)
   设置状态	
nextStates(self, prefix)
   得到后续状态	


阶段，一种特殊的状态
=========================
流程里面可以设置阶段，这个阶段也可以使用状态引擎来操作。

每个阶段所对应的状态名为 stage.stage_name

可以使用IStateMachine接口来调整流程单所处的阶段