=========================================
引入费用管理
=========================================

好，前面几课下来，我们已经初步掌握了单个流程定制的一些技巧。

接下来，我们看看如果制作多表关联的复杂应用。

.. contents::
.. sectnum::

理解需求
=================
这一课中，我们需要引入一个个人的订餐费记账的功能，也就是每次订餐，需要自动扣除订餐费用。

另外，当然作为管理员，需要预先设置每个人的账户初始金额情况

还有每次订餐成功，需要通知订餐人员价格和余款。

好了，就这些了，已经有些复杂了。

简单分析一下，需要这个功能，订餐系统至少需要2个栏目才可以了。

先定义好用户费用表单
===============================
大家已经掌握了表单定制的技巧，这里就不再累赘叙说了。

.. image:: img/fee-user-money.png

注意，这个表的名字叫：user_money


部署剩余费用的数据管理器
==============================
点击 定制 链接，再部署一个人员剩余费用的数据管理器：

.. image:: img/fee-money.png

这样订餐系统就成这样了：

.. image:: img/fee-tabs.png

让余额表是数据自动维护
==================================
经过上面的部署，就差一步: 在订餐的同时，自动扣除订餐人的余额费用。

我们到订餐流程的“订餐”步骤的“通过”操作表单，调整最后的触发脚本为如下::

    # 定餐成功，计算余额
    user_money = parentcontainer['dingcan.user_money']
    username = [sheet['creators'][0]]
    money = ITinyTable(user_money.values()).queryOneRow(username=username).get('money')
    left_money = money - sheet['price']

    # 通知
    msg_notifier.send('成功订餐',
                       '''您的订餐： %s 已经订餐成功，价格：%d元，
    您的余额是%d元。届时请正确认领''' % (sheet['title'], sheet['price'], left_money),
                       sheet['creators'], bound_obj=sheet
                      )

    # 需要扣费
    ITinyTable(user_money.values()).setValue('money', left_money, username=username )

注意：

1. parentcontainer就是当前表单的容器的容器，就是我们部署的那个订餐系统容器了
2. 这里有个ITinyTable的适配接口，用来对表格进行操作


