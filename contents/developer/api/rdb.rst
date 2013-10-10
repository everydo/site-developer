---
title: 关系数据库访问
description: 如何利用SQL接口访问关系数据库
---

===========================
关系数据库访问
===========================

易度默认采用的是ZODB，数据库适合管理文档和流程，使用非常方便。但是在数据分析方面，和传统的关系数据库比较，还是存在不足。

另外，大量传统的CRM、ERP等系统，还是以关系数据库做为存储，和这些系统的集成也需要访问关系数据库。

易度提供了对关系数据库访问，目前支持的数据库包括：Mysql、 Oracle、Microsoft SQL Server 、PostgreSQL。

快速示例
=====================
接口基于标准的  `DB-API 2 <http://www.python.org/dev/peps/pep-0249/>`_ 规范，底层做了连接池管理，使用简单高效::

    # 得到一个Mysql数据的连接
    connection = get_db_connection('mysql', host='127.0.0.1', user='username', passwd='password', db='test_db')
    cursor = connection.cursor()
 
    # 执行一个查询
    cursor.execute("select username, fullname from users;")
    #取得上个查询的结果，是单个结果
    data = cursor.fetchone()
    print "username is %s, fullname is  %s " % data[0], data[1]

    # 插入一条记录
    cursor.execute("insert into users values('zhangsan', '张三')")

    # 插入多条记录
    values=[('lisi', "李四"), 
            ('wangwu', "王五"), 
            ('laozhang', "老张")]       
    cursor.executemany("""insert into users values(%s, %s) """, values)

    connection.commit()
 
    # 释放连接， 将连接返回连接池 (注意，这里必须释放)
    cursor.close()
    connection.close()

函数：get_db_connection
================================
::

   get_db_connection(db_type, **kwargs)

此函数会返回一个符合 `DB-API 2 <http://www.python.org/dev/peps/pep-0249/>`_ 规范的连接，其中：

- db_type:  连接的数据库类型，可选：

  - mysql: `MySQL数据库 <https://pypi.python.org/pypi/MySQL-python>`_
  - ms_sql_server: `Microsoft SQL Server <https://code.google.com/p/pymssql/wiki/PymssqlExamples>`_  
  - oracle: `Oracle数据库 <http://cx-oracle.sourceforge.net/html/module.html>`_ 
  - postgresql: `PostgreSQL <http://www.pygresql.org/readme.html>`_ 

- kwargs: 各种数据库实际需要的连接参数, 比如主机、用户名，密码等，不同的数据库，所需要的参数也有所不同

MySQL的连接参数 ::

    connection = get_db_connection('mysql', host='127.0.0.1', user='username', passwd='password', db='test_db')

Microsoft SQL Server的连接参数 ::

     connection = get_db_connection('ms_sql_db', host='127.0.0.1', user='user', password='password', database='test_db')

PostgreSQL 的连接参数 ::
 
     connection = get_db_connection('postgresql', host='127.0.0.1', user='user', password='password', database='test_db)
 
Oracle 的连接参数::

     connection = get_db_connection('oracle', user='username', password='passwd', dsn='127.0.0.1:1523/test_db')

