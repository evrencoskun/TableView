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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.test.adapters.CornerTestAdapter;
import com.evrencoskun.tableview.test.data.SimpleData;
import com.evrencoskun.tableview.test.matchers.ViewWidthMatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyRightOf;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class CornerLayoutTest {

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
    public void testDefaultCorner() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(R.layout.corner_default));

        TableView tableView =  activity.findViewById(R.id.tableview);
        Assert.assertNotNull(tableView);

        CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();
        Assert.assertNotNull(cornerTestAdapter);

        mActivityTestRule.runOnUiThread(() -> tableView.setAdapter(cornerTestAdapter));

        SimpleData simpleData = new SimpleData(5);

        mActivityTestRule.runOnUiThread(() -> cornerTestAdapter.setAllItems(
                simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells()));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        RelativeLayout cornerView = (RelativeLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        TextView cornerViewTextView = (TextView) cornerView.getChildAt(0);
        Assert.assertEquals("Corner", cornerViewTextView.getText());

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyLeftOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyAbove(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testTopLeftCorner() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(R.layout.corner_top_left));

        TableView tableView =  activity.findViewById(R.id.tableview);
        Assert.assertNotNull(tableView);

        CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();
        Assert.assertNotNull(cornerTestAdapter);

        mActivityTestRule.runOnUiThread(() -> tableView.setAdapter(cornerTestAdapter));

        SimpleData simpleData = new SimpleData(5);

        mActivityTestRule.runOnUiThread(() -> cornerTestAdapter.setAllItems(
                simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells()));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        RelativeLayout cornerView = (RelativeLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        TextView cornerViewTextView = (TextView) cornerView.getChildAt(0);
        Assert.assertEquals("Corner", cornerViewTextView.getText());

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyLeftOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyAbove(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testBottomLeftCorner() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(R.layout.corner_bottom_left));

        TableView tableView =  activity.findViewById(R.id.tableview);
        Assert.assertNotNull(tableView);

        CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();
        Assert.assertNotNull(cornerTestAdapter);

        mActivityTestRule.runOnUiThread(() -> tableView.setAdapter(cornerTestAdapter));

        SimpleData simpleData = new SimpleData(5);

        mActivityTestRule.runOnUiThread(() -> cornerTestAdapter.setAllItems(
                simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells()));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        RelativeLayout cornerView = (RelativeLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        TextView cornerViewTextView = (TextView) cornerView.getChildAt(0);
        Assert.assertEquals("Corner", cornerViewTextView.getText());

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyLeftOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyBelow(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testTopRightCorner() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(R.layout.corner_top_right));

        TableView tableView =  activity.findViewById(R.id.tableview);
        Assert.assertNotNull(tableView);

        CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();
        Assert.assertNotNull(cornerTestAdapter);

        mActivityTestRule.runOnUiThread(() -> tableView.setAdapter(cornerTestAdapter));

        SimpleData simpleData = new SimpleData(5);

        mActivityTestRule.runOnUiThread(() -> cornerTestAdapter.setAllItems(
                simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells()));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        RelativeLayout cornerView = (RelativeLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        TextView cornerViewTextView = (TextView) cornerView.getChildAt(0);
        Assert.assertEquals("Corner", cornerViewTextView.getText());

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyRightOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyAbove(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testBottomRightCorner() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(R.layout.corner_bottom_right));

        TableView tableView =  activity.findViewById(R.id.tableview);
        Assert.assertNotNull(tableView);

        CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();
        Assert.assertNotNull(cornerTestAdapter);

        mActivityTestRule.runOnUiThread(() -> tableView.setAdapter(cornerTestAdapter));

        SimpleData simpleData = new SimpleData(5);

        mActivityTestRule.runOnUiThread(() -> cornerTestAdapter.setAllItems(
                simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells()));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        RelativeLayout cornerView = (RelativeLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        TextView cornerViewTextView = (TextView) cornerView.getChildAt(0);
        Assert.assertEquals("Corner", cornerViewTextView.getText());

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyRightOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyBelow(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testCornerConstructor() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext(), false);
        Assert.assertNotNull(tableView);

        // initialize was false so set properties and call initialize
        tableView.setCornerViewLocation(ITableView.CornerViewLocation.BOTTOM_LEFT);
        tableView.initialize();

        RelativeLayout rl = new RelativeLayout(InstrumentationRegistry.getInstrumentation().getTargetContext());
        rl.addView(tableView);

        CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();
        Assert.assertNotNull(cornerTestAdapter);

        mActivityTestRule.runOnUiThread(() -> tableView.setAdapter(cornerTestAdapter));

        SimpleData simpleData = new SimpleData(5);

        mActivityTestRule.runOnUiThread(() -> cornerTestAdapter.setAllItems(
                simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells()));


        mActivityTestRule.runOnUiThread(() -> activity.setContentView(rl));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        RelativeLayout cornerView = (RelativeLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        TextView cornerViewTextView = (TextView) cornerView.getChildAt(0);
        Assert.assertEquals("Corner", cornerViewTextView.getText());

        // Check that Corner to is to the right of the column headers
        onView(withId(R.id.corner_view)).check(isCompletelyLeftOf(withId(R.id.ColumnHeaderRecyclerView)));

        // Check that Corner to is to the above of the row headers
        onView(withId(R.id.corner_view)).check(isCompletelyBelow(withId(R.id.RowHeaderRecyclerView)));
    }

    @Test
    public void testSetRowHeaderWidthLeft() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(R.layout.corner_top_left));

        TableView tableView =  activity.findViewById(R.id.tableview);
        Assert.assertNotNull(tableView);

        CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();
        Assert.assertNotNull(cornerTestAdapter);

        mActivityTestRule.runOnUiThread(() -> tableView.setAdapter(cornerTestAdapter));

        SimpleData simpleData = new SimpleData(5);

        mActivityTestRule.runOnUiThread(() -> cornerTestAdapter.setAllItems(
                simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells()));

        // Set a new width on row header
        mActivityTestRule.runOnUiThread(() -> tableView.setRowHeaderWidth(200));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        RelativeLayout cornerView = (RelativeLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        TextView cornerViewTextView = (TextView) cornerView.getChildAt(0);
        Assert.assertEquals("Corner", cornerViewTextView.getText());

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
    public void testSetRowHeaderWidthRight() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(R.layout.corner_top_right));

        TableView tableView =  activity.findViewById(R.id.tableview);
        Assert.assertNotNull(tableView);

        CornerTestAdapter cornerTestAdapter = new CornerTestAdapter();
        Assert.assertNotNull(cornerTestAdapter);

        mActivityTestRule.runOnUiThread(() -> tableView.setAdapter(cornerTestAdapter));

        SimpleData simpleData = new SimpleData(5);

        mActivityTestRule.runOnUiThread(() -> cornerTestAdapter.setAllItems(
                simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells()));

        // Set a new width on row header
        mActivityTestRule.runOnUiThread(() -> tableView.setRowHeaderWidth(200));

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // Check that the corner view is created
        // The Corner view uses cell_layout which has LinearLayout as top item
        RelativeLayout cornerView = (RelativeLayout) tableView.getAdapter().getCornerView();
        Assert.assertNotNull(cornerView);
        // Check the corner view is visible
        Assert.assertEquals(View.VISIBLE, cornerView.getVisibility());
        // Check that it is the expected corner view by checking the text
        // The first child of the RelativeLayout is a textView (index starts at zero)
        TextView cornerViewTextView = (TextView) cornerView.getChildAt(0);
        Assert.assertEquals("Corner", cornerViewTextView.getText());

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
