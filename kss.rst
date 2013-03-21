.. Contents::
.. sectnum::

编写KSS类
================
命名和基类:  
------------------
kss的view一般命名为 XXXKssView，需要继承zopen.kssaddons.KSSView 

表单内容的展现
-------------------
对于展现表单的kss，名字为 addAppForm (url: add_app_form):

    render_add_app =  ViewPageTemplateFile('add_app.pt')

    @kssaction
    def addAppForm(self):
        # ... 这里可补充本来应该在update里面处理的东西

        # 内容区
        self.ksszopen.showContentForm(self.render_add_app())

如果上方区域的，则是::

        self.ksszopen.showTabPage(self.render_add_app())

通用变量param1, param2
----------------------------------
使用前换成一个友好的变量名，比如::
    mode, url = param1, param2

对于表单提交后，仍然是kss处理： addFlowSubmit (add_flow_submit)

错误提示
--------------
在正文区域提示信息::

  self.ksszopen.issuePortalMessage(self, message, msgtype='info', )
  self.ksszopen.issuePortalMessage(self, message, msgtype=‘error', )

在上方contentbar区域提示信息::

  self.ksszopen.issuePortalMessage(self, message, msgtype='info', position='contentbar')

ActionServer模式
================================
不需要另外写kss规则，就进行服务器端的kss交互。

这样可减少kss规则的数量，提升性能。

一般有2中情况会导致ActionServer: onclick 和 onsubmit.

页面的写法
--------------------------
::

 <a class="KSSActionServer"
    kssattr:url="" 
    kssattr:kssfiles="" 
    kssattr:node="div|.profile" 
    kssattr:param1="" 
    kssattr:param2="" 
    kssattr:param3="" 
    kssattr:param4="" 
    > click me </a>

KSS写法
--------------------------
::

 action-server: url(ssss) notloadedKss('upload.kss adsfa.kss');

DOM结构
--------------------------
.KSSActionServer
    点击会发生action-server操作的按钮或链接

编码步骤
--------------------------
1. 所有resource.zcml中，对该kss的引用。对于界面中需要使用action-server的操作，做如下的处理

#. 给需要发生action-server操作的按钮或链接的class加上KSSActionServer

#. kssview中用到的kssaction,KSSView改为从zopen.kssaddons中导入::

    from zopen.kssaddons import kssaction,KSSView

#. 参照上面的页面的写法给按钮或链接加上kssattr:url,kssattr:kssfiles,kssattr:param1,kssattr:param2

   kssattr:url: 
        指定action-server的链接，是必需的

   kssattr:kssfiles: 
        用于action-server返回的html需要的kss,例如点击权限按钮后，需要动态加载localrole.kss,
        可以这样写：kssattr:kssfiles="localrole.kss"
        需要加载多个kss：kssattr:kssfiles="localrole.kss selector.kss",以空格分开

   kssattr:param1,kss:param2：
        这两个是用于提交你需要的参数的，如果没有需要可以不写. 如果使用，对应的kssview中需要做 相应的代码调整。

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

 ksszopen.actionShowHide()

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
kssview::

  ksszopen.showTabPage(page_html)


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

  ksszopen = self.getCommandSet('zopen')
  ksszopen.showContentForm(form_html)


formsubmit模式
=======================
使用场景
--------------------
非常多，几乎所有的ajax表单提交都可以用

DOM结构
--------------------
.KSSFormArea
  整个表单区域

form.KSSFormSubmit
  需要ajax提交的表单

.KSSFormShowHide
  表单提交时需要切换状态的地方

页面的写法
--------------------
::

 <form action="@@submit.html" class="KSSFormSubmit" kssattr:kssfiles="">
 </form>

action-server
-------------------------
如果发现表单出错，可取消::

  ksszopen.resetForm()

全选和全不选
=======================
典型使用场景
------------------
项目中发送消息的时候，全选项目成员

DOM结构
-----------------
.KSSCheckArea
  整个选择的作用区

.KSSCheckAll
  全选checkbox

.KSSUnCheckAll
  全不选checkbox

.KSSCheckItem
  需要被选中或不选的checkbox

.KSSSelect
  选择全选或全不选后需要变换显示的地方

kss代码优化步骤
============================
action-client
----------------------------
1.清理.kss中已经没有用的kss代码，虽然没有用到，但每次加载都会计算的，所以要去除

2.规范id与class的写法，id为'kss-xxxxx'，class为'KSSxxxxx'
  例如：kss-portal-search，KSSSearch

3..kss中要以id为基准去写
   例如：#kss-prtal-search a:click

4.套用模式，不要写重复的同样效果的kss代码

action-server
---------------------------
1.找出页面中不常用的功能

2.对于不常用的功能，用ActionServer模式可以改为action-server的操作，具体参照该模式的编码步骤

3.使用ActionServer模式后kssview中的代码有些是可能可以去除，要去除多余的代码

注释
--------------------------
无论是kss还是kssview中都希望能加上正确的注释，因为没有注释，维护会变得很困难


"ksszopen" 插件
========================
命令
-------------
redirect
    跳转, 参数包括url和target，url是跳转到地址，target如果有值，就是内嵌iframe的名字。

clear
    清除

addSectionOption
    给select添加option

issurePortalMessage
    显示消息，三个参数 msg, type, position。其中 position表示位置，contentbar就是上方contentbar区域的提示

选择器
-----------------
parentnodecss('tr|.kk')
    父节点下的某个css，如果是形式 table|*pageid ，则会先从kssattr中获取到pagid的值作为css(如果css中包括空格，则用 * 代理)

parentnodenextnode('tr')
    父节点的下一个节点

辅助函数
------------------
kssAttrJoin('lal', '*itemid', '/@@edit.html')
    合并kss，其中带 * 的标记表示需要从kssattr中获取的

函数
----------------
kssServerAction(node, actionName, parms)
    在javascript中发送消息


