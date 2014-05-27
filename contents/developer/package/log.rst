---
title: 日志
description: 系统操作日志
---

=============================
操作日志
=============================

添加日志
=============
任何对象都可以记录日志。

添加日志::

  obj.logs.add(operation, text)

其中：

- operation为操作类型
- text为操作描述

搜索日志
=============

列出日志::

  obj.logs.query(action=None, start=None, end=None, principal_id=None,
            person=None, text=None, batch_start=0, size=20)

返回为一个List,每个元素为一个dict，dict里面包含日志的所有信息。文件和文件夹通用。在文件夹下会返回该文件夹下的所有日志。
日志按时间排序。

操作日志的搜索接口与前面的对象搜索基本一致。

看个例子，搜索24小时内，admin用户下载操作记录, 按时间递减排序:::

 site_name = get_root().name
 now = time.time()
 before_one_days = now - 24 * 3600
 results = LogQuery(site_name=site_name).\
     # 限制用户是admin
     filter(displayname__allof=[u'admin']).\
     # 限制是下载操作
     filter(operation__allof=[u'download']).\
     # 限制是24小时内的日志
     filter(timestamp__range=(float(before_one_days), float(now))).
     sort('-timestamp')
 
如果没有parse操作，日志默认就是按照事件倒序排列的，所以.sort('-timestamp')可以省略。


目前可以搜索的操作分别是:::

 download downloadPDF : 下载 和 下载PDF 操作
 upload created newFlow   : 5种创建操作
 save editoutside newrevision : 6种编辑操作
 rename: 2种重命名操作
 copy move : 拷贝 和 移动 操作
 removed delete : 2种删除操作
 subscription : 订阅操作
 comment : 评论操作
 print ： 打印操作
 sendSMS sendSmsReport ： 发送短信 和 发送短信报告 操作
 sendout : 外发操作
 assign : 分配权限操作
 pending published return private free ：改变状态操作
 login logout : 登录 和 登出 操作
 
