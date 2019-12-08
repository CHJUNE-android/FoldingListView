package com.liblary.foldinglistview.animation

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.Transformation

class FoldAnimation(
    private val mFoldMode: FoldAnimationMode,
    private val mCameraHeight: Int,
    duration: Long
) :
    Animation() {
    private var mFromDegrees: Float = 0.toFloat()
    private var mToDegrees: Float = 0.toFloat()
    private var mCenterX: Float = 0.toFloat()
    private var mCenterY: Float = 0.toFloat()
    private var mCamera: Camera? = null

    enum class FoldAnimationMode {//열겨형 클래스
        FOLD_UP, UNFOLD_DOWN, FOLD_DOWN, UNFOLD_UP
    }

    init {
        this.fillAfter = true
        this.duration = duration
    }

    fun withAnimationListener(animationListener: Animation.AnimationListener): FoldAnimation {
        this.setAnimationListener(animationListener)
        return this
    }

    fun withStartOffset(offset: Int): FoldAnimation {
        this.startOffset = offset.toLong()
        return this
    }

    fun withInterpolator(interpolator: Interpolator?): FoldAnimation {//Interpolator : 시작부터 종료까지의 표현방법
        if (interpolator != null) {
            this.interpolator = interpolator
        }
        return this
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        this.mCamera = Camera()
        mCamera!!.setLocation(0f, 0f, (-mCameraHeight).toFloat())

        this.mCenterX = (width / 2).toFloat()
        when (mFoldMode) {//해당 모드에 맞게 실행. : 각자 어떤모드인지 공부 필요
            FoldAnimationMode.FOLD_UP -> {
                this.mCenterY = 0f
                this.mFromDegrees = 0f
                this.mToDegrees = 90f
            }
            FoldAnimationMode.FOLD_DOWN -> {
                this.mCenterY = height.toFloat()
                this.mFromDegrees = 0f
                this.mToDegrees = -90f
            }
            FoldAnimationMode.UNFOLD_UP -> {
                this.mCenterY = height.toFloat()
                this.mFromDegrees = -90f
                this.mToDegrees = 0f
            }
            FoldAnimationMode.UNFOLD_DOWN -> {
                this.mCenterY = 0f
                this.mFromDegrees = 90f
                this.mToDegrees = 0f
            }
            else -> throw IllegalStateException("Unknown animation mode.")
        }
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val camera = mCamera//카메라 뭐하는 놈인지 공부 필요
        val matrix = t.matrix//매트릭스도 공부 필요
        val fromDegrees = mFromDegrees
        val degrees = fromDegrees + (mToDegrees - fromDegrees) * interpolatedTime

        camera!!.save()
        camera.rotateX(degrees)
        camera.getMatrix(matrix)
        camera.restore()

        matrix.preTranslate(-mCenterX, -mCenterY)
        matrix.postTranslate(mCenterX, mCenterY)
    }

    override fun toString(): String {
        return "FoldAnimation{" +
                "mFoldMode=" + mFoldMode +
                ", mFromDegrees=" + mFromDegrees +
                ", mToDegrees=" + mToDegrees +
                '}'.toString()
    }

}
