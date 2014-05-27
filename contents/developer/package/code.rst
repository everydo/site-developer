---
title: 代码
description: 编写脚本代码
---

=================
代码
=================

目前支持python，之后会支持javascript

python脚本
======================
python脚本可以直接通过浏览器调用

脚本采用python语言书写，存放在scripts中. 其中:

- setup: 用于部署
- upgrade(last_version='') : 用于升级

在软件包里面注册一个代码::

  root.packages.register_code('zopen.sales', 
        {'name':'setup',
         'permission':'Access',
         'template':'standard',
         'args':'',
         'code':'print "hello, world"',
        })

其中template是输出套用的模板：

- standard: 系统标准模版，支持应用权限、编辑和设置
- blank: 不使用现有模版页首和页脚，但保持现有界面风格
- kss: KSS服务脚本
- json: json api
- none: 不使用任何模版

也可以得到一个代码::

  root.packages.get_code(name)

查看软件包的所有代码::

  root.packages.list_codes(package_name)

调用一个脚本
====================
可以这样调用脚本::

   root.call_code('zopen.api:calc', ztq_queue='', **kw)

导入导出
===============
为了方便书写，可导出一个标准的python函数::

  root.packages.export_code('zopen.sales:setup')

导出结果如下::

    @view_config(permission='zopen.Access', use_template='standard', icon=u'')
    def setup(redirect = True):
        """安装脚本

        初始化规则"""

        app = deployApplet('zopen.remind.workflows.remind', context, 'remind', '提醒')
        IObjectIndexer(app).index()
        #创建规则

