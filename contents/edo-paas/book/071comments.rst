.. Contents::
.. sectnum::

评注服务
==========

几乎所有的对象都可以被评注。

添加评注的方法为：::

    ICommentManager(context).addComment(body, author, email)

列表显示评注的方法为：::

    for comment in ICommentManager(context):
        print comment.body
        print IDublinCore(comment).creators
        print IDublinCore(comment).modified


最后的评论::

   print len(ICommentManager(context))

   last_comment = ICommentManager(context)[-1]
   print last_comment.body
   print IDublinCore(last_comment).creators
   print IDublinCore(last_comment).modified
