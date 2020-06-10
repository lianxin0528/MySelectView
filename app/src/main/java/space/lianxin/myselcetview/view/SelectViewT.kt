package space.lianxin.myselcetview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent

/**
 * description:右侧快速索引（onDraw实现）
 * 弹窗和文字占比2:1
 * @date: 2020/6/9 22:13
 * @author: lianxin
 */
class SelectViewT constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) :
  View(context, attrs, defStyle) {

  constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
    // 初始化画笔
    paint = Paint()
    // 设置画笔颜色
    paint.color = defaultColor
    // 抗锯齿
    paint.isAntiAlias = true
  }

  constructor(context: Context?) : this(context, null)

  // 绘制View画笔
  private var paint: Paint = Paint()
  // 默认条目颜色
  private val defaultColor = Color.BLUE
  // 选中条目颜色
  private val selectColor = Color.RED
  // 圆背景色
  private val circleColor = Color.RED
  // 圆上字的颜色
  private val circlrTextColor = Color.WHITE

  // 条目宽度
  private var itemWidth: Int = 0
  // 条目高度
  private var itemHeight: Int = 0

  // 索引值
  private var items = arrayListOf(
    "A", "B", "C", "D", "E", "F", "G",
    "H", "I", "J", "K", "L", "M", "N",
    "O", "P", "Q", "R", "S", "T",
    "U", "V", "W", "X", "Y", "Z"
  )
  // 当前选中索引下标，默认未选中任何索引
  private var selectIndex: Int = -1

  // 设置索引改变监听
  private var onSelectIndexChangeListener: OnSelectIndexChangeListener? = null

  /**
   * 视图测量，根据测量结果，算出每个item需要的宽高。
   */
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    // 计算条目所需要的宽度
//    itemWidth = MeasureSpec.getSize(widthMeasureSpec)
    itemWidth = measuredWidth

    // 计算条目所需要的高度
//    itemHeight = MeasureSpec.getSize(heightMeasureSpec) / items.size
    itemHeight = measuredHeight / items.size

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
        // 计算圆心位置,半径
        val cx = itemWidth / 3f
        val cy = (itemHeight * selectIndex + (itemHeight / 2)).toFloat()
        val r = itemWidth * 0.3f
        drawCircle(canvas, cx, cy, r)
      } else {
        paint.color = defaultColor
      }

      // 读取条目文字，开始绘制
      val word = items[i]
      paint.textSize = 40f
      paint.getTextBounds(word, 0, 1, rect)
      // 文字宽高
      val wordWidth = rect.width()
      val wordHeight = rect.height()
      // 计算文字出现位置并绘制
      val wordX = itemWidth * 0.835f - (wordWidth / 2)
      val wordY = (itemHeight / 2 + wordHeight / 2 + i * itemHeight).toFloat()
      canvas?.drawText(word, wordX, wordY, paint)
    }

  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent?): Boolean {
    super.onTouchEvent(event)

    // 判断事件
    when (event?.action) {
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
        // 根据Y轴坐标计算选中的索引值
        val index = (event.y / itemHeight).toInt()
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


  private fun drawCircle(canvas: Canvas?, cx: Float, cy: Float, r: Float) {
    // 保存画笔颜色
    val saveColor = paint.color
    paint.color = circleColor

    // 画实心圆
    paint.style = Paint.Style.FILL
    canvas?.drawCircle(cx, cy, r, paint)

    // 画实心圆上的文字
    paint.color = circlrTextColor
    paint.textSize = 60f
    paint.getTextBounds(items[selectIndex], 0, 1, rect)
    // 计算文字位置
    val wordX = cx - (rect.width() / 2)
    val wordY = cy + (rect.height() / 2)
    canvas?.drawText(items[selectIndex], wordX, wordY, paint)
    // 还原画笔颜色
    paint.color = saveColor
  }

  /**
   * 设置下标改变监听
   */
  fun setOnSelectIndexChangeListener(l: OnSelectIndexChangeListener) {
    onSelectIndexChangeListener = l
  }


}


