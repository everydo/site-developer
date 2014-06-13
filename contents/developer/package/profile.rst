---
title: 工作台
description: 工作台是个人的工作区域
---

=========
工作台
=========

系统每个用户都有一个工作台。可以安装部署各种应用

工作台栏目安装脚本
=====================
工作台初始化脚本, 会在个人工作台安装栏目。

用户第一次访问，会执行这些安装脚本。

::

   root.profiles.register_setup('zopen.disk:setup', enabled=True)


