---
title: 关系数据库访问
description: 如何利用SQL接口访问关系数据库
---

===========================
关系数据库访问
===========================

ZODB数据库适合管理文档和流程，但是在数据分析方面，和传统的关系数据库比较，还是存在不足。
另外，大量传统的CRM、ERP等系统，还是以关系数据库做为存储，和这些系统的集成也需要访问关系数据库。

关系数据库访问采用类似python db-api的接口，底层做了连接池管理，使程序的数据库连接管理更方便、更高效



支持的数据库
-----------------

- Mysql
- Oracle
- Microsoft SQL Server
- PostgreSQL

接口:  get_db_connection(db_type, \*\*kwargs)
------------------------------------------------------

- db_type (连接的数据库类型，支持：mysql、ms_sql_server、oracle、 postgresql)
- kwargs（具体查看所使用的连接包的文档）

  - `mysql <https://pypi.python.org/pypi/MySQL-python>`_
  - `Microsoft SQL Server <https://code.google.com/p/pymssql>`_  
  - `Oracle‎ <http://cx-oracle.sourceforge.net‎>`_ 
  - `PostgreSQL <http://www.pygresql.org/readme.html>`_ 

- 返回一个符合 `DB-API 2 <http://www.python.org/dev/peps/pep-0249/>`_ 规范的连接

使用示范 ::

      db_type = 'mysql'

      # 连接不同的数据库，kwargs的key有所不同 
      if db_type == 'mysql':
          connection = get_db_connection('mysql', host='127.0.0.1', user='username', passwd='password', db='python')
      elif db_type == 'ms_sql_server':
          connection = get_db_connection('ms_sql_db', host='127.0.0.1', user='user', password='password', database='python')
      elif db_type == 'postgresql':
          connection = get_db_connection('postgresql', host='127.0.0.1', user='user', password='password', database='python')
      elif db_type == 'oracle':
          connection = get_db_connection('oracle', 'username','passwd','127.0.0.1:1523/python')

      cursor = connection.cursor()
 
      #执行一个查询
      cursor.execute("select * from test;")
      #取得上个查询的结果，是单个结果
      data = cur.fetchone()
      print "key is %s, value is  %s " % data[0], data[1]

      # 插入一条记录
      cursor.execute("insert into test values('key', 'value')")

      # 插入多条记录
      values=[(1, "hello I`m recode 1"), (2, "hello I`m recode 2"), (3, "hello I`m recode 3")]       
      cursor.executemany("""insert into test values(%s,%s) """, values)

      connection.commit()
 
      # 释放连接， 将连接返回连接池
      cursor.close()
      connection.close()
