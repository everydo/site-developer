---
title: KSS交互
description: 编写ajax交互应用
---

====================
KSS交互
====================

.. Contents::
.. sectnum::

使用kss，无须编写javascript，便可编写前端ajax界面. 

问题:

1. 编写ajax页面，过于复杂，需要懂javascript，而且前后端交互非常麻烦
2. 前端技术变化非常快，各种MVC框架，各种开发模式，前端框架的变化，导致应用需要重写
3. 手机版，出于性能考虑，需要原生界面

解决方法：

- 根据前端的类型，在服务器采用python生成需要的js或者json命令
- 前端解释执行这些命令
- 服务端的UI操作逻辑，可以使用DOM, 但是尽量不能和html绑太死

好处：

- 无需学习js便可写出ajax应用
- 学习曲线非常平滑
- 前端框架变化，不影响

向服务端发起KSS请求
=========================
分连接和表单和js这3种

链接
--------------------------
点击触发kss请求的DOM结构要求, 可以给链接或者button设置::

 <a class="kss"
    href="@zopen.sales:test"
    kss:param1="" kss:param2=""> click me </a>

其中:

- ``class="kss"`` : 表示发起kss请求
- ``href="@zopen.sales:test"`` : 指定action-server的链接，是必需的
- ``kss:param1`` ``kss:param2`` : 向服务端发起的参数，如果没有需要可以不写. 

表单
-----
把表单的内容，发给扩展软件包zopen.sales的test脚本::

 <form action="@zopen.sales:test" class="kss">
 </form>

js函数
----------------
在javascript中发送kss请求::

   kss(node, url, parms)

服务端kss请求处理
====================

在软件包里面, 创建一个python脚本，将模板设置为 kss 即可.

kss模板的脚本，无需返回任何值，ui的操作通过 ``kss`` 来实现

站点提示信息::

      kss.issuePortalMessage(message, msgtype='info', )
      kss.issuePortalMessage(message, msgtype=‘error', )

跳转, 参数url是跳转到地址，target如果有值，就是内嵌iframe的名字::

   kss.redirect(url, taget)

遮罩方式显示一个表单::

    form = init_form(form_json)
    content = kss.render_form(form, errors=errors, title, action, action_url, id, class)
    kss.dialogModal(content, focus=True, fixed=False, async=True, width=600, mode=None)

kss.clear
    清除

kss.addSectionOption
    给select添加option

选择器
-----------------
kss.parentnodecss('tr|.kk')
    父节点下的某个css，如果是形式 table|*pageid ，则会先从kss属性中获取到pagid的值作为css(如果css中包括空格，则用 * 代理)

kss.parentnodenextnode('tr')
    父节点的下一个节点

常见交互模式
===============

Close模式
---------------
点击某个链接，关闭某个区域. 使用场景非常多:

1. 弹出消息
2. 人员删除

交互过程：

1. 点击界面元素 ``.KSSCloseAction``
2. 从点击处向上找到区域 ``.KSSDeleteArea`` , 如果存在，删除之
3. 从点击处向上找到区域 ``.KSSCloseArea`` , 如果存在，删除此区域下的 ``.KSSDeleteItem`` 区域

ShowHide模式
------------------
纯client端的展开/收缩切换，所有右侧面板，均采用这个模式

交互过程:

1. 点击展开变化元素 ``.KSSShowHideAction``
2. 向上找到区域 ``.KSSShowHideArea`` 
3. 在此区域中，找到所有的 ``.KSSShowHideTarget`` , 进行显示隐藏的切换

为了支持二级展开，我们还提供 ``.KSSShowHideArea2/.KSSShowHideAction2/.KSSShowHideTarget2``

由服务器再次触发一次ShowHide操作::

  kss.actionShowHide()

KSSDefault模式
------------------------
kss默认是禁止preventdefault的，这个可以打开

比如点击链接的时候，执行关闭操作，同时进入某个链接。

只需要在class中增加 KSSDefault即可

Tab模式
-----------
一组标签按钮的选中状态切换 , 使用场景:

1. 右侧的功能选择按钮，比如文件的上传、编写、创建文件夹等。
2. 上方的按钮
3. 任务展开的下方操作功能区

交互过程:

1. 点击一个 ``.KSSTabItem`` ，进入选中状态 ``.KSSTabSelected``
2. 向上找到区域 ``.KSSTabArea`` , 此区域找到所有的其他的 ``.KSSTabItem`` ，设置为未选中 ``.KSSTabPlain``

TabPage模式
--------------------
是Tab模式的扩展，支持页面的切换。 使用场景

1. 上方的功能切换
2. 任务展开页面的操作

交互模式:

1. 点击某个tab
2. tab变成选中
3. tab页面开始显示正在加载
4. 加载页面完成，正在加载去除
5. 切换tab，页面隐藏，显示正在加载
6. 点击关闭链接，可关闭当前的tab页面，同时tab标签也不选中

服务端使用方法::

  kss.showTabPage(page_html)

