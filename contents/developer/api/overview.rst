---
title: 开放API概览
description: 总体说明系统的开放API架构
---

======================
开放API概览
======================

系统的开放API，建立在http协议上，依托在oauth2.0协议之上的，返回结果的形式json。

请求的基本形式是::

  http://service-domain-name/api/v1/module_name/api_name?arg1=value1&arg2=value2

服务组成
====================
::

      +-------------------+      +---------------+
      | 账户管理: account |------| 组织架构: org |
      +-------------------+\  /  +---------------+
               |            \/
  .............|............/\..................
               |           /  \
  +----------------------+/    +----------------+
  | 工作平台: workonline |-----| 文件存储: file |
  +----------------------+     +----------------+
                    |
               +------------------+
               | 文件转换: viewer |
               +------------------+

其中账户管理、组织架构有唯一的服务API入口地址，其他服务的API入口地址，需要通过account提供的 ``get_instance`` 接口来查询.


Oauth2简介
=============
易度开放平台（Everydo Open Platform）是基于易度办公平台，为第三方应用提供授权访问控制，
使第三方应用可以访问易度办公平台提供的各种服务。

易度开放平台使用 `OAuth2.0协议  <http://oauth.net/2/>`_  来管理第三方应用的授权和访问。

开发应用申请
===============
应用的开发者，首先向易度（developer@everydo.com）申请注册一个应用，易度会提供如下2个信息用于开发：

- client_id: 应用的ID
- client_secret: 应用的密钥，这个需要妥善保存

access_token
==============================================
每一次访问系统API的时候，需要附带access_token信息才可以。

可以在请求headers中添加Authorization: ::

    GET /api/v1/org/get_objects_info?account=zopen&objects=ou:default,person:admin HTTP/1.1
    Host: oc.easydo.cn
    Authorization: Bearer access_token字符串
    Cache-Control: no-cache

也可以在请求的参数加上直接加上access_token参数 ::
    
    http://oc.easydo.cn/api/v1/org/get_objects_info?account=zopen&objects=ou:default,person:admin&access_token=access_token字符串 


登录授权得到access_token
=========================
用户登录之后，得到授权码code，应用通过code来换token

1. 如果没有登录，浏览器会跳转进入授权页面::

     https://example.oc.everydo.com/authorize?client_id=123050457758183&redirect_uri=http://www.example.com/response

   文档系统为用户显示一个授权页面，用户在此页面确认是否同意应用的请求. 
   包括2个请求参数:

   - client_id       :申请应用时分配的AppId
   - redirect_uri    :授权回调地址

2. 如果用户同意授权，会重定向::

     http://www.example.com/response&code=343434

   这样应用会获取到一个验证授权码(code)

3. 用作access_token接口的请求参数换取access_token::

    https://example.oc.everydo.com/api/v1/oauth2/access_token?client_id=12305045775&client_secret=dsdkjk******dsdd&grant_type=code&code=343434

返回数据::

  {
       "access_token": "ACCESS_TOKEN",
       "refresh_token": "REFRESH_TOKEN",
       "expires_in": 1234,
       "remind_in":"798114",
       "uid":"user.admin"
  }

其中access_token就是用于api请求认证的。

更新：refresh_token
======================
出于安全考虑，可以通过refresh_token来定期更新token，详细api见附说明

应用在已经拥有了用户授权的情况下，可能由于access_token已经过期了，或者旧的access_token存在泄密的风险的时候，应用可以通过这种方式去交换获得一个新的access_token。

使用示范::

  https://example.oc.everydo.com/api/v1/oauth2/access_token?client_id=12305045775&client_secret=dsdkjk******dsdd&grant_type=refresh_token&refresh_token=434fhjfhs******dsdkj

返回数据::

  {
       "access_token": "ACCESS_TOKEN",
       "refresh_token": "REFRESH_TOKEN",
       "expires_in": 1234,
       "remind_in":"798114",
       "uid":"admin"
  }


接口文档
===================

/api/v1/oauth2/access_token
-----------------------------------------------------------
基于OAuth2的access_token

1. HTTP请求方式

    GET/POST

2. 请求参数

    =============  ===== ===============   =====================================================================
    参数名          必填      类型及范围            说明
    =============  ===== ===============   =====================================================================
    client_id      true   string           申请应用时分配的ID
    client_secret  true   string	         申请应用时分配的AppKey
    grant_type     true   string           请求的类型，可选（authorization_code、refresh_token)
    code           false  string           调用authorize获得的code值（grant_type为authorization_code时需要填写）
    refresh_token  false  string           刷新授权码（grant_type为refresh_token是需要填写）
    =============  ===== ===============   =====================================================================


3. 返回数据

    =============== =========== ========================================================
    返回值字段      字段类型    字段说明
    =============== =========== ========================================================
    access_token    string      作为API调用时带的令牌
    refresh_token   string      用于更新用户的access_token， 只能使用一次
    =============== =========== ========================================================


/api/v1/oauth2/get_token_info
-----------------------------------------
当前登录用户的基本信息:

返回::

   {'app_id'  : 'workonline',
    'account' : 'zopen',
    'user' : 'test',
    }

