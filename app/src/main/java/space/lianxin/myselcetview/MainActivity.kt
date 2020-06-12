package space.lianxin.myselcetview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import space.lianxin.myselcetview.view.OnSelectIndexChangeListener
import space.lianxin.myselcetview.view.SelectViewT

class MainActivity : AppCompatActivity() {

  // 成员控件
  private var selectView: SelectViewT? = null
  private var content: TextView? = null

  // 联系人快速索引
  private var items = arrayListOf(
    "A", "B", "C", "D", "E", "F", "G",
    "H", "I", "J", "K", "L", "M", "N",
    "O", "P", "Q", "R", "S", "T",
    "U", "V", "W", "X", "Y", "Z", "#"
  )

  // 索引改变监听
  private var indexChange = object : OnSelectIndexChangeListener {
    override fun onSelectIndexChange(word: String, index: Int) {
      content?.text = word
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // 初始化
    selectView = findViewById(R.id.sv_main_select)

//    selectView?.setItems(false, hasFriend = false, items = items)
    selectView?.setItems(true, hasFriend = false, items = items)
//    selectView?.setItems(false, hasFriend = true, items = items)
//    selectView?.setItems(true, hasFriend = true, items = items)

    content = findViewById(R.id.tv_main_content)
    selectView?.setOnSelectIndexChangeListener(indexChange)
  }


}
