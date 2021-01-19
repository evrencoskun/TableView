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
import android.view.View;
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
public class CornerViewTest {

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
    public void testEmptyTable() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        SimpleData simpleData = new SimpleData(0);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is not created (therefore not shown)
        View cornerView = tableView.getAdapter().getCornerView();
        Assert.assertNull(cornerView);
    }

    @Test
    public void testEmptyTableResetNonEmptyTable() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        SimpleData simpleData = new SimpleData(0);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        // Check that the corner view is not created (therefore not shown)
        View cornerView = tableView.getAdapter().getCornerView();
        Assert.assertNull(cornerView);

        // Change the items of data to reset
        SimpleData simpleDataReset = new SimpleData(2);
        simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                simpleDataReset.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is now created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerViewReset = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerViewReset);
        // Check the corner view is now visible
        Assert.assertEquals(View.VISIBLE, cornerViewReset.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the LinearLayout is a textView (index starts at zero)
        TextView cornerViewResetTextView = (TextView) cornerViewReset.getChildAt(0);
        Assert.assertEquals("Cell Data", cornerViewResetTextView.getText());
    }

    @Test
    public void testEmptyTableResetEmptyTable() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        SimpleData simpleData = new SimpleData(0);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        // Check that the corner view is not created (therefore not shown)
        View cornerView = tableView.getAdapter().getCornerView();
        Assert.assertNull(cornerView);

        // Change the items of data to reset
        SimpleData simpleDataReset = new SimpleData(0);
        simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                simpleDataReset.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is still not created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerViewReset = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNull(cornerViewReset);
    }

    @Test
    public void testNonEmptyTable() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        SimpleData simpleData = new SimpleData(1);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerView = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the LinearLayout is a textView (index starts at zero)
        TextView cornerViewTextView = (TextView) cornerView.getChildAt(0);
        Assert.assertEquals("Cell Data", cornerViewTextView.getText());
    }

    @Test
    public void testNonEmptyTableResetNonEmpty() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        SimpleData simpleData = new SimpleData(1);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        // Check that the corner view is created before resetting to empty
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerView = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());

        // Change the items of data to reset
        SimpleData simpleDataReset = new SimpleData(2);
        simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                simpleDataReset.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is still created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerViewReset = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerViewReset);
        // Check the corner view is still visible
        Assert.assertEquals(View.VISIBLE, cornerViewReset.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the LinearLayout is a textView (index starts at zero)
        TextView cornerViewResetTextView = (TextView) cornerViewReset.getChildAt(0);
        Assert.assertEquals("Cell Data", cornerViewResetTextView.getText());
    }

    @Test
    public void testNonEmptyTableResetEmpty() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        SimpleData simpleData = new SimpleData(1);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        // Check that the corner view is created before resetting to empty
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerView = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);

        // Change the items of data to reset
        SimpleData simpleDataReset = new SimpleData(0);
        simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                simpleDataReset.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is still created but visibility is gone
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerViewReset = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerViewReset);
        // Check the corner view visibility is GONE
        Assert.assertEquals(View.GONE, cornerViewReset.getVisibility());
    }

    @Test
    public void testColumnHeadersOnlyTable() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        // Only want column headers
        SimpleData simpleData = new SimpleData(5, 0);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is not created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerView = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNull(cornerView);
    }

    @Test
    public void testColumnHeadersOnlyTableResetNonEmptyTable() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        // Only want column headers
        SimpleData simpleData = new SimpleData(5, 0);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        // Check that the corner view is not created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerView = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNull(cornerView);

        // Change the items of data to reset
        SimpleData simpleDataReset = new SimpleData(5);
        simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                simpleDataReset.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is not created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerViewReset = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerViewReset);
        // Check the corner view is now visible
        Assert.assertEquals(View.VISIBLE, cornerViewReset.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the LinearLayout is a textView (index starts at zero)
        TextView cornerViewResetTextView = (TextView) cornerViewReset.getChildAt(0);
        Assert.assertEquals("Cell Data", cornerViewResetTextView.getText());
    }

    @Test
    public void testColumnHeadersOnlyTableResetEmptyTable() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        // Only want column headers
        SimpleData simpleData = new SimpleData(5, 0);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        // Check that the corner view is not created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerView = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNull(cornerView);

        // Change the items of data to reset
        SimpleData simpleDataReset = new SimpleData(0);
        simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                simpleDataReset.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is not created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerViewReset = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNull(cornerViewReset);
    }

    @Test
    public void testColumnHeadersOnlyTableShowCorner() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        // Set the option to show corner view when there is not row data
        tableView.setShowCornerView(true);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        // Only want column headers
        SimpleData simpleData = new SimpleData(5, 0);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerView = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the LinearLayout is a textView (index starts at zero)
        TextView cornerViewResetTextView = (TextView) cornerView.getChildAt(0);
        Assert.assertEquals("Cell Data", cornerViewResetTextView.getText());
    }

    @Test
    public void testColumnHeadersOnlyTableShowCornerResetEmptyTable() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        // Set the option to show corner view when there is not row data
        tableView.setShowCornerView(true);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        // Only want column headers
        SimpleData simpleData = new SimpleData(5, 0);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerView = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);

        // Change the items of data to reset
        SimpleData simpleDataReset = new SimpleData(0);
        simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                simpleDataReset.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is still created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerViewReset = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerViewReset);
        // Check the corner view visibility is GONE
        Assert.assertEquals(View.GONE, cornerViewReset.getVisibility());
    }

    @Test
    public void testColumnHeadersOnlyTableShowCornerResetNonEmptyTable() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertNotNull(tableView);

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        // Set the option to show corner view when there is not row data
        tableView.setShowCornerView(true);

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        // Only want column headers
        SimpleData simpleData = new SimpleData(5, 0);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerView = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);

        // Change the items of data to reset
        SimpleData simpleDataReset = new SimpleData(2);
        simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                simpleDataReset.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is still created
        // The Corner view uses cell_layout which has LinearLayout as top item
        LinearLayout cornerViewReset = (LinearLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerViewReset);
        // Check the corner view visibility is VISIBLE
        Assert.assertEquals(View.VISIBLE, cornerViewReset.getVisibility());
    }
}
