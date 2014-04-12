---
title: 软件包格式
description: 使用python语言来打包一个应用
---

======================
软件包格式
======================

每个应用使用软件包来发布和交换。软件包就是一个tgz的压缩包。

总体借用Python语言来定义表单和流程. 压缩包内的文件组织::

  __init__.py #  软件包基本信息
  workflows/
    sales_chance/  # 第一个流程
      form.py # 表单
      steps.py # 流程步骤
      stages.py # 阶段定义
      config.py # 流程设置属性
    invoice/  # 第二个流程
  metadata/ # 整个应用的属性定义
    settings.py # 应用设置信息
    archive.py # 档案管理属性集
  i18n/
    aaa.po
    bbb.po 
  scripts/ # 代码
    setup.py   # 安装程序
    get_abc.py   # 安装程序
  templates/ # 动态模板
    main.pt
  screenshots/ # 产品截图
  resources/ #  资源文件

表单 form.py
================
示例如下::

    #-*-encoding=utf-8-*-
    title="销售机会"
    description="""这是销售机会的解释"""
    displayed_columns=['responsibles', '_stage', 'client', 'start', 'lastlog']
    form_layout = "table"
    custom_template = ""
    facetag = ""

    fields=(
        TextLineField(
            description='一句话说明销售的内容',
            title='机会简述',
            required=False,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            size=30,
            default_value_exp='""',
            name='title'
    ),
        DataItemSelectField(
            container_exp='getParent(container)["orgs"]',
            multiple=True,
            description='',
            edoclass='zopen.contacts.org',
            title='客户',
            required=True,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            show_info='title',
            default_value_exp='PersistentList([])',
            name='client'
    ),
        ComputedField(
            description='',
            title='客户信息',
            required=False,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp="callScript(context,request, 'zopen.sales.client_info')",
            name='client_info'
    ),
        PersonSelectField(
            description='该销售机会的销售员',
            title='销售员',
            required=True,
            storage='field',
            validation_exp='',
            write_condition='',
            selectable_object='persononly',
            read_condition='',
            multiple_selection=False,
            default_value_exp='PersistentList([])',
            name='responsibles'
    ),
        TagSelectField(
            container_exp='container',
            description='',
            title='分类信息',
            required=False,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='PersistentList([])',
            name='subjects'
    ),
        TextField(
            rows=5,
            description='',
            title='销售机会详情',
            required=False,
            storage='field',
            cols=10,
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp="ISettings(container)['template']",
            rich_text=False,
            name='case_info'
    ),
        TextField(
            rows=5,
            description='',
            title='报价方案',
            required=False,
            storage='field',
            cols=10,
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='',
            rich_text=False,
            name='plan_info'
    ),
        ReferenceField(
            container_exp="context['folder'] is not None and intids.getObject(context['folder'])",
            is_global=False,
            multiple=True,
            description='',
            title='相关文档',
            required=False,
            storage='field',
            upload=True,
            validation_exp='',
            write_condition='',
            search_subtree=True,
            read_condition='',
            default_value_exp='PersistentList([])',
            name='files'
    ),
        FolderSelectField(
            is_global=True,
            description='',
            title='文件存放区',
            required=False,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='ISettings(container).get("folder","")',
            name='folder'
    ),
        TextField(
            rows=5,
            description='',
            title='上次跟进',
            required=False,
            storage='field',
            cols=10,
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='',
            rich_text=False,
            name='lastlog'
    ),
        TextField(
            rows=5,
            description='',
            title='跟进记录',
            required=False,
            storage='field',
            cols=10,
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='',
            rich_text=False,
            name='log'
    ),
        DateField(
            minutestep=60,
            description='',
            title='下次跟进时间',
            showtime=True,
            required=True,
            storage='field',
            validation_exp='',
            write_condition='',
            read_condition='',
            default_value_exp='datetime.datetime(*(datetime.datetime.now() + datetime.timedelta(1)).timetuple()[:4])',
            name='start'
    ),)

    def update_trigger(context, old_context):
        # 如果有根据记录，做记录循环，并保存为评论
        log = (context['log'] or '').strip()
        if log:
            context['lastlog'] = log
            context['log'] = ''
            ICommentManager(context).addComment(log)

        if old_storage:
            for user_id in old_storage['responsibles']:
                IGrantManager(context).unsetRole('zopen.Editor',user_id)

        for user_id in context['responsibles']:
            IGrantManager(context).grantRole(r'zopen.Editor', user_id)

        # 如果下次跟进时间，小于当前时间，则将下次跟进时间改为当前时间+2天
        if context['start'] <= datetime.datetime.now():
            context['start']=datetime.datetime(*(datetime.datetime.now() + datetime.timedelta(2)).timetuple()[:4])

流程步骤定义
====================
我们采用标准的Python语法来定义流程：

1. 类名: 步骤名
2. 类的成员变量: 步骤的属性
3. 类的方法名: 步骤的操作name
4. 类方法的函数体：步骤的触发脚本

::

  #-*-encoding=utf-8-*-

  # 第一个步骤
  class Start:
        title='新的销售机会',
        fields=['title', 'client', u'responsibles', u'case_info', 'subjects'],
        invisible_fields=['plan_info', 'files', u'folder', 'lastlog', 'log', 'start'],
        condition='',
        responsibles='[request.principal.id]',

        # 进入这个步骤触发
        def __init__(): 
            pass

        # 这是一个流程操作
        @action('提交', ['Communicate'], finish_condition='', nextsteps_conditions='', stage=u'valid')
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
        title='了解需求背景',
        fields=['title', 'case_info', u'files', u'log', u'start', 'subjects'],
        invisible_fields=['plan_info', 'lastlog'],
        condition='',
        responsibles='context["responsibles"]',

        # 进入这个步骤触发
        def __init__(): 
            pass

        # 第一个步骤 
        @action('重复或无效, 不再跟进', [], finish_condition='', nextsteps_conditions='', condition=u'', stage=u'no_valid')
        def duplicated(context, container, task, step):
            pass

        # 第二个步骤
        @action('需求了解完毕', ['SubmitPlan'], finish_condition='', nextsteps_conditions='', stage=u'planing')
        def AA8372( context, container, task, step):
            pass

  # 第三个步骤
  class SubmitPlan:
        title='方案确认',
        fields=['title', 'case_info', 'plan_info', 'files', 'log', 'start', 'subjects'],
        invisible_fields=[],
        condition='',
        responsibles='context["responsibles"]',

        # 进入这个步骤触发
        def __init__(): 
            if 'stage.delayed' in context.stati:
                IStateMachine(context).setState('flowsheet.pending', do_check=False)

        # 操作一
        @action('暂停，以后再联系', ['SubmitPlan'], finish_condition='', nextsteps_conditions='', condition=u'', stage=u'delayed')
        def pause(context, container, step, task):
            pass

        @action('接受方案，准备合同', ['SubmitFile'], finish_condition='', nextsteps_conditions='', stage=u'plan_accept')
        def accept( context, container, step, task):
            pass

        @action('无法满足需求', ['Lost'], finish_condition='', nextsteps_conditions='', condition=u'', stage=u'lost')
        def cannotdo( context, container, step, task):
            pass

        @action('已选用其它产品', ['Lost'], finish_condition='', nextsteps_conditions='',
            condition="'stage.lost' != IStateMachine(context).getState('stage').name", stage='lost')
        def other( context, container, step, task):
            pass

  # 最后一个步骤
  class SubmitFile:
        title='签订合同',
        fields=['files', 'log', 'start'],
        invisible_fields=[],
        condition='',
        responsibles='context["responsibles"]',

        # 进入这个步骤触发
        def __init__(): 
            pass

        @action('合同签订', [], finish_condition='', nextsteps_conditions='', stage=u'turnover')
        def sign(context, container, step, task):
            pass

        @action('变故，以后再联系', ['SubmitPlan'], finish_condition='', nextsteps_conditions='', condition='', stage='delayed')
        def contact_later(context, container, step, task):
            pass

        @action('失败', ['Lost'], finish_condition='', nextsteps_conditions='', stage='lost')
        def fail( context, container, step ,task):
            pass

  class Lost:
        title='丢单确认',
        fields=[],
        invisible_fields=[],
        condition='',
        responsibles='ISettings(container)["manager"]',

        # 进入这个步骤触发
        def __init__(): 
            pass

        @action( '确认丢单', nextsteps=[], finish_condition='', nextsteps_conditions='', stage=u'lost')
        def confire_fail( context, container, step, task):
            pass

        @action( '继续跟单', ['SubmitPlan'], finish_condition='', nextsteps_conditions='', stage=u'planing')
        def continue( context, container, step, task):
            pass

