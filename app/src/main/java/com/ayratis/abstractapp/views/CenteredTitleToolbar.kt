package com.ayratis.abstractapp.views


import android.content.Context
import android.graphics.Point
import androidx.appcompat.widget.Toolbar
import android.util.AttributeSet
import android.view.WindowManager
import android.widget.TextView
import com.ayratis.abstractapp.R


class CenteredTitleToolbar : Toolbar {

    private var _titleTextView: TextView? = null
    private var _screenWidth: Int = 0
    private var _centerTitle = true

    private val screenSize: Point
        get() {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val screenSize = Point()
            display.getSize(screenSize)

            return screenSize
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        _screenWidth = screenSize.x

        _titleTextView = TextView(context)
        _titleTextView?.setTextAppearance(context, R.style.ToolbarTitleText)
        _titleTextView?.text = ""
        addView(_titleTextView)
    }

    override protected fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (_centerTitle) {
            val location = IntArray(2)
            _titleTextView?.getLocationOnScreen(location)
            _titleTextView?.let {
                it.translationX = it.translationX + (-location[0] + _screenWidth / 2 - it.width / 2)
            }
        }
    }

    override fun setTitle(title: CharSequence) {
        _titleTextView?.text = title
        requestLayout()
    }

    override fun setTitle(titleRes: Int) {
        _titleTextView?.setText(titleRes)
        requestLayout()
    }

    fun setTitleCentered(centered: Boolean) {
        _centerTitle = centered
        requestLayout()
    }

    override fun setTitleTextColor(color: Int) {
        super.setTitleTextColor(color)
        if(_titleTextView!= null){
            _titleTextView?.setTextColor(color)
        }
    }
}
