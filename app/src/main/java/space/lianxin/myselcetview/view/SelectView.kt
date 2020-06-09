package space.lianxin.myselcetview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MotionEvent


/**
 * description:右侧快速索引
 * @date: 2020/6/9 22:13
 * @author: lianxin
 */
class SelectView constructor(context: Context?, attrs: AttributeSet?, defStyle: Int)
  : View(context, attrs, defStyle) {

  constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
    // 初始化画笔
    paint = Paint()
    // 设置画笔颜色
    paint.color = defaultColor
    // 抗锯齿
    paint.isAntiAlias = true
  }

  constructor(context: Context?) : this(context, null) {

  }

  // 绘制View画笔
  private var paint: Paint = Paint()
  // 默认条目颜色
  private val defaultColor = Color.BLUE
  // 选中条目颜色
  private val selectColor = Color.RED


  // 条目宽度
  private var itemWidth: Int = 0
  // 条目高度
  private var itemHeight: Int = 0

  // 索引值
  private var items = arrayListOf(
    "A", "B", "C", "D", "E", "F", "G",
    "H", "I", "J", "K", "L", "M", "N",
    "O", "P", "Q", "R", "S", "T",
    "U", "V", "W", "X", "Y", "Z")
  // 当前选中索引下标，默认未选中任何索引
  private var selectIndex: Int = -1

  // 设置索引改变监听
  private var onSelectIndexChangeListener: OnSelectIndexChangeListener?
    set(value) {
      this.onSelectIndexChangeListener = value
    }
    private get() = onSelectIndexChangeListener


  /**
   * 视图测量，根据测量结果，算出每个item需要的宽高。
   */
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    // 计算条目所需要的宽度
    itemWidth = measuredWidth

    // 计算条目所需要的高度
    itemHeight = measuredHeight / items.size

  }

  /**
   * 绘制view
   */
  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    // 绘制每个条目
    for (i in 1..items.size) {

      // 判断下标命中
      if (selectIndex == i - 1)
        paint.color = selectColor
      else
        paint.color = defaultColor

      // 读取条目文字，开始绘制
      var word = items[i - 1]
      var rect = Rect()
      paint.getTextBounds(word, 0, 1, rect)
      // 文字宽高
      var wordWidth = rect.width()
      var wordHeight = rect.height()
      // 计算文字出现位置并绘制
      val wordX = (itemWidth / 2 - wordWidth / 2).toFloat()
      val wordY = (itemHeight / 2 + wordHeight / 2 + i * itemHeight).toFloat()
      canvas?.drawText(word, wordX, wordY, paint)
    }

  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    super.onTouchEvent(event)
    // 判断事件
    when (event?.action) {
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
        // 根据Y轴坐标计算选中的索引值
        var index = (event.y / itemHeight).toInt()
        // 判断索引更改
        if (index != selectIndex) {
          // 更改索引下标，并重绘
          selectIndex = index
          invalidate()
          // 接口回调
          if (onSelectIndexChangeListener != null) {
            onSelectIndexChangeListener?.onSelectIndexChange(items[selectIndex])
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


}