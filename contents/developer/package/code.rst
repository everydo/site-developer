---
title: 代码
description: 编写脚本代码
---

=================
代码
=================

目前支持python，之后会支持javascript

::

  IPackages(root).register_code(name, code)
  IPackages(root).get_code(name, code)
  IPackages(root).list_codes(package_name)

python脚本
======================
python脚本可以直接通过浏览器调用

脚本采用python语言书写，存放在scripts中. 其中:

- setup: 用于部署
- upgrade(last_version='') : 用于升级

对于需要通过浏览器发起的请求，如下书写::

    @view_config(permission='zopen.Access', use_template='standard', icon=u'')
    def setup(redirect = True):
        """安装脚本

        初始化规则"""

        app = deployApplet('zopen.remind.workflows.remind', context, 'remind', '提醒')
        IObjectIndexer(app).index()
        #创建规则

如果仅仅是内部调用，则如下处理::

    def list_users():
        pass

