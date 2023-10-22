package com.example.mystoryapps.view.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.mystoryapps.R
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText: TextInputEditText{
    private lateinit var passwordIcon: Drawable

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private fun init(){
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_password) as Drawable
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 16

        setHint(R.string.password)
        setButtonDrawables(startOfTheText = passwordIcon)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    error = resources.getString(R.string.password_validation)
                } else {
                    error = null
                }
            }
        })
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
}