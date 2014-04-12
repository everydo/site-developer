---
title: 日志
description: 系统操作日志
---

=============================
操作日志
=============================

日志管理
=============
用IObjectLogManager适配器对操作日志统一管理,接口和原来基本一致,去除了清空日志的clear方法

- 添加日志

  addObjectLog(operation,text)

  operation为操作类型,text为操作描述

- 列出日志

  listLogs

  返回为一个List,每个元素为一个dict，dict里面包含日志的所有信息。文件和文件夹通用。在文件夹下会返回该文件夹下的所有日志。
  日志按时间排序。

操作历史的状态和维护
===============================
回收站管理
-------------------
需要在操作日志里面支持：

1. 能恢复删除的文件和文件夹

   - 方便的查找到话全部回收站中的内容：放入回收站 move2recycle
   - 一旦还原，不可再执行恢复操作：

     - 更改从前的操作类型：回收站(已还原) recyclereverted

   - 一旦永久删除，不可在执行恢复操作：

     - 更改从前的操作类型：回收站(已删除) recycledeleted

2. 在后台自动永久删除回收站中指定时间(比如30天)后的文件

   - 使用wget一个url来清理，需要控制请求来源remote为Localhost
   - 需要方便的找出处于回收站中的文件
   - 记录新的操作历史：自动清理回收站：cleanrecycle

操作历史维护接口说明
-----------------------
操作维护是在操作日志上可进行的操作，使用IActionManager维护

1. 列出日志上可执行的操作

  - listActions

2. 执行该操作

  - excuteAction(operation)

收回 临时外发授权
-------------------------
sendout -> sendoutreverted

操作日志接口IObjectLogManager为一个适配器

主要有一个方法::

  addObjectLog(operation, text=''):

其中:

- operation为操作类型
- text为操作描述

其中操作类型::

               # 公共操作
               'addTeamMember': translate(_('add_team_member', default='Add team member'), context=request),
               'removeTeamMember': translate(_('remove_team_member', default='Remove team member'), context=request),
               'pending': translate(_('pending', default='Pending'), context=request),
               'published': translate(_('published', default='Published'), context=request),
               'return': translate(_('retract', default='Retract'), context=request),
               'private': translate(_('private', default='Private'), context=request),
               'free': translate(_('change_to_public', default='Change to public'), context=request),
               'removed': translate(_('delete', default='Delete'), context=request),
               'subscription': translate(_('subscription', default='Subscription'), context=request),
               'assign': translate(_('assign', default='Assign'), context=request),
               'sendSMS': translate(_('sendSMS', default='Send SMS'), context=request),
               'sendSmsReport': translate(_('sendSmsReport', default='Send sms report'), context=request),
               'comment': translate(_('comment', default='Comment'), context=request),

               # 文件库操作
               'upload': translate(_('create', default='Create'), context=request),
               'newrevision': translate(_('new_revision', default='New revision'), context=request),
               'removerevision': translate(_('remove_revision', default='Remove revision'), context=request),
               'removedrevision': translate(_('remove_revision', default='Remove revision'), context=request),
               'revertrevision': translate(_('revert_revision', default='Revert revision'), context=request),
               'save': translate(_('edit', default='Edit'), context=request),
               'editoutside': translate(_('external_edit', default='External edit'), context=request),
               'rename': translate(_('rename', default='Rename'), context=request),
               'move': translate(_('move', default='Move'), context=request),
               'copy': translate(_('copy', default='Copy'), context=request),
               'print': translate(_('print', default='Print'), context=request),
               'move2recycle': translate(_('delete', default='Delete'), context=request),
               'recyclereverted': translate(_('delete', default='Delete'), context=request),
               'delete': translate(_('delete', default='Delete'), context=request),
               'revert': translate(_('revert', default='Revert'), context=request),
               'recycledeleted': translate(_('complete_remove', default='Complete remove'), context=request),
               'cleantrush': translate(_('clean_trush', default='Clean trush'), context=request),
               'download': translate(_('download', default='Download'), context=request),
               'downloadPDF': translate(_('download_pdf', default='Download PDF'), context=request),
               'sendout': translate(_('sendout', default='Sendout'), context=request),
               # 外部编辑
               'external_editor': translate(_('external_editor', default='external Editor'), context=request),
               # 定版
               'fixversion': translate(_('fixversion', default='Fix version'), context=request),
               # 修改扩展属性
               'modifyMetadata': translate(_('modifyMetadata', default='modify metadata'), context=request),
               # 删除扩展属性
               'removeMetadata': translate(_('removeMetadata', default='Remove metadata'), context=request),

               # 流程操作
               'newFlow': translate(_('create_flow', default='Create flow'), context=request),
               'newFlowSheet': translate(_('create_flowsheet', default='Create flowsheet'), context=request),
               'newFlowTask': translate(_('create_flowtask', default='Create flowtask'), context=request),
               'finishFlowTask': translate(_('finish_flowtask', default='Finish flowtask'), context=request),

搜索日志
----------------------------------
操作日志的搜索接口与前面的对象搜索基本一致。

看个例子，搜索24小时内，admin用户下载操作记录, 按时间递减排序:::

 site_name = get_root().name
 now = time.time()
 before_one_days = now - 24 * 3600
 results = LogQuery(site_name=site_name).\
     # 限制用户是admin
     filter(displayname__allof=[u'admin']).\
     # 限制是下载操作
     filter(operation__allof=[u'download']).\
     # 限制是24小时内的日志
     filter(timestamp__range=(float(before_one_days), float(now))).
     sort('-timestamp')
 
如果没有parse操作，日志默认就是按照事件倒序排列的，所以.sort('-timestamp')可以省略。


目前可以搜索的操作分别是:::

 download downloadPDF : 下载 和 下载PDF 操作
 upload created newPlan newProject newFlow   : 5种创建操作
 save editoutside editTask editPlan editProject newrevision : 6种编辑操作
 rename renameProject : 2种重命名操作
 copy move : 拷贝 和 移动 操作
 removed delete : 2种删除操作
 subscription : 订阅操作
 comment : 评论操作
 print ： 打印操作
 sendSMS sendSmsReport ： 发送短信 和 发送短信报告 操作
 sendout : 外发操作
 assign : 分配权限操作
 pending published return private free activeProject holdProject closeProject：改变状态操作
 login logout : 登录 和 登出 操作
 
