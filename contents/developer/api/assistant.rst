---
title: 桌面助手
description: 桌面助手的对外API
---

=================
桌面助手
=================

.. contents::
.. sectnum::

由于浏览器功能有限，一些特殊的功能，必须借助桌面助手来完成，比如：

- 外部编辑
- 文件下载同步
- 支持断点续传的同步

桌面助手就是一个安装在用户电脑上的一个服务程序，一旦安装，可以通过http协议通知桌面助手执行相关工作。
可以基于桌面助手开发很多复杂的应用。

桌面助手服务器在 ``http://127.0.0.1:5000`` 监听GET请求。

在所有发往这个地址的请求中，带上callback参数即可支持JSONP响应，解决Ajax跨域的问题。

在所有发往桌面助手服务器的请求中，带上 ``build_number`` 参数指定需要的桌面助手最低build版本号，可以让低版本桌面助手自动升级。

文件管理API
===============
文件相关操作的API

文件下载 ``/worker/new/download``
---------------------------------------

参数：

- uids: 要下载的文件/文件夹的uid，可以出现多个
- server: 指定服务器
- account: 指定账户
- instance: 指定实例
- path: 要下载到的本地文件夹路径
- build_number: 所需的桌面助手最低build版本号
- token: token

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "is_alive": true, 
        "worker_id": "id"
    }

文件上传 ``/worker/new/upload``
----------------------------------

参数：

- uid: 要上传到服务端文件夹的uid
- server: 指定服务器
- account: 指定账户
- instance: 指定实例
- paths: 要上传的本地文件/文件夹的路径，可以出现多个
- build_number: 所需的桌面助手最低build版本号 
- token: token

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "is_alive": true, 
        "worker_id": "id"
    }

文件同步 ``/worker/new/sync``
---------------------------------

参数：

- uid: 要同步的文件夹的uid
- type: 同步类型，共有三种类型pull、push与sync分别对应向下、向上和双向同步
- server: 指定服务器
- account: 指定账户
- instance: 指定实例
- path: 要同步的本地同步区路径
- build_number: 所需的桌面助手最低build版本号
- token: token

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "is_alive": true, 
        "worker_id": "id"
    }

获取冲突列表 ``/filestore/conflicts``
----------------------------------------

参数：

- server: 指定服务器
- instance: 指定实例
- account: 指定帐号
- root_uid: 项目所属的本地同步区的uid
- root_local_folder: 项目所属的本地同步区的路径
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "conflicts": [
            {
                'uid': "id", 
                'revision': "revision", 
                'local_path': "local_path", 
                'server_path': "server_path", 
                'modified': "time string", 
                'md5': "md5 string", 
                'root_uid': "id", 
                'conflict': true, 
                'last_sync': "time string"
            }
        ]
    }


显示本地同步区 ``/filestore/sync_paths``
----------------------------------------------------------
显示服务端文件夹对应的本地同步区

参数：

- server: 指定服务器
- instance: 指定实例
- account: 指定帐号
- uid: 文件夹的uid
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "paths": [
            "localpath_1", 
            "localpath_2_if_any"
        ]
    }

工作管理API
============
包括UI和任务管理方面的API。

任务列表 ``/worker/all``
----------------------------------

参数：

- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "workers": [
            {
                "worker_id": "id", 
                "worker_name": "name", 
                "state": "running", 
                "error": ""
            }
        ]
    }

任务查询 ``/worker/state``
---------------------------------

参数：

- worker_id: 任务的id
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "worker_id": "id", 
        "worker_name": "name", 
        "state": "running", 
        "error_msg": ""
    }

新建任务 ``/worker/new/<worker_name>``
-------------------------------------------------
新建的任务会自动开始

参数：

- build_number: 所需的桌面助手最低build版本号
- ...相应任务模块需要的参数

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "is_alive": true, 
        "worker_id": "id"
    }

暂停任务 ``/worker/pause``
--------------------------------

参数：

- worker_id: 任务的id
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "is_alive": true, 
        "worker_id": "id"
    }

开始任务 ``/worker/start``
--------------------------------

参数：

- worker_id: 任务的id
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "is_alive": true, 
        "worker_id": "id"
    }

取消任务 ``/worker/cancel``
--------------------------------

参数：

- worker_id: 任务的id
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "is_alive": true, 
        "worker_id": "id"
    }

用户界面API
===================

选择文件夹 ``/ui/select_folder``
----------------------------------

参数：

- server 指定服务器，必需
- account: 指定账户，必需
- instance: 指定实例，必需
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "selected": false, 
        "path": null
    }

  若用户选择了路径，则selected为true且path为选择的路径

选择文件 ``/ui/select_files``
---------------------------------
通过向桌面助手服务器/select_files路径发送GET请求，来选择若干个本地文件

参数：

- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "paths": [
            "path_to_file_1", 
            "path_to_file_2"
        ]
    }

冒泡提示 ``/ui/message``
---------------------------

参数：

- title: 提示信息的标题，通常是简短的描述
- body: 提示信息的正文
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容: 成功则返回 ``{"status": "done"}`` 

JS SDK
============
JavaScript SDK 是一个 JavaScript 脚本文件 ``assistent.js`` ，用于简化Web端的开发，其中集成了一些通用的方法。

依赖
------------------

- jQuery 库（1.4 以上版本）
- jQuery-JSONP 用于解决跨域问题，项目地址 https://github.com/jaubourg/jquery-jsonp 

初始化
-------------------

引入 SDK 脚本文件，初始化一个 ``Assistent`` 对象，使用这个对象完成页面上与桌面助手相关的大部分操作::

  var assistent = new Assistent({
    'server': '服务器', 
    'instance': '实例', 
    'account': '账户', 
    'token': 'token', 
    'min_build': '所需的桌面助手最低版本号'
  })

下载 ``download``
-----------------------------------------------------------
::

   download(uids, localpath, callback)

下载若干个文件到指定的本地路径下。其中 ``uids`` 是多个uid的数组

任务添加之后会调用 ``callback`` 函数处理任务信息::

        edo_assistent.download([123, 124], 'D:/', function(worker_info){
            if(worker_info.is_alive){
                console.log('下载任务正在运行');
                console.log('任务 ID 是：' + worker_info.worker_id);
            }
        });

上传 ``upload_files``
------------------------------------------------------------------------
::

  upload_files(folder_uid, local_files, callback)

上传若干个本地文件到指定文件夹中，其中 ``local_files`` 是多个本地文件路径的数组。

任务添加之后会调用 ``callback`` 函数处理任务信息::

        edo_assistent.upload_files(
            110, 
            ['D:/new.txt', 'E:/old.doc'], 
            function(worker_info){
                if(worker_info.is_alive){
                    console.log('上传任务正在运行');
                    console.log('任务 ID 是：' + worker_info.worker_id);
                }
        });

同步 ``sync``
----------------------
::

   sync(folder_uid, local_path, type, callback)

其中:
    
    - ``folder_uid`` 是同步区的uid；
    - ``local_path`` 是同步区的本地路径；
    - ``type`` 是同步类型，共有三种： ``pull`` 、 ``push`` 和 ``sync`` ；

任务添加之后会调用 ``callback`` 函数处理返回的任务信息::

        edo_assistent.sync(
            110, 
            'D:/sync_folder', 
            'push', 
            function(worker_info){
                if(worker_info.is_alive){
                    console.log('向上同步任务正在进行');
                    console.log('任务 ID 是：' + worker_info.worker_id);
                }
        });

选择文件夹 ``select_folder``
----------------------------------------
::

   select_folder(callback)

选择本地文件夹，选择之后将会调用传入的 ``callback`` 函数处理返回的JSON信息::

        edo_assistent.select_folder(function(local_path){
            console.log('选择的文件夹路径是：' + local_path);
        });
    
选择文件 ``select_files``
-----------------------------------------
::

   select_files(callback)

选择若干个本地文件，选择之后会调用传入的 ``callback`` 函数处理返回的JSON信息::

        edo_assistent.select_files(function(paths){
            for(var i = 0, l = paths.length; i < l; i ++){
                console.log('选择了文件：' + paths[i]);
            }
        });

选择同步文件夹 ``select_sync_folder``
----------------------------------------------------
::

  select_sync_folder(folder_uid, callback)

列出指定文件夹的本地同步区，获取数据之后会调用 ``callback`` 函数处理返回的路径::

        edo_assistent.select_sync_folder(110, function(paths){
            for(var path in paths){
                console.log('发现一个同步区：' + path);
            }
        });

