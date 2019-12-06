package com.liblary.foldinglistview.fold

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.widget.RelativeLayout

class FoldingCellView : RelativeLayout {//애니메이션 진행할때 필요한 뷰
    var backView: View? = null
    var frontView: View? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        this.layoutParams = layoutParams
        this.clipToPadding = false
        this.clipChildren = false
    }

    constructor(frontView: View?, backView: View, context: Context) : super(context) {
        this.frontView = frontView
        this.backView = backView

        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        this.clipToPadding = false
        this.clipChildren = false

        if (this.backView != null) {
            this.addView(this.backView)
            val mBackViewParams = this.backView!!.layoutParams as LayoutParams
            mBackViewParams.addRule(ALIGN_PARENT_BOTTOM)
            this.backView!!.layoutParams = mBackViewParams
            layoutParams.height = mBackViewParams.height
        }

        if (this.frontView != null) {
            this.addView(this.frontView)
            val frontViewLayoutParams = this.frontView!!.layoutParams as LayoutParams
            frontViewLayoutParams.addRule(ALIGN_PARENT_BOTTOM)
            this.frontView!!.layoutParams = frontViewLayoutParams
        }

        this.layoutParams = layoutParams
    }

    fun withFrontView(frontView: View): FoldingCellView {
        this.frontView = frontView

        if (this.frontView != null) {
            this.addView(this.frontView)
            val frontViewLayoutParams = this.frontView!!.layoutParams as LayoutParams
            frontViewLayoutParams.addRule(ALIGN_PARENT_BOTTOM)
            this.frontView!!.layoutParams = frontViewLayoutParams
        }
        return this
    }

    fun withBackView(backView: View): FoldingCellView {
        this.backView = backView

        if (this.backView != null) {
            this.addView(this.backView)
            val mBackViewParams = this.backView!!.layoutParams as LayoutParams
            mBackViewParams.addRule(ALIGN_PARENT_BOTTOM)
            this.backView!!.layoutParams = mBackViewParams

            val layoutParams = this.layoutParams as LayoutParams
            layoutParams.height = mBackViewParams.height
            this.layoutParams = layoutParams
        }

        return this
    }

    fun animateFrontView(animation: Animation) {
        if (this.frontView != null)
            frontView!!.startAnimation(animation)
    }

}
