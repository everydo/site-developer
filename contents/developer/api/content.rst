---
title: 内容管理
description: 文件属性，表单数据增删改
---

==========================
内容管理
==========================


.. contents::
.. sectnum::

内容管理content：负责内容(包括文件夹、文件、快捷方式、表单)的元数据、授权等

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
- object_types
- content_type
- revision
- created
- modified
- title
- description

/api/v1/content/items
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

api/v1/content/create_folder
----------------------------------
创建文件夹

url参数：

- account
- instance
- uid : 所在父文件夹的id
- path : 所在父文件夹的路径 ，和uid二选一
- name : 文件夹名

返回：创建的文件夹的元数据

api/v1/content/upload
----------------------------------
POST方法上传文件一个文件到指定位置

form参数：

- account
- instance
- uid: 所在文件夹
- path: 文件夹位置，和uid二选一
- filename: 文件名
- base_revision: （可选）如果服务器最新版本不是这个版本，就报告冲突

返回：上传文件的metadata信息

api/v1/content/delete
----------------------------------
删除对象

url参数：

- account
- instance
- uid
- path

返回：被删除对象的元数据

api/v1/content/move
----------------------------------
将对象从一个地方，转移到另外的地方

url参数：

- account
- instance
- uid
- path
- to_uid: 目标文件夹的uid
- to_path: 目标文件夹的path，和上面二选一
- name: 新的文件名(可选）

返回：对象的元数据

api/v1/content/download
----------------------------------
获取带签名信息的下载的临时url

参数：

- account
- instance
- uid : 123123,所在文件夹，和path二选一
- path: /files/folder_a/ 文件夹路径， 和uid二选一
- mime：下载的mime类型，如果下载原始文件，不传递此参数

返回：

- 302直接跳转到具体的文件服务地址

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

api/v1/content/copy
----------------------------------
复制对象

url参数：

- account
- instance
- uid
- path
- to_uid: 目标文件夹的uid
- to_path: 目标文件夹的path，和上面二选一
- name: 新的文件名(可选）

返回：新对象的元数据

api/v1/content/delta
----------------------------------
查找更新日志，用于文件同步

参数：

- account
- instance
- uid : 123123,所在文件夹，和path二选一
- path: /files/folder_a/ 文件夹路径， 和uid二选一
- actions: 日志操作内容，默认是[movein, moveout, rename, remove, new, update]
- modified: 从什么时候开始

返回：

- has_more: 是否还有？
- entries: 可能发生增删改移动

  - uid: 发生变化的文件id
  - path: 所在路径
  - revision: 当时的版本号
  - timestamp: 发生时间
  - action: movein/moveout/rename/remove/new/update

https://www.dropbox.com/developers/core/docs#delta
