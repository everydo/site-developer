---
title: 短信发送
description: 独立的短信发送服务
---

==========
短信发送
==========

每个账户可以独立购买短信发送服务，通过开放的API来发送短信.

/api/v1/sms/send
==========================
发送短信

参数：

- account
- instance: 具体的实例
- mobiles: 发给手机号码
- users: 发给用户
- ous: 发给部门
- groups: 发给组
- exclude: 排除的用户
- message: 限1000字，超过自动截断

返回：

- tid: 本次发送标示
- valid_count: 有效的手机号码数量
- invalid_mobiles: 无效的号码
- invalid_users: 无效的用户，用户不存在或者没有手机号码，或者号码不存在。包括部门和组里面的用户
- invalid_ous: 无效的部门
- invalid_groups: 无效的组
- splitted_message_count: 分割的短信条数

/api/v1/sms/result
=========================
查询送达情况

参数：

- account
- instance: 具体的实例
- tid：

返回：

- sucess_count: 成功发送数量
- errors: 发送失败的用户、号码

  - user_id:
  - mobile:
  - reason:

/api/v1/sms/get_operation_options
======================================
查看运营参数

参数：

- account
- instance: 具体的实例

返回：

详细的运营参数，包括：

- amount: 允许发送的短信数量
- used: 已经发送的短信数量
