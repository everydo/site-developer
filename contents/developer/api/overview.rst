---
title: 开放API概览
description: 总体说明系统的开放API架构
---

======================
开放API概览
======================

系统的开放API，建立在http协议上，依托在oauth2.0协议之上的，返回结果的形式json。

请求的基本形式是::

  http://service-domain-name/api/v1/module_name/api_name?arg1=value1&arg2=value2

服务地址
====================
系统服务组成包括:

- 账户管理: account
- 组织架构: org
- 工作平台: 应用名字为 workonline
- 文件服务: 应用名字为 file(viewer), 包括上传、下载、转换等
- 短信发送：应用名字为 sms

其中账户管理、组织架构有唯一的服务API入口地址，其他服务的API入口地址，需要通过account提供的接口来查询::

  http://app.easydo.cn/api/v1/account/get_api_address?account=zopen&app=workonline&instance=default

返回值和错误信息
=========================

模块清单
=====================
账户管理：

- account：实例管理、续费、服务地址查询、认证配置


.. image::

  <svg width="640" height="480" xmlns="http://www.w3.org/2000/svg">
 <!-- Created with SVG-edit - http://svg-edit.googlecode.com/ -->
 <g>
  <title>Layer 1</title>
  <ellipse ry="38" rx="87" id="svg_1" cy="132" cx="242" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="#000000"/>
  <ellipse id="svg_4" ry="38" rx="87" cy="123.5" cx="489" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="#000000"/>
  <ellipse id="svg_6" ry="38" rx="87" cy="347.5" cx="509" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="#000000"/>
  <text xml:space="preserve" text-anchor="middle" font-family="serif" font-size="24" id="svg_8" y="147" x="243" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="#ffff00">云存储</text>
  <text xml:space="preserve" text-anchor="middle" font-family="serif" font-size="24" id="svg_9" y="126" x="478" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="#ffff00">WO</text>
  <text xml:space="preserve" text-anchor="middle" font-family="serif" font-size="24" id="svg_11" y="355" x="504" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="#ffff00">浏览器</text>
  <line id="svg_13" y2="240" x2="-58" y1="239" x1="-60" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line id="svg_16" y2="160" x2="502" y1="307" x1="515" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line id="svg_17" y2="159" x2="500" y1="177" x1="490" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line stroke="#000000" id="svg_18" y2="161" x2="501" y1="175" x1="516" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" fill="none"/>
  <polyline id="svg_21" points="74 332 74 332" stroke-linecap="round" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="none"/>
  <line id="svg_24" y2="124" x2="407" y1="125" x1="329" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line id="svg_25" y2="158" x2="300" y1="319" x1="470" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line id="svg_26" y2="310" x2="484" y1="162" x1="473" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line id="svg_27" y2="160" x2="303" y1="168" x1="301" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line id="svg_28" y2="159" x2="299" y1="157" x1="317" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line id="svg_29" y2="241" x2="238" y1="240" x1="238" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line id="svg_30" y2="125" x2="328" y1="116" x1="332" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line id="svg_31" y2="126" x2="328" y1="133" x1="336" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke="#000000" fill="none"/>
  <line id="svg_32" y2="310" x2="483" y1="304" x1="490" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="none"/>
  <line id="svg_33" y2="311" x2="481" y1="307" x1="472" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="none"/>
  <text fill="#000000" stroke="#000000" stroke-width="0" stroke-dasharray="null" stroke-linejoin="null" stroke-linecap="null" x="286" y="60" id="svg_2" font-size="24" font-family="serif" text-anchor="middle" xml:space="preserve">浏览器表单上传模式</text>
  <text stroke="#000000" xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_3" y="285" x="344" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" fill="none">1</text>
  <ellipse ry="8" rx="9" id="svg_5" cy="225" cx="460" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="#000000"/>
  <ellipse id="svg_7" ry="8" rx="9" cy="222.5" cx="521" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="#000000"/>
  <ellipse id="svg_12" cy="241" cx="316" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="#000000"/>
  <ellipse id="svg_14" cy="237" cx="316" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="#000000"/>
  <ellipse id="svg_15" cy="238" cx="321" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="#000000"/>
  <ellipse id="svg_19" cy="232" cx="321" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="#000000"/>
  <ellipse id="svg_20" cy="224" cx="326" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="#000000"/>
  <ellipse id="svg_22" ry="8" rx="9" cy="238.5" cx="354" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="#000000"/>
  <text xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_35" y="330" x="305" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="#ffff00"/>
  <text xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_34" y="220" x="530" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="#ffff00"/>
  <ellipse id="svg_36" ry="8" rx="9" cy="109.5" cx="368" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="null" stroke="#000000" fill="#000000"/>
  <text stroke="#000000" xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_37" y="245" x="356" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" fill="#ffff00">2</text>
  <text xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_39" y="230" x="460" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="#ffff00">1</text>
  <text stroke="#000000" xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_41" y="226" x="524" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" fill="#ffff00">3</text>
  <text xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_42" y="113" x="370" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" stroke="#000000" fill="#ffff00">4</text>
  <text transform="matrix(32.6667 0 0 14.8726 -760 -3820.34)" stroke="#000000" xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_43" y="284" x="27" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" fill="#000000"/>
  <text transform="matrix(28 0 0 9.11111 -621 -2173.78)" stroke="#000000" xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_44" y="281" x="26" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" fill="#000000"/>
  <text transform="matrix(1 0 0 1.04167 0 -10.9583)" stroke="#000000" xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_46" y="291" x="90" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" fill="#000000">1：浏览器得到上传token</text>
  <text transform="matrix(1 0 0 1 0 0)" stroke="#000000" xml:space="preserve" text-anchor="middle" font-family="serif" font-size="11" id="svg_47" y="320" x="28" stroke-linecap="null" stroke-linejoin="null" stroke-dasharray="null" stroke-width="0" fill="#000000"/>
 </g>
</svg>

