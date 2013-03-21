.. contents::

标签组
======================

标签组实现了多维度、多层次、可管理的标签管理。如果要添加一个标签:

ITagsManager(sheet).addTag('完成')

希望同时去除这个标签组中的所在维度其他的标签， 比如"处理中"这样的状态，因为二者不能同存:

ITagsanager(sheet).addTag('完成', exclude=True)

这里使用ITagManager进行标签管理。完整接口为

- listTags(): 得到全部Tags
- setTags(tags): 更新Tags
- addTag(tag, exclude=False):
  添加一个Tag, 如果exclude，则添加的时候， 把FaceTag的同一类的其他标签删除
- delTag(tag): 删除指定Tag
- canEdit(): 是否可以编辑

另外，使用IFaceTagSetting可进行标签设置的管理：

- getFaceTagText(): 得到face tag 文字
- setFaceTagText(text): 
  设置face tag文字，会自动转换的, 典型如下:

  """

  按产品

  -wps

  -游戏

  -天下

  -传奇

  -毒霸

  按部门

  -研发

  -市场

  """

- getFaceTagSetting(): 得到全部的face tag setting

  """

  [(按产品, (wps, (游戏, (天下, 传奇)), 毒霸)),


  (按部门, (研发, 市场))]

  """
