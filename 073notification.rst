.. Contents::
.. sectnum::


object_notify (重要，最常用)
======================================
object_notify可采用用户对对象的订阅方式(短信、邮件、系统消息)，进行消息发送::

    def object_notify(context, body, title='', to_pids=None, event='', from_pid=None, sms=None, email=None, exclude_me=True, exclude_ids=[], bind=False):
        """ 把消息发送给相关人员, 不同的通知方式，采用不同的通知文本
     
        body: 默认的消息文本 sms: 短信通知文本  email 电邮通知文本     
        title: 标题
        to_pids: 如果为空，发送给对象的订阅人
        event: 用于自定义标题: 张三 - event: title
        exclude_me: 是否允许自己给自己发通知
        exclude_ids: 需要排除的人
        bind: 是否绑定对象(context)
        """

notifier_message
=============================

消息通知工具接口：::

    send (title, body, to_ids,from_id=None, exclude_ids=[],bound_obj=None) #发送消息

参数：

- title:	标题
- body:	正文
- to_ids:	收件人，是一个list或者tuple
- from_id:	(可选) 发信人
- exclude_ids:	(可选) 被排除在外的id
- bound_obj:	(可选) 绑定对象


notifier_email: 电子邮件
=====================================
可直接发送邮件，有个to_emails参数::

  send(self, title, body,to_ids, to_emails=[], from_id=None,exclude_ids=[],bound_obj=None)

notifier_sms: 短信
=====================================
可直接发送短信，有个to_numbers参数::

 send(self, title, body,to_ids, to_numbers=[], from_id=None,exclude_ids=[],bound_obj=None)
