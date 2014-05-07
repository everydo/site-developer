---
title: 文件服务
description: 文件上传、下载、转换
---

==========================
文件服务
==========================


.. contents::
.. sectnum::

文件服务file: 负责文件的上传下载和文件存储相关的内容

/api/v1/file/get 
----------------------
下载文件, 支持 http_range 进行断点续传

url参数：

- account: zopen, 账户名
- instance: default, 站点名
- uid: 12312312, 文件唯一ID
- revision: 12, 版本ID，可以查找历史版本

返回：

- 返回文件内容
- http消息头包含文件的元数据，位于 ``x-edo-metadata`` 中，包括基础的元数据：

  - uid: 121212 , 文件的唯一ID
  - revision: 12121, 具体的版本号
  - filename: "Getting_Started.pdf"
  - bytes: 230783, 文件的大小
  - modified: 121231231.12, 修改时间戳
  - content_type": "application/pdf",

参照：

https://www.dropbox.com/developers/core/docs#files-GET

api/v1/file/put
---------------------------------
使用PUT方法，上传一个文件，消息头必须包括 Content-Length 以便检查完整性, 最多支持150M文件

url参数：

- account: zopen, 账户名
- instance: default, 站点名
- uid: 12312312, 文件所在文件夹的uid，和path任选一个
- path: /files/abc.doc, 文件路径，和uid任选一个
- overwrite: true/false, 如果文件存在，是否保存为新版本，或者自动改名
- parent_revision: 12, 上一版本的版本号，用于检查冲突, 如果冲突，则合并失败，必须在下载最新版本解决冲突之后上传。

请求正文：文件内容

返回: 文件元数据, 同上

参照：

https://www.dropbox.com/developers/core/docs#files_put

api/v1/file/chunked_upload 
------------------------------------------
使用PUT方法，超过150M的大文件分片逐个上传，支持断点续传，每个分片不超过150M，典型是4M. 每个分片临时保留24小时，/commit_chunked_upload后提交完成。

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

api/v1/file/commit_chunked_upload
--------------------------------------------------
提交断点续传，类似/put, 但是是POST方式提交，无内容。

url参数：

- account: zopen, 账户名
- instance: default, 站点名
- uid: 12312312, 文件所在文件夹的uid，和path任选一个
- path: /files/abc.doc, 文件路径，和uid任选一个
- overwrite: true/false, 如果文件存在，是否保存为新版本，或者自动改名
- parent_revision: 12, 上一版本的版本号，用于检查冲突, 如果冲突，则合并失败，必须在下载最新版本解决冲突之后上传。
- upload_id: 上传会话的id

返回: 文件元数据, 同上

参照：

https://www.dropbox.com/developers/core/docs#commit-chunked-upload

/api/v1/file/transform
---------------------------------------
转换和回调接口. 可主动发起转换，转换完成，进行回调。

传入参数：

- account: 需要转换的账号
- instance: 需要转换的站点
- location: 需要转换的文件相对于站点的路径
- targets: 需要专门的目标Mime类型
- callback： 转换完成的回调url, 如果转换已经完成，则立刻回调

/api/v1/file/get_viewer_secret
-------------------------------
获取查看密匙, 得到一个转换密匙

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

