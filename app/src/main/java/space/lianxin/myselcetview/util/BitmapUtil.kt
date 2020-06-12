package space.lianxin.myselcetview.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * description:
 * @date: 2020/6/11 23:39
 * @author: lianxin
 */
fun getBitmapFromDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap {
  val drawable = ContextCompat.getDrawable(context, drawableId)

  return if (drawable is BitmapDrawable) {
    drawable.bitmap
  } else if (drawable is VectorDrawable || drawable is VectorDrawableCompat) {
    val bitmap = Bitmap.createBitmap(
      drawable.intrinsicWidth,
      drawable.intrinsicHeight,
      Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    bitmap
  } else {
    throw IllegalArgumentException("unsupported drawable type")
  }
}