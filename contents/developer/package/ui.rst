---
title: 前端组件和交互
description: 编写ajax交互应用
---

====================
前端组件和交互
====================

.. Contents::
.. sectnum::

``一切皆python``, 这是前端的指导思想：

1. 使用python来输出UI

   - 无需了解前端UI框架的html细节
   - 前端UI框架的更替，对软件包毫无影响

2. 使用python来驱动交互

   - 无需懂javascript，便可编写前端ajax界面
   - 前端框架的选择更替，对软件包毫无影响

这个指导思想，让软件包的前端开发更简单、API更持久，未来甚至可以兼容到手机原生界面。

针对这2个需求，我们提供了2套API：

- ui: 生成界面，包括通用基础的、以及系统特有的组件
- kss: 服务端驱动前端交互变化

示例
============
一个独立的页面::

    h1 = ui.h1('新的表单')
    form = ui.form(action='', title='', description='')\
                    .fields({'name':'title', type="input"}, data={'title':'the title'}, errors=errors)\
                    .action('save', '保存')\ 
                    .kss(@zopen.sales:test)  # 发起一个服务端kss请求
    help = ui.div('some help')
    return h1 + form + help

上面返回的是界面对象，我们会根据前端的类型（web、手机、桌面应用）自动适配，当然也可以只返回html::

    return h1.html() + form.html() + help.html()

kss请求提交到服务端，处理数据，并驱动前端UI::

  h1 = ui.h1('新的表单')
  form = ui.form(action='', title='', description='')\
                .fields({'name':'title', type="input"}, data={'title':'the title'}, errors=errors)\
                .action('save', '保存')\
                .kss(@zopen.sales:test)
  kss.modal(h1 + form)

向服务端发起请求
=========================
分链接和表单和js这3种

链接
--------------------------
::

  link = ui.link('click me', 'http://google.com')\    # 创建一个连接
                .kss('@zopen.sales:test?param1=xx&param2=xxx')  # 发起kss请求
                .loading('请稍等...')  # 点击发起kss之后，显示正在加载

其中kss表示，点击此链接，将向服务器发起一个ajax请求。

表单
-----
前面表单一章，表单生成的描述::

   form = ui.form(action='', title='', description='')\  # 表单的标题和action
                .fields({'name':'title', type="input"}, data={'title':'the title'}, errors=errors).\
                .action('save', '保存')\ # 增加一个按钮
                .kss(@zopen.sales:test)  # kss表单，而不是普通的表单

按钮
----------------------
::

   button = ui.button('发起新流程')\   # 按钮的连接
            .kss('@@issue_workflow_show')\  # 发起kss请求
            .loading('请稍等...')  # 点击发起kss之后，显示正在加载

面板
--------------
::

   pannel = ui.div()\
                .add(form)\  # 增加一个表单对象
                .add(button) # 再增加一个按钮

下拉菜单
-------------
::

  ui.dropmenu()

标签页
--------------------
::

  ui.tabs()\
        .tem()


导航树
------------
::

   navtree = ui.navtree(link_pattern='', 
                        kss_pattern='', 
                        expand_pattern='',  
                        data=[
                           {'title': 'level1_root',
                            'uid':'23423',
                            'icon': '',
                            'children': [ {
                                 'title': 'level1_1',
                                 'uid': '1231231',
                                 'icon': '',
                                 'children':[], },
                                {'title': 'level1_2',
                                 'uid': '1312312',
                                 'icon': '',
                                 'children': [ {
                                       'title':'level2_1',
                                       'uid': '23123',
                                       'icon':'',
                                       'children':[], },
                               ] } ] ])

children 值为None,不会出现展开图标。没有children表示用于Ajax展开。

文件预览
----------
::

   ui.file_preview()

分页条
----------
::

   ui.batch(context, request, batch)

内置功能按钮
------------------
关注按钮::

  ui.buttons.subscribe(context, request)

授权按钮::

  ui.buttons.permission(context, request)

关注按钮::

    ui.buttons.favorite(context, request)    # 收藏按钮(参数show_text默认True)

新建流程::

   ui.buttons.new_dataitem(datacontainer, title='发起新流程')

文件、流程、文件夹的遮罩查看::

   ui.buttons.preview(obj, title='发起新流程')

可选视图菜单按钮::

   ui.buttons.views(context, request)

内置面板
-----------------
通知方式面板::

    ui.portlets.notification(context, request)     # 通知方式面板

关注面板::

    ui.portlets.subscription(context, request)    # 关注面板

评注区域::

    ui.portlets.comment(context, request)        # 评注组件

标签组面板::

    ui.portlets.tag_groups(context, request)     # 标签组面板

内置链接
==================
查看个人的profile::

   ui.links.profile(pid)

服务端kss请求处理
====================

在软件包里面, 创建一个python脚本，将模板设置为 kss 即可.

kss模板的脚本，无需返回任何值，ui的操作通过 ``kss`` 来实现

站点提示信息::

   kss.message(message, type='info', )
   kss.message(message, type='error', )

跳转, 参数url是跳转到地址，target如果有值，就是内嵌iframe的名字::

   kss.redirect(url, taget)

显示一个modal窗口
------------------------
遮罩方式显示一个表单::

   kss.modal(form, width=600)

选择器
-----------------
可以类似jquery选择对象进行操作, 选择方法和jquery完全相同::

    kss.select("#content")   # 直接css定位
    kss.closet("div").find('dd')  # 采用漫游traves的方法

清空某个输入项::

   kss.closet("#input").clear()

ShowHide模式
======================
纯client端的展开/收缩切换，所有右侧面板，均采用这个模式

交互过程:

1. 点击展开变化元素 ``.KSSShowHideAction``
2. 向上找到区域 ``.KSSShowHideArea`` 
3. 在此区域中，找到所有的 ``.KSSShowHideTarget`` , 进行显示隐藏的切换

为了支持二级展开，我们还提供 ``.KSSShowHideArea2/.KSSShowHideAction2/.KSSShowHideTarget2``

由服务器再次触发一次ShowHide操作::

  kss.showhide()
  kss.closet('.KSSShowHideAction').showhide()

