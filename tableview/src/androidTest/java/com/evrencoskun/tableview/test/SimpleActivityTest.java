/*
 * Copyright (c) 2020. Andrew Beck
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.evrencoskun.tableview.test;

import android.app.Activity;
import android.widget.LinearLayout;
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

        SimpleTestAdapter simpleTestAdapter = new SimpleTestAdapter();
        Assert.assertNotNull(simpleTestAdapter);

        tableView.setAdapter(simpleTestAdapter);

        SimpleData simpleData = new SimpleData(5);

        simpleTestAdapter.setAllItems(simpleData.getColumnHeaders(), simpleData.getRowHeaders(),
                simpleData.getCells());

        mActivityTestRule.runOnUiThread(() -> activity.setContentView(tableView));

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
