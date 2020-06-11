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

/**
 * description:右侧快速索引（onDraw实现）
 * @date: 2020/6/9 22:13
 * @author: lianxin
 */
class SelectViewT @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0) :
  View(context, attrs, defStyle) {

  // 绘制View画笔
  private var paint: Paint = Paint()
  // 默认条目颜色
  private val defaultColor = Color.rgb(153, 153, 153)
  // 选中条目颜色
  private val selectColor = Color.rgb(48, 209, 139)
  // 弹出文字颜色
  private val popColor = Color.rgb(255, 255, 255)

  // 内置上边距,单位dp
  private val heightMarginTop: Int = 30
  // 内置下边距,单位dp
  private val heightMarginBottom: Int = 30

  // 条目宽度
  private var viewWidth: Int = 0
  // 文字宽度，包括边距，单位：dp
  private val wordWidth: Int = 16
  // 条目高度
  private var viewHeight: Int = 0
  // 文字高度，包括边距，单位：dp
  private val wordHeight: Int = 16
  // 弹出图片与文字的横向距离，单位：dp
  private val imgMargin: Float = 19.5f
  // 文字纵向距离，单位dp
  private val wordMargin: Float = 1f
  // 弹出图片宽度,单位：dp
  private val imgWidth: Int = 48
  // 弹出图片高度,单位：dp
  private val imgHeight: Int = 48
  // 绘制的文字大小,单位：px
  private val wordSize: Float = 11.dp2px(context!!)

  // 索引值
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
    viewWidth = (wordWidth + imgMargin + imgWidth).dp2px(context).toInt()
    // 计算条目所需要的高度
    viewHeight = ((wordHeight + wordMargin) * items.size - wordMargin + heightMarginTop + heightMarginBottom).dp2px(context).toInt()
    Log.d("qingyi", "onMeasure: viewWidth=${viewWidth},viewHeight=${viewHeight}")

    MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.getMode(widthMeasureSpec))
    MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.getMode(heightMeasureSpec))
    setMeasuredDimension(viewWidth, viewHeight)



  }

  private var rect: Rect = Rect()

  /**
   * 绘制view
   */
  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    // 绘制每个条目
    for (i in 0 until items.size) {

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
      val wordX = viewWidth - ((wordWidth.dp2px(context) + ww) / 2)
      val wordY = (heightMarginTop + (i * (wordHeight + wordMargin))).dp2px(context) + ((wordHeight.dp2px(context) + wh) / 2)
      Log.d("qingyi", "onDraw: 绘制第${i}个，wordX=$wordX,wordY=$wordY")
      canvas?.drawText(word, wordX, wordY, paint)

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
        val index = ((event.y - heightMarginTop.dp2px(context)) / (wordHeight + wordMargin).dp2px(context)).toInt()
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
              onSelectIndexChangeListener?.onSelectIndexChange(items[selectIndex])
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

  // 图片资源
  private var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.float_alphabet)

  private fun drawPop(canvas: Canvas?) {
    // 保存画笔颜色
    val c = paint.color

    // 绘制图片,left:图片左顶点x轴坐标；top:图片左顶点y轴坐标
    val left = 0f
    val top = (heightMarginTop + selectIndex * (wordHeight + wordMargin) + wordHeight / 2 - imgHeight / 2).dp2px(context)
    canvas?.drawBitmap(bitmap, left, top, paint)
    // 绘制文字
    paint.textSize = 20.dp2px(context)
    paint.color = popColor
    // 根据图片左顶点计算坐标。
    paint.getTextBounds(items[selectIndex], 0, 1, rect)
    val vx = rect.width()
    val vy = rect.height()
    // 文字位置:文在在图中左右比例2:3，垂直居中
    val wordX = left + (imgWidth.dp2px(context) - vx) * 0.4f
    val wordY = top + ((imgHeight.dp2px(context) + vy) / 2)
    canvas?.drawText(items[selectIndex], wordX, wordY, paint)

    //还原画笔颜色
    paint.color = c
  }

  /**
   * 设置下标改变监听
   */
  fun setOnSelectIndexChangeListener(l: OnSelectIndexChangeListener) {
    onSelectIndexChangeListener = l
  }


}


