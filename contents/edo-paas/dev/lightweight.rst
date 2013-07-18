==================================
致轻量级web框架爱好者
==================================

.. sectnum::

易度是一个开发平台，本文针对熟悉轻量级web开发框架(比如Django, ROR)的读者编写。

首先要说的是，易度并不是一个简单的轻量级web开发框架，而是支持类轻量级web开发模式的一个云办公平台: 

http://everydo.com

易度软件包结构
===================
code.google.com上的 `everydo-marketplace <http://code.google.com/p/everydo-marketplace/source/browse/trunk/>`__ 项目，
包含了一组扩展软件包的源代码，你可前往观摩下.

易度软件包符合django的MTV结构，必须采用这个结构：

- view.py

  这里是view，每个视图就是一个函数，可直接在浏览器上通过 ++script++packagename.scriptname来调用

- __init__.py

  可选，这个是对软件包的描述信息

- model/

  可选，这里存放模型数据，每个模型一个py文件

- templates/

  可选，这里是模板，模板采用page template语法，每个模板是一个pt文件。

- workflows/

  可选，这里存放流程的定义

如何编写
===============
最简单的一个hello world, 就是指需要一个view.py文件，内容如下::

    #-*-encoding=utf-8-*-
    @script_attr(permission='zope.View', use_template='basic')
    def index():
        """应用首页

        这是软件包的首页"""

        print 'hello, world'
        return printed

这里:

- script_attr是一个python的修饰器(decorator)，表明:

  - 这个页面的权限是查看权限zope.View，如果公开则是zope.Public
  - 输出使用模板，现在是basic标准模版，也可以为none或者blank

- 函数名为index，这个约定为应用首页
- 因为是网站应用，因此传统的print不会生效，会保存到一个printed的变量里面返回


运行软件包
==================
必须注册一个易度开发版账号
-------------------------------------
http://everydo.com/signup.rst

注意，应该选择开发版，而不是运营版

打包导入
----------------------
比如你要把zopen.demo打包::

   tar czf zopen.demo.tgz zopen.demo

然后登录到你的易度开发账号里面，点击右上方的软件包，进入软件包管理页面。

.. image:: img/import-pkg.png
   :width: 600

点击右侧的导入按钮，导入打包的软件包。

  这里稍停留下：未来，我们会提供命令行的导入工具，简化此步骤，如同google app engine一样。


部署运行
-----------------
导入后，系统会提示快速部署，可点击执行，便可看到最终的hello world效果。

浏览器GUI开发
=====================
实际上，也可在浏览器上开发，这样开发会更方便。

而且对于model和流程的定义，几乎必须通过浏览器上的相关工具定义才是最适合的。


