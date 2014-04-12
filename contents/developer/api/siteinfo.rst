---
title: 系统设置
description: 读取和设置系统的各种信息
---

系统设置
=============

.. Contents::
.. sectnum::

使用 `ISiteInfo` 来读取和设置

文档设置
==============
``get_operation(option_name=None, default=None)``
  得到某个运营选项参数, 比如::

    ('sms', '短信数量', '条','item-count','buyamount'),
    ('apps_packages', '软件包数量', '','int','resource'),
    ('flow_records', '数据库记录', '条','int','resource'),
    ('docsdue', '文档使用期限', '月','permonth-time','buyamount'),
    ('docs_quota', '文件存储限额(M)', '','str','resource'),
    ('docs_users', '文档许可用户数', '','int','resource'),

    # 是否企业版判断，必须为True
    ('docs_publish', "文档发布", '', 'bool', 'function'),
    # 是否高级企业版判断
    ('flow_customize', '流程定制', '','bool','function'),
    # 是否开发版
    ('apps_scripting', '允许开发软件包', '','bool','function'),

