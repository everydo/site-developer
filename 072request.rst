.. Contents::

请求和响应对象
===================
request是一个内置变量，存放了当前浏览器的请求信息。

- ``request.principal.id #当前用户id``
- ``request[field_name] #提交的表单信息``
    如果通过表单来上传文件，表单的action属性里面要给__disable_frs4wsgi=1这个参数，
    然后通过request.get('field_name','')来获取文件的对象进行操作，如果不加该参数，上传
    的文件会自动进入系统的FRS中，不能通过request.get()来取得。
- 其他的CGI信息

request.response是当前请求的返回信息：

- ``request.response.setHeader('Content-Type', 'application/excel') #设置消息头``
- ``request.response.redirect(url) #跳转到另外一个地址``

如果需要得到Session信息，可使用ISession(request)进行操作
