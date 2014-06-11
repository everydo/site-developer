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


向服务端发起KSS请求
=========================
分连接和表单和js这3种

链接
--------------------------
点击触发kss请求的DOM结构要求, 可以给链接或者button设置::

 <a class="kss"
    kss:url="zopen.sales:test" 
    kss:node="div|.profile" 
    kss:param1="" 
    kss:param2="" 
    kss:param3="" 
    kss:param4="" 
    > click me </a>

其中:

- kssattr:url: 指定action-server的链接，是必需的
- kssattr:node: 节点
- kssattr:param1,kss:param2: 向服务端发起的参数，如果没有需要可以不写. 如果使用，对应的kssview中需要做 相应的代码调整。

表单
-----
把表单的内容，发给扩展软件包zopen.sales的test脚本::

 <form action="zopen.sales:test" class="kss">
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
parentnodecss('tr|.kk')
    父节点下的某个css，如果是形式 table|*pageid ，则会先从kss属性中获取到pagid的值作为css(如果css中包括空格，则用 * 代理)

parentnodenextnode('tr')
    父节点的下一个节点

Close模式
====================================================
点击某个链接，关闭某个区域

交互过程
-----------------------
1. 点击关闭某个区域

2. 点击，隐藏区域A(.KSSCloseArea)，然后删除区域A中的需要删除的区域(.KSSDeleteItem)

典型使用场景
-----------------------
非常多。

1. 弹出消息
2. 人员删除

DOM结构
-------------------
.KSSDeleteArea
  需要删除的区域

.KSSCloseArea
  需要隐藏的区域

.KSSDeleteItem
  隐藏区域中需要删除的区域

.KSSCloseAction
  关闭操作链接

ShowHide模式
====================================================
纯client端的展开/收缩切换 

交互过程
-----------------------
1. 点击某个链接，展开；
2. 再点击取消/关闭等链接，关闭恢复到从前。

典型使用场景
-----------------------
非常多。

1. web文件夹访问
2. 高级搜索

DOM结构
-------------------
.KSSShowHideArea / .KSSShowHideArea2
  整个操作的发生范围

.KSSShowHideAction / .KSSShowHideAction2
  点击切换的链接

.KSSShowHideTarget / .KSSShowHideTarget2
  发生显示隐藏的作用区

服务器支持命令
--------------------
由服务器再次触发一次ShowHide操作::

 kss.actionShowHide()

鼠标移动Hover模式
=========================
鼠标移动到某个区域，进行上下文相关的操作

交互过程
--------------------
纯client端的交互

1. 移动鼠标进入敏感区域，
2. 部分区域加亮，同时显示工具条；
3. 移出敏感区域，不加亮，隐藏工具条

使用场景
-------------------
1. 任务列表，移动鼠标，横条加亮
2. 编辑标

DOM结构
-----------------
.KSSHoverArea
   敏感区域

.KSSHoverHilight
   敏感区域的加亮部分，增加class: kssattr('hoverclass')

.KSSHoverToolbar
   临时显示的功能更工具条

KSSDefault模式
=======================
kss默认是禁止preventdefault的，这个可以打开

比如点击链接的时候，执行关闭操作，同时进入某个链接。

只需要在class中增加 KSSDefault即可

展开搜索Expand模式
=================================
点击展开，显示详细信息，再点击收缩

交互模式
-------------------
1. 点击横条
2. 立刻开始展开，展开给与提示

Tab模式
==========================================
一组标签按钮的选中状态切换 

交互过程
----------------------
一组按钮，都有选中和未选中2种状态

1. 点击一个，进入选中状态
2. 点击其他的按钮，当前选中状态丢失，切换为所选按钮
3. 再次点击当前选中，丢失选中状态

使用场景
---------------------
1. 右侧的功能选择按钮，比如文件的上传、编写、创建文件夹等。
2. 上方的按钮
3. 任务展开的下方操作功能区

DOM结构
---------------------
.KSSTabArea
  整个Tab模式的作用区

.KSSTabItem
  每个Tab条目，点击这个触发

.KSSTabPlain
  未选中状态

.KSSTabSelected
  选中状态

TabPage模式
==================================
是Tab模式的扩展，支持页面的切换。

交互模式
--------------
1. 点击某个tab
2. tab变成选中
3. tab页面开始显示正在加载
4. 加载页面完成，正在加载去除
5. 切换tab，页面隐藏，显示正在加载
6. 点击关闭链接，可关闭当前的tab页面，同时tab标签也不选中

使用场景
----------------
1. 上方的功能切换
2. 任务展开页面的操作

DOM结构
--------------------------
.KSSCloseTab
  关闭当前的Tab页面

XXX
  TODO


使用方法
---------------------
::

  kss.showTabPage(page_html)


ContentForm模式
======================
内容区表单，通常会和右侧的添加按钮配合使用

交互
---------------
1. 点击右侧的Tab按钮
2. 中间区域显示正在加载
3. 完成后，在中间区域显示一个表单，正在加载不再显示
4. 点击右侧其他的tab，中间区域隐藏，显示正在加载，直至表单显示
5. 表单取消后，表单关闭，显示从前内容，右侧栏的功能选择按钮需要复原

使用场景
--------------
编写文档、上传文件等

DOM结构
-------------
#kss-content-form
  整个KSS表单

.KSSContentFormAction
  点击链接，显示内容区表单

.KSSContentFormCancel
  取消关闭链接, 会：

  1. 关闭临时的ksscontentform
  2. 显示content区域
  3. 配合右侧区域，隐藏所有的.KSSContentFormAction .KSSTabSelected，显示所有的.KSSContentFormAction .KSSTabPlain

css要求
-----------------
中间区域的表单一般要使用一个showhide的div套数，显示灰色的背景，表示是临时的表单。

Action Server
---------------------
现在kssaddons里面有方法，统一处理::

  kss.showContentForm(form_html)

