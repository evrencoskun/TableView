/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableviewsample;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.filter.Filter;
import com.evrencoskun.tableview.pagination.Pagination;
import com.evrencoskun.tableviewsample.tableview.TableViewAdapter;
import com.evrencoskun.tableviewsample.tableview.TableViewListener;
import com.evrencoskun.tableviewsample.tableview.TableViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private EditText searchField;
    private Spinner moodFilter, genderFilter, itemsPerPage;
    public ImageButton previousButton, nextButton;
    public EditText pageNumberField;
    public TextView tablePaginationDetails;

    private AbstractTableAdapter mTableViewAdapter;
    private TableView mTableView;
    private Filter mTableFilter; // This is used for filtering the table.
    private Pagination mPagination; // This is used for paginating the table.

    // This is a sample class that provides the cell value objects and other configurations.
    private TableViewModel mTableViewModel;

    private boolean mPaginationEnabled = false;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);

        searchField = layout.findViewById(R.id.query_string);
        searchField.addTextChangedListener(mSearchTextWatcher);

        moodFilter = layout.findViewById(R.id.mood_spinner);
        moodFilter.setOnItemSelectedListener(mItemSelectionListener);

        genderFilter = layout.findViewById(R.id.gender_spinner);
        genderFilter.setOnItemSelectedListener(mItemSelectionListener);

        itemsPerPage = layout.findViewById(R.id.items_per_page_spinner);

        View tableTestContainer = layout.findViewById(R.id.table_test_container);

        previousButton = layout.findViewById(R.id.previous_button);
        nextButton = layout.findViewById(R.id.next_button);
        pageNumberField = layout.findViewById(R.id.page_number_text);
        tablePaginationDetails = layout.findViewById(R.id.table_details);

        if (mPaginationEnabled) {
            tableTestContainer.setVisibility(View.VISIBLE);
            itemsPerPage.setOnItemSelectedListener(onItemsPerPageSelectedListener);

            previousButton.setOnClickListener(mClickListener);
            nextButton.setOnClickListener(mClickListener);
            pageNumberField.addTextChangedListener(onPageTextChanged);
        } else {
            tableTestContainer.setVisibility(View.GONE);
        }

        // Let's get TableView
        mTableView = layout.findViewById(R.id.tableview);

        initializeTableView();

        if (mPaginationEnabled) {
            mTableFilter = new Filter(mTableView); // Create an instance of a Filter and pass the
            // created TableView.

            // Create an instance for the TableView pagination and pass the created TableView.
            mPagination = new Pagination(mTableView);

            // Sets the pagination listener of the TableView pagination to handle
            // pagination actions. See onTableViewPageTurnedListener variable declaration below.
            mPagination.setOnTableViewPageTurnedListener(onTableViewPageTurnedListener);
        }


        return layout;
    }

    private void initializeTableView() {
        // Create TableView View model class  to group view models of TableView
        mTableViewModel = new TableViewModel(getContext());

        // Create TableView Adapter
        mTableViewAdapter = new TableViewAdapter(getContext(), mTableViewModel);

        mTableView.setAdapter(mTableViewAdapter);
        mTableView.setTableViewListener(new TableViewListener(mTableView));

        // Create an instance of a Filter and pass the TableView.
        //mTableFilter = new Filter(mTableView);

        // Load the dummy data to the TableView
        mTableViewAdapter.setAllItems(mTableViewModel.getColumnHeaderList(), mTableViewModel
                .getRowHeaderList(), mTableViewModel.getCellList());


        //mTableView.setHasFixedWidth(true);

        /*for (int i = 0; i < mTableViewModel.getCellList().size(); i++) {
            mTableView.setColumnWidth(i, 200);
        }*)

        //mTableView.setColumnWidth(0, -2);
        //mTableView.setColumnWidth(1, -2);

        /*mTableView.setColumnWidth(2, 200);
        mTableView.setColumnWidth(3, 300);
        mTableView.setColumnWidth(4, 400);
        mTableView.setColumnWidth(5, 500);*/

    }

    public void filterTable(String filter) {
        // Sets a filter to the table, this will filter ALL the columns.
        mTableFilter.set(filter);
    }

    public void filterTableForMood(String filter) {
        // Sets a filter to the table, this will only filter a specific column.
        // In the example data, this will filter the mood column.
        mTableFilter.set(TableViewModel.MOOD_COLUMN_INDEX, filter);
    }

    public void filterTableForGender(String filter) {
        // Sets a filter to the table, this will only filter a specific column.
        // In the example data, this will filter the gender column.
        mTableFilter.set(TableViewModel.GENDER_COLUMN_INDEX, filter);
    }

    // The following four methods below: nextTablePage(), previousTablePage(),
    // goToTablePage(int page) and setTableItemsPerPage(int itemsPerPage)
    // are for controlling the TableView pagination.
    public void nextTablePage() {
        mPagination.nextPage();
    }

    public void previousTablePage() {
        mPagination.previousPage();
    }

    public void goToTablePage(int page) {
        mPagination.goToPage(page);
    }

    public void setTableItemsPerPage(int itemsPerPage) {
        mPagination.setItemsPerPage(itemsPerPage);
    }

    // Handler for the changing of pages in the paginated TableView.
    private Pagination.OnTableViewPageTurnedListener onTableViewPageTurnedListener = new
            Pagination.OnTableViewPageTurnedListener() {
        @Override
        public void onPageTurned(int numItems, int itemsStart, int itemsEnd) {
            int currentPage = mPagination.getCurrentPage();
            int pageCount = mPagination.getPageCount();
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);

            if (currentPage == 1 && pageCount == 1) {
                previousButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.INVISIBLE);
            }

            if (currentPage == 1) {
                previousButton.setVisibility(View.INVISIBLE);
            }

            if (currentPage == pageCount) {
                nextButton.setVisibility(View.INVISIBLE);
            }

            tablePaginationDetails.setText(getString(R.string.table_pagination_details, String
                    .valueOf(currentPage), String.valueOf(itemsStart), String.valueOf(itemsEnd)));

        }
    };


    private AdapterView.OnItemSelectedListener mItemSelectionListener = new AdapterView
            .OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // 0. index is for empty item of spinner.
            if (position > 0) {

                String filter = Integer.toString(position);

                if (parent == moodFilter) {
                    filterTableForMood(filter);
                } else if (parent == genderFilter) {
                    filterTableForGender(filter);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Left empty intentionally.
        }
    };

    private TextWatcher mSearchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterTable(String.valueOf(s));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private AdapterView.OnItemSelectedListener onItemsPerPageSelectedListener = new AdapterView
            .OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int itemsPerPage;
            switch (parent.getItemAtPosition(position).toString()) {
                case "All":
                    itemsPerPage = 0;
                    break;
                default:
                    itemsPerPage = Integer.valueOf(parent.getItemAtPosition(position).toString());
            }

            setTableItemsPerPage(itemsPerPage);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == previousButton) {
                previousTablePage();
            } else if (v == nextButton) {
                nextTablePage();
            }
        }
    };

    private TextWatcher onPageTextChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int page;
            if (TextUtils.isEmpty(s)) {
                page = 1;
            } else {
                page = Integer.valueOf(String.valueOf(s));
            }

            goToTablePage(page);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
