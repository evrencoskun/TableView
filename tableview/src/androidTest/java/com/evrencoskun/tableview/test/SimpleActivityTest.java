/*
 * MIT License
 *
 * Copyright (c) 2021 Andrew Beck
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.evrencoskun.tableview.test;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.evrencoskun.tableview.*;
import com.evrencoskun.tableview.test.adapters.SimpleTestAdapter;
import com.evrencoskun.tableview.test.data.SimpleData;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SimpleActivityTest {

    @Rule
    public ActivityTestRule<TestActivity> mActivityTestRule =
            new ActivityTestRule<TestActivity>(TestActivity.class){

        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
        }

        @Override
        protected void afterActivityFinished(){
            super.afterActivityFinished();
        }
    };

    @Test
    public void testTableViewCreate(){
        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);
    }

    @Test
    public void testDefaults(){
        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertFalse(tableView.isAllowClickInsideCell());
        Assert.assertTrue(tableView.isShowHorizontalSeparators());
        Assert.assertTrue(tableView.isShowVerticalSeparators());
    }

    @Test
    public void testSetAttributes(){
        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertFalse(tableView.isAllowClickInsideCell());
        Assert.assertTrue(tableView.isShowHorizontalSeparators());
        Assert.assertTrue(tableView.isShowVerticalSeparators());
    }

    @Test
    public void testAdapterCreate(){
        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);
    }

    @Test
    public void testSmallTable() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        SimpleData simpleData = new SimpleData(5);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());


        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the row header was created as expected at 5th Row (index starts at zero)
        // cell_layout has LinearLayout as top item
        LinearLayout rowLinearLayout = (LinearLayout) tableView.getRowHeaderLayoutManager().getChildAt(4);
        Assert.assertNotNull(rowLinearLayout);
        // The first child of the LinearLayout is a textView (index starts at zero)
        TextView rowTextView = (TextView) rowLinearLayout.getChildAt(0);
        Assert.assertEquals("r:4", rowTextView.getText());

        // Check that the column header was created as expected at 5th Row (index starts at zero)
        // cell_layout has LinearLayout as top item
        LinearLayout columnLinearLayout = (LinearLayout) tableView.getColumnHeaderLayoutManager().getChildAt(4);
        Assert.assertNotNull(columnLinearLayout);
        // The first child of the LinearLayout is a textView (index starts at zero)
        TextView columnTextView = (TextView) columnLinearLayout.getChildAt(0);
        Assert.assertEquals("c:4", columnTextView.getText());

    }
}
