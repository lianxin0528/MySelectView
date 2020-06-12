package space.lianxin.myselcetview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.MotionEvent
import space.lianxin.myselcetview.R


/**
 * description:右侧快速索引(PopWindow实现)
 * @date: 2020/6/9 22:13
 * @author: lianxin
 */
@Deprecated("")
class SelectView @JvmOverloads constructor(
  context: Context?,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) :
  View(context, attrs, defStyle) {

  // 绘制View画笔
  private var paint: Paint = Paint()
  // 默认条目颜色
  private val defaultColor = Color.BLUE
  // 选中条目颜色
  private val selectColor = Color.RED

  // popwindow
  private var pop: PopWord = PopWord(context, null)

  // 条目宽度
  private var itemWidth: Int = 0
  // 条目高度
  private var itemHeight: Int = 0

  // 索引值
  private var items = arrayListOf(
    "A", "B", "C", "D", "E", "F", "G",
    "H", "I", "J", "K", "L", "M", "N",
    "O", "P", "Q", "R", "S", "T",
    "U", "V", "W", "X", "Y", "Z", "#"
  )
  // 当前选中索引下标，默认未选中任何索引
  private var selectIndex: Int = -1

  init {

    // 属性值
    val arr = context?.obtainStyledAttributes(attrs, R.styleable.SelectView)
    arr?.recycle()


    // 初始化画笔
    this.paint = Paint()
    // 设置画笔颜色
    paint.color = defaultColor
    // 抗锯齿
    paint.isAntiAlias = true
    // 初始化popWindow
    pop = PopWord(context, null)
  }


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
      if (selectIndex == i)
        paint.color = selectColor
      else
        paint.color = defaultColor

      // 读取条目文字，开始绘制
      val word = items[i]
      paint.textSize = 40f
      paint.getTextBounds(word, 0, 1, rect)
      // 文字宽高
      val wordWidth = rect.width()
      val wordHeight = rect.height()
      // 计算文字出现位置并绘制
      val wordX = (itemWidth / 2 - wordWidth / 2).toFloat()
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
        Log.d("lianxin", "$index")
        // 判断索引更改
        if (index != selectIndex) {
          // 更改索引下标，并重绘
          selectIndex = index
          invalidate()
          // 判断事件是否还在控件内部。
          if (selectIndex in 0 until items.size) {
            // 显示弹窗
            // 计算偏移值
            val xoff = -pop.contentView.measuredWidth
            var yoff =
              itemHeight * selectIndex + (itemHeight / 2) - (pop.contentView.measuredHeight / 2)
//            var yoff = itemHeight * selectIndex
            // 判断偏移值是否正确
            yoff = if (yoff < 100) 100 else yoff

            Log.d("lianxin", "xoff=$xoff,yoff=${yoff},event.y=${event.y}")
            if (event.action == MotionEvent.ACTION_DOWN) {
              setPopWordShowAs(items[selectIndex], xoff, yoff)
            } else {
              pop.setWord(items[selectIndex])
              pop.update(this, xoff, yoff, -1, -1)
            }
            // 接口回调
            if (onSelectIndexChangeListener != null) {
              onSelectIndexChangeListener?.onSelectIndexChange(items[selectIndex], selectIndex)
            }
          }
        }
      }
      MotionEvent.ACTION_UP -> {
        // 取消选中的下标。
        selectIndex = -1
        invalidate()
        // 取消pop显示
        if (pop.isShowing)
          pop.dismiss()
      }
    }
    return true
  }

  /**
   * @param word 需要显示的文字
   * @param xoff X轴偏移
   * @param yoff Y轴偏移
   */
  private fun setPopWordShowAs(word: String, xoff: Int, yoff: Int) {
    pop.setWord(word)
    pop.showAsDropDown(this, xoff, yoff)

  }

//  // 设置监听
//  fun setOnSelectIndexChangeListener(l: OnSelectIndexChangeListener) {
//    onSelectIndexChangeListener = l
//  }


}