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

桌面助手服务器在 ``http://127.0.0.1:5000`` 监听请求。

在所有发往这个地址的请求中，带上callback参数即可支持JSONP响应，解决Ajax跨域的问题。

在所有发往桌面助手服务器的请求中，带上build参数指定需要的桌面助手最低build版本号，可以让低版本桌面助手自动升级。

文件管理API
===============
文件相关操作的API封装在filestore模块的FileStore类中。

文件下载 ``/new_task/download``
---------------------------------------
调用FileStore实例的 ``download_file`` 方法可以下载一个文件。

参数：

- ``token`` token
- ``metadata`` 文件的metadata
- ``local_folder`` 要将文件下载到的本地文件夹路径
- ``root_uid=None`` （下载单个文件时不需要）指定这个文件属于哪个同步区
- ``last_sync=None`` （下载单个文件时不需要，但文件属于同步区内时才需要）指定这个文件最近的同步时间

返回值：

- None

文件上传 ``/new_task/upload``
----------------------------------
调用FileStore实例的upload_file方法可以上传一个文件。

参数：

- ``token`` token
- folder_uid 要上传到服务端文件夹的uid
- local_path 文件的本地路径
- root_uid=None （上传单个文件时不需要，进行文件夹同步时才需要）指定这个文件属于哪个同步区
- last_sync=None （上传单个文件时不需要，进行文件夹同步时才需要）指定这个文件最近的同步时间
- parent_rev=None （上传单个文件时不需要，进行文件夹同步时才需要）revision，指定这个文件是基于哪个版本进行修改的
- file_uid=None （上传单个文件时不需要，进行文件夹同步时才需要）这个文件的uid

返回值：

- None

文件同步
--------------
文件同步分为向上和向下同步。

向上同步调用FileStore实例的push方法来完成。

参数：

- token token
- uid 同步区的uid
- local_folder 同步区的本地文件夹路径

返回值：

- None

检查冲突
--------------
调用FileStore实例的check_push_conflict方法可以检查本地项目是否与服务端冲突。

参数：

- client=None WoClient实例，必需
- item=None 由local_diff返回的项目，必需。结构是：`{'type': 'new_folder', 'item': 'database record dict or path string'}`
- root_uid=None 项目所属的本地同步区的uid， 必需
- parent_uid=None 项目所在父文件夹的uid，必需
- root_local_folder=None 项目所属的本地同步区的路径，必需

返回值：

- 冲突时返回True，并在返回前在本地数据库中标记好冲突；不冲突返回False

通用API
============
包括UI和任务管理方面的API。

任务查询
--------------


选择文件夹
--------------
选择本地文件夹有两种方式：

- 通过向桌面助手服务器/select_folder路径发送GET请求
- 调用ui_client模块的select_folder方法
- 通过ui_client.select_folder方法

  参数：

  - default=None 默认路径，可选，建议为None
  - title='选择文件夹' 对话框的标题，可选
  - description='' 描述，可选
  - server=None 指定服务器（可以使用WoClient获取），必需
  - account=None 指定账户（可以使用WoClient获取），必需
  - instance=None 指定实例（可以使用WoClient获取），必需

  返回值：

  - 格式：字符串
  - JSON内容：`{"selected": false, "path": null}` 若用户选择了路径，则selected为true且path为选择的路径

- 通过向桌面助手服务器/select_folder路径发送GET请求

  参数：

  - server 指定服务器，必需
  - account: 指定账户，必需
  - instance: 指定实例，必需

  响应：

  - 格式：JSON/JSONP
  - JSON内容：`{"selected": false, "path": null}` 若用户选择了路径，则selected为true且path为选择的路径

选择文件
--------------
通过向桌面助手服务器/select_files路径发送GET请求，来选择若干个本地文件

参数：

- 不需要额外参数

响应：

- 格式：JSON/JSONP
- JSON内容：`{"paths": ["path_to_file_1", "path_to_file_2"]}`


显示服务端文件夹对应的本地同步区
--------------------------------------
通过向桌面助手服务器/sync_paths路径发送GET请求，来获取一个服务端文件夹对应的本地同步区列表

参数：

- server 指定服务器（可以通过WoClient获取）
- instance 指定实例（可以通过WoClient获取）
- account 指定帐号(可以通过WoClient获取)
- uid 文件夹的uid

响应：
- 格式：JSON/JSONP
- JSON内容：`{"paths": ["localpath_1", "localpath_2_if_any"]}`

冒泡提示
--------------
提供冒泡提示有两种方式：

- 可以通过调用ui_client模块的message方法
- 或向桌面助手服务器发送GET请求
- 通过ui_client.message方法

    参数：

    - title 提示信息的标题，通常是简短的描述
    - body 提示信息的正文

    返回值：

    - None

- 通过向桌面助手服务器/message路径发送GET请求

    参数：

    - title 提示信息的标题，通常是简短的描述
    - body 提示信息的正文

    响应：

    - 格式：JSON/JSONP
    - JSON内容：成功则返回`{"status": "done"}`

JS SDK
============
JavaScript SDK用于简化Web端的开发，其中集成了一些通用的方法。


使用JavaScript SDK的方法是在页面尾部（或在定义了edo_assistent_opts变量后的任意位置）载入SDK脚本文件，脚本会自动初始化，并创建一个edo_assistent全局对象。通过调用这个对象的方法，可以完成页面上与桌面助手相关的大部分操作。

edo_assistent_opts是用于初始化edo_assistent对象的一些设置，内容如下::

    {
        server: "服务器", 
        account: "帐号", 
        instance: "实例", 
        token: "token", 
        min_build: 1 // 这是所需的最低桌面助手build版本号
    }

初始化好的edo_assistent有以下方法。

- ``fail_back()`` 当桌面助手没有正确响应请求时（通常是由于没有安装或没有启动桌面助手）调用这个方法，会在页面上提示用户安装或启动桌面助手。
- ``select_folder(callback)`` 选择本地文件夹，选择之后将会调用传入的callback函数处理返回的JSON信息。
- ``download(uids, localpath)`` 下载若干个文件到指定的本地路径下。其中uids是多个uid的数组。
- select_files(callback) 选择若干个本地文件，选择之后会调用传入的callback函数处理返回的JSON信息。
- upload_files(folder_uid, local_files) 上传若干个本地文件到指定文件夹中，其中local_files是多个本地文件路径的数组。
- select_sync_folder(folder_uid, callback) 列出指定文件夹的本地同步区，获取数据之后会调用callback函数处理返回的JSON信息。
- sync(folder_uid, local_path, type, callback) 同步。其中folder_uid是同步区的uid；local_path是同步区的本地路径；type是同步类型，共有三种：pull、push和sync；

