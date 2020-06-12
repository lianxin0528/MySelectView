package space.lianxin.myselcetview.util

import android.content.Context

/**
 * description:
 * @date: 2020/6/11 12:25
 * @author: lianxin
 */
fun Float.dp2px(context: Context): Float = this * context.resources.displayMetrics.density

fun Int.dp2px(context: Context): Float = this * context.resources.displayMetrics.density

//fun dp2px(context: Context, dp: Float): Float = dp * context.resources.displayMetrics.density

//fun px2dp(context: Context, px: Float): Float = px / context.resources.displayMetrics.density