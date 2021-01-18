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

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.evrencoskun.tableview.*;
import com.evrencoskun.tableview.test.adapters.CornerTestAdapter;
import com.evrencoskun.tableview.test.adapters.SimpleTestAdapter;
import com.evrencoskun.tableview.test.data.SimpleData;
import com.evrencoskun.tableview.test.matchers.ViewWidthMatcher;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.PositionAssertions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class ReverseLayoutTest {

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
    public void testDefaultLayout() throws Throwable {
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

        // Check that column headers are ordered Left to Right
        onView(withText("c:0")).check(isCompletelyLeftOf(withText("c:1")));

        // Check that first cell data row are ordered Left to Right
        onView(withText("r:0c:0")).check(isCompletelyLeftOf(withText("r:0c:1")));
    }

    @Test
    public void testReverseLayout() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(R.layout.reverse_layout));

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

        // Check that column headers are ordered Right to Left
        onView(withText("c:0")).check(isCompletelyRightOf(withText("c:1")));

        // Check that first cell data row are ordered Right to Left
        onView(withText("r:0c:0")).check(isCompletelyRightOf(withText("r:0c:1")));
    }

    @Test
    public void testReverseConstructor() throws Throwable {
        Activity activity = mActivityTestRule.getActivity();

        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext(), false);
        Assert.assertNotNull(tableView);

        // initialize was false so set properties and call initialize
        //Set CornerView to Top Right and ReverseLaout = true
        tableView.setCornerViewLocation(ITableView.CornerViewLocation.TOP_RIGHT);
        tableView.setReverseLayout(true);
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

        // Check that column headers are ordered Right to Left
        onView(withText("c:0")).check(isCompletelyRightOf(withText("c:1")));

        // Check that first cell data row are ordered Right to Left
        onView(withText("r:0c:0")).check(isCompletelyRightOf(withText("r:0c:1")));
    }

}
