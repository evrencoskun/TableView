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

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.test.adapters.SimpleTestAdapter;
import com.evrencoskun.tableview.test.data.SimpleData;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CornerViewTest {

    @Rule
    public ActivityScenarioRule<TestActivity> mActivityTestRule =
            new ActivityScenarioRule<>(TestActivity.class);

    @Test
    public void testEmptyTable() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    SimpleData simpleData = new SimpleData(0);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is not created (therefore not shown)
                    View cornerView = simpleTestAdapter.getCornerView();
                    Assert.assertNull(cornerView);
                });
    }

    @Test
    public void testEmptyTableResetNonEmptyTable() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    SimpleData simpleData = new SimpleData(0);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    // Check that the corner view is not created (therefore not shown)
                    View cornerView = simpleTestAdapter.getCornerView();
                    Assert.assertNull(cornerView);

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(2);
                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    // Check that the corner view is now created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerViewReset = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerViewReset);

                    // Check the corner view is now visible
                    Assert.assertEquals(View.VISIBLE, cornerViewReset.getVisibility());

                    // Check that it is the expected corner view by checking the text
                    // The first child of the LinearLayout is a textView (index starts at zero)
                    TextView cornerViewResetTextView = (TextView) cornerViewReset.getChildAt(0);
                    Assert.assertEquals("Cell Data", cornerViewResetTextView.getText());
                });
    }

    @Test
    public void testEmptyTableResetEmptyTable() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    SimpleData simpleData = new SimpleData(0);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is not created (therefore not shown)
                    View cornerView = simpleTestAdapter.getCornerView();
                    Assert.assertNull(cornerView);

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(0);
                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    // Check that the corner view is still not created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerViewReset = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNull(cornerViewReset);
                });
    }

    @Test
    public void testNonEmptyTable() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    SimpleData simpleData = new SimpleData(1);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerView = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerView);

                    // Check the corner view is visible
                    Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());

                    // Check that it is the expected corner view by checking the text
                    // The first child of the LinearLayout is a textView (index starts at zero)
                    TextView cornerViewTextView = (TextView) cornerView.getChildAt(0);
                    Assert.assertEquals("Cell Data", cornerViewTextView.getText());
                });
    }

    @Test
    public void testNonEmptyTableResetNonEmpty() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    SimpleData simpleData = new SimpleData(1);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    // Check that the corner view is created before resetting to empty
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerView = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerView);

                    // Check the corner view is visible
                    Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(2);
                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is still created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerViewReset = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerViewReset);

                    // Check the corner view is still visible
                    Assert.assertEquals(View.VISIBLE, cornerViewReset.getVisibility());

                    // Check that it is the expected corner view by checking the text
                    // The first child of the LinearLayout is a textView (index starts at zero)
                    TextView cornerViewResetTextView = (TextView) cornerViewReset.getChildAt(0);
                    Assert.assertEquals("Cell Data", cornerViewResetTextView.getText());
                });
    }

    @Test
    public void testNonEmptyTableResetEmpty() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    SimpleData simpleData = new SimpleData(1);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    // Check that the corner view is created before resetting to empty
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerView = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerView);

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(0);
                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is still created but visibility is gone
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerViewReset = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerViewReset);

                    // Check the corner view visibility is GONE
                    Assert.assertEquals(View.GONE, cornerViewReset.getVisibility());
                });
    }

    @Test
    public void testColumnHeadersOnlyTable() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    // Only want column headers
                    SimpleData simpleData = new SimpleData(5, 0);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is not created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerView = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNull(cornerView);
                });
    }

    @Test
    public void testColumnHeadersOnlyTableResetNonEmptyTable() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    // Only want column headers
                    SimpleData simpleData = new SimpleData(5, 0);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    // Check that the corner view is not created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerView = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNull(cornerView);

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(5);
                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is not created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerViewReset = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerViewReset);

                    // Check the corner view is now visible
                    Assert.assertEquals(View.VISIBLE, cornerViewReset.getVisibility());

                    // Check that it is the expected corner view by checking the text
                    // The first child of the LinearLayout is a textView (index starts at zero)
                    TextView cornerViewResetTextView = (TextView) cornerViewReset.getChildAt(0);
                    Assert.assertEquals("Cell Data", cornerViewResetTextView.getText());
                });
    }

    @Test
    public void testColumnHeadersOnlyTableResetEmptyTable() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    // Only want column headers
                    SimpleData simpleData = new SimpleData(5, 0);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    // Check that the corner view is not created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerView = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNull(cornerView);

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(0);
                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is not created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerViewReset = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNull(cornerViewReset);
                });
    }

    @Test
    public void testColumnHeadersOnlyTableShowCorner() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    // Set the option to show corner view when there is not row data
                    tableView.setShowCornerView(true);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    // Only want column headers
                    SimpleData simpleData = new SimpleData(5, 0);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerView = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerView);

                    // Check the corner view is visible
                    Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());

                    // Check that it is the expected corner view by checking the text
                    // The first child of the LinearLayout is a textView (index starts at zero)
                    TextView cornerViewResetTextView = (TextView) cornerView.getChildAt(0);
                    Assert.assertEquals("Cell Data", cornerViewResetTextView.getText());
                });
    }

    @Test
    public void testColumnHeadersOnlyTableShowCornerResetEmptyTable() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    // Set the option to show corner view when there is not row data
                    tableView.setShowCornerView(true);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    // Only want column headers
                    SimpleData simpleData = new SimpleData(5, 0);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    // Check that the corner view is created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerView = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerView);

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(0);
                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is still created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerViewReset = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerViewReset);

                    // Check the corner view visibility is GONE
                    Assert.assertEquals(View.GONE, cornerViewReset.getVisibility());
                });
    }

    @Test
    public void testColumnHeadersOnlyTableShowCornerResetNonEmptyTable() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    // Set the option to show corner view when there is not row data
                    tableView.setShowCornerView(true);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    // Only want column headers
                    SimpleData simpleData = new SimpleData(5, 0);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    // Check that the corner view is created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerView = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerView);

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(2);
                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is still created
                    // The Corner view uses cell_layout which has LinearLayout as top item
                    LinearLayout cornerViewReset = (LinearLayout) simpleTestAdapter.getCornerView();
                    Assert.assertNotNull(cornerViewReset);

                    // Check the corner view visibility is VISIBLE
                    Assert.assertEquals(View.VISIBLE, cornerViewReset.getVisibility());
                });
    }
}
