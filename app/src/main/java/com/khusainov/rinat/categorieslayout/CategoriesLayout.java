package com.khusainov.rinat.categorieslayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CategoriesLayout extends ViewGroup {

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
            final int childL = offset + l + layoutParams.leftMargin;
            final int childT = t + layoutParams.topMargin;
            final int childR = child.getMeasuredWidth() + offset + layoutParams.rightMargin + layoutParams.leftMargin;
            final int childB = child.getMeasuredHeight() + offset + layoutParams.bottomMargin + layoutParams.topMargin;

            if (childR < r) {
                child.layout(childL, childT, childR, childB);
                offset = childR;
            } else {
                return;
            }
        }
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
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
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
}

