---
title: 关系数据库访问
description: 如何利用SQL接口访问关系数据库
---

===========================
关系数据库访问
===========================

ZODB数据库适合管理文档和流程，但是在数据分析方面，和传统的关系数据库比较，还是存在不足。
另外，大量传统的CRM、ERP等系统，还是以关系数据库做为存储，和这些系统的集成也需要访问关系数据库。

关系数据库访问采用类似python db-api的接口，底层做了连接池管理



支持的数据库
-----------------

- Mysql
- Oracle
- Microsoft SQL Server
- PostgreSQL

接口:  get_db_connection
------------------------------

连接参数：

- db_type (可选：mysql、ms_sql_server、oracle、 postgresql)
- 其他连接参数（由使用的连接包决定）

所使用的数据库连接包：

- mysql：
    - mysql-python
    - 项目地址: `https://pypi.python.org/pypi/MySQL-python <https://pypi.python.org/pypi/MySQL-python>`_   
- Microsoft SQL Server:
    - pymssql
    - 项目地址： `https://code.google.com/p/pymssql <https://code.google.com/p/pymssql>`_  
- Oracle:
    - cx_Oracle
    - 项目地址： `http://cx-oracle.sourceforge.net‎ <http://cx-oracle.sourceforge.net‎>`_ 
- PostgreSQL: 
    - PyGreSQL
    - 项目地址：  `http://www.pygresql.org/readme.html <http://www.pygresql.org/readme.html>`_ 


<<<<<<< HEAD
使用示范：:::
=======
使用示范-mysql ::
>>>>>>> 增加postgresql使用示范

      # mysql
      connection = get_db_connection('mysql', host='127.0.0.1', user='username', passwd='password', db='python')
      cursor = connection.cursor()
 
      #执行一个查询
      cursor.execute("SELECT VERSION()")
      #取得上个查询的结果，是单个结果
      data = cur.fetchone()
      print "Database version : %s " % data


      value = [1,"inserted ?"];
      # 插入一条记录
      cursor.execute("insert into test values(%s,%s)",value);
 
      values=[(1, "hello I`m recode 1"), (2, "hello I`m recode 2"), (3, "hello I`m recode 3")] 
      
      # 插入多条记录
      cursor.executemany("""insert into test values(%s,%s) """,values);

      connection.commit()
 
      # 关闭连接，释放资源
      cursor.close();

使用示范-postgresql ::

      # postgresql
      pgdb_conn = get_db_connection('postgresql', host='127.0.0.1', user='username', passwd='password', dbname='python')
 
      #查询表 1         
      sql_desc = "select * from tbl_product3"  
      for row in pgdb_conn.query(sql_desc).dictresult():  
          print row  
   
      #查询表2          
      sql_desc = "select * from tbl_test_port"  
      for row in pgdb_conn.query(sql_desc).dictresult():  
          print row   
   

      #插入记录     
      sql_desc = "INSERT INTO tbl_product3(sv_productname) values('apple')"  
      try:  
          pgdb_conn.query(sql_desc)  
      except Exception, e:  
          print 'insert record into table failed'  
          pgdb_conn.close()    
          return      


      # 关闭连接，释放资源
      pgdb_conn.close()         


