---
title: 测试框架
description: 如何对后端开发进行测试
---

==============
测试框架
==============

.. contents::

测试框架用于后端开发的自动化测试。

基本的测试流程
======================
测试脚本使用标准的扩展应用脚本编写，测试脚本必须以 ``test_`` 开头来命名。使用 ``root.tests`` 辅助完成::

  testcase = root.tests

测试环境准备
--------------------
主要是初始化站点内容、授权等操作::

  testcase.setup(context, objects)

objects是一个yaml格式的站点结构描述文件::

  [ {object_type:
     title:
     description:
     schema:(,),
     mdset:(,),
     fields: {''},
     acl_allow:
     acl_deny:
     properties: {'zopen.archive:archev':{'number':}, },
     children: [ {}, {} ] }
  ]

模拟登陆
--------------
不需要用户输入用户名密码信息，可以直接用下面的脚本登陆::

  testcase.login(username, groups)

运行测试脚本
-------------------
主要是需要验证结果::

  testcase.test(expression, title='说明：测试某某') 

其中expression就是一个测试表达式，如果成功返回 ``True`` ，否则 ``False`` ::

  'modify.archived' in obj.stati

销毁测试环境
------------------
销毁用于销毁setup里面创建的对象, 销毁整个setup建立起来的对象::

  testcase.tear_down()

返回测试报告
----------------------
返回测试结果::

  return testcase.get_result()

返回测试结果的列表::

 [ {title:"某某测试", result:true/false} ] 

