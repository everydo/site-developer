.. Contents::
.. sectnum::

文件管理接口
=================

“IRevisionManager”: 版本管理
-------------------------------------

- save(comment='', metadata={}): 存为一个新版本
- retrieve(selector=None, preserve=()): 获得某一个版本
- getHistory(preserve=()): 得到版本历史清单信息
- remove(selector, comment="", metadata={}, countPurged=True): 删除某个版本 
- getWorkingVersionData(): 得到当前工作版本的版本信息，取出来后，在外部维护数据内容
- orderedFolders(): 返回排序后的文件夹集合 
- updateOrder(order): 更新排序


“IFileSender”: 文件外发
------------------------------------

- sendFile(request, creator, send_to, deadline, hashid=None, attach_type=['mail'], password='', description='', send_mail=True)::
  
    """ 发送一个文件
            creator: 发送人的principal_id
            send_to: 收件人，如['firewood@gmail.com', 'firewood1@gmail.com']
            deadline: 查看过期时间，datetime类型
            hashid: 外发历史的hash id，新建时可以为空
            attach_type: 权限(preview: 预览，src: 下载源文件，'pdf': 下载为pdf)
            password: 查看密码，可以为空
            description: 外发说明文字
    """



- sendLink(request, creator, send_to, deadline, hashid=None, attach_type=['preview', 'src', 'pdf'], 
  password='', description='',send_mail=False): 

  获得一个临时授权的链接

- listHistory() 查看临时授权历史

IFolderManager
------------------
addFile(name, title=u'', description=u'', content_type='text/plain', data='')

添加一个文件

File对象
------------
- get_data(): 得到文件的数据
- set_data(data, filename=None, content_type=None)

  设置文件的内容, filename也是用来计算content_type的, 2个二选一就行

相关方法
-----------------------

- “renderFilePreview”: 文件预览组件
- createShortCut(doc, folder, request)：创建快捷方式
