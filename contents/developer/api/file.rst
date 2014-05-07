---
title: 文件服务
description: 文件上传、下载、转换
---

==========================
文件服务
==========================


.. contents::
.. sectnum::

文件服务file: 负责文件的上传、下载和转换相关的内容.

/api/v1/file/get_viewer_secret
-------------------------------
获取查看密匙, 得到一个转换密匙。注意：这个密匙普通用户无法得到，只有账户管理员才能得到。

传入参数：

- account
- instance

输出密匙::

   {'secret': ''}

/api/v1/file/refresh_viewer_secret
----------------------------------------
更新查看密匙, 得到一个新密匙

传入参数：

- account
- instance

输出新的密匙::

   {'secret': ''}

/api/v1/file/clear_viewer_secret
-----------------------------------
清空转换密匙，这样无需签名，就可以进行文档转换了

传入参数：

- account
- instance

输出::

   {'secret': ''}

/api/v1/file/get
---------------------------------
下载文件, 支持 http_range 进行断点续传

url参数：

- account: zopen, 账户名
- instance: default, 站点名
- uid: 12312312, 文件唯一ID
- revision: 12, 版本ID，可以查找历史版本
- path: 在文件仓库中的相对地址
- mime(可选): 转换的mime类型
- subfile(可选): 主要压缩包中包含的的文件路径
- app_id: 第三方应用的ID，默认为空即可
- username: 访问用户的名字，仅作记录用
- timestamp: 截止时间的时间戳，如果不填写，则永久可查看
- permission: 签名授予的查看权限 download/preview/print
- signcode: 签名信息. 具体算法见后(如果密匙为空，可省略签名)

签名(signcode)算法:

使用查看器的程序，如果需要对查看的url进行权限保护，需要传入签名字段。签名的生成算法如下::

  MD5(account:instance:path:timestamp:app_id:username:permission:secret)

如果无法得到scret，可以通过内容content接口直接获取签名。

HTTP错误返回值:

如果HTTP返回码是200，表示正确，正文是具体的转换内容

否则，含义如下：

- 400: 签名不正确
- 401: 超时
- 403: 路径无权限
- 404: 无此文件
- 405: 正在转换
- 406: 转换失败
- 407: 正在下载
- 408: ip不匹配
- 409: 账户不存在

此时范围内容是详细错误原因::

   {"msg": "文件加密" }

正常返回文件内容

- http消息头包含文件的元数据，位于 ``x-edo-metadata`` 中，包括基础的元数据：

  - uid: 121212 , 文件的唯一ID
  - revision: 12121, 具体的版本号
  - filename: "Getting_Started.pdf"
  - bytes: 230783, 文件的大小
  - modified: 121231231.12, 修改时间戳
  - content_type": "application/pdf",


api/v1/file/chunked_upload 
------------------------------------------
使用PUT方法，超过150M的大文件分片逐个上传，支持断点续传，每个分片不超过150M，典型是4M. 每个分片临时保留24小时，/content/commit_chunked_upload后提交完成。

参数：

- account: zopen, 账户名
- instance: default, 站点名
- upload_id: 上传的session id, 如果为空，表示新建一个上传
- offset: 0 上传数据的起始偏移

请求正文：文件内容

返回：

- upload_id: "23234we"
- offset: 3337
- expires: session失效时间

参照：

https://www.dropbox.com/developers/core/docs#chunked-upload

/api/v1/file/transform
---------------------------------------
转换和回调接口. 可主动发起转换，转换完成，进行回调。

传入参数：

- account: 需要转换的账号
- instance: 需要转换的站点
- location: 需要转换的文件相对于站点的路径
- targets: 需要专门的目标Mime类型
- callback： 转换完成的回调url, 如果转换已经完成，则立刻回调

