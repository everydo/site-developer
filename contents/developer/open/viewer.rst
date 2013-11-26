---
title: 文档查看接口
description: 在外部系统中，查看易度的文档
---

=====================
文档查看接口
=====================

需求
=========
易度对文档有专业的文档预览、打印、转换PDF等功能，这些功能其他相关系统也需要，比如OA、项目管理等。

出于安全性的考虑，要求对云查看的预览地址进行权限保护：只有指定的人员，在指定的时间内可以访问

总体方案
====================
OA 和 云查看服务器共享一套密匙，用于对请求下载转换后的文件的URL进行签名，确保调用URL合法。URL合法的标准包括：

- 签名正确
- 请求发生的IP地址匹配(可选)
- 没有超过使用期限(可选)

查看器调用API
--------------
::

  <div class="viewer" style="height: 100%"></div>
  <script type="text/javascript"]]>
    var viewer = EdoViewer.createViewer('.viewer', {
        serverURL: 'http://viewer.everydo.com',
        sourceURL: 'http://192.168.12.111/abc.doc',
        ip: '192.168.1.188', 
        timestamp: '1268901715',
        account: '',
        username: 'panjunyong',
        signcode: 'asdf123123asdf12', 
        
    });
    viewer.load();
  </script>

其中：

- serverURL: 云查看服务器的地址,
- sourceURL: 原始文件的下载地址,
- ip: 浏览器的ip地址，如不填写则不做IP检查
- timestamp: 截止时间的时间戳，如果不填写，则永久可查看
- account: 服务器密匙对应的账户，默认为空即可
- username: 访问用户的名字，仅作记录用
- signcode: 签名信息. 

注意：如果云查看没有设置secret，则signcode可以为空，此时云查看不会做安全防护

还可以有更多的参数：

- width：宽度
- height：高度
- allow_print：是否允许打印
- allow_copy：是否允许复制
- waterprint_text: 水印文字
- waterprint_size: 水印字体大小
- waterprint_alpha: 水印透明度
- waterprint_color：水印颜色
- waterprint_x: x方向位置
- waterprint_y: y方向位置
- waterprint_rotation: 方向旋转(从 0 到 180 的值表示顺时针方向旋转；从 0 到 -180 的值表示逆时针方向旋转)
- loading_info: 文档正在加载的提示
- converting_info: 文档正在转换的提示
- timeout_info: 文档转换超时的提示

签名(signcode)算法
-------------------
1. 得到原始文件在服务端的存放地址(location)::

       /files/MD5(sourceURL) + '.' + 文件后缀

2. 使用将下面的信息连接，生成md5，这个md5就是signcode

   - location 
   - ip
   - timestamp
   - account
   - username 
   - secret

