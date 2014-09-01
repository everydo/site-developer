---
title: 桌面助手
description: 桌面助手的对外API
---

=================
桌面助手
=================

由于浏览器功能有限，一些特殊的功能，必须借助桌面助手来完成，比如：

- 外部编辑
- 文件下载同步
- 支持断点续传的同步

桌面助手就是一个安装在用户电脑上的一个服务程序，一旦安装，可以通过http协议通知桌面助手执行相关工作。
可以基于桌面助手开发很多复杂的应用。

桌面助手服务器在 ``http://127.0.0.1:5000`` 监听GET请求。

在所有发往这个地址的请求中，带上callback参数即可支持JSONP响应，解决Ajax跨域的问题。

在所有发往桌面助手服务器的请求中，带上``build_number``参数指定需要的桌面助手最低build版本号，可以让低版本桌面助手自动升级。

文件管理API
===============
文件相关操作的API

文件下载 ``/new_worker/download``
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
- JSON内容: ``{"is_alive": true, "worker_id": "id"}``

文件上传 ``/new_worker/upload``
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
- JSON内容: ``{"is_alive": true, "worker_id": "id"}``

文件同步 ``/new_worker/sync``
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
- JSON内容: ``{"is_alive": true, "worker_id": "id"}``

检查向上同步冲突 ``check_push_conflict``
----------------------------------------

参数：

- local_path: 要检查的项目的本地路径
- root_uid: 项目所属的本地同步区的uid
- parent_uid: 项目所在父文件夹的uid
- root_local_folder: 项目所属的本地同步区的路径
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容: ``{"conflicted": true}``

通用API
============
包括UI和任务管理方面的API。

任务列表 ``/all_workers``
----------------------------------

参数：

- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容: ``{"workers": [{"worker_id": "id", "worker_name": "name", "status": "running", "error_msg": ""}]}``

任务查询 ``/worker_status``
---------------------------------

参数：

- worker_id: 任务的id
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {"worker_id": "id", 
     "worker_name": "name", 
     "status": "running", 
     "error_msg": ""}

选择文件夹 ``/select_folder``
----------------------------------

参数：

- server 指定服务器，必需
- account: 指定账户，必需
- instance: 指定实例，必需
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {"selected": false, "path": null}

  若用户选择了路径，则selected为true且path为选择的路径

选择文件 ``/select_files``
-------------------------------
通过向桌面助手服务器/select_files路径发送GET请求，来选择若干个本地文件

参数：

- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容::

    {"paths": ["path_to_file_1", "path_to_file_2"]}


显示服务端文件夹对应的本地同步区 ``/sync_paths``
--------------------------------------------------

参数：

- server: 指定服务器
- instance: 指定实例
- account: 指定帐号
- uid: 文件夹的uid
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容: ``{"paths": ["localpath_1", "localpath_2_if_any"]}``

冒泡提示 ``/message``
------------------------

参数：

- title: 提示信息的标题，通常是简短的描述
- body: 提示信息的正文
- build_number: 所需的桌面助手最低build版本号

响应：

- 格式: JSON/JSONP
- JSON内容: 成功则返回``{"status": "done"}``

JS SDK
============
JavaScript SDK用于简化Web端的开发，其中集成了一些通用的方法。


使用JavaScript SDK的方法是在页面尾部（或在定义了``edo_assistent_opts``变量后的任意位置）载入SDK脚本文件，脚本会自动初始化，并创建一个``edo_assistent``全局对象。通过调用这个对象的方法，可以完成页面上与桌面助手相关的大部分操作。

``edo_assistent_opts``是用于初始化``edo_assistent``对象的一些设置，内容如下::

    {
        server: "服务器", 
        account: "帐号", 
        instance: "实例", 
        token: "token", 
        min_build: 1 // 这是所需的最低桌面助手build版本号
    }

初始化好的``edo_assistent``有以下方法

- ``fail_back()`` 当桌面助手没有正确响应请求时（通常是由于没有安装或没有启动桌面助手）调用这个方法，会在页面上提示用户安装或启动桌面助手。
- ``select_folder(callback)`` 选择本地文件夹，选择之后将会调用传入的``callback``函数处理返回的JSON信息。
- ``download(uids, localpath)`` 下载若干个文件到指定的本地路径下。其中``uids``是多个uid的数组。
- ``select_files(callback)`` 选择若干个本地文件，选择之后会调用传入的``callback``函数处理返回的JSON信息。
- ``upload_files(folder_uid, local_files)`` 上传若干个本地文件到指定文件夹中，其中``local_files``是多个本地文件路径的数组。
- ``select_sync_folder(folder_uid, callback)`` 列出指定文件夹的本地同步区，获取数据之后会调用``callback``函数处理返回的JSON信息。
- ``sync(folder_uid, local_path, type, callback)`` 同步。其中``folder_uid``是同步区的uid；``local_path``是同步区的本地路径；``type``是同步类型，共有三种：pull、push和sync；

