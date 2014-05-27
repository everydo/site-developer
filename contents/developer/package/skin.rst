---
title: 站点皮肤
description: 自定义皮肤，更换皮肤
---

===============
站点皮肤
===============

制作皮肤模板
=====================
首先需要制作一个皮肤::

  template = ""
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="%%i18n" class="html">
      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>{{title}}</title>
        <link rel="cache-url" href="{{cache_url}}" />
        <link type="text/css" rel="stylesheet" href="{{cache_url}}/zopen.css" media="all" />
        <script charset="utf-8" src="{{cache_url}}/zopen.core.js"></script>
      </head>
      <body>
          <div>{{nav}}</div>
          <div>{{top}}</div>
          <div>{{left}}</div>
          <div>{{above}}</div>
          <div>{{content}}</div>
          <div>{{right}}</div>
      </body>
    </html>

皮肤模板是一个标准的html，里面可变的部分使用 ``{{ }}`` 开头的变量来代替，完整的默认皮肤可以在站点皮肤管理设置中查看。

注册皮肤
=============
把这个皮肤注册到站点的皮肤注册表::

   root.packages.register_skin('',
              {'name':'bootstrap', 
                'title':'Twitter的Bootstrap风格皮肤', 
                'template':template, 
                'description':''})

也可以注册到某个软件包中::

   root.packages.register_skin('zopen.cool_skin',
        {'name':'bootstrap', 
                'title':'Twitter的Bootstrap风格皮肤', 
                'template':template, 
                'description':''})

更换皮肤
==================
设置某个容器，采用新的皮肤::

    container.set_setting('skin', 'bootstrap')
    container.set_setting('skin', 'zopen.cool_skin:bootstrap')

实际查找皮肤可以::

    container.get_setting('skin', default='bootstrap', inherit=True)

资源文件
==============
皮肤会使用各种资源文件，包括 css/js/图片 等。

所有资源文件，会在浏览器缓存，一旦变化，会自动更新。

因此在浏览器上引用资源文件，应该使用版本号::

   root.packages.resource_url('zopen.sales:abc.css')

这个返回最终cdn中的地址::

   http://easydo-cdn.server.com/zopen.sales/abc-1das1121.css

在皮肤中可以这样引用::

    <script charset="utf-8" src="{{reource_url/zopen.sales:abc.js}}"></script>

