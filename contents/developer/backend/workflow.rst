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

定义工作流步骤
====================

工作流由步骤Step和操作Action组成，可以用2种方式定义。

json格式
---------------
json格式适合软件自动生成，适合在浏览器上解析读取::

    workflow_json = {
       "start": {"title" : '新的销售机会',
                 "fields": ['title', 'client', 'responsibles', 'case_info', 'subjects'],
                 "invisible_fields": ['plan_info', 'files', 'folder', 'lastlog', 'log', 'start'],
                 "on_enter": "",
                 "condition": '',
                 "stage": 'valid',
                 "responsibles": '[request.principal.id]',
                 "actions": [ { "name":'submit',
                                "title":'提交',
                                "finish_condition":'',
                                "nextsteps":['communicate'],
                                "on_submit":"",
                              }
                            ]
               },

      "auto_step": { "title": '这是一个自动步骤，无人参与',
                  "condition":'',
                  "on_enter": "",
                  "stage":'planing',
                      },

      "communicate": { "title": '了解需求背景',
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
                 }

将这个工作流注册到系统::

   IWorkflows(root).register('sales_query', workflow_json, package='zopen.sales')

也可以得到工作流定义信息::

   salse_query_wfl = IWorkflows(root).get('sales_query', package='zopen.sales')

python格式
------------------
json格式的问题是，流程如果存在大量脚本，不方便书写和阅读，也不方便检查错误。因此，系统提供一种借用python的书写格式::

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

将这个工作流注册到系统，需要转换为json格式在导入::

   IWorkflows(root).python2json(workflow_py)

也可以把json转为python方便书写::

   IWorkflows(root).json2python(workflow_json)

执行工作流
====================
然后启动一个流程::

   IWorkitems(item).start(('zopen.sales:query', ))

通过程序触发某个操作::

   IWorkitems(item).excute_action(step_name, action_name, as_principal=None, comment="")

其中：

- step_name: 步骤
- action_name: 操作
- as_principal: 可以指定以某人的身份去执行这个流程(如:users.admin)。

可以查看可以编辑、已经不让查看的表单项::

   IWorkitems(item).allowed_fields(pid)
   IWorkitems(item).disabled_fields(pid)

如果希望得到某个流程单的当前任务::

   IWorkitems(item).list_workitems(pid, state)

可以设置某个具体的workitem的信息::

    for workitem in IWorkitems(item).list_workitems():
        workitem_md = IMetadata(workitem)
        print '创建时间', workitem_md['created']
        print '工作项名', workitem_md['title']
        print '负责人', workitem_md['responsibles']
        print '完成时间', workitem_md['end']
        print '期限', workitem_md['deadline']

