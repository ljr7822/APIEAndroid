package com.xiaoxun.apie.common.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.utils.dpF

@SuppressLint("ResourceAsColor")
class NumberStepperView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var btnDecrease: TextView
    private var btnIncrease: TextView
    private var etNumber: EditText

    private var defaultNumber = 1 // 默认数值
    private var step = 1 // 加减步长
    private var minNumber = 1 // 最小值
    private var editTextColor = context.getColor(R.color.apieTheme_colorBlack) // 输入框文字颜色
    private var editTextSize = 15.dpF

    init {
        LayoutInflater.from(context).inflate(R.layout.number_stepper_view, this, true)

        btnDecrease = findViewById(R.id.btn_decrease)
        btnIncrease = findViewById(R.id.btn_increase)
        etNumber = findViewById(R.id.et_number)

        // 读取 XML 属性
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.NumberStepperView)
            defaultNumber = typedArray.getInt(R.styleable.NumberStepperView_defaultNumber, 1)
            step = typedArray.getInt(R.styleable.NumberStepperView_step, 1)
            editTextColor = typedArray.getColor(R.styleable.NumberStepperView_editTextColor, context.getColor(R.color.apieTheme_colorBlack))
            editTextSize = typedArray.getDimension(R.styleable.NumberStepperView_editTextSize, 15.dpF)
            minNumber = defaultNumber
            typedArray.recycle()
        }

        etNumber.setText(defaultNumber.toString())
        etNumber.setTextColor(editTextColor)
        updateDecreaseButtonState()

        // 监听按钮点击
        btnDecrease.setOnClickListener { decreaseNumber() }
        btnIncrease.setOnClickListener { increaseNumber() }
    }

    private fun decreaseNumber() {
        val currentStr = etNumber.text.toString()
        if (currentStr.isEmpty()) {
            return
        }
        val currentValue = currentStr.toInt()
        if (currentValue > minNumber) {
            val newValue = currentValue - step
            etNumber.setText(newValue.toString())
        }
        updateDecreaseButtonState()
    }

    private fun increaseNumber() {
        val currentStr = etNumber.text.toString()
        val currentValue =  if (currentStr.isEmpty()) {
            "1".toInt()
        } else {
            currentStr.toInt()
        }
        val newValue = currentValue + step
        etNumber.setText(newValue.toString())
        updateDecreaseButtonState()
    }

    private fun updateDecreaseButtonState() {
        val currentValue = etNumber.text.toString().toInt()
        btnDecrease.isEnabled = currentValue > minNumber
        btnDecrease.alpha = if (currentValue > minNumber) 1.0f else 0.5f
    }

    // 设置默认值
    fun setDefaultNumber(value: Int) {
        defaultNumber = value
        minNumber = value
        etNumber.setText(value.toString())
        updateDecreaseButtonState()
    }

    // 设置步长
    fun setStep(value: Int) {
        step = value
    }

    // 获取当前值
    fun getNumber(): Int {
        return etNumber.text.toString().toInt()
    }
}
