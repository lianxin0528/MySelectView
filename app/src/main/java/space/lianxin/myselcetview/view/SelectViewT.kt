package space.lianxin.myselcetview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.MotionEvent
import space.lianxin.myselcetview.R
import space.lianxin.myselcetview.util.dp2px
import space.lianxin.myselcetview.util.getBitmapFromDrawable

/**
 * description:右侧快速索引（onDraw实现）
 * @date: 2020/6/9 22:13
 * @author: lianxin
 */
class SelectViewT @JvmOverloads constructor(
  context: Context?,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) :
  View(context, attrs, defStyle) {

  // 绘制View画笔
  private var paint: Paint = Paint()
  // 默认条目文字颜色
  private val defaultColor = Color.rgb(153, 153, 153)
  // 选中条目文字颜色
  private val selectColor = Color.rgb(48, 209, 139)
  // 弹出文字颜色
  private val popColor = Color.rgb(255, 255, 255)

  // 内置上边距,单位px
  private var heightMarginTop: Int = 0
  // 内置下边距,单位px
  private var heightMarginBottom: Int = 0

  // 条目宽度
  private var viewWidth: Int = 0
  // 文字宽度，包括边距，单位：px
  private var wordWidth: Int = 0
  // 条目高度
  private var viewHeight: Int = 0
  // 文字高度，包括边距，单位：px
  private var wordHeight: Int = 0
  // 弹出图片与文字的横向距离，单位：px
  private var imgMargin: Float = 0f
  // 文字纵向距离，单位px
  private var wordMargin: Float = 0f
  // 弹出图片宽度,单位：px
  private var imgWidth: Int = 0
  // 弹出图片高度,单位：px
  private var imgHeight: Int = 0
  // 绘制的文字大小,单位：px
  private var wordSize: Float = 0f
  // 文字开始位置
  private var wordStart: Int = 0
  // 是否有最近索引字符串
  private val lately = "lately"
  // 是否有好友索引字符串
  private val friend = "friend"
  // 快速索引图片大小, 单位px
  private var imgSize = 0

  // 索引值(默认为A-Z,#)
  private var items = arrayListOf(
    "A", "B", "C", "D", "E", "F", "G",
    "H", "I", "J", "K", "L", "M", "N",
    "O", "P", "Q", "R", "S", "T",
    "U", "V", "W", "X", "Y", "Z", "#"
  )
  // 当前选中索引下标，默认未选中任何索引
  private var selectIndex: Int = -1

  // 设置索引改变监听
  private var onSelectIndexChangeListener: OnSelectIndexChangeListener? = null

  init {
    initDimen()
    init()
  }

  /**
   * 初始化尺寸
   */
  private fun initDimen() {
    // 默认值
    heightMarginTop = 30.dp2px(context).toInt()
    heightMarginBottom = heightMarginTop
    wordWidth = 16.dp2px(context).toInt()
    wordHeight = wordWidth
    imgMargin = 19.5f.dp2px(context)
    wordMargin = 1f.dp2px(context)
    imgWidth = 48.dp2px(context).toInt()
    imgHeight = imgWidth
    wordSize = 11.dp2px(context)
    imgSize = 9.dp2px(context).toInt()

    // 外部值
  }

  /**
   * 初始化
   */
  private fun init() {
    // 初始化画笔
    paint = Paint()
    // 设置画笔颜色
    paint.color = defaultColor
    // 抗锯齿
    paint.isAntiAlias = true
  }

  /**
   * 视图测量，根据测量结果，算出每个item需要的宽高。
   */
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    // 计算条目所需要的宽度
    viewWidth = (wordWidth + imgMargin + imgWidth).toInt()
//    viewWidth = if (selectIndex != -1) {
//      (wordWidth + imgMargin + imgWidth).dp2px(context).toInt()
//    } else {
//      wordWidth.dp2px(context).toInt()
//    }

    // 计算条目所需要的高度
    viewHeight = ((wordHeight + wordMargin) * items.size - wordMargin + heightMarginTop + heightMarginBottom).toInt()
    Log.d("qingyi", "onMeasure: viewWidth=${viewWidth},viewHeight=${viewHeight}")

    MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.getMode(widthMeasureSpec))
    MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.getMode(heightMeasureSpec))
    setMeasuredDimension(viewWidth, viewHeight)
  }

  // 图片资源
  // 弹窗背景图
  private var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.float_alphabet)
  // lately图片资源
  private var bitmapGrayLately =
    getBitmapFromDrawable(context!!, R.drawable.ic_access_time_gray_24dp)
  private var bitmapBlueLately =
    getBitmapFromDrawable(context!!, R.drawable.ic_access_time_blue_24dp)
  private var bitmapWhiteLately =
    getBitmapFromDrawable(context!!, R.drawable.ic_access_time_white_24dp)
  // friend图片资源
  private var bitmapGrayFriend =
    getBitmapFromDrawable(context!!, R.drawable.ic_friend_gray_24dp)
  private var bitmapBlueFriend =
    getBitmapFromDrawable(context!!, R.drawable.ic_friend_blue_24dp)
  private var bitmapWhiteFriend =
    getBitmapFromDrawable(context!!, R.drawable.ic_friend_white_24dp)

  // 从onDraw中拿出来，减少创建次数。
  private var rect: Rect = Rect()
  private val rf = RectF()

  /**
   * 绘制view
   */
  @SuppressLint("DrawAllocation")
  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    // 绘制最近联系和好友
    for (i in 0 until wordStart) {

      // 计算图片位置
      val left = viewWidth - ((wordWidth + imgSize) / 2)
      val top = heightMarginTop + i * (wordHeight + wordMargin) + wordHeight / 2 - imgSize / 2
      val right = left + imgSize
      val bottom = top + imgSize
      rf.set(left.toFloat(), top, right.toFloat(), bottom)

      if (items[i] == lately) {
        // 判断索引命中
        if (i == selectIndex) {
          canvas?.drawBitmap(bitmapBlueLately, null, rf, paint)
          drawPop(canvas)
        } else {
          canvas?.drawBitmap(bitmapGrayLately, null, rf, paint)
        }
      } else {
        if (i == selectIndex) {
          canvas?.drawBitmap(bitmapBlueFriend, null, rf, paint)
          drawPop(canvas)
        } else {
          canvas?.drawBitmap(bitmapGrayFriend, null, rf, paint)
        }
      }
    }

    // 绘制每个条目
    for (i in wordStart until items.size) {

      // 判断下标命中,选择画笔颜色
      if (selectIndex == i) {
        paint.color = selectColor
        // 画弹窗
        drawPop(canvas)
      } else {
        paint.color = defaultColor
      }

      // 读取条目文字，开始绘制
      val word = items[i]
      paint.textSize = wordSize
      paint.getTextBounds(word, 0, 1, rect)
      // 文字宽高
      val ww = rect.width()
      val wh = rect.height()
      Log.d("qingyi", "ondraw绘制第${i}个，ww=$ww,wh=$wh")
      // 计算文字出现位置并绘制
      val wordX = viewWidth - ((wordWidth + ww) / 2)
      val wordY = (heightMarginTop + (i * (wordHeight + wordMargin))) + ((wordHeight + wh) / 2)
      Log.d("qingyi", "onDraw: 绘制第${i}个，wordX=$wordX,wordY=$wordY")
      canvas?.drawText(word, wordX.toFloat(), wordY, paint)

      // 使用Paint.Paint.Align.CENTER简化文字左右对齐方式，简化问题。
      // 但是还是需要计算Y轴中值点。
//      paint.textAlign = Paint.Align.CENTER
//      val wordX = viewWidth - (wordWidth.dp2px(context) / 2)
//      val wordY = (heightMarginTop + (i * (wordHeight + wordMargin))).dp2px(context) + ((wordHeight.dp2px(context)) / 2)
//      Log.d("qingyi", "onDraw: 绘制第${i}个，wordX=$wordX,wordY=$wordY")
//      canvas?.drawText(word, wordX, wordY, paint)
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent?): Boolean {
    super.onTouchEvent(event)

    // 判断事件
    when (event?.action) {
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
        // 根据Y轴坐标计算选中的索引值
        val index = ((event.y - heightMarginTop) / (wordHeight + wordMargin)).toInt()
        Log.d("qingyi", "onTouchEvent: event.y=${event.y}")
        // 判断索引更改
        if (index != selectIndex) {
          // 更改索引下标，并重绘
          selectIndex = index
          invalidate()
          // 判断事件是否还在控件内部。
          if (selectIndex in 0 until items.size) {
            // 接口回调
            if (onSelectIndexChangeListener != null) {
              onSelectIndexChangeListener?.onSelectIndexChange(
                items[selectIndex],
                selectIndex
              )
            }
          }
        }
      }
      MotionEvent.ACTION_UP -> {
        // 取消选中的下标。
        selectIndex = -1
        invalidate()
      }
    }
    return true
  }


  /**
   * 绘制弹窗
   * @param canvas 画布
   */
  private fun drawPop(canvas: Canvas?) {
    // 保存画笔颜色
    val c = paint.color

    // 绘制图片,left:图片左顶点x轴坐标；top:图片左顶点y轴坐标
    val left = 0f
    val top = heightMarginTop + selectIndex * (wordHeight + wordMargin) + wordHeight / 2 - imgHeight / 2
    canvas?.drawBitmap(bitmap, left, top, paint)

    // 判断绘制内容
    if (selectIndex < wordStart) {
      // 绘制图片
      // 计算图片位置, 按照比例计算
      val l = imgWidth * 0.25f
//      val t = ((heightMarginTop + (selectIndex * (wordHeight + wordMargin))) + imgHeight) * 0.31f
      val t = heightMarginTop + selectIndex * (wordHeight + wordMargin)
      val r = imgWidth * 0.62f
      val b = t + imgHeight * 0.37f
      rf.set(l, t, r, b)
      // 分情况绘制
      if (items[selectIndex] == lately) {
        canvas?.drawBitmap(bitmapWhiteLately, null, rf, paint)
      } else {
        canvas?.drawBitmap(bitmapWhiteFriend, null, rf, paint)
      }
    } else {
      // 绘制文字
      paint.textSize = 20.dp2px(context)
      paint.color = popColor
      // 根据背景图片左顶点计算坐标。
      paint.getTextBounds(items[selectIndex], 0, 1, rect)
      val vx = rect.width()
      val vy = rect.height()
      // 文字位置:文在在图中左右比例2:3，垂直居中
      val wordX = left + (imgWidth - vx) * 0.4f
      val wordY = top + ((imgHeight + vy) / 2)
      canvas?.drawText(items[selectIndex], wordX, wordY, paint)
    }

    //还原画笔颜色
    paint.color = c
  }

  /**
   * 设置下标改变监听
   */
  fun setOnSelectIndexChangeListener(l: OnSelectIndexChangeListener) {
    onSelectIndexChangeListener = l
  }

  /**
   * 设置需要显示的快速索引列表
   * @param hasLately 添加最近联系人快速索引？
   * @param hasFriend 添加好友快速索引？
   */
  fun setItems(hasLately: Boolean, hasFriend: Boolean, items: ArrayList<String>) {
    if (hasFriend) {
      items.add(0, friend)
      wordStart++
    }
    if (hasLately) {
      items.add(0, lately)
      wordStart++
    }
    this.items = items
  }

}


