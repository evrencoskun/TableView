package com.evrencoskun.tableview.test.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static java.util.Collections.emptyList;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.test.TestActivity;
import com.evrencoskun.tableview.test.data.SimpleData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AbstractTableAdapterTest {

    @Rule
    public ActivityScenarioRule<TestActivity> mActivityTestRule =
            new ActivityScenarioRule<>(TestActivity.class);

    private SimpleData mData;
    private TableView mTableView;
    private SimpleTestAdapter mAdapter;

    @Before
    public void before() {
        mData = new SimpleData(5);
        mTableView = new TableView(InstrumentationRegistry.getInstrumentation().getContext());

        mAdapter = new SimpleTestAdapter();
        mAdapter.setTableView(mTableView);
    }

    @Test
    public void testCornerViewStateWithDisabledCorner() {
        mTableView.setShowCornerView(false);

        assertNull(mAdapter.getCornerView());

        mAdapter.setAllItems(null, null, null);

        assertNull(mAdapter.getCornerView());

        mAdapter.setAllItems(mData.getColumnHeaders(), mData.getRowHeaders(), mData.getCells());

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.VISIBLE, mAdapter.getCornerView().getVisibility());

        mAdapter.setAllItems(emptyList(), emptyList(), emptyList());

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.GONE, mAdapter.getCornerView().getVisibility());
    }

    @Test
    public void testCornerViewStateWithEnabledCorners() {
        mTableView.setShowCornerView(true);

        assertNull(mAdapter.getCornerView());

        mAdapter.setAllItems(mData.getColumnHeaders(), mData.getRowHeaders(), mData.getCells());

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.VISIBLE, mAdapter.getCornerView().getVisibility());

        mAdapter.setAllItems(null, null, null);

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.GONE, mAdapter.getCornerView().getVisibility());

        // We set some data, that we then reset to empty
        mAdapter.setAllItems(mData.getColumnHeaders(), mData.getRowHeaders(), mData.getCells());
        mAdapter.setAllItems(emptyList(), emptyList(), emptyList());

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.GONE, mAdapter.getCornerView().getVisibility());

        mAdapter.setAllItems(mData.getColumnHeaders(), null, null);

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.VISIBLE, mAdapter.getCornerView().getVisibility());

        mAdapter.setAllItems(null, mData.getRowHeaders(), null);

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.GONE, mAdapter.getCornerView().getVisibility());

        mAdapter.setAllItems(mData.getColumnHeaders(), mData.getRowHeaders(), null);

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.VISIBLE, mAdapter.getCornerView().getVisibility());
    }

    @Test
    public void testCornerViewStateWithToggledCorners() {
        mTableView.setShowCornerView(true);

        assertNull(mAdapter.getCornerView());

        mAdapter.setAllItems(mData.getColumnHeaders(), mData.getRowHeaders(), mData.getCells());

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.VISIBLE, mAdapter.getCornerView().getVisibility());

        mTableView.setShowCornerView(false);
        mAdapter.setAllItems(mData.getColumnHeaders(), mData.getRowHeaders(), mData.getCells());

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.VISIBLE, mAdapter.getCornerView().getVisibility());

        mAdapter.setAllItems(null, null, null);

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.GONE, mAdapter.getCornerView().getVisibility());

        // We set some data, that we then reset to empty
        mAdapter.setAllItems(mData.getColumnHeaders(), mData.getRowHeaders(), mData.getCells());
        mAdapter.setAllItems(emptyList(), emptyList(), emptyList());

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.GONE, mAdapter.getCornerView().getVisibility());

        mAdapter.setAllItems(mData.getColumnHeaders(), null, null);

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.GONE, mAdapter.getCornerView().getVisibility());

        mAdapter.setAllItems(null, mData.getRowHeaders(), null);

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.GONE, mAdapter.getCornerView().getVisibility());

        mAdapter.setAllItems(mData.getColumnHeaders(), mData.getRowHeaders(), null);

        assertNotNull(mAdapter.getCornerView());
        assertEquals(View.VISIBLE, mAdapter.getCornerView().getVisibility());
    }
}
