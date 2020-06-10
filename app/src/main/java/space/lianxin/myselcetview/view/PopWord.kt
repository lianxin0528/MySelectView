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
@Suppress("DEPRECATION")
class PopWord constructor(context: Context?, word: String?) : PopupWindow(context) {

  // 整体视图
  private var mView: View? = null

  // 文字
  private var word: String? = null
  private var mTextView: TextView? = null

  fun setWord(word: String?) {
    this.word = word
    mTextView?.text = word
  }

  init {

    // 加载视图
    val inflate: LayoutInflater =
      context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    mView = inflate.inflate(R.layout.pop_word, null)
    mView?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    mTextView = mView?.findViewById(R.id.tv_pop)
    setWord(word)

    // 设置视图
    contentView = mView
    width = ViewGroup.LayoutParams.WRAP_CONTENT
    height = ViewGroup.LayoutParams.WRAP_CONTENT
    isFocusable = false
    setBackgroundDrawable(BitmapDrawable())
  }


}