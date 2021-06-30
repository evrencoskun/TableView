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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.test.adapters.SimpleTestAdapter;
import com.evrencoskun.tableview.test.data.SimpleData;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SimpleActivityTest {

    @Rule
    public ActivityScenarioRule<TestActivity> mActivityTestRule =
            new ActivityScenarioRule<>(TestActivity.class);

    @Test
    public void testDefaults() {
        TableView tableView =
                new TableView(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Assert.assertFalse(tableView.isAllowClickInsideCell());
        Assert.assertTrue(tableView.isShowHorizontalSeparators());
        Assert.assertTrue(tableView.isShowVerticalSeparators());
    }

    @Test
    public void testSmallTable() {
        mActivityTestRule.getScenario()
                .onActivity(activity -> {
                    TableView tableView = new TableView(activity);
                    tableView.setId(R.id.tableview);

                    RelativeLayout rl = new RelativeLayout(activity);
                    rl.addView(tableView);

                    SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();

                    tableView.setAdapter(simpleTestAdapter);

                    SimpleData simpleData = new SimpleData(5);

                    simpleTestAdapter.setAllItems(
                            simpleData.getColumnHeaders(), simpleData.getRowHeaders(), simpleData.getCells());

                    activity.setContentView(rl);
                });

        // Check that the row header was created as expected at 5th Row (index starts at zero)
        // cell_layout has LinearLayout as top item
        Matcher<View> rowHeaders = allOf(withParent(withId(R.id.tableview)), withParentIndex(1));
        Matcher<View> rowHeader = allOf(withParent(rowHeaders), withParentIndex(4));

        onView(allOf(withParent(rowHeader), withParentIndex(0)))
                .check(matches(withText("r:4")));

        // Check that the column header was created as expected at 5th Row (index starts at zero)
        // cell_layout has LinearLayout as top item
        Matcher<View> columnHeaders = allOf(withParent(withId(R.id.tableview)), withParentIndex(0));
        Matcher<View> columnHeader = allOf(withParent(columnHeaders), withParentIndex(4));

        onView(allOf(withParent(columnHeader), withParentIndex(0)))
                .check(matches(withText("c:4")));
    }
}
