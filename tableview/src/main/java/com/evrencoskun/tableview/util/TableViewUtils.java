/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableview.util;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by evrencoskun on 18/09/2017.
 */

public class TableViewUtils {


    /**
     * Helps to force width value before calling requestLayout by the system.
     */
    public static void setWidth(View view, int width) {
        // Change width value from params
        ((RecyclerView.LayoutParams) view.getLayoutParams()).width = width;

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View
                .MeasureSpec.EXACTLY);
        view.measure(widthMeasureSpec, heightMeasureSpec);

        view.requestLayout();
    }

    /**
     * Gets the exact width value before the view drawing by main thread.
     */
    public static int getWidth(View view) {
        view.measure(LinearLayout.LayoutParams.WRAP_CONTENT, View.MeasureSpec.makeMeasureSpec
                (view.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        return view.getMeasuredWidth();
    }

}
