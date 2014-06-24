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

参照 `GWT <http://www.gwtproject.org/doc/latest/RefWidgetGallery.html>`__ 和
`pyjs <https://github.com/pyjs/pyjs/tree/master/pyjswidgets/pyjamas>`__ 以及
`twitter bootstrap <http://v3.bootcss.com/components/>`__ ,
针对这2个需求，我们提供了2套API：

- ui: 生成界面，包括通用基础的、以及系统特有的组件
- kss: 服务端驱动前端交互变化

示例
============
一个独立的页面::

    h1 = ui.h1('新的表单')
    form = ui.form(action='', title='', description='')\
                    .fields(form_def,
                            data={'title':'the title'}, 
                            errors=errors)\
                    .button('save', '保存')\
                    .kss(@zopen.sales:test)  # 发起一个服务端kss请求
    help = ui.div('some help')
    return h1 + form + help

上面返回的是界面对象，我们会根据前端的类型（web、手机、桌面应用）自动适配，当然也可以只返回html::

    return h1.html() + form.html() + help.html()

kss请求提交到服务端，处理数据，并驱动前端UI::

  h1 = ui.h1('新的表单')
  form = ui.form(action='', title='', description='')\
                .fields(form_def, data={'title':'the title'}, errors=errors)\
                .button('save', '保存')\
                .kss('@zopen.sales:test')
  kss.modal(h1 + form)

ui 元素
=========================

链接
--------------------------
::

  link = ui.link('click me', href='http://google.com')\    # 创建一个连接
                .kss('@zopen.sales:test?param1=xx&param2=xxx')\  # 发起kss请求
                .loading('请稍等...')  # 点击发起kss之后，显示正在加载

其中kss表示，点击此链接，将向服务器发起一个ajax请求。

表单
-----
前面表单一章，表单生成的描述::

   form = ui.form(action='', title='', description='')\  # 表单的标题和action
                .fields(form_def, data={'title':'the title'}, errors=errors).\
                .action('save', '保存')\ # 增加一个按钮
                .kss('@zopen.sales:test')  # kss表单，而不是普通的表单

其中fields的书写方法，见 ``表单处理`` 

输入控件
-----------------
只显示一个控件::

   input = ui.field(name='title', type='TextLine')

按钮
----------------------
::

   button = ui.button('发起新流程')\   # 按钮的连接
            .kss('@@issue_workflow_show')\  # 发起kss请求
            .loading('请稍等...')  # 点击发起kss之后，显示正在加载
            .size('lg')

可选的size: lg, sm, xs

下拉菜单
-------------
::

  menu = ui.menu(ui.link('aaa', url='google.com').kss('@zopen.test:tt').active(),
           ui.separator(),
           ui.link('bbb', url='google.com').kss('@zopen.test:tt'))

  button.dropdown(menu)
  button.dropup(menu)

按钮组
---------------
::

  ui.button_group(btn1, btn2).virtical().justify()
  

导航
--------------------
::

  ui.nav(ui.link('title', url).kss('@zopen.test:tt').active(),
         ui.link('title 2', url).kss('@zopen.test:tt'),
        )

带切换页面的tab也导航::

  ui.tabs()\
        .tab(ui.link('title', url="").active(), ui.pannel())\
        .tab(ui.link('title', url="").kss('@zopen.test:tt'), ui.pannel())

路径
--------------
::

  ui.breadcrumb(
        ui.link('node 2', url='').kss.('@zopen.test:tt')
        ui.link('node 1', url='').active().kss('@zopen.test:tt'),
                )

树
------------
::

   tree = ui.tree(ui.link('level1_root').kss('@zopen.sales:aa')\
                        .add(ui.link('level1').kss('@zopen.sael:bb'))\
                        .add(ui.link('level2').kss('@zopen.sael:bb')\
                                .add('level2 1').kss('@zopen.sales:cc')))

文件预览
----------
::

   ui.file_preview()

分页条
----------
::

   ui.batch(context, request, batch)

ui布局组件
=================

面板
--------------
::

   pannel = ui.pannel(form, button).horizon()

显示一个modal窗口
------------------------
遮罩方式显示一个表单::

   kss.modal(form, width=600)

系统功能组件
==================
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
--------------
查看个人的profile::

   ui.links.profile(pid)

kss交互命令
====================

在软件包里面, 创建一个python脚本，将模板设置为 kss 即可.

kss模板的脚本，无需返回任何值，ui的操作通过 ``kss`` 来实现

站点消息提示
-----------------
站点提示信息::

   kss.message(message, type='info', )
   kss.message(message, type='error', )

选择器
-----------------
可以类似jquery选择对象进行操作, 选择方法和jquery完全相同::

    kss.select("#content")   # 直接css定位
    kss.closet("div").find('dd')  # 采用漫游traves的方法

清空某个输入项::

   kss.closet("#input").clear()

操作历史
---------------
::

   kss.history.push_state(data, title)
   kss.history.replace_state(data, title)
   kss.history.back()
   kss.history.go(2)

跳转
---------
参数url是跳转到地址，target如果有值，就是内嵌iframe的名字::

   kss.redirect(url, taget)

事件触发和捕获
=======================
首先需要在网页上设置事件处理方法::

   ui.on('dataitem-change').kss("@zopen.test:refresh")

在kss触发一个事件::

   kss.trigger('dataitem-change', uid=12312, title=123123')

这时候会向服务器发起一个kss请求::

   @zopen.test:refresh?event=dateitem-change&uid=1312&title=123123

在 ``zopen.test:refresh`` 中做事件处理

前端脚本
==============
可以直接写python来执行前端逻辑，python会解释生成前端需要的语言，比如javascript::

   ui.button('aa').on('click', "process_click")')
   ui.script('zopen.tests:python/base.py')

