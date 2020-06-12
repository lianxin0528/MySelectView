package space.lianxin.myselcetview.view

/**
 * description: 索引值发生改变回调。
 * @date: 2020/6/9 23:40
 * @author: lianxin
 */
interface OnSelectIndexChangeListener {

  /**
   * @param word 当前选中的索引值
   * @param index 快速索引的下标，包含lately(最近联系)和friend(好友)。
   */
  fun onSelectIndexChange(word: String, index: Int)

}