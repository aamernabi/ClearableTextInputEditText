package com.aamernabi

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.design.widget.TextInputEditText
import android.support.v4.content.res.ResourcesCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View.OnTouchListener

/**
 * ClearableTextInputEditTextExample
 * Created by EResolute on 10/2/2018.
 */
class ClearableTextInputEditText : TextInputEditText {

    private var mClearButton: Drawable? = null
    @DrawableRes private val defaultDrawable = R.drawable.ic_clear_grey

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearableTextInputEditText, defStyleAttr, 0)
        typedArray?.let { it ->
            if (it.hasValue(R.styleable.ClearableTextInputEditText_clearDrawable)) {
                mClearButton = it.getDrawable(R.styleable.ClearableTextInputEditText_clearDrawable)
                mClearButton?.let {
                    it.callback = this
                }
            }
        }
        typedArray.recycle()

        attrs?.let {
            mClearButton = ResourcesCompat.getDrawable(resources, defaultDrawable, null)
        }
        setOnTouchListener(OnTouchListener { _, event ->
            if (compoundDrawablesRelative[2] != null) {
                var clearButtonStart: Float // Used for LTR languages
                var clearButtonEnd: Float  // Used for RTL languages
                var isClearButtonClicked = false
                mClearButton?.let {
                    if (layoutDirection == LAYOUT_DIRECTION_RTL) {
                        clearButtonEnd = (it.intrinsicWidth + paddingStart).toFloat()
                        if (event.x < clearButtonEnd) {
                            isClearButtonClicked = true
                        }
                    } else {
                        clearButtonStart = (width - paddingEnd - it.intrinsicWidth).toFloat()
                        if (event.x > clearButtonStart) {
                            isClearButtonClicked = true
                        }
                    }

                    if (isClearButtonClicked) {
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            mClearButton = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear_black, null)
                            showClearButton()
                        }
                        if (event.action == MotionEvent.ACTION_UP) {
                            mClearButton = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear_grey, null)
                            text?.clear()
                            hideClearButton()
                            return@OnTouchListener true
                        }
                    } else {
                        return@OnTouchListener false
                    }
                }
            }
            false
        })

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if(it.isNotEmpty()) {
                        showClearButton()
                    } else {
                        hideClearButton()
                    }
                } ?: hideClearButton()
            }

        })
    }

    private fun showClearButton () {
        if (mClearButton != null) {
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, mClearButton, null)
        } else {
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, defaultDrawable, 0)
        }
    }

    private fun hideClearButton () {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
    }
}