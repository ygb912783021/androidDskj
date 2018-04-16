package dingshi.com.hibook.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.ViewPropertyAnimation;

import dingshi.com.hibook.utils.PixelUtils;

/**
 * Created by apple on 2017/10/30.
 */

public class MyBehavior extends CoordinatorLayout.Behavior<ViewGroup> {

    Context context;

    public MyBehavior() {
    }

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        childHeight = PixelUtils.dip2Px(context, 150);
    }

//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
//        return dependency instanceof Button;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
//        child.setX(dependency.getX() + 200);
//        child.setY(dependency.getY() + 200);
//        child.setText(dependency.getX() + "," + dependency.getY());
//        return true;
//    }

    /**
     * 累计滑动的所有距离
     */
    int addValue;

    boolean flag = true;

    int childHeight;
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    //1.判断滑动的方向 我们需要垂直滑动
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, ViewGroup child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, ViewGroup child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);

        if (dy > 0 && flag && child.getMeasuredHeight() != PixelUtils.dip2Px(context, 50)) {
            anim(child, childHeight, PixelUtils.dip2Px(context, 50));
            flag = false;
        } else if (dy < 0 && flag && child.getMeasuredHeight() != childHeight) {
            flag = false;
            anim(child, child.getMeasuredHeight(), childHeight);
        }
    }

    int mProgress;
    ValueAnimator animator;

    public void anim(final ViewGroup child, int start, int end) {
        animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams param = child.getLayoutParams();
                param.height = mProgress;
                child.setLayoutParams(param);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                flag = true;
                animation.cancel();
                addValue = 0;
                Log.i("tag", "cancel---->");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(500);
        animator.start();
    }

}
