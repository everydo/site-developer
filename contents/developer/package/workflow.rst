---
title: 工作流
description: 表单和流程操作接口，包括表单自动生成
---

=================
工作流
=================

工作流附加在表单之上，按照预先的定义，相关人员参与，按照一定的流程执行一系列的流程步骤。

.. Contents::
.. sectnum::

介绍工作流
======================
典型的工作流示例::


        /--> 步骤1.1 --> 步骤1.2 --\  /--- 步骤4 --\
        |                          |  |            |
  提交 -+--> 步骤2.1 --------------+--+            +-- 结束
        |                          |  |            |
        \--> 步骤3.1 --> 步骤3.2 --/  \--- 步骤5 --/

其中：

1. 提交步骤，以及中间步骤1-5都是需要人参与的，一般需要填写表单，这些步骤称为人工参与步骤
2. 步骤1/2/3完成之后，启动步骤4/5，这2批流程中间，我们可以增加一个衔接节点，便于流程条件控制。衔接节点不需要用户参与。
3. 步骤4/5结束，流程完结。也需要增加一个结束节点，结束节点也不需要人工参与
4. 所有步骤，都需要设置参与条件，都可能引起流程进行阶段的变化。

流程步骤定义
=================
和之前版本的改进：

1. 步骤可设置 自动触发的后续步骤: auto_steps, 方便实现无需人员干预的自动步骤
2. 如果步骤没有操作，表示这个步骤无需人员干预
3. 去除操作项中的stage, nextsteps_condition, 在步骤中增加stage

工作流由步骤Step和操作Action组成::

    workflow_json = {'name':'sales',
                     'title':'销售流程',
                     'description':'销售',
                     'steps':[ {"name":"start",
                         "title" : '新的销售机会',
                         "fields": ['title', 'client', 'responsibles', 'case_info', 'subjects'],
                         "invisible_fields": ['plan_info', 'files', 'folder', 'lastlog', 'log', 'start'],
                         "on_enter": "",
                         "condition": '',
                         "hours": 0,  # 时限, 如果非0，流程任务会创建生成 deadline
                         "stage": 'valid',
                         "responsibles": '[request.principal.id]',
                         "actions": [ { "name":'submit',
                                        "title":'提交',
                                        "type": 'default',   # 正常的流程操作
                                        "condition":'',
                                        "finish_condition":'',
                                        "next_steps":['communicate'],
                                        "on_submit":"",
                                      }
                                    ]
                       },

                        { "name":"auto_step"
                          "title": '这是一个自动步骤，无人参与',
                          "condition":'',
                          "on_enter": "",
                          "stage":'planing', 
                          "auto_steps":['communicate', ],
                        },

                        {"name": "communicate",
                         "title": '了解需求背景',
                          "fields": ['title', 'case_info', 'files', 'log', 'start', 'subjects'],
                          "invisible_fields":['plan_info', 'lastlog'],
                          "on_enter": "",
                          "condition":'',
                          "stage":'planing',
                          "responsibles":'context["responsibles"]',
                          "actions": [ {"name":'duplicated',
                                        "title":'重复或无效, 不再跟进',
                                        "type": 'error',   # 这里error，表示流程异常操作
                                        "next_steps":[],
                                        "finish_condition":'',
                                        "condition":'',
                                       },
                                       {"name": '8372',
                                        "title": '需求了解完毕',
                                        "type": 'default',   
                                        "next_steps": ['submit_plan'],
                                        "finish_condition":'',
                                       }
                                      ]
                              }
                         }]}

将这个工作流注册到系统::

   root.packages.register_workflow('zopen.sales', workflow_json)

也可以得到工作流定义信息::

   salse_query_wfl = root.packages.get_workflow('zopen.sales:sales_query')

关联工作流
====================
目前工作流主要是和数据项关联，保存在数据容器的设置项中::

   datacontainer.settings['item_workflow']

另外，任何流程，都可以发起关联流程或表单. 容器和条目，可以分别设置不同的关联流程::

    container.settings['item_related_datacontainers'] =
                (root.object_uid(datacontainer1), root.object_uid(datacontainer2))

    container.settings['container_related_datacontainers'] =
                (root.object_uid(datacontainer3),)

工作项 WorkItem
=======================
流程扭转
--------------
可以为任何一个数据dataitem，启动一个流程::

   dataitem.workitems.start(request=request)

一旦启动流程，流程定义的其实步骤就开始执行，产生一些工作项。 

可以查看表单::

    dataitem.workitems.get_form_elements(request)

返回::

    (   [], #fields, 
        [], # invisible_fields, 
        [(workitem_name, action_name, action_title)] #actions
    )

查看所有的工作项::

    for workitem in dataitem.workitems.values():
        print workitem.name
        print workitem.title
        print workitem.action_name

得到具体某个workitem::

   workitem = dataitem.workitems.get(workitem_name)

通过程序触发某个操作，推动流程前进::

   item.workitems.execute(workitem_name, action_name, as_principal=None, comment="")

其中：

- workitem_name: 某个工作项的name
- action_name: 操作
- as_principal: 可以指定以某人的身份去执行这个流程(如:users.admin)。

工作项历史
----------------
查看工作项::

   workitems = item.workitems.query(pid, state)

每个工作项:

- object_types: (WorkItem, Item)
- metadata: ('zopen.plan:plan:review',) # 软件包名:流程名:步骤名
- stati: flowtask.active
- acl: 

  - Responsible: 负责人
  - Delegator: 委托人

- md 信息：

    - 'workflow': ('zopen.plan:plan',)  # 具体的流程定义
    - 'step': ('review',)           # 具体的步骤
    - 'deadline': '2012-12-13',     # 工作期限
    - 'finished': '',               # 完结时间
    - start: 计划开始时间(工作安排)
    - end: 计划结束时间(工作安排)
    - delegations: { delegator: [pids] }

- actions : 操作清单

  - username: 用户名
  - action_name": 操作id
  - title: 操作名称
  - time: 操作时间
  - comment: 说明

工作项的状态包括:

- flowtask.active: 活动', '任务正在处理中
- flowtask.pending: '暂停', '暂停处理该任务'),
- flowtask.abandoned: '取消', '任务已被取消'),
- flowtask.finished: '完成', '任务已经处理完成')),

流程时限
-----------------
为提高效率，有些流程有严格的扭转时限，比如3个工作日之内必须完成。

1. 定义流程的时候，设置后步骤时限 ``hours`` , 比如3天就是 3 * 24 = 72小时
2. 可通过 ``deadline`` 属性来搜索近期即将到期的工作项

流程委托 ``delegate``
------------------------
当负责人休假、生病，可将工作委托其他人处理，期间他仍然可以选择自己处理::

   dataitem.delegate(responsible, delegators)

如果取消某个负责人的代理::

   dataitem.undelegate(responsible)

每个人可以根据转交策略进行转交(不同位置，委托给谁处理)::

   root.profiles.delegate(pid, policy=[{'location':[], pids:[]}])
   root.profiles.undelegate(pid) # 停止转交

读取设置::

   root.profiles.get(pid, 'delegation_policy')
   root.profiles.get(pid, 'delegation')

流程转交转交 ``deliver``
---------------------------------
将某个人的工作完全转交工作给其他人负责，自己不再处理::

   item.workitems.deliver(workitem_name, pid, new_responsibles)

协助 ``assist``
-----------------------
将工作转给其他人给出意见，其他人完成之后，流程扭转回自己继续处理::

   item.workitems.assist(workitem_name, pid, new_responsibles)

协助的工作，只会有一个 "确定" 操作，可以通过 ``workitem.next_workitem`` 是否为空来甄别是否是协助工作 , 
``workitem.assist_to`` 存放了协助的对象

工作流脚本
===================
on_submit: 操作提交
---------------------
操作提交的触发脚本，可以同时对表单进行校验，如果表单满足要求，步骤才能继续。

on_submit的返回值，就是校验的错误结果::

  {'title': '必须输入'}

on_enter: 进入新的步骤
-------------------------------
进入新的步骤，系统默认会给该步骤的执行人发送一封通知邮件.

可以关闭系统自动通知功能, 只需要返回::

  {'auto_notify':False}

工作流规则
==================
如果软件包的流程不能满足需求，有一些特殊的定制要求，可以通过工作流规则来实现。

工作流规则无需改变原始软件包中的流程定义，可以在流程部署之后，在规则中增加一个工作流规则。

工作流规则中，可以利用下面的变量编写脚本::

- ``event.workitem`` 当时的工作项
- ``event.workitem.md['step']`` 步骤名
- ``event.workitem.actions[-1]['action_name']`` 操作名

导出为python格式
===================
为方便书写和阅读，系统可将流程导出为一种借用python的书写格式::

   root.packages.export_workflow('zopen.sales:sales_query')

1. 类名: 步骤名
2. 类的成员变量: 步骤的属性
3. 类的方法名: 步骤的操作name
4. 类方法的函数体：步骤的触发脚本

文件名为sales.py::

   title = '销售流程'
   description = '销售'

   # 第一个步骤
   class Start:
        title='新的销售机会'
        condition=''
        stage = "requirement"

        responsibles='[request.principal.id]'
        fields=['title', 'client', u'responsibles', u'case_info', 'subjects']
        invisible_fields=['plan_info', 'files', u'folder', 'lastlog', 'log', 'start']

        # 进入这个步骤触发
        def __init__(): 
            pass

        # 这是一个流程操作
        @action('提交', ['Communicate'], condition="", finish_condition='', )
        def submit(step, context):
            #建立项目文件夹
            case_obj = container
            if case_obj.md['folder']:
                try:
                    filerepos = root.object_by_uid(case_obj.md['folder'])
                    year = str(datetime.datetime.now().year)
                    month = str(datetime.datetime.now().month) + '月'
                    if year not in filerepos:
                        year_folder = filerepos.add_folder(year)
                        year_folder.index()
                    else:
                        year_folder = filerepos[year]
                    if month not in year_folder:
                        month_folder = year_folder.add_folder(month)
                        month_folder.index()
                    else:
                        month_folder = year_folder[month]

                    project_folder = month_folder.add_folder(context['title'])
                    project_folder.index()
                    context.md['folder'] = root.object_uid(project_folder)
                except KeyError:
                    pass
            else:
                return {'title':"error"}

  # 第二个步骤
  class Communicate:
        title='了解需求背景'
        condition=''
        stage = "requirement"

        responsibles='context["responsibles"]'
        fields=['title', 'case_info', u'files', u'log', u'start', 'subjects']
        invisible_fields=['plan_info', 'lastlog']

        # 进入这个步骤触发
        def __init__(): 
            pass

        # 这是一个流程操作
        @action('重复或无效, 不再跟进', [], finish_condition='', condition=u'', )
        def duplicated(context, container, workitem, step):
            pass

        # 这是一个流程操作
        @action('需求了解完毕', ['SubmitPlan'], finish_condition='', )
        def AA8372( context, container, workitem, step):
            pass

  # 第三个步骤
  class SubmitPlan:
        title='方案确认'
        condition=''
        stage = "solution"

        responsibles='context["responsibles"]'
        fields=['title', 'case_info', 'plan_info', 'files', 'log', 'start', 'subjects']
        invisible_fields=[]

        # 进入这个步骤触发
        def __init__(): 
            if 'stage.delayed' in context.stati:
                IStateMachine(context).setState('flowsheet.pending', do_check=False)

        # 操作一
        @action('暂停，以后再联系', ['SubmitPlan'], finish_condition='', condition=u'' )
        def pause(context, container, step, workitem):
            pass

        @action('接受方案，准备合同', ['SubmitFile'], finish_condition='', )
        def accept( context, container, step, workitem):
            pass

        @action('无法满足需求', ['Lost'], finish_condition='', condition=u'' )
        def cannotdo( context, container, step, workitem):
            pass

        @action('已选用其它产品', ['Lost'], finish_condition='', 
                condition="'stage.lost' not in context.stati", )
        def other( context, container, step, workitem):
            pass

  # 最后一个步骤
  class SubmitFile:
        title='签订合同'
        condition=''
        stage = "contract"

        responsibles='context["responsibles"]'
        fields=['files', 'log', 'start']
        invisible_fields=[]

        # 进入这个步骤触发
        def __init__(): 
            pass

        @action('合同签订', [], finish_condition='')
        def sign(context, container, step, workitem):
            pass

        @action('变故，以后再联系', ['SubmitPlan'], finish_condition='', condition='' )
        def contact_later(context, container, step, workitem):
            pass

        @action('失败', ['Lost'], finish_condition='', )
        def fail( context, container, step, workitem):
            pass

  # 这是一个自动步骤：1）没有负责人 2）没有后续操作 3）有自动步骤
  class AfterContract:
        title="合同准备完成"
        condition=''
        stage='turnover'

        auto_steps=['ConfirmLost']

        # 进入这个步骤触发
        def __init__(): 
            pass

  class ConfirmLost:
        title='丢单确认'
        condition=''
        stage='losting'

        responsibles='ISettings(container)["manager"]'
        fields=[]
        invisible_fields=[]

        # 进入这个步骤触发
        def __init__(): 
            pass

        @action( '确认丢单', ['Lost'], condition="", finish_condition='')
        def confire_fail( context, container, step, workitem):
            pass

        @action( '继续跟单', ['SubmitPlan'], condition="",finish_condition='')
        def continue( context, container, step, workitem):
            pass

  class Lost:
        title='签订合同'
        condition=''
        stage='lost'

        next_steps=[]

        # 进入这个步骤触发
        def __init__(): 
            pass

  class End:
        title='签订合同'
        condition=''
        stage='turnover'

        next_steps=[]

        # 进入这个步骤触发
        def __init__(): 
            pass

将这个工作流转换成真正的工作流定义::

   root.packages.import_workflow('zopen.sales:query', workflow_py)

