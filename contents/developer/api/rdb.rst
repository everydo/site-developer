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

接口:  get_connection
------------------------------

连接参数：

- db_type (参数选择：mysql、ms_sql_server、oracle、 postgresql)
- 其他连接参数（由使用的连接包决定）

数据库连接包：

- mysql：mysql-python



