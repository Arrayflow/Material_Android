package com.example.materialtest

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.awt.font.NumericShaper
import java.time.Duration

fun main() {
    println(max(33.33, 44.1, 55.41, 330.0))
}

//定义一个可接受不定参数的max函数，利用Comparable<T>指定为T的泛型上限
fun <T : Comparable<T>> max(vararg nums: T): T {
    if (nums.isEmpty()) {
        throw Exception("Parmas can not be empty")
    }
    var maxNum = nums[0]
    for (num in nums) {
        if (num > maxNum) {
            maxNum = num
        }
    }
    return maxNum
}

//Snackbar的高阶写法
fun View.showSnackbar(text: String, actionText: String? = null,
                      duration: Int = Snackbar.LENGTH_SHORT,
                      block: (() -> Unit) ?= null) {
    val snackbar = Snackbar.make(this, text, duration)
    if (actionText != null && block != null) {
        snackbar.setAction(actionText) {
            block()
        }
    }
    snackbar.show()
}