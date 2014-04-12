---
title: 关注、收藏、赞
description: 关注、收藏属于知识管理的范畴，将内容和人建立联系
---

======================
关注、收藏
======================

.. Contents::
.. sectnum::

关注、收藏属于知识管理的范畴，将内容和人建立联系.

关注ISubscriptionManager
============================
关注也就是订阅，会收到一些通知

可以用如下方法得到对象的订阅人:

  ``ISubscriptionManager(context).getSubscribedMembers()``

可以看到使用了ISubscriptionManager接口，这个接口的详细说明如下：

- setSubscribeMethods(methods): 设置订阅方式
- getSubscribeMethods(): 得到订阅方式
- getSubscribedMembers(): 得到一组已订阅的人员(tuple.)
- setSubscribedMembers(members): 将对象的订阅列表设置成members 
- subscribeMember(member): 新增一个订阅人员(人员不允许是 ‘Anonymous’)
- subscribeMembers(members): 新增一组订阅人员
- unsubscribeMember(member): 删除一个订阅人员
- subscribeAuthenticatedMember(): 在当前的订阅列表里面新增当前的登录人员
- unsubscribeAuthenticatedMember(): 在订阅列表里面删除当前的登录人员
- hasSubscriptionFor(member=None): 如果member 在订阅列表返回‘True’，否则返回‘False’
- hasSubscriptionForAuthenticatedMember(): 当前登录人员在订阅列表返回‘True’，否则返回‘False’

收藏
===============
收藏是系统的基础服务，任何对象都可以被收藏

已经有的功能：

- 收藏一个对象
- 设置收藏的标签
- 删除一个收藏

之后会补充的功能包括：

- 查看文档的收藏数量
- 查看哪些人收藏
- 统计最热收藏

赞
===========
TODO


