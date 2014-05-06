---
title: 内容管理
description: 文件上传、下载、属性信息，表单数据增删改
---

==========================
内容管理
==========================

.. contents::
.. sectnum::

扩展属性
================

/api/v1/metadata/get
-------------------------
得到扩展属性

文件
============

/api/v1/file/list
-------------------------
文件夹内容, 也可以是查找一个文件的信息

url参数：

- account: zopen, 账户名
- instance: default, 站点名
- uid: 12312312, 文件唯一ID，和path任选一个
- path: /files/abc.doc, 文件路径，和uid任选一个

- file_limit: 10000(最大25000)，返回内容的限额

返回：

文件夹自身信息，以及内容的清单:

- uid
- path
- revision: 12121, 具体的版本号(如果是文件)
- bytes: 230783, 文件的大小 (如果是文件)
- content_type": "application/pdf", (如果是文件)
- contents

  - uid: 121212 , 文件的唯一ID
  - path: "/Getting_Started.pdf", 所在路径
  - modified: 121231231.12, 修改时间戳
  - revision: 12121, 具体的版本号(如果是文件)
  - bytes: 230783, 文件的大小 (如果是文件)
  - content_type": "application/pdf", (如果是文件)

/api/v1/file/get 
----------------------
下载文件, 支持 http_range 进行断点续传

url参数：

- account: zopen, 账户名
- instance: default, 站点名
- uid: 12312312, 文件唯一ID，和path任选一个
- path: /files/abc.doc, 文件路径，和uid任选一个

- rev: 1212, 版本ID，可以查找历史版本

返回：

- 返回文件内容
- http消息头包含文件的元数据，位于 ``x-edo-metadata`` 中，包括基础的元数据：

  - uid: 121212 , 文件的唯一ID
  - path: "/Getting_Started.pdf", 所在路径
  - revision: 12121, 具体的版本号
  - bytes: 230783, 文件的大小
  - modified: 121231231.12, 修改时间戳
  - content_type": "application/pdf",

参照：

https://www.dropbox.com/developers/core/docs#files-GET

api/v1/file/put
---------------------------------
使用PUT方法，上传一个文件，消息头必须包括 Content-Length 以便检查完整性

url参数：

- account: zopen, 账户名
- instance: default, 站点名
- uid: 12312312, 文件唯一ID，和path任选一个
- path: /files/abc.doc, 文件路径，和uid任选一个
- overwrite: true/false, 如果文件存在，是否保存为新版本，或者自动改名
- parent_revision: 12, 上一版本的版本号，用于检查冲突, 如果冲突，则拷贝一个分支版本

请求正文：文件内容

返回: 文件元数据, 同上

参照：

https://www.dropbox.com/developers/core/docs#files_put

api/v1/file/chunked_upload 
------------------------------------------
断点续传

参照：

https://www.dropbox.com/developers/core/docs#chunked-upload

api/v1/file/commit_chunked_upload
--------------------------------------------------
提交断点续传

参照：

https://www.dropbox.com/developers/core/docs#commit-chunked-upload

api/v1/file/delta 
----------------------------------
查找文件更新

参数：

- account
- instance
- uid
- path
- cursor: modified: 从什么时候开始

返回：

- cursor: 其实也是一个时间戳，用于下次继续请求
- has_more: 是否还有？
- entries: 可能发生增删改移动

  - path
  - uid
  - action: movein/moveout/rename/remove/new/update

https://www.dropbox.com/developers/core/docs#delta

