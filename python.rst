.. Contents::

用途
===================
被其他的脚本调用
-------------------------
call_script('zopen.api.calc', a, b)

页面
----------------
- 权限
- 模板

生成代码
==================
::

 @script_attr(permission='zopen.Access',use_template='none',icon=u'')
 def getResponsibles(responsibles):
    """得到各电子签收负责人

 None"""

几种模板
===================
空模板
---------
也就是没有任何模板

空白模板
----------
保留css/js的模板

基础basic模板
---------------
- 脚本的标题，作为
- 

标准standard模板
------------------
::

                view.left = html.get('left', '')
                view.right = html.get('right', '')
                view.body = html.get('body', '')
                view.title = html.get('title', '')
                view.description = html.get('description', '')

Ajax/KSS请求
-----------------------

