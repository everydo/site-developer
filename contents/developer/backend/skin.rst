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
        <title>%%title</title>
        <link rel="cache-url" href="%%cache_url" />
        <link type="text/css" rel="stylesheet" href="%%cache_url/zopen.css" media="all" />
        <script charset="utf-8" src="%%cache_url/zopen.core.js"></script>
      </head>
      <body>
          <div>%%nav</div>
          <div>%%top</div>
          <div>%%left</div>
          <div>%%above</div>
          <div>%%content</div>
          <div>%%right</div>
      </body>
    </html>

皮肤模板是一个标准的html，里面可变的部分使用 ``%%`` 开头的变量来代替，完整的默认皮肤可以在站点皮肤管理设置中查看。

注册皮肤
=============
把这个皮肤注册到站点的皮肤注册表::

   ISkins(root).register('bootstrap', 'Twitter的Bootstrap风格皮肤', template, description='')

更换皮肤
==================
设置某个容器，采用新的皮肤::

   ISkinManager(container).set_skin('bootstrap')

