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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.widget.RelativeLayout;

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
                    Assert.assertNull(simpleTestAdapter.getCornerView());
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

                    activity.setContentView(rl);

                    // Check that the corner view is not created (therefore not shown)
                    Assert.assertNull(simpleTestAdapter.getCornerView());

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(2);

                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    // Check that the corner view is now created
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());
                });

        // Check the corner view is now visible
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        onView(withId(R.id.corner_text))
                .check(matches(withText("Corner")));
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
                    Assert.assertNull(simpleTestAdapter.getCornerView());

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(0);

                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    // Check that the corner view is still not created
                    Assert.assertNull(simpleTestAdapter.getCornerView());
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
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());
                });

        // Check the corner view is now visible
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        onView(withId(R.id.corner_text))
                .check(matches(withText("Corner")));
    }

    @Test
    public void testNonEmptyTableResetNonEmpty() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);
                    tableView.setId(R.id.tableview);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    SimpleData simpleData = new SimpleData(1);

                    simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                            simpleData.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is created before resetting to empty
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());
                });

        // Check the corner view is visible
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = activity.findViewById(R.id.tableview);

                    SimpleTestAdapter simpleTestAdapter = (SimpleTestAdapter) tableView.getAdapter();

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(2);

                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    // Check that the corner view is still created
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());
                });

        // Check the corner view is still visible
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        onView(withId(R.id.corner_text))
                .check(matches(withText("Corner")));
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
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(0);

                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is still created but visibility is gone
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());
                });

        // Check the corner view visibility is GONE
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.GONE)));
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
                    Assert.assertNull(simpleTestAdapter.getCornerView());
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
                    Assert.assertNull(simpleTestAdapter.getCornerView());

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(5);

                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is not created
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());
                });

        // Check the corner view is now visible
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        onView(withId(R.id.corner_text))
                .check(matches(withText("Corner")));
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
                    Assert.assertNull(simpleTestAdapter.getCornerView());

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(0);

                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is not created
                    Assert.assertNull(simpleTestAdapter.getCornerView());
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
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());
                });

        // Check the corner view is now visible
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        onView(withId(R.id.corner_text))
                .check(matches(withText("Corner")));
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
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(0);

                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is still created
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());
                });

        // Check the corner view visibility is GONE
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.GONE)));
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
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());

                    // Change the items of data to reset
                    SimpleData simpleDataReset = new SimpleData(2);
                    simpleTestAdapter.setAllItems(simpleDataReset.getColumnHeaders(), simpleDataReset.getRowHeaders(),
                            simpleDataReset.getCells());

                    activity.setContentView(rl);

                    // Check that the corner view is still created
                    Assert.assertNotNull(simpleTestAdapter.getCornerView());
                });

        // Check the corner view visibility is VISIBLE
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
    }
}
