---
title: UI组件和交互
description: 编写ajax交互应用
---

====================
UI组件和交互
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
- view: 服务端驱动前端交互变化

示例
============
一个独立的页面::

    h1 = ui.h1('新的表单')
    form = ui.form(title='', description='')\
                    .fields(form_def,
                            data={'title':'the title'}, 
                            errors=errors)\
                    .button('save', '保存')\
                    .on('submit', '@zopen.sales:test') \ # 发起一个服务端请求
                    .loading('加载中...')  # 设置加载提示文字
    help = ui.div('some help')
    return h1 + form + help

上面返回的是界面对象，我们会根据前端的类型（web、手机、桌面应用）自动适配，当然也可以只返回html::

    return h1.html() + form.html() + help.html()

请求提交到服务端，处理数据，并驱动前端UI::

  h1 = ui.h1('新的表单')
  form = ui.form(action='', title='', description='')\
                .fields(form_def, data={'title':'the title'}, errors=errors)\
                .button('save', '保存')\
                .on('submit', '@zopen.sales:test')
  view.modal(h1 + form)

ui 元素
=========================

链接
--------------------------
::

  link = ui.link('click me', href='http://google.com')\    # 创建一个连接
                .on('click', '@zopen.sales:test?param1=xx&param2=xxx')\  # 发起ajax请求
                .loading('请稍等...')  # 点击发起请求之后，显示正在加载


如果连接需要配一个图标，则可以::

  link.icon('star')

``.loading('请稍等...', '')`` 表示点击后，在链接处出现加载标识。如果希望整个页面主区域出现加载等待，可以设置所在的layout区域::

  link.loading('正在加载...', 'main')
  link.loading('', 'right')

有些链接，内部可以是多个内容，比如::

  ui.link('', href='#')\
        .child(ui.h1('大标题'))\
        .child(ui.text('一些描述信息'))

如果链接需要增加徽章::

  link.badge('12')

输入控件
-----------------
只显示一个控件::

   input = ui.field(name='title', type='TextLine')

一段文字
---------------
:: 

   ui.text('some html text')

如果需要完全保留原始格式, 不折行::

   ui.text('some html text').pre()

如果希望黯淡的方式显示次要文字，可以::

   ui.text('lalal a').discreet()

按钮
----------------------
::

   button = ui.button('发起新流程')\   # 按钮的连接
            .on('click', '@@issue_workflow_show')\  # 发起请求
            .loading('请稍等...')\  # 点击发起之后，显示正在加载
            .size('large')\  # 大尺寸
            .icon('star')

可选的size: large, small, xsmall

html代码
----------------
也可以直接显示一段html代码::

  html = ui.html('<ul><li>asdf</li></ul>')

UI集合
===========================

列表组
---------------
列表组包括一组对象, 每个对象占一行，鼠标经过会高亮，选中行业可加亮。 参看 `bootstrap章节 <http://v3.bootcss.com/components/#list-group>`__ ::

   ui.list_group(ui.link('abc', href='').on('click', '@zopen.test:test').active(),
                ui.link('dd', href=''),
                )

可以做出比较复杂的列表组::

   ui.list_group(
      ui.link('', href='#')\
            .child(ui.text('大标题'))\
            .child(ui.text('一些描述信息').discreet())\
            .on('click', '@zopen.test:testt')\
            .active(),

      ui.link('abc', href='').on('click', '@zopen.test:test'),
                )

下拉菜单
-------------
::

  menu = ui.menu(ui.link('aaa', url='google.com').on('click', '@zopen.test:tt').active(),
                   ui.separator(),
                   ui.link('bbb', url='google.com').on('click', '@zopen.test:tt'))

  button.dropdown(menu)
  button.dropup(menu)

表单
-----
前面表单一章，表单生成的描述::

   form = ui.form(action='', title='', description='')\  # 表单的标题和action
                .fields(form_def, data={'title':'the title'}, errors=errors).\
                .action('save', '保存')\ # 增加一个按钮
                .on('submit', '@zopen.sales:test')  # 表单，而不是普通的表单

其中fields的书写方法，见 ``表单处理`` 


按钮组
---------------
::

  ui.button_group(btn1, btn2).virtical().justify()

面板
--------------
一个面包包括多个组件，默认竖排::

   panel = ui.panel(form, button)

也可以横排::

   panel.horizon()

可将面板做成可折叠的::

   panel.collapse(True)  # True表示初始折叠

可以增加title, 参看 `bootstrap章节 <http://v3.bootcss.com/components/#panels>`__ ::

   panel.title(ui.text('面板示例'))

可以在title右侧增加一个toolbox::

   panle.toolbox(ui.botton('设置').on('click', '@zopen.seals:ad')) # 一个按钮
   panle.toolbox(menu) # 增加一个menu

也可以增加一个footer::

   panel.footer(ui.link('sss', url))

导航
--------------------
::

  ui.nav(ui.link('title', url).on('click', '@zopen.test:tt').active(),
         ui.link('title 2', url).on('click', '@zopen.test:tt'),
        )

二级导航::

  ui.nav(ui.link('title', url).on('click', '@zopen.test:tt').active(),
         ui.link('title 2', url).on('click', '@zopen.test:tt'),
        ).sub()

带切换页面的tab也导航::

  ui.tabs()\
        .tab(ui.link('title', url="").active(), ui.panel())\
        .tab(ui.link('title', url="").on('click', '@zopen.test:tt'), ui.panel())

其中 ``on`` 用于动态加载页面内容，动态加载脚本可以这样写::

    text = ui.text('this is page from server. :-)')
    view.tabs.closest().active_panel().set_content(text)

其中：

- ``view.tabs.closest()`` 找到最近的一个tabs组件；
- ``active_panel()`` 找到tabs当前活动的panel
- ``set_content(text)`` 设置panel的内容

可以看到每个组件包括ui方法来构建组件，和view命令来操作组件

路径
--------------
::

  ui.breadcrumb(
        ui.link('node 2', url='').on_click('@zopen.test:tt')
        ui.link('node 1', url='').active().on_click('@zopen.test:tt'),
                )

树
------------
::

   tree = ui.tree(ui.link('level1_root').on_click('@zopen.sales:aa')\
                        .child( ui.link('level1').on('click', '@zopen.sael:bb').on('expand', '@zopen.test:aaa')\
                        .child( ui.link('level2').on_click('@zopen.sael:bb')\
                                   .child(ui.link('level2 1').on('click', '@zopen.sales:cc'))
                              )
                  )

默认tree是收缩的，可以将第一级展开::

   tree.expand()

对于动态展开的，设置 ``.child`` 的时候，需要附加展开的处理方法 ``on('expand',`` ，这里可以动态为该节点增加子节点::

   view.tree.child( uilink('level1', id="uid").on('click', '@zopen.sael:bb') )
   view.tree.child( uilink('level1', id="uid").on('click', '@zopen.sael:bb').on('expand', '@zopen.aa:ff') )

分页条
----------
::

   ui.pagination(batch, start=0).on('click', '@zopen.sales:listing')

UI视图
================

评论视图
--------------

Feed
----------

Items
---------

UI模块
==============
模块定义UI基础行为

显示一个modal窗口
------------------------
遮罩方式显示一个表单::

   view.modal(form, width=600)

系统功能组件
==================

文件查看器
----------------
::

   ui.doc_viewer(context, request).image()

我的工作
--------------
我的代办事项::

   ui.my_workitems(context, reqeust, pid=None)

其中pid表示谁的代办事项.

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

view交互命令
====================

在软件包里面, 创建一个python脚本，ui的操作通过 ``view`` 来实现

站点消息提示
-----------------
站点提示信息::

   view.message(message, type='info', )
   view.message(message, type='error', )

选择器
-----------------
可以借助ui对象提供的选择器进行选择，比如上面的::

    view.tabs.closest().active_panel()

内容操作
------------
设置中间的主区域内容，可以::

   view.layout.main().set_content(form)

设置右侧区域的内容，可以::

   view.layout.right().set_content(form)

也可以在右侧区域，补充一个内容::

   view.layout.right().append(form)
   view.layout.right().prepend(form)

清空内容区上方列::

   view.layout.above().empty()

左右侧列都可以显示隐藏::

   view.layout.hide_left()
   view.layout.show_left()
   view.layout.hide_right()
   view.layout.show_right()

桌面助手
-----------------
上传文件::

   view.assistent.upload_files(folder_uid, local_files)

下载文件::

   view.assistent.download_files(uids)

文件夹同步::

   view.assistent.sync_folder(folder_uid)

操作历史
---------------
::

   view.history.push_state(data, title)
   view.history.replace_state(data, title)
   view.history.back()
   view.history.go(2)

跳转
---------
参数url是跳转到地址，target如果有值，就是内嵌iframe的名字::

   view.redirect(url, taget)

禁用事件触发
----------------------
如果不希望每次点击都进行事件触发，可以禁用::

   view.off('click')

事件触发和捕获
=======================
首先需要在网页上设置事件处理方法::

   ui.script().on('dataitem-change', "@zopen.test:refresh")

在view触发一个事件::

   view.trigger('dataitem-change', uid=12312, title=123123')

这时候会向服务器发起一个请求::

   @zopen.test:refresh?event=dateitem-change&uid=1312&title=123123

在 ``zopen.test:refresh`` 中做事件处理

前端脚本
==============
可以直接写python来执行前端逻辑，python会解释生成前端需要的语言，比如javascript::

   ui.button('aa').on('click', '', func="process_click")
   ui.script('zopen.tests:python/base.py').on('data-change', '', func)

如果希望和html混合
==========================
我们不希望你用html/css/js，如果你还是想用，可以使用 `.html()` 转换为html::

   blabla = ui.link('a') + ui.link('b')
   html = blabla.html() + '<a href=""> you html code</a>'
   return html

