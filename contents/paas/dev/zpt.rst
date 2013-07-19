===================================
页面模版的使用
===================================

如果简单的页面，直接使用python的print输出即可。

如果复杂的页面，可采用页面模版(Page Template)

.. Contents::
.. sectnum::

理解页面模版
===============
页面模版，一般用来生成html页面，供python脚本使用。

页面模板是一种类似于 ``asp/jsp/php`` 页面的技术，即包含有程序逻辑的页面结构。
特点：

1. 符合xhtml语法规则
2. 基本不引入新的html标签
3. 通过引入特殊的属性来扩展

使用Zope页面模版
==========================
页面模板主要用于生成动态页面, 将数据在页面上恰当的位置以恰当的形式显示出来，下面通过在界面上操作示例来说明它的使用方法。

创建一个页面模板
----------------------------
与添加Script(Python)一样，页面模板还在软件包中添加，从添加下拉列表中选择Page Template。

(TODO: 创建截图)

创建一个名字叫做hellopage的页面模版，内容为::


      <h2>你好，<span tal:content="python:username">content title or id</span>! </h2>

注意:

- 这里有一个很特殊的 tal:content 属性
- 这个属性的值是一个python表达式：python:username


在python 脚本中调用
--------------------------------
将前面hello world教程中的python脚本正文改成如下::

  username = request.principal.id
  return render_template('hellopage', username=username)

这里渲染hellopage这个模板，并传入参数username

运行结果
---------------
测试运行后，可以在浏览器中看到运行结果，再查看所生成的HTML页面源代码::

       <h2>你好，<span>admin</span>! </h2>

可以看到，除了其中有一些HTML标记中含有以"tal:"开头的属性消失了，替换成了更为具体的内容。

这里，tal:content   表示以属性值填充HTML标记内容，上面例子中span标记中使用了tal:content，以传入的username值来替换其内容。

以"tal:"开头的一系列属性值采用了一种叫做TALES动态的表达式。ZPT正是通过这一组特殊的扩展属性来实现动态效果的。

后面我们将详细介绍这组扩展属性和TALES动态表达式的使用。从上面的例子中，我们可以看到，ZPT没有引入新的标记，而是主要通过扩展属性来实现动态效果，因此ZPT和HTML能够完全兼容。这是和传统的ASP/PHP/JSP最大的不同和优势之处。

TALES表达式
=======================
前面我们看到，在ZPT中扩展属性的动态部分采用TALES表达式完成。
TALES，其全称为模板属性语言表达式语法 (Template Attributes Language Expression Syntax).

TALES最早是专门用于页面模版的，现在TALES已经作为一个通用的表达式语言，在Zope/Plone的ZMI定制中大量使用。
比如我们前面在定制portal_actions工具中的操作项的时候，其中的condition、action，都是TALES表达式。

TALES表达式有三种基本形式: 字符串表达式和 Python 表达式。


字符串表达式
----------------
字符串表达式适合输出动态文本的场合。字符串表达式以string:前缀起始，使用起来非常简单。比如::

  string:Just text. There's no path here.

将直接返回字符串"Just text. There's no path here."。

也可以在字符串中包含一个动态的变量，比如::

  string:copyright $year, by me.

其中在变量名前使用 ``$`` 符号，表示是在引用一个变量，Zope在解释时会将它替换成变量的值。
这个例子中当year变量定义为2007时(本章后面会讲到在页面模板中定义变量)，最终结果会是"copyright 2007, by me."

有时变量与其它字符之间没有空格或标点符号等分隔符，这需要使用 ``{}`` 来显式地指出变量名称的部分，
如这个例子中变量vegetable会被替换成其值::

  string:Three ${vegetable}s, please.

如果在字符串中需要直接使用 ``$`` 符号，则需要多写一个 ``$`` 来转义，如::

  string: In $$US it costs.

Python表达式
-----------------
Python表达式用于评估一行Python代码，这是在TALES中直接使用Python的表达式。
Python表达式以python:作为前缀，可以使用Python语言格式的表达式，使用非常灵活，功能也最强大。

如下面的例子返回当前调用对象的title属性::

  python:IDublinCore(context).title

而这个例子则返回调用对象的的 ``files`` 子文件夹中的所有内容::

  python:context.files.values()

但注意，Python表达式中不能使用象if和while这样的语句，
因为在Python中if和while是语句而不是表达式。

此外，Zope还对访问受保护的信息、更改安全数据和创建无限循环这样的错误进行一些安全限制。
更多信息，请参见前一章中关于Script(Python)的安全限制部分。
这些安全限制对于在页面模板中使用的python表达式也同样适用。

TALES绑定变量
---------------------
实际上与脚本类似的是，页面模板中也可使用context和request变量。

tal属性
=========================
从这一节开始，我们将详细讨论ZPT的各种语法。首先我们从tal扩展属性开始。

tal属性是是对xhtml的一个扩展，这部分扩展用于对xhtml代码进行动态的操作，包括内容和属性的填充和替换、循环、条件、删除等。

tal:content/tal:replace: 内容替换
--------------------------------------
从最简单的需求开始，往一个静态的页面上添加动态的内容。

假设你有一个静态页面如下::

 <html>
  <body>
   <h2>title</h2>
   <p>the paragraph.</p>
  </body>
 </html>

现在往上增加动态的内容，如将标题替换为页面标题，段落内容替换为模板的标题。
你可以在title标记和p标记上增加tal属性::

 <html>
  <body>
   <h2 tal:content="python:getName(context)">title</h2>
   <p tal:content="python:IDublinCore(context).title">the paragraph.</p>
  </body>
 </html>

..
  注意，这里的template和context是在所有页面模版中可直接使用的变量(绑定变量)，
  分别表示模版自身和调用模版的上下文对象。

在ZMI中添加一个页面模板并写入以上的内容，单击Test标签页来测试运行它，
你可以看到页面上模板的id出现在二级标题h2上，而调用的文件夹(zpt)的标题显示为段落内容。

在新页面中中打开测试页面，可以看到测试运行的URL::

  http://localhost:8080/zpt/totest

查看页面源文件如下::

 <html>
   <body>
     <h2>totest</h2>
     <p>zpt</p>
   </body>
 </html>

可以看到使用tal作为动态生成的部分都被替换成了相应的内容。

由context变量的动态特性还可以知道：totest模板可以根据获取规则调用在不同的位置。
再回到这个文件夹中创建一个子文件夹 ``testfolder`` ，创建好后在上面单独的测试页面修改URL为::

  http://localhost:8080/zpt/testfolder/totest

再次查看页面源文件::

 <html>
   <body>
     <h2>totest</h2>
     <p>testfolder</p>
   </body>
 </html>

根据获取规则你知道，由于在新建的文件夹还没有totest对象，它会找到上一级文件夹的totest对象，此时运行结果中包含
页面模板的标题没有变，但p标记段落中的内容变成了'testfolder'，

这个例子展示了tal:content的替换规则和context变量的用法，并再一次验证了获取规则所起的作用。
同时也展示了在ZMI中创建页面模板和测试运行的步骤，因此以下的例子中不再多写ZMI操作步骤，只需要照样操作即可测试。

tal:replace与tal:content类似，只不过替换更多了一层，连HTML标记一起替换掉，如::

    <p tal:replace="python:getName(context)">the paragraph will be replaced.</p>

可以直接将上面的例子中的tal:content替换为tal:replace，在测试运行时，
查看生成页面的源代码，
可以发现，结果页面上的p标记没有了，输出直接是"context/title_or_id"的内容。

如果在显示时不需要这多余的一层HTML标记，这时使用 ``tal:replace`` 就很有用处。

使用structure插入原始的HTML
............................
正常情况下，tal:replace和tal:content语句都将其内容中所有含有的HTML标记和内容以一种转义过的形式来展现，
这样以结果中就可以显示在纯文本段落中了，
例如你要显示的字符串含有'<','&'等各种符号时，
这些符号在转换过的页面源代码中将变为'&lt;'和'&amp;'。比如显示request变量::

 <p tal:content="request">request</p>

显示的页面将是html的源代码，如图12.4所示，实际的页面很长，这里只取了开头的一部分：

.. figure:: img/zpt/default-request.png
   :alt: 直接打印request变量

   图 12.4 直接打印request变量

但是如果希望直接显示html，而非源代码，可以增加 ``structure`` 修饰，比如::

 <p tal:content="structure request">request</p>

这样，我们可直接看到最终的html效果，如图12.5所示，实际的页面很长，这里只取了开头的一部分：

.. figure:: img/zpt/structure-request.png
   :alt: 使用structure修饰request

   图 12.5 使用structure修饰request

tal:attributes: 属性替换
-------------------------------
这个是用来作修改html标记的属性用的，如a标记的href，img标记的src属性，还有各种html标记的title属性等。

我们可以修改a标记链接地址，比如::

  <html>
    <body>
      <h2>test atttributes</h2>
      <a tal:attributes="href context/@@absolute_url">link to folder</a>
    </body>
  </html>

这一段内容在zpt中测试运行会生成如下的HTML代码::

  <html>
    <body>
        <h2>totest</h2>
        <a href="http://localhost:8080/zpt">link to folder</a>
    </body>
  </html>

可以看到a标记的href属性已被转换。

如果你试图写两个tal:attributes来替换两个不同的属性的话，可以看到在ZMI中保存时也提示出错，
事实上这也是 xhtml 的特性之一，它不允许一个标记有两个相同的属性，
解决方法是在一个tal:attributes语句中写多个属性，
它们之间以分号分隔开::

  <img tal:attributes="src string:${doc/getIcon}; title string:${doc/title}" />

但在生成XML文件时，属性可以自由定义，可以使用XML名称空间随意定义需要的属性，例如::

  <Description
      dc:Creator="creator name"
      tal:attributes="dc:Creator context/owner/getUserName">
    Description</Description>

简单的把XML名称空间前缀放在属性名称前面，你可以用XML名称空间创建属性。

tal:condition: 条件判断
--------------------------------------
正如在开篇示例中所见，tal:condition用来作条件判断，是否显示所在的标记。这与程序逻辑中的 ``if`` 结构很类似。

如在表单控制的时候经常需要检查用户对某个域有没有输入。
下面这两个例子中检测request上是否有message变量，
其中前一个例子检测是否设置了message并测试它的值是否为真，
而后一个例子仅仅检测request/message是否存在::

  <p tal:condition="request/message | nothing"
     tal:content="request/message">message goes here</p>

  <p tal:condition="exists:request/message"
     tal:content="request/message">message goes here</p>
 
..
  TODO: 这个例子不大好
  这里仅当context/title输出为真的时候，才显示<p>段落标记的内容；否则整条<p>段落标记将不显示。

  如果你想表达的是不存在 title 属性时才显示一段内容，参照上面的TALES表达式一段内容，可以知道not路径扩展表达式正是所需要的::

       <p tal:condition="not:context/title"> ... </p>

tal:define: 定义变量
--------------------------------------
在程序结构中有一类型典型的用途就是要定义变量以方便在其它处使用，
在页面模板中使用tal:define也可以定义变量::

  <p tal:define="title context/title_or_id">
      ... <i tal:content="title">The title</i> ...
  </p>

与attributes同样的，如果要定义多个变量可以写在同一个tal:define内部，将它们以分号分隔开::

 <ul tal:define="objs context/contentValues; ids context/contentIds">

注意，这里定义的变量也是有作用域的，就是说它只在定义的局部存在，
当定义它的HTML标记结束以后这个变量自动销毁，如下面这个例子中，
title变量是定义在p标记上，在p标记结束后继续使用title将会报错::

  <p tal:define="title context/title_or_id">
      ... <i tal:content="title">The title</i> ...
  </p>
  <!-- 下面这一句会发生错误 -->
  ... <i tal:content="title">The title</i> ...

定义全局变量
................

如果要在标记封闭后继续使用这个变量，一般地解决方法是将变量定义在更为外层的HTML标记上，
还有另一种解决方法是将变量定义成全局的。
全局变量使用global前缀定义，如::

  <p tal:define="global title string:Foo bar">
      ... <i tal:content="title">The title</i> ...
  </p>
  <i tal:content="title">We still have a title</i>

全局的变量定义之后就可以在后面的标签中使用，而不管html标签是否封闭，它是直到页面结束才消失的。
在下面要讲到的宏的概念中，定义在宏内的全局变量可以用在使用这个宏的模板中，
这样相当于扩大了全局变量的作用域，增加了全局量的使用范围。
在后面一章要讲到的Plone的模板开发中，就是使用这种方法定义了很多全局变量，
使得Plone中的模板开发更为方便。

tal:repeat: 循环结构
--------------------------------------
在介绍了顺序结构和条件结构之后，剩下的就是第三种，循环结构，tal:repeat正是用于这个目的。

这是一个简单的例子，它以HTML无序列表的方式显示5个字符串::

  <ul> <li tal:repeat="i python:range(1,6)"
           tal:content="string:this is No.  $i"/> </ul>

运行结果是::

  <ul> <li>this is No. 1
       <li>this is No. 2
       <li>this is No. 3
       <li>this is No. 4
       <li>this is No. 5 </ul>

可以看到，tal:repeat相当于一种定义语句，每循环一次都在range(1, 6)中顺序取一个值定义给变量i，
直到循环结束。

这是一个稍复杂的例子，从context/objectValues上返回的是一个列表，
context是调用的文件夹，context/objectValues则返回这个文件夹中的所有对象组成的列表。

由于是在table的行上循环，可以看到测试运行的结果是一个有很多行的表格，每行显示一个标题。

这是它的源代码，很简短::

  <table>
    <tr tal:repeat="row context/objectValues">
        <td tal:content="row/title_or_id">Title</td>
    </tr>
  </table>

每次循环从context/objectValues上取一个值，定义给row变量，
在循环过程中从row变量上读出其title_or_id。

从repeat语句上还可以得到很多有用的变量，如可以读出循环的编号，
下面再给这个表格增加一列显示其编号::

  <table>
    <tr tal:repeat="row context/objectValues">
        <td tal:content="repeat/row/number">1</td>
        <td tal:content="row/title_or_id">Title</td>
    </tr>
  </table>

可以看到的结果是一个两列的表，在第一列中显示的是循环的编号，这是 ``repeat/row/number`` 所替换成的，

而 ``tal:content="repeat/row/number"`` 中的repeat是上面提到的绑定变量，
它是一个字典值，在repeat变量上以路径表达式漫游到row可以得到row循环变量，
在这个变量上可以读到一些有用的属性：

- index - 循环的序号，从0开始
- number - 循环的序号，从1开始
- even - 对于偶数序号(0, 2, 4, ...)为真。
- odd - 对于奇数序号(1, 3, 5, ...)为真。
- start - 对于起始循环为真(index 0)。
- end - 对于结尾或最终的循环为真
- length - 序列长度，就是循环总次数
- letter - 用小写字母计数，"a" - "z", "aa" - "az", "ba" - "bz", ..., "za" - "zz", "aaa" - "aaz"等等。
- Letter - 用大写字母计数。

如你想将这个表格中的编号改为从0开始可以将上面的代码改写为 ``tal:content="repeat/row/index"`` 。

既然都是从循环变量上读，为什么不能直接写成 "repeat/index" 是否更简单？
这是为了考虑循环嵌套的情况，在嵌套的循环中使用不同的循环变量可以在内层读出外层的循环变量。

这是一个嵌套循环的例子，显示了一个数学乘法表::

  <table border="1">
    <tr tal:repeat="x python:range(1, 13)">
      <tal:rep tal:repeat="y python:range(1, 13)">
        <td tal:content="python:'%d x %d = %d' % (x, y, x*y)">
          X x Y = Z
        </td>
      </tal:rep>
    </tr>
  </table>

注意这个例子中使用了简单的tal:rep标记，这个并不是有效的html标记，
它的作用仅仅是在Zope解释时作为一个循环控制结构，下文将有详细的介绍。

tal:repeat另外一个没有提供的有用的特性是排序。
如果你想对一个列表排序，你或者编写自己的排序脚本（在Python里是相当容易的）,
或者你可以使用sequence.sort工具函数。

以下是一个如何按照标题对一个列表排序，然后按照修改日期排序的例子::

  <table tal:define="contents context/contentValues;
                     sort_on python:(('title', 'nocase', 'asc'),
                                     ('bobobase_modification_time', 'cmp', 'desc'));
                     sorted_contents python:sequence.sort(contents, sort_on)">
    <tr tal:repeat="item sorted_contents">
      <td tal:content="item/title">title</td>
      <td tal:content="item/bobobase_modification_time">
        modification date</td>
    </tr>
  </table>

这个例子试图通过在sort函数外边定义sort参数。
在这个例子里，如何对序列排序的描述是在sort_on变量里定义的。
关于sequence.sort函数的更多信息请参见附录常用API参考。

tal执行顺序
--------------
当每个元素中只有一个tal语句时，执行的顺序是简单的。
从root元素开始，执行每个元素的语句，然后访问每个下级元素，按照这个顺序，执行他们的语句，依次类推。

可是，存在相同的元素拥有多个tal语句的情况。
除了tal:content和tal:replace语句不能结合在一起外，任何语句的结合都可能出现在相同的元素里边。

当一个元素有多个语句时，他们的执行顺序如下:

#. define
#. condition
#. repeat
#. content or replace
#. attributes
#. omit-tag

由于tal:on-error语句只有当发生错误时才会运行，因此，它不参与执行优先级排序。

注意condition位于repeat之前执行，在一些例子中，你可能想对循环变量进行判断，
如这个例子中，在10个数字上循环，但要跣过数字3，::

  <!-- 有错误的模板 -->
  <ul>
    <li tal:repeat="n python:range(10)"
        tal:condition="python:n != 3"
        tal:content="n">
      1
    </li>
  </ul>

但这个例子不会工作，因为condition会在repeat之前运行，此时变量n还没有定义，于是报告异常。
为此需要修改一下::

  <ul>
    <tal:rep repeat="n python:range(10)">
      <li tal:condition="python:n != 3"
          tal:content="n">
        1
      </li>
    </tal:rep>
  </ul>

在这里使用了tal:rep标记，它并不会显示在输出中。condition在repeat内层执行，因此是可以运行的。

使用tal扩展标记
--------------------
前面我们都是介绍tal属性，其实也可以直接使用tal标记的，比如::

  <tal:block define="objs context/objectValues">
      ...
  </tal:block>

tal标记就是指以tal:开头的标记，它使用了tal的名字空间，这也是利用了xml语言的扩展特性。
这里的tal:block仅仅用于表示一个结构，这个标记不会输出生成html。

另外，一旦使用tal形式的标记，则此标记的的tal属性名中的tal: 可省略不写，
上面例子中的define就表示tal:define。

以tal作为标记可以在冒号后面使用任何有意义的名称，如循环时可以使用::

  <tal:items repeat="val context/objectValues">
    ...
  </tal:items>

这种形式，不仅省去了写出不必要的标记的麻烦，还使用了有意义的名字，
增加了页面模板的可读性。

