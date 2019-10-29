package com.khusainov.rinat.categorieslayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CategoriesLayout extends ViewGroup {

    private final GestureDetector mGestureDetector = new GestureDetector(getContext(), new CategoriesGestureDetector());

    private static final String TAG = "CategoriesLayout";

    private int mStartPosition = 0;

    public CategoriesLayout(Context context) {
        super(context);
    }

    public CategoriesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCategories(@NonNull List<Category> categories) {
        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        for (Category category : categories) {
            final TextView view = (TextView) layoutInflater.inflate(R.layout.category_view, this, false);
            view.setText(String.valueOf(category.getSum()));
            addView(view);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int requestedWidth = 0;
        int requestedHeight = 0;
        int childState = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                final ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                requestedHeight = Math.max(requestedHeight, child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                requestedWidth += child.getMeasuredWidth() + layoutParams.rightMargin + layoutParams.leftMargin;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }
        setMeasuredDimension(resolveSizeAndState(requestedWidth, widthMeasureSpec, childState),
                resolveSizeAndState(requestedHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int offset = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            final int childL = offset + layoutParams.leftMargin;
            final int childT = layoutParams.topMargin;
            final int childR = child.getMeasuredWidth() + offset + layoutParams.rightMargin + layoutParams.leftMargin;
            final int childB = child.getMeasuredHeight() + layoutParams.bottomMargin + layoutParams.topMargin;
            if (i < mStartPosition || childR > getWidth()) {
                child.layout(0, 0, 0, 0);
            } else {
                child.layout(childL, childT, childR, childB);
                offset = childR;
            }
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * Any layout manager that doesn't scroll will want this.
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    private void moveRight() {
        mStartPosition++;
        if (mStartPosition >= getChildCount()) {
            mStartPosition = getChildCount() - 1;
        }
        requestLayout();
    }

    private void moveLeft() {
        mStartPosition--;
        if (mStartPosition < 0) {
            mStartPosition = 0;
        }
        requestLayout();
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    private class CategoriesGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "onFling() called with: e1 = [" + e1 + "], e2 = [" + e2 + "], velocityX = [" + velocityX + "], velocityY = [" + velocityY + "]");
            final float delta = e2.getX() - e1.getX();
            if (delta < 0) {
                moveRight();
            } else {
                moveLeft();
            }
            return true;
        }
    }
}

