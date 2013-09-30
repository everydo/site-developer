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
    - 项目地址：https://pypi.python.org/pypi/MySQL-python
- Microsoft SQL Server:
    - pymssql
    - 项目地址： https://code.google.com/p/pymssql/ 
- Oracle:
    - cx_Oracle
    - 项目地址： http://cx-oracle.sourceforge.net‎
- PostgreSQL: 
    - PyGreSQL
    - 项目地址：  http://www.pygresql.org/readme.html


使用示范：:::


      connection = get_db_connection('mysql', user='username', passwd='password', db='python')
      cursor = connection.cursor()
 
 
      value = [1,"inserted ?"];
      # 插入一条记录
      cursor.execute("insert into test values(%s,%s)",value);
 
      values=[]
      # 生成插入参数值
      for i in range(20):
          values.append((i,'Hello mysqldb, I am recoder ' + str(i)))
 
      # 插入多条记录
      cursor.executemany("""insert into test values(%s,%s) """,values);
      connection.commit()
 
      # 关闭连接，释放资源
      cursor.close();





