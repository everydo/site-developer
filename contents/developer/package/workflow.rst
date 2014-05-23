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
                         "stage": 'valid',
                         "responsibles": '[request.principal.id]',
                         "actions": [ { "name":'submit',
                                        "title":'提交',
                                        "condition":'',
                                        "finish_condition":'',
                                        "nextsteps":['communicate'],
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
                                        "nextsteps":[],
                                        "finish_condition":'',
                                        "condition":'',
                                       },
                                       {"name": '8372',
                                        "title": '需求了解完毕',
                                        "nextsteps": ['submit_plan'],
                                        "finish_condition":'',
                                       }
                                      ]
                              }
                         }]}

将这个工作流注册到系统::

   IPackages(root).register_workflow('zopen.sales', workflow_json)

也可以得到工作流定义信息::

   salse_query_wfl = IPackages(root).get_workflow('zopen.sales:sales_query')

导出为python格式
===================
为方便书写和阅读，系统可将流程导出为一种借用python的书写格式::

   IPackages(root).export_workflow('zopen.sales:sales_query')

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
            if ISettings(case_obj)['folder']:
                try:
                    filerepos = intids.getObject(int(ISettings(case_obj)['folder']))
                    year = str(datetime.datetime.now().year)
                    month = str(datetime.datetime.now().month) + '月'
                    if year not in filerepos:
                        year_folder = filerepos.addFolder(year)
                        IObjectIndexer(year_folder).indexObject()
                    else:
                        year_folder = filerepos[year]
                    if month not in year_folder:
                        month_folder = year_folder.addFolder(month)
                        IObjectIndexer(month_folder).indexObject()
                    else:
                        month_folder = year_folder[month]

                    project_folder = month_folder.addFolder(context['title'])
                    IObjectIndexer(project_folder).indexObject()
                    ISettings(context)['folder'] = intids.getId(project_folder)
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

   IPackages(root).import('zopen.sales:query', workflow_py)

和之前版本的改进：

1. 步骤可设置 自动触发的后续步骤: auto_steps, 方便实现无需人员干预的自动步骤
2. 如果步骤没有操作，表示这个步骤无需人员干预
3. 去除操作项中的stage, nextsteps_condition, 在步骤中增加stage

执行工作流
====================
可以为任何一个item，启动一个流程::

   IWorkitems(item).start('zopen.sales:query')

一旦启动流程，流程定义的其实步骤就开始执行，产生一些工作项。

也可以再次查看绑定的工作流::

   IWorkitems(item).get_workflow()

查看工作项::

   IWorkitems(item).list_workitems(pid, state)

通过程序触发某个操作，推动流程前进::

   IWorkitems(item).excute_action(step_name, action_name, as_principal=None, comment="")

其中：

- step_name: 步骤
- action_name: 操作
- as_principal: 可以指定以某人的身份去执行这个流程(如:users.admin)。

可以查看某个用户可以编辑、已经不让查看的表单项::

   IWorkitems(item).allowed_fields(pid)
   IWorkitems(item).disabled_fields(pid)

可以设置某个具体的workitem的信息::

    for workitem in IWorkitems(item).list_workitems():
        print '创建时间', workitem['created']
        print '工作项名', workitem['title']
        print '负责人', workitem['responsibles']
        print '完成时间', workitem['end']
        print '期限', workitem['deadline']

数据容器中的表单流程
=====================================
使用数据容器可以简便的支持表单流程::

  app_container.set_setting('item_workflows', ('zopen.sales:query',))

