---
title: 流程定义语言
description: 总体借用python语言来定义
---

======================
流程定义语言
======================

表单的定义
================
示例如下::

    #-*-encoding=utf-8-*-
    title=u"""销售机会"""
    description=u"""这是销售机会的解释"""
    displayed_columns=['responsibles', '_stage', 'client', 'start', 'lastlog']
    form_layout = u"""table"""
    custom_template = u""""""
    facetag = u""""""

    fields=(
        TextLineField(
            description=u'一句话说明销售的内容',
            title=u'机会简述',
            required=False,
            storage=u'field',
            validation_exp=u'',
            write_condition=u'',
            read_condition=u'',
            size=30,
            default_value_exp=u'""',
            name=u'title'
    ),
        DataItemSelectField(
            container_exp=u'getParent(container)["orgs"]',
            multiple=True,
            description=u'',
            edoclass=u'zopen.contacts.org',
            title=u'客户',
            required=True,
            storage=u'field',
            validation_exp=u'',
            write_condition=u'',
            read_condition=u'',
            show_info=u'title',
            default_value_exp=u'PersistentList([])',
            name=u'client'
    ),
        ComputedField(
            description=u'',
            title=u'客户信息',
            required=False,
            storage=u'field',
            validation_exp=u'',
            write_condition=u'',
            read_condition=u'',
            default_value_exp=u"callScript(context,request, 'zopen.sales.client_info')",
            name=u'client_info'
    ),
        PersonSelectField(
            description=u'该销售机会的销售员',
            title=u'销售员',
            required=True,
            storage=u'field',
            validation_exp=u'',
            write_condition=u'',
            selectable_object=u'persononly',
            read_condition=u'',
            multiple_selection=False,
            default_value_exp=u'PersistentList([])',
            name=u'responsibles'
    ),
        TagSelectField(
            container_exp=u'container',
            description=u'',
            title=u'分类信息',
            required=False,
            storage=u'field',
            validation_exp=u'',
            write_condition=u'',
            read_condition=u'',
            default_value_exp=u'PersistentList([])',
            name=u'subjects'
    ),
        TextField(
            rows=5,
            description=u'',
            title=u'销售机会详情',
            required=False,
            storage=u'field',
            cols=10,
            validation_exp=u'',
            write_condition=u'',
            read_condition=u'',
            default_value_exp=u"ISettings(container)['template']",
            rich_text=False,
            name=u'case_info'
    ),
        TextField(
            rows=5,
            description=u'',
            title=u'报价方案',
            required=False,
            storage=u'field',
            cols=10,
            validation_exp=u'',
            write_condition=u'',
            read_condition=u'',
            default_value_exp=u'',
            rich_text=False,
            name=u'plan_info'
    ),
        ReferenceField(
            container_exp=u"context['folder'] is not None and intids.getObject(context['folder'])",
            is_global=False,
            multiple=True,
            description=u'',
            title=u'相关文档',
            required=False,
            storage=u'field',
            upload=True,
            validation_exp=u'',
            write_condition=u'',
            search_subtree=True,
            read_condition=u'',
            default_value_exp=u'PersistentList([])',
            name=u'files'
    ),
        FolderSelectField(
            is_global=True,
            description=u'',
            title=u'文件存放区',
            required=False,
            storage=u'field',
            validation_exp=u'',
            write_condition=u'',
            read_condition=u'',
            default_value_exp=u'ISettings(container).get("folder","")',
            name=u'folder'
    ),
        TextField(
            rows=5,
            description=u'',
            title=u'上次跟进',
            required=False,
            storage=u'field',
            cols=10,
            validation_exp=u'',
            write_condition=u'',
            read_condition=u'',
            default_value_exp=u'',
            rich_text=False,
            name=u'lastlog'
    ),
        TextField(
            rows=5,
            description=u'',
            title=u'跟进记录',
            required=False,
            storage=u'field',
            cols=10,
            validation_exp=u'',
            write_condition=u'',
            read_condition=u'',
            default_value_exp=u'',
            rich_text=False,
            name=u'log'
    ),
        DateField(
            minutestep=60,
            description=u'',
            title=u'下次跟进时间',
            showtime=True,
            required=True,
            storage=u'field',
            validation_exp=u'',
            write_condition=u'',
            read_condition=u'',
            default_value_exp=u'datetime.datetime(*(datetime.datetime.now() + datetime.timedelta(1)).timetuple()[:4])',
            name=u'start'
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
3. 类方法的函数体：步骤的触发脚本

::

  #-*-encoding=utf-8-*-

  # 第一个步骤
  class Start:
        graph_width=80,
        title=u'新的销售机会',
        fields=[u'title', u'client', u'responsibles', u'case_info', 'subjects'],
        invisible_fields=[u'plan_info', u'files', u'folder', 'lastlog', 'log', 'start'],
        graph_x=364,
        graph_y=385,
        trigger=ur"""""",
        graph_height=30,
        condition=u'',
        responsibles=u'[request.principal.id]',

        # 这是一个流程操作
        def submit(title=u'提交', graph_x=0, graph_y=0, nextsteps=['Communicate'],
                  finish_condition=u'', nextsteps_conditions=u'', stage=u'valid'):
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
        graph_width=80,
        title=u'了解需求背景',
        fields=[u'title', u'case_info', u'files', u'log', u'start', 'subjects'],
        invisible_fields=[u'plan_info', u'lastlog'],
        graph_x=364,
        graph_y=385,
        trigger=ur"""""",
        graph_height=30,
        condition=u'',
        responsibles=u'context["responsibles"]',

        # 第一个步骤 
        def duplicated( title=u'重复或无效, 不再跟进', graph_x=0, graph_y=0, nextsteps=[],
            finish_condition=u'', nextsteps_conditions=u'', condition=u'', stage=u'no_valid'),
            pass

        # 第二个步骤
        def AA8372( title=u'需求了解完毕', graph_x=0, graph_y=0, nextsteps=['SubmitPlan'],
            finish_condition=u'', nextsteps_conditions=u'', stage=u'planing'):
            pass

  # 第三个步骤
  class SubmitPlan:
        graph_width=80,
        title=u'方案确认',
        fields=[u'title', u'case_info', 'plan_info', u'files', u'log', 'start', 'subjects'],
        invisible_fields=[],
        graph_x=364,
        graph_y=385,
        trigger=ur"""if 'stage.delayed' in context.stati:IStateMachine(context).setState('flowsheet.pending', do_check=False)""",
        graph_height=30,
        condition=u'',
        responsibles=u'context["responsibles"]',

        # 操作一
        def pause( title=u'暂停，以后再联系', graph_x=0, graph_y=0, nextsteps=['SubmitPlan'],
               finish_condition=u'', nextsteps_conditions=u'', condition=u'', stage=u'delayed'):
            pass

        def accept( title=u'接受方案，准备合同', graph_x=0, graph_y=0, nextsteps=['SubmitFile'],
            finish_condition=u'', nextsteps_conditions=u'', stage=u'plan_accept'):
            pass

        def cannotdo( title=u'无法满足需求', graph_x=0, graph_y=0, nextsteps=['Lost'],
            finish_condition=u'', nextsteps_conditions=u'', condition=u'', stage=u'lost'):
            pass

        def other( title=u'已选用其它产品', graph_x=0, graph_y=0, nextsteps=['Lost'],
            finish_condition=u'', nextsteps_conditions=u'',
            condition=u"'stage.lost' != IStateMachine(context).getState('stage').name",
            stage=u'lost'):
            pass

  # 最后一个步骤
  class SubmitFile:
        title=u'签订合同',
        fields=[u'files', 'log', 'start'],
        invisible_fields=[],
        graph_x=364,
        graph_y=385,
        trigger=ur"""""",
        graph_height=30,
        condition=u'',
        responsibles=u'context["responsibles"]',

        def sign( title=u'合同签订', graph_x=0, graph_y=0, nextsteps=[],
            finish_condition=u'', nextsteps_conditions=u'', stage=u'turnover'):
            pass

        def contact_later( title=u'变故，以后再联系', graph_x=0, graph_y=0,
            nextsteps=['SubmitPlan'], finish_condition=u'', nextsteps_conditions=u'',
            condition=u'', stage=u'delayed'):
            pass

        def fail( title=u'失败', graph_x=0, graph_y=0, nextsteps=['Lost'], finish_condition=u'',
            nextsteps_conditions=u'', stage=u'lost'):
            pass

  class Lost:
        title=u'丢单确认',
        fields=[],
        invisible_fields=[],
        graph_x=364,
        graph_y=385,
        trigger=ur"""""",
        graph_height=30,
        condition=u'',
        responsibles=u'ISettings(container)["manager"]',

        def confire_fail( title=u'确认丢单', graph_x=0, graph_y=0, nextsteps=[],
            finish_condition=u'', nextsteps_conditions=u'', stage=u'lost'):
            pass

        def continue( title=u'继续跟单', graph_x=0, graph_y=0, nextsteps=[u'SubmitPlan'],
            finish_condition=u'', nextsteps_conditions=u'', stage=u'planing'):
            pass
