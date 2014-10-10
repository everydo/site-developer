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
- oc_server: 指定oc服务器
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
- oc_server: 指定oc服务器
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
- mode: 同步类型，共有三种类型pull、push与sync分别对应向下、向上和双向同步
- server: 指定服务器
- oc_server: 指定oc服务器
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


显示本地同步区 ``/filestore/sync_paths``
----------------------------------------------------------
显示服务端文件夹对应的本地同步区

参数：

- server: 指定服务器
- oc_server: 指定oc服务器
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

列出本地所有同步区 ``/filestore/list_sync_folders``
--------------------------------------------------------------------
列出所有同步区信息。（如果提供可选参数，必须提供3个完整的参数）

参数：

- build_number: 所需的桌面助手最低build版本号
- oc_server: 指定oc服务器
- instance: 指定实例（可选）
- account: 指定帐号（可选）

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        'sync_folders': [
            {
                'oc_server': 'api_server_address', 
                'instance': 'instance', 
                'account': 'account', 
                'local_path': 'local_folder_path', 
                'server_path': 'server_folder_path', 
                'uid': 'unique_id_on_server', 
                'modified': 'last_modified_time_in_UTC'
            }
        ]
    }

列出下载的零散文件 ``/filestore/list_files``
------------------------------------------------------
零散文件是不在同步区中的文件，也就是用户单独下载的文件。（如果提供可选参数，必须提供3个完整的参数）

参数：

- build_number: 所需的桌面助手最低build版本号
- oc_server: 指定oc服务器（可选），指定此参数则只返回此站点同步区
- instance: 指定实例（可选）
- account: 指定帐号（可选）

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        'files': [
            {
                'oc_server': 'api_server_address', 
                'instance': 'instance', 
                'account': 'account', 
                'uid': 'unique_id_on_server', 
                'revision': 'revision_on_server', 
                'local_path': 'local_path_to_file', 
                'server_path': 'server_path_to_file', 
                'modified': 'last_modified_time_in_UTC', 
                'md5': 'MD5_hash_value', 
                'conflict': false, 
                'usage': 'sync/download/view/edit'
            }
        ]
    }

删除下载的文件 ``/filestore/delete_file``
---------------------------------------------------------
删除下载的文件，如果文件下载之后被修改过，会删除指定文件记录而保留磁盘上的文件。

参数：

- build_number: 所需的桌面助手最低build版本号
- oc_server: 指定oc服务器
- instance: 指定实例
- account: 指定帐号
- local_path: 文件的本地路径

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "success": true, 
        "msg": "Some message"
    }

解除同步关联 ``/filestore/remove_sync_folder``
---------------------------------------------------------
解除同步关联，只删除指定同步区，不会改动磁盘上的内容。

参数：

- build_number: 所需的桌面助手最低build版本号
- oc_server: 指定oc服务器
- instance: 指定实例
- account: 指定帐号
- local_path: 同步区的本地路径

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "success": true, 
        "msg": "Some message"
    }

建立同步关联 ``/filestore/setup_sync_folder``
---------------------------------------------------------
建立指定服务端文件夹和指定本地文件夹的同步关联

参数：

- build_number: 所需的桌面助手最低build版本号
- oc_server: 指定oc服务器
- server: 指定wo服务器
- instance: 指定实例
- account: 指定帐号
- uid: 服务端文件夹的uid
- local_path: 指定的本地文件夹路径
- token: token

响应：

- 格式: JSON/JSONP
- JSON内容::

    {
        "success": true, 
        "msg": "Some messge"
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
                "title": "human_readable_descriptions", 
                "detail": {
                    "account": "account", 
                    "build_number": "1", 
                    "instance": "default", 
                    "name": "download", 
                    "path": "D:\\local_path", 
                    "server": "http://your_server:your_port", 
                    "oc_server": "http://your_server:your_port", 
                    "state": "running/finished/error", 
                    "token": "token_string", 
                    "uids": "uid_1,uid_2,uid_3,uid_4_if_any"
                }
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
        "detail": {
            "account": "account", 
            "build_number": "1", 
            "instance": "default", 
            "name": "download", 
            "path": "D:\\local_path", 
            "server": "http://your_server:your_port", 
            "oc_server": "http://your_server:your_port", 
            "state": "running/finished/error", 
            "token": "token_string", 
            "uids": "uid_1,uid_2,uid_3,uid_4_if_any"
        }
    }

新建任务 ``/worker/new/<worker_name>``
-------------------------------------------------
新建的任务会自动开始

参数：

- build_number: 所需的桌面助手最低build版本号
- pid: 由谁发起的任务（归属于谁的任务），是一个以 ``users.`` 开头的字符串
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

选择本地路径 ``/ui/select_paths``
----------------------------------------------------

参数：

- oc_server 指定oc服务器，必需
- account: 指定账户，必需
- instance: 指定实例，必需
- build_number: 所需的桌面助手最低build版本号
- mode: file/files/folder

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
- type: 消息类型，可能的值为：none, info, warn, error 。将会在消息上显示对应的图标。
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容: 成功则返回 ``{"success": true}`` 

打开文件或文件夹 ``/ui/open``
--------------------------------------
传递的参数是文件夹则调用默认文件管理器打开文件夹，是文件则调用默认的处理程序打开文件。

参数：

- local_path: 文件或文件夹的本地路径

响应：

- 格式: JSON/JSONP
- JSON内容: ``{"success": true}``

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

  var edo_assistent = new Assistent({
    'server': '服务器', 
    'oc_server': 'oc服务器', 
    'instance': '实例', 
    'account': '账户', 
    'token': 'token', 
    'pid': '任务发起的账户，例如 users.test ', 
    'min_build': '所需的桌面助手最低版本号',
    'download': {'mac':url, 'linux':url, 'windows':}
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

   sync(folder_uid, local_path, mode, callback)

其中:
    
    - ``folder_uid`` 是同步区的uid；
    - ``local_path`` 是同步区的本地路径；
    - ``mode`` 是同步类型，共有三种： ``pull`` 、 ``push`` 和 ``sync`` ；

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

选择文件夹 ``select_paths``
----------------------------------------
::

   select_paths(mode, callback)

其中，multiple表示是否支持多选，mode指示可以选择什么：

- file: 选择单个文件
- files: 选择多个文件
- folder: 选择单个文件夹

选择之后将会调用传入的 ``callback`` 函数处理返回的JSON信息::

        edo_assistent.select_paths('files', function(paths){
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

