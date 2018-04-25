package com.spacebitlabs.plantly.addplant

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.content.Context
import android.support.transition.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.dpToPixels
import kotlinx.android.synthetic.main.view_expandable_input.view.*


/**
 * Created by afzal on 2018-01-01.
 */
class ExpandableInputView : FrameLayout, View.OnClickListener {
    private var isExpanded = false
    private var isAnimating = false

    private var labelText = ""
    private var valueText = ""

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_expandable_input, this)

        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ExpandableInputView)

        labelText = styledAttrs.getString(R.styleable.ExpandableInputView_label)
        valueText = styledAttrs.getString(R.styleable.ExpandableInputView_value)

        setAttrs()

        styledAttrs.recycle()

        layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        setOnClickListener(this)
    }

    private fun setAttrs() {
        label.text = labelText
        setValue(valueText)
    }

    fun setValue(value: String) {
        collapsed_value.setText(value, TextView.BufferType.NORMAL)
        expanded_value.setText(value, TextView.BufferType.NORMAL)
    }

    override fun onClick(v: View?) {
        if (!isAnimating) {
            toggle()
        }
    }

    private fun toggle() = if (isExpanded) collapse() else expand()

    private fun expand() {
        isAnimating = true
        isExpanded = true
        val transition = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(AutoTransition())
            .addListener(endTransition)

        // have to animate translationZ separately because
        // beginDelayedTransitions doesn't work with it
        val zAnimator = ObjectAnimator.ofFloat(this, "translationZ", 8f)
        zAnimator.start()

        val lp = container.layoutParams
        lp.height = dpToPixels(108)
        container.layoutParams = lp

        val marginLp = layoutParams as MarginLayoutParams
        marginLp.topMargin = dpToPixels(16)
        marginLp.bottomMargin = dpToPixels(16)
        layoutParams = marginLp

        collapsed_value.visibility = View.GONE
        expanded_value.visibility = View.VISIBLE

        TransitionManager.beginDelayedTransition(this, transition)
    }

    private fun collapse() {
        isAnimating = true
        isExpanded = false
        val transition = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(AutoTransition())
            .addListener(endTransition)

        // have to animate translationZ separately because
        // beginDelayedTransitions doesn't work with it
        val zAnimator = ObjectAnimator.ofFloat(this, "translationZ", 0f)
        zAnimator.start()

        val lp = container.layoutParams
        lp.height = dpToPixels(56)
        container.layoutParams = lp

        val marginLp = layoutParams as MarginLayoutParams
        marginLp.topMargin = dpToPixels(0)
        marginLp.bottomMargin = dpToPixels(0)
        layoutParams = marginLp

        collapsed_value.visibility = View.VISIBLE
        expanded_value.visibility = View.GONE

        TransitionManager.beginDelayedTransition(this, transition)
    }

    private val endTransition = object : TransitionListenerAdapter() {

        override fun onTransitionStart(transition: Transition) {
            super.onTransitionStart(transition)
            // it's collapsing so switch views before animation
//            if (!isExpanded) {
//                collapsed_value.visibility = View.VISIBLE
////                expanded_value.visibility = View.GONE
//                collapsed_value.animate().alpha(1f)
//                expanded_value.animate().alpha(0f)
//            } else {
////                collapsed_value.visibility = View.GONE
//                expanded_value.visibility = View.VISIBLE
//                collapsed_value.animate().alpha(0f)
//                expanded_value.animate().alpha(1f)
//            }
        }

        override fun onTransitionEnd(transition: Transition) {
            isAnimating = false
            // it's expanding so switch views after animation
//            if (isExpanded) {
//                expanded_value.visibility = View.VISIBLE
//                collapsed_value.animate().alpha(0f)
//                expanded_value.animate().alpha(1f)
////                collapsed_value.visibility = View.GONE
//            }
        }
    }
}