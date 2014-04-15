---
title: 易度python SDK
description: 如何使用Python语言访问易度服务
---

::

    from edo_api import EDOClient

    client = EDOClient(....., 'http://zopen.oc.easyd.cn' )

    # 公司的基本信息
    client.account.get_attribte('') # 得到公司名等各种信息

    # 得到人员信息
    client.org_info.list_users()
    client.org_info.new_user()

    # 站点
    client.sites.list_sites()
    site = client.get_site('default')

    # 文档管理
    site.files.upload(folder_id, data)

    # 工作流管理
    site.workflows.list_dataitems(workflow_container_id)

    # 通知
    site.notify(obj_id, msg)

    # 权限

    # 评论
