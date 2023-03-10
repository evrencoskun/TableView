changelog.md

TableView is a powerful and beatifull looking Android library for displaying complex 
data structures and rendering tabular data composed of rows, columns and cells.

The current implementation at https://github.com/evrencoskun/TableView
requires a lot of copy&paste from example code to use the TableView in a clientapp.

Product vision: reduce the amount of (copied) code to integrate TableView into a clientapp.

* all app specific data comes from tableview independent POJOs (i.e. List<MySamplePojo> from TestData.createSampleData())
* all info how the POJO is displayed in the tableview comes from ColumnDefinition (i.e. List<ColumnDefinition<MySamplePojo>> from TestData.createColumnDefinitions())
* no more need to implement app-specific XxxViewHolder, XxxModell for Cell, Column, RowHeader
* no more need to implement app-specific TableViewAdapter, TableViewModel
* no more need to define app specific layout for table_view_Xxx_layout.xml for Cell, Column, RowHeader
* Instead of the app-specific classes/layouts (mentioned in "no more need") there are generic, app-independant in the new modul tableviewutil.
* For legacy apps the tableview lib can be used as before.

## Additional Requirements

* java-8 lamda expressions to define column values and custom cell-view(-holders). See createColumnDefinitions() in TestData.java 

# Some differences between old and new style app that uses tableview

### old style:

build.gradle dependencies:

    implementation "com.evrencoskun.library:tableview:$tableview_version"

Mainfragment.java 

    private void initializeTableView(TableView tableView){
        // Create TableView Adapter
        mTableAdapter = new MyTableAdapter(getContext());
        tableView.setAdapter(mTableAdapter);

        // Create listener
        tableView.setTableViewListener(new MyTableViewListener(tableView));
    }

Define app specific classes and resources

* MainFragment.java
* MainActivity.java
* MainViewModel.java
* MyTableViewModel.java
* ColumnHeaderViewHolder.java
* MyTableAdapter.java
* CellModel.java
* RowHeaderViewHolder.java
* MyTableViewListener.java
* ColumnHeaderModel.java
* GenderCellViewHolder.java
* MoneyCellViewHolder.java
* RowHeaderModel.java
* CellViewHolder.java

* ic_down.xml
* ic_up.xml
* activity_main.xml
* fragment_main.xml
* tableview_cell_layout.xml
* tableview_column_header_layout.xml
* tableview_corner_layout.xml
* tableview_gender_cell_layout.xml
* tableview_money_cell_layout.xml
* tableview_row_header_layout.xml

### new style:

build.gradle dependencies:

    implementation "com.evrencoskun.library:tableview:$tableview_version"
    implementation "com.evrencoskun.library:tableviewutil:$tableview_version"

Mainfragment.java

    private void initializeTableView(TableView tableView){
        List<ColumnDefinition<MySamplePojo>> columnDefinitions = TestData.createColumnDefinitions();
        List<MySamplePojo> pojos = TestData.createSampleData();
        // Create TableView View model class  to group view models of TableView
        TableViewModel<MySamplePojo> tableViewModel = new TableViewModel(columnDefinitions, pojos);

        // Create TableView Adapter
        TableViewAdapter<MySamplePojo> tableViewAdapter = new TableViewAdapter<>(columnDefinitions);

        tableView.setAdapter(tableViewAdapter);
        tableView.setTableViewListener(new MyTableViewListener(tableView));

        // Create an instance of a Filter and pass the TableView.
        //mTableFilter = new Filter(tableView);

        // Load the dummy data to the TableView
        tableViewAdapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel
                .getRowHeaderList(), tableViewModel.getCellList());

    }

Define app specific classes and resources

* MySamplePojo.java
* MyTableViewListener.java
* TestData.java
* MainActivity.java
* MainFragment.java

* activity_main.xml
* fragment_main.xml
