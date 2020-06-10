package space.lianxin.myselcetview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import space.lianxin.myselcetview.view.OnSelectIndexChangeListener
import space.lianxin.myselcetview.view.SelectView

class MainActivity : AppCompatActivity() {

  // 成员控件
  private var selectView: SelectView? = null
  private var content: TextView? = null

  // 索引改变监听
  private var indexChange = object : OnSelectIndexChangeListener {
    override fun onSelectIndexChange(word: String) {
      content?.text = word
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // 初始化
    selectView = findViewById(R.id.sv_main_select)
    content = findViewById(R.id.tv_main_content)
    selectView?.setOnSelectIndexChangeListener(indexChange)
  }


}
