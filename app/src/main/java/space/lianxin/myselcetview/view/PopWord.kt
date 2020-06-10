package space.lianxin.myselcetview.view

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import space.lianxin.myselcetview.R

/**
 * description:
 * @date: 2020/6/10 08:55
 * @author: lianxin
 */
class PopWord : PopupWindow {

  // 整体视图
  private var mView: View? = null
  // 文字
  private var word: String
    private get() {
      return word
    }
    set(value) {
      this.word = value
    }
  private var mTextView: TextView? = null

  constructor(context: Context?, word: String) : super(context) {

    // 初始化
    this.word = word

    // 加载视图
    val inflate: LayoutInflater =
      context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    mView = inflate.inflate(R.layout.pop_word, null)
    mTextView = mView?.findViewById(R.id.tv_pop)

    mTextView?.text = word

    // 设置视图
    contentView = mView
    width = ViewGroup.LayoutParams.WRAP_CONTENT
    height = ViewGroup.LayoutParams.WRAP_CONTENT
    isFocusable = false
    setBackgroundDrawable(BitmapDrawable())
  }


}