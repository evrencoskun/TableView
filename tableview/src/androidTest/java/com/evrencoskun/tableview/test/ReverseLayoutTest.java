/*
 * MIT License
 *
 * Copyright (c) 2020 Andrew Beck
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
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyRightOf;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.widget.RelativeLayout;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.test.adapters.CornerTestAdapter;
import com.evrencoskun.tableview.test.data.SimpleData;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReverseLayoutTest {

    @Rule
    public ActivityScenarioRule<TestActivity> mActivityTestRule =
            new ActivityScenarioRule<>(TestActivity.class);

    @Test
    public void testDefaultLayout() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    activity.setContentView(R.layout.corner_default);

                    TableView tableView = activity.findViewById(R.id.tableview);
                    Assert.assertNotNull(tableView);

                    CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();

                    tableView.setAdapter(cornerTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    cornerTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());
                });

        // Check that the corner view is created and visible
        // The Corner view uses cell_layout which has RelativeLayout as top item
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that column headers are ordered Left to Right
        onView(withText("c:0")).check(isCompletelyLeftOf(withText("c:1")));

        // Check that first cell data row are ordered Left to Right
        onView(withText("r:0c:0")).check(isCompletelyLeftOf(withText("r:0c:1")));
    }

    @Test
    public void testReverseLayout() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    activity.setContentView(R.layout.reverse_layout);

                    TableView tableView = activity.findViewById(R.id.tableview);
                    Assert.assertNotNull(tableView);

                    CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();

                    tableView.setAdapter(cornerTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    cornerTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());
                });

        // Check that the corner view is created and visible
        // The Corner view uses cell_layout which has RelativeLayout as top item
        onView(withId(R.id.corner_view))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that column headers are ordered Right to Left
        onView(withText("c:0")).check(isCompletelyRightOf(withText("c:1")));

        // Check that first cell data row are ordered Right to Left
        onView(withText("r:0c:0")).check(isCompletelyRightOf(withText("r:0c:1")));
    }

    @Test
    public void testReverseConstructor() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity, false);

                    // initialize was false so set properties and call initialize
                    //Set CornerView to Top Right and ReverseLayout = true
                    tableView.setCornerViewLocation(ITableView.CornerViewLocation.TOP_RIGHT);
                    tableView.setReverseLayout(true);
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
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));

        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        onView(allOf(withParent(withId(R.id.corner_view)), withParentIndex(0)))
                .check(matches(withText("Corner")));

        // Check that column headers are ordered Right to Left
        onView(withText("c:0")).check(isCompletelyRightOf(withText("c:1")));

        // Check that first cell data row are ordered Right to Left
        onView(withText("r:0c:0")).check(isCompletelyRightOf(withText("r:0c:1")));
    }
}
