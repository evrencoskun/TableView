package com.evrencoskun.tableview.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by evrencoskun on 18/09/2017.
 */

public class TableViewUtils {


    /**
     * Helps to force width value before calling requestLayout by the system.
     */
    public static void setWidth(View p_jView, int p_nWidth) {
        // Change width value from params
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) p_jView.getLayoutParams();
        params.width = p_nWidth;
        p_jView.setLayoutParams(params);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(p_nWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(p_jView.getMeasuredHeight(),
                View.MeasureSpec.EXACTLY);
        p_jView.measure(widthMeasureSpec, heightMeasureSpec);

        p_jView.requestLayout();
    }

    /**
     * Gets the exact width value before the view drawing by main thread.
     */
    public static int getWidth(View p_jView) {
        p_jView.measure(LinearLayout.LayoutParams.WRAP_CONTENT, View.MeasureSpec.makeMeasureSpec
                (p_jView.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        return p_jView.getMeasuredWidth();
    }

}
