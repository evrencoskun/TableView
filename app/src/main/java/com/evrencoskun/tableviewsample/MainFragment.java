/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
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

package com.evrencoskun.tableviewsample;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.filter.Filter;
import com.evrencoskun.tableview.pagination.Pagination;
import com.evrencoskun.tableviewsample.tableview.TableViewAdapter;
import com.evrencoskun.tableviewsample.tableview.TableViewListener;
import com.evrencoskun.tableviewsample.tableview.TableViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private Spinner moodFilter, genderFilter;
    private ImageButton previousButton, nextButton;
    private TextView tablePaginationDetails;
    private TableView mTableView;
    @Nullable
    private Filter mTableFilter; // This is used for filtering the table.
    @Nullable
    private Pagination mPagination; // This is used for paginating the table.

    private boolean mPaginationEnabled = false;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText searchField = view.findViewById(R.id.query_string);
        searchField.addTextChangedListener(mSearchTextWatcher);

        moodFilter = view.findViewById(R.id.mood_spinner);
        moodFilter.setOnItemSelectedListener(mItemSelectionListener);

        genderFilter = view.findViewById(R.id.gender_spinner);
        genderFilter.setOnItemSelectedListener(mItemSelectionListener);

        Spinner itemsPerPage = view.findViewById(R.id.items_per_page_spinner);

        View tableTestContainer = view.findViewById(R.id.table_test_container);

        previousButton = view.findViewById(R.id.previous_button);
        nextButton = view.findViewById(R.id.next_button);
        EditText pageNumberField = view.findViewById(R.id.page_number_text);
        tablePaginationDetails = view.findViewById(R.id.table_details);

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
        mTableView = view.findViewById(R.id.tableview);

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
    }

    private void initializeTableView() {
        // Create TableView View model class  to group view models of TableView
        TableViewModel tableViewModel = new TableViewModel();

        // Create TableView Adapter
        TableViewAdapter tableViewAdapter = new TableViewAdapter(tableViewModel);

        mTableView.setAdapter(tableViewAdapter);
        mTableView.setTableViewListener(new TableViewListener(mTableView));

        // Create an instance of a Filter and pass the TableView.
        //mTableFilter = new Filter(mTableView);

        // Load the dummy data to the TableView
        tableViewAdapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel
                .getRowHeaderList(), tableViewModel.getCellList());

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

    public void filterTable(@NonNull String filter) {
        // Sets a filter to the table, this will filter ALL the columns.
        if (mTableFilter != null) {
            mTableFilter.set(filter);
        }
    }

    public void filterTableForMood(@NonNull String filter) {
        // Sets a filter to the table, this will only filter a specific column.
        // In the example data, this will filter the mood column.
        if (mTableFilter != null) {
            mTableFilter.set(TableViewModel.MOOD_COLUMN_INDEX, filter);
        }
    }

    public void filterTableForGender(@NonNull String filter) {
        // Sets a filter to the table, this will only filter a specific column.
        // In the example data, this will filter the gender column.
        if (mTableFilter != null) {
            mTableFilter.set(TableViewModel.GENDER_COLUMN_INDEX, filter);
        }
    }

    // The following four methods below: nextTablePage(), previousTablePage(),
    // goToTablePage(int page) and setTableItemsPerPage(int itemsPerPage)
    // are for controlling the TableView pagination.
    public void nextTablePage() {
        if (mPagination != null) {
            mPagination.nextPage();
        }
    }

    public void previousTablePage() {
        if (mPagination != null) {
            mPagination.previousPage();
        }
    }

    public void goToTablePage(int page) {
        if (mPagination != null) {
            mPagination.goToPage(page);
        }
    }

    public void setTableItemsPerPage(int itemsPerPage) {
        if (mPagination != null) {
            mPagination.setItemsPerPage(itemsPerPage);
        }
    }

    // Handler for the changing of pages in the paginated TableView.
    @NonNull
    private final Pagination.OnTableViewPageTurnedListener onTableViewPageTurnedListener = new
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

    @NonNull
    private final AdapterView.OnItemSelectedListener mItemSelectionListener = new AdapterView
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

    @NonNull
    private final TextWatcher mSearchTextWatcher = new TextWatcher() {
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

    @NonNull
    private final AdapterView.OnItemSelectedListener onItemsPerPageSelectedListener = new AdapterView
            .OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int itemsPerPage;
            if ("All".equals(parent.getItemAtPosition(position).toString())) {
                itemsPerPage = 0;
            } else {
                itemsPerPage = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            setTableItemsPerPage(itemsPerPage);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @NonNull
    private final View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == previousButton) {
                previousTablePage();
            } else if (v == nextButton) {
                nextTablePage();
            }
        }
    };

    @NonNull
    private final TextWatcher onPageTextChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int page;
            if (TextUtils.isEmpty(s)) {
                page = 1;
            } else {
                page = Integer.parseInt(String.valueOf(s));
            }

            goToTablePage(page);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
