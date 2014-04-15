---
title: WebDAV接口 
description: 使用标准的webdav协议，访问易度的文件
---

==============================
易度文件访问WebDAV接口
==============================

.. contents:: 内容
.. sectnum::

背景
=========
易度文档管理系统，在企业信息化体系中，是一个文档存储和管理的基础服务。企业的其他信息系统，需要将文档存储到易度文档管理系统中。这需要易度文档管理系统，提供一套标准的访问API，提供给其他系统使用。

.. image:: img/arch.png

易度采用互联网标准化组织IETF所批准的WebDAV协议，来和外部系统进行数据交换。

易度采用WebDAV标准的好处是：

- WebDAV已经有10多年的历史，非常成熟，非常全面
- WebDAV是协议层面的，和语言无关，支持更广泛
- 存在大量支持库，各种语言都支持，特别是Java/Python语言，开发比较方便
- 各种主流的软件和服务，都提供webdav接口，它是一个活的标准，包括：

  - windows、IIS、office，以及最新的手机端，都支持WebDAV的接口
  - Java的内容仓库规范JCR 170样板服务jackrabbit，也采用webdav作为对外接口
  - 当前主流的云存储比如box.net，都提供webdav操作接口

webdav协议介绍
================
由于易度采用标准的WebDAV接口，互联网上存在大量介绍资料。这里主要提供简单的资料介绍和指引，方便集成开发工作的展开。

webdav协议组成:
------------------------
- RFC4918：WebDAV基础，创建文件夹、属性，COPY/MOVE,加锁解锁
- RFC3253：DeltaV，版本管理
- RFC3648：OrderColl，排序文件夹
- RFC3744：ACL，授权管理
- RFC4316：PropType，属性类型
- RFC4331：Quota，文件夹配额管理
- RFC4437：Redir，资源的引用和跳转
- RFC4709：Mount，web上集成弹出窗口进行WebDAV管理
- RFC4791：CalDAV，日程管理
- RFC5323：SEARCH，搜索
- RFC5397：Current Principal      当前登录用户ID
- RFC5689：Extended MKCOL，支持各种资源类型，支持属性设置
- RFC5842：BIND，一个文件存放在多个位置

易度支持RFC4918, RFC5323这2个协议

扩展的HTTP方法
-------------------------
除了基础的 GET/PUT/HEAD 之外，还包括新的:

PROPFIND：
   属性查询，也可以用于查看文件夹的内容列表
PROPPATCH：
   修改属性，删除属性
MKCOL：
   创建文件夹
COPY：
   复制
MOVE：
   转移
LOCK：
   加锁
UNLOCK：
   解锁
SEARCH：
   搜索
ACL：
   授权

认证：Basic
----------------------
这个是最基础的http认证方式, 在所有请求的消息头中，增加::

   Authorization: Basic xxxx 

其中xxx是 用户名:密码的base64编码 

属性操作
-------------------
1. PROPFIND：属性查找

   a) 查看文件夹包含的内容清单
   b) 查看文件自身的属性
   c) 文件名、大小、修改时间
   d) 易度扩展属性

2. PROPPATCH

   a) 修改属性
   b) 删除属性

属性组成
--------------
- Webdav属性由2部分组成 

  - Namespace
  - 属性ID
- 一个namespace，包括一组属性 

Webdav内置属性
--------------------
Namespace为：DAV: 包括： 

- creationdate：创建时间 
- displayname：文件名 
- getcontentlanguage：语言 
- getcontentlength ：长度 
- getcontenttype : mime类型 
- getlastmodified ：修改时间 
- resourcetype ：文件，还是文件夹？ 


易度webdav扩展
======================
易度对标准的webdav进行扩展,主要是对属性操作。

易度自带属性
----------------
易度自带的一组内部属性,  Namespace为::

   http://ns.everydo.com/basic

具体包括： 

- intid：内部唯一整数标识(活)
- attachments：附件的intids 
- related：关联的 
- subjects：标签 

例子：设置附件/关联文件

- 得到附件的intid属性 
- 设置主文件的attachments/related属性 
- 值为附件的intid 
- 如果有多个附件，intid用逗号相隔 

易度扩展属性
--------------------------
用户可自定义的扩展属性  （详见后） 

- 定义扩展属性 
- 设置生效文件夹 

Namespace为::

  http://ns.everydo.com/extended 

属性ID为::

       元数据名.属性字段名 

如：archive_archive.archival_number 

定义扩展属性
---------------
.. image:: img/metadata-list.png

.. image:: img/define-metadata.png

设置扩展属性生效的文件夹
--------------------------------
在需要使用扩展属性的文件夹下，添加增补属性规则 

.. image:: img/metadata-rule.png

使用扩展属性
---------------------
.. image:: img/use-metadata.png

搜索: DAV:basicsearch
-------------------------------
- DAV:select : 返回结果定义 
- DAV:from ：范围 

  - href:离根节点的 相对路径 
  - depth：只支持0和无穷 
- DAV:where ：条件，支持： 

  - subjects：标签，多标签逗号分隔 
  - displayname：标题 
- DAV:orderby ：结果集排序
- DAV:limit ：限制长度

对扩展属性搜索
-----------------------
目前易度还不支持直接对自定义的扩展属性进行搜索。一个折中的方法是:

1. 使用API更新扩展属性的时候，将字段值，加入到文档的标签subjects中
2. 这样可以直接利用标签（subjects属性）来进行搜索
3. 如果用户在易度界面上更改这些值，也可以在扩展属性定义地方加上同步维护的脚本

具体例子，比如一个文档，有如下字段：

- 部门： 开发部
- 类型： 设计文档

可以直接打上2个标签：开发部、设计文档，这样对subject就可以搜索了。

如果多个属性有重复的值，可以在值中加上前缀即可区分，比如 部门_开发部，类型_设计文档

开发调试环境
======================
- 可在线注册

  http://everydo.com/paas/signup.rst 

无需安装，在线调试, 永远最新版本 

易度文件地址说明
==========================
1. 文件上传，需要指定资源路径，比如:

   http://server/default/folder/abc.txt

2. 文件的下载地址: 就是资源路径

   http://server/default/folder/abc.txt

3. 文件的查看地址: 在资源路径加上/@@view.html

   http://server/default/folder/abc.txt/@@view.html

典型集成需求
==================
功能需求:

1. 上传文件
2. 设置附件
3. 设置分类标签
4. 设置扩展属性
5. 下载文件
6. 搜索文件

准备操作： 

- 为集成系统规划存档文件夹 
- 为集成系统开设一个集成专用帐号 
- 给这个帐号授权，允许上传文件 

集成： 

- 使用http basic进行登录认证 
- 使用标准的http put上传文件 
- 使用webdav扩展的proppatch设置属性 
- 使用webdav扩展的propfind查询属性 

集成案例
=============
- 京华OA

  - 文档审批完毕，直接进入易度 
  - 流程审批单转换为html，在易度中存档 
  - 自动设置文档附件 
  - 设置文档权限 
  - 在京华OA中，可直接预览 文档 
  - 在京华OA中，可直接查看相关文档 
  - 在京华OA中，可直接进行文档搜索

- 中软

附一：Java开发接口
================================
JAVA SDK：jackrabbit webdav library

- 支持4918，3253，3648，3744，5323，5842
- jackrabbit属于Java内容仓库(JCR)的一个参考实现，侧重服务端的实现。也包括webdav客户端访问库 
- http://jackrabbit.apache.org/jackrabbit-webdav-library.html 

- 更多库介绍： http://wiki.apache.org/jackrabbit/WebDAV 
- 中文使用资料： http://yiyu.iteye.com/blog/896302 

具体参看 `java demo 代码 <https://github.com/everydo/site-developer/tree/master/contents/developer/open/java>`__

- 查看文件夹和文件（PROPFIND）： 
- 修改属性（PROPPATCH）： 
- 上传文件（PUT）： 
- 搜索（SEARCH）： 
- 删除（DELETE）： 
- 下载（GET）： 
- 创建文件夹（MkCol）:
- 设置附件:
 
修改易度扩展属性::

    DavPropertySet newProps=new DavPropertySet();  
    DavPropertyNameSet removeProperties=new DavPropertyNameSet();   
    DavProperty testProp=new DefaultDavProperty(" archive_archive.archival_number ", “aasd23232342", 
                          Namespace.getNamespace("http://ns.everydo.com/extended"));  
    newProps.add(testProp);  
    PropPatchMethod proPatch=new PropPatchMethod("http://www.somehost.com/duff/test4.txt", newProps, removeProperties);   
    client.executeMethod(proPatch);  
    System.out.println(proPatch.getStatusCode() + " "+ proPatch.getStatusText());


附二：python开发接口
======================================
Python SDK: Python webdav lib
支持比较完整，包括4918，3744，3253，5323等 https://launchpad.net/python-webdav-lib 


还有很多其他的， 使用方便，但是不完整

上传文件PUT ::

    from webdav.WebdavClient import CollectionStorer 
    webdavConnection = CollectionStorer(webdavUrl, validateResourceNames=False)
    webdavConnection.connection.addBasicAuthorization(username, password)
    webdavConnection.addResource(name, content=None, properties=None, lockToken=None)

下载文件 GET::

    resource = ResourceStorer(url)
    Resource.downloadContent()      

文件夹内容: PROPFIND::

    webdavConnection = CollectionStorer(webdavUrl, validateResourceNames=False)
    webdavConnection.getCollectionContents():

文件属性PROPFIND::

    webdavConnection.findAllProperties()
    resource.readProperty('http://ns.everydo.com/extended', u'archive_archive.archival_number')

修改属性 PROPATCH::

    resource.writeProperties(
    {
    ('http://ns.everydo.com/extended', u'archive_archive.archival_number'):'abcdefg-good'}
    )

SEARCH ::

    webdavUrl = r'http://xtz:8089/++skin++EDOWorkonlineSkin/wo/default.zopen.standalone'
    username, password  = 'admin‘, 'admin'
    webdavConnection = CollectionStorer(webdavUrl, validateResourceNames=False)
    webdavConnection.connection.addBasicAuthorization(username, password)
    condition = TupleTerm([])
    condition.addTerm(MatchesTerm('subjects', u'标签三,标签4'))

    results = webdavConnection.search(condition, [('DAV:', "displayname"), ('http://ns.everydo.com/basic', 'subjects')],
                        orderby=Descend(('DAV:', 'displayname')), limit=Limit(1), path='files/dav' )

    for obj_url, result in results.iteritems():
        print '***' * 20
        print obj_url
        for prop in result.values():
            print '%s: '%prop.name
            print prop.textof()

