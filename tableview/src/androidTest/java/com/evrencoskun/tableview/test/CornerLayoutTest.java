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
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyRightOf;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.widget.RelativeLayout;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.test.adapters.CornerTestAdapter;
import com.evrencoskun.tableview.test.data.SimpleData;
import com.evrencoskun.tableview.test.matchers.ViewWidthMatcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CornerLayoutTest {

    @Rule
    public ActivityScenarioRule<TestActivity> mActivityTestRule =
            new ActivityScenarioRule<>(TestActivity.class);

    @Test
    public void testDefaultCorner() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    activity.setContentView(R.layout.corner_default);

                    TableView tableView = activity.findViewById(R.id.tableview);

                    CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();

                    tableView.setAdapter(cornerTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    cornerTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());
                });

        // Check that the corner view is created and visible
        // The Corner view uses cell_layout which has RelativeLayout as top item
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyLeftOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyAbove(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testTopLeftCorner() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    activity.setContentView(R.layout.corner_top_left);

                    TableView tableView = activity.findViewById(R.id.tableview);

                    CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();

                    tableView.setAdapter(cornerTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    cornerTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());
                });

        // Check that the corner view is created and visible
        // The Corner view uses cell_layout which has RelativeLayout as top item
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyLeftOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyAbove(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testBottomLeftCorner() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    activity.setContentView(R.layout.corner_bottom_left);

                    TableView tableView = activity.findViewById(R.id.tableview);

                    CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();

                    tableView.setAdapter(cornerTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    cornerTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());
                });

        // Check that the corner view is created and visible
        // The Corner view uses cell_layout which has RelativeLayout as top item
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyLeftOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyBelow(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testTopRightCorner() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    activity.setContentView(R.layout.corner_top_right);

                    TableView tableView = activity.findViewById(R.id.tableview);

                    CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();

                    tableView.setAdapter(cornerTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    cornerTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());
                });

        // Check that the corner view is created and visible
        // The Corner view uses cell_layout which has RelativeLayout as top item
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyRightOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyAbove(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testBottomRightCorner() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    activity.setContentView(R.layout.corner_bottom_right);

                    TableView tableView = activity.findViewById(R.id.tableview);

                    CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();

                    tableView.setAdapter(cornerTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    cornerTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());
                });

        // Check that the corner view is created and visible
        // The Corner view uses cell_layout which has RelativeLayout as top item
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyRightOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyBelow(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testCornerConstructor() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity, false);

                    // initialize was false so set properties and call initialize
                    tableView.setCornerViewLocation(ITableView.CornerViewLocation.BOTTOM_LEFT);
                    tableView.initialize();

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();

                    tableView.setAdapter(cornerTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    cornerTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());

                    activity.setContentView(rl);
                });

        // Check that the corner view is created and visible
        // The Corner view uses cell_layout which has RelativeLayout as top item
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyLeftOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyBelow(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testSetRowHeaderWidthLeft() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    activity.setContentView(R.layout.corner_top_left);

                    TableView tableView = activity.findViewById(R.id.tableview);

                    CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();

                    tableView.setAdapter(cornerTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    cornerTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());

                    // Set a new width on row header
                    tableView.setRowHeaderWidth(200);
                });

        // Check that the corner view is created and visible
        // The Corner view uses cell_layout which has RelativeLayout as top item
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyLeftOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyAbove(withId(R.id.RowHeaderRecyclerView)));

        // Check that the corner is new width
        onView(withId(R.id.corner_view)).check(matches(new ViewWidthMatcher(200)));

        // Check that the row header is new width
        onView(withId(R.id.RowHeaderRecyclerView)).check(matches(new ViewWidthMatcher(200)));
    }

    @Test
    public void testSetRowHeaderWidthRight() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    activity.setContentView(R.layout.corner_top_right);

                    TableView tableView = activity.findViewById(R.id.tableview);

                    CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();

                    tableView.setAdapter(cornerTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    cornerTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());

                    // Set a new width on row header
                    tableView.setRowHeaderWidth(200);
                });

        // Check that the corner view is created and visible
        // The Corner view uses cell_layout which has RelativeLayout as top item
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyRightOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyAbove(withId(R.id.RowHeaderRecyclerView)));

        // Check that the corner is new width
        onView(withId(R.id.corner_view)).check(matches(new ViewWidthMatcher(200)));

        // Check that the row header is new width
        onView(withId(R.id.RowHeaderRecyclerView)).check(matches(new ViewWidthMatcher(200)));
    }
}
