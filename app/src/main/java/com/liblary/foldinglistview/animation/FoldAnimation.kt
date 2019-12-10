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
        //애니메이션 관련 블로그
        //http://m.blog.daum.net/creazier/15311330?np_nil_b=1
        val camera = mCamera//뷰를 사용자에게 보여주는 역할. 카메라가 돌면 사용자 입장에서 회전하는걸로 보여지게된다.
        val matrix = t.matrix//매트릭스도 공부 필요
        val fromDegrees = mFromDegrees
        val degrees = fromDegrees + (mToDegrees - fromDegrees) * interpolatedTime

        camera!!.save()
        camera.rotateX(degrees)
        camera.getMatrix(matrix)
        camera.restore()

        matrix.preTranslate(-mCenterX, -mCenterY)//변화하기 전
        matrix.postTranslate(mCenterX, mCenterY)//변화 후
    }

    override fun toString(): String {
        return "FoldAnimation{" +
                "mFoldMode=" + mFoldMode +
                ", mFromDegrees=" + mFromDegrees +
                ", mToDegrees=" + mToDegrees +
                '}'.toString()
    }

}
