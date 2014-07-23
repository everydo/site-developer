=======
title: 查看器swf扩展格式
description: 对swf扩展格式的说明
=======

查看器swf扩展格式
===========================
易度文档可以转换为flash，通过易度查看器，实现在线流式播放。

为了支持分页播放，易度的flash格式，和标准的swf，稍微有差别.

文件分帧
-------------
采用多个文件来存放文件，每个文件存放3页。

如果第一个文件是(1-3页)::

     http://server.com/path/to/folder/transformed.swf

那么第二个文件(4-6页)命名为::

     http://server.com/path/to/folder/transformed1.swf

swf格式
-----------
为了方便查看器显示总体页数情况，在每个swf头部包括一个文件信息的json字符串，具体格式::  

  {"totalPage":"30","fromPage":"1","toPage":"3"}

其中：

- totalPage: 总页数
- fromPage: 当前文件的起始页数
- toPage: 文件的最后页数
