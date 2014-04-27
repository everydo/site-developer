---
title: OAuth2接口
description: 易度提供基于OAuth2的标准接口访问形式
---

==================================
OAuth2接口：应用访问授权
==================================

.. sectnum:: 
.. contents::

接口简介
=============
易度开放平台（Everydo Open Platform）是基于易度办公平台，为第三方应用提供授权访问控制，
使第三方应用可以访问易度办公平台提供的各种服务。

易度开放平台使用 `OAuth2.0协议  <http://oauth.net/2/>`_  来管理第三方应用的授权和访问。

申请应用
===============
应用的开发者，首先向易度（developer@everydo.com）申请注册一个应用，易度会提供如下2个信息用于开发：

- client_id: 应用的ID
- client_secret: 应用的密钥，这个需要妥善保存

access_token的获取
==============================================
每一次访问系统API的时候，需要附带access_token信息才可以。易度开放平台提供了三种不同的access_token获取方式, 详细见下。

首次获取access_token
--------------------------
基于用户信息安全理由，用户在很多时候并不愿意轻易透露用户名、密码给第三方的应用，所以我们也支持了一个比较安全的第三方应用授权方式，这种方式需要用户在网页上进行授权。具体流程：

1. 应用向文档系统请求授权
2. 文档系统为用户显示一个授权页面，用户在此页面确认是否同意应用的请求
3. 如果用户同意授权，应用会获取到一个验证授权码(code)
4. 应用再次先系统发起请求，验证应用自身的安全性，文档系统返回一个授权令牌(access_token)。

请求::

  https://example.oc.everydo.com/authorize?client_id=123050457758183&redirect_uri=http://www.example.com/response

同意授权后会重定向::

  http://www.example.com/response&code=343434

应用得到code之后继续向OC发送请求::

  https://example.oc.everydo.com/api/v1/oauth2/access_token?client_id=12305045775&client_secret=dsdkjk******dsdd&grant_type=code&code=343434

返回数据::

  {
       "access_token": "ACCESS_TOKEN",
       "refresh_token": "REFRESH_TOKEN",
       "expires_in": 1234,
       "remind_in":"798114",
       "uid":"user.admin"
  }

refresh_token: 刷新
----------------------------------

应用在已经拥有了用户授权的情况下，可能由于access_token已经过期了，或者旧的access_token存在泄密的风险的时候，应用可以通过这种方式去交换获得一个新的access_token。

使用示范::

  https://example.oc.everydo.com/api/v1/oauth2/access_token?client_id=12305045775&client_secret=dsdkjk******dsdd&grant_type=refresh_token&refresh_token=434fhjfhs******dsdkj

返回数据::

  {
       "access_token": "ACCESS_TOKEN",
       "refresh_token": "REFRESH_TOKEN",
       "expires_in": 1234,
       "remind_in":"798114",
       "uid":"user.admin"
  }

使用access_token
===========================================
有2种方法:
- 在请求的参数加上直接加上access_token参数 ::
    
    http://oc.easydo.cn/api/v1/org/get_objects_info?account=zopen&objects=ou:default,person:admin&access_token=jsifjdsfijfksjkj2429dkjsd

- 一是在请求headers中添加Authorization: ::

    GET /api/v1/org/get_objects_info?account=zopen&objects=ou:default,person:admin HTTP/1.1
    Host: oc.easydo.cn
    Authorization: Bearer jsifjdsfijfksjkj2429dkjsd
    Cache-Control: no-cache


接口文档
===================


/api/v1/oauth2/authorize: 请求用户授权Token
--------------------------------------------------
基于OAuth2的authorize接口，用了得到用户的验证授权码(code)

1. HTTP请求方式

    GET/POST

2. 请求参数

    =============  ======== ===============   =========================================================
    参数名            必填   类型及范围            说明
    =============  ======== ===============   =========================================================
    client_id       true     string	            申请应用时分配的AppId
    redirect_uri    true     string	            授权回调地址
    =============  ======== ===============   =========================================================

3. 返回数据

    =========== =========== ==========================================================
    返回值字段  字段类型    字段说明
    =========== =========== ==========================================================
    code        string      验证授权码，用作access_token接口的请求参数换取access_token
    =========== =========== ==========================================================



/api/v1/oauth2/access_token: 获取授权过的Access Token
-----------------------------------------------------------
基于OAuth2的access_token接口。

1. HTTP请求方式

    GET/POST

2. 请求参数

    =============  ===== ===============   =====================================================================
    参数名          必填      类型及范围            说明
    =============  ===== ===============   =====================================================================
    client_id      true   string           申请应用时分配的ID
    client_secret  true   string	         申请应用时分配的AppKey
    grant_type     true   string           请求的类型，可选（authorization_code、password、refresh_token)
    code           false  string           调用authorize获得的code值（grant_type为authorization_code时需要填写）
    refresh_token  false  string           刷新授权码（grant_type为refresh_token是需要填写）
    username       false  string           用户名（grant_type为password是需要填写）
    password       false  string           密码（grant_type为password是需要填写）
    =============  ===== ===============   =====================================================================


3. 返回数据

    =============== =========== ========================================================
    返回值字段      字段类型    字段说明
    =============== =========== ========================================================
    access_token    string      作为API调用时带的令牌
    refresh_token   string      用于更新用户的access_token， 只能使用一次
    =============== =========== ========================================================



/api/v1/oauth2/get_token_info
--------------------------------------
当前登录用户的基本信息，这个是我们对oauth2的一个扩展接口

返回::
   
   {'app_id' : 'workonline',
    'account' : 'zopen',
    'pid'    : 'users.test'
    }

