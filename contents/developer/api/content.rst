---
title: 内容管理
description: 文件上传、下载、属性信息，表单数据增删改
---

==========================
内容管理
==========================


.. contents::
.. sectnum::

内容管理分为 ``content`` 接口和 ``file`` 2种。

- content：负责内容(包括文件夹、文件、快捷方式、表单)的元数据、授权等
- file: 负责文件的上传下载和文件存储相关的内容

注意content和file接口分别位于不同的服务器上，需要请求请求不同的服务器。

内容content
====================

/api/v1/content/metadata
-------------------------
得到对象(文件夹、文件、快捷方式、容器、表单)的元数据

url参数：

- account: zopen, 账户名
- instance: default, 站点名
- uid: 12312312, 文件唯一ID，和path任选一个
- path: /files/abc.doc, 文件路径，和uid任选一个

返回：

会返回基础的对象信息以及按需返回的mdset. 其中基础信息包括：

- uid
- path
- bytes
- content_type
- revision
- created
- modified
- title
- description
- subjects
- schemas: 对象schema定义的字段

  - ...

- mdset:

  - zopen.archive:archive

    - number
    - dept

/api/v1/content/list
-------------------------
文件夹内容, 也可以是查找一个文件的信息

url参数：

- account: zopen, 账户名
- instance: default, 站点名
- uid: 12312312, 文件唯一ID，和path任选一个
- path: /files/abc.doc, 文件路径，和uid任选一个

- limit: 10000(最大25000)，返回内容的限额

返回：

文件夹自身信息，以及内容的清单, 参照 metadata的返回

/api/v1/content/search
-------------------------
搜索.  只能搜索到有权限查看的内容，在body中填写查询条件, 具体参照软件包中搜索一节::

  'query':[ # 类似ES
               ],
  'sort':{},
  'aggs':{},
      'limit':1
  'size':20
  'from':1

搜索结果::

  {count:10,
   results: [ { ''  },
            ]
  }

api/v1/content/delta 
----------------------------------
查找文件更新

参数：

- account
- instance
- uid : 123123,所在文件夹，和path二选一
- path: /files/folder_a/ 文件夹路径， 和uid二选一
- modified: 从什么时候开始

返回：

- has_more: 是否还有？
- entries: 可能发生增删改移动

  - uid: 发生变化的文件id
  - path: 所在路径
  - action: movein/moveout/rename/remove/new/update

https://www.dropbox.com/developers/core/docs#delta

文件file
============

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

