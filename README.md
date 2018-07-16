<div align="center">
    <img src="https://raw.githubusercontent.com/evrencoskun/TableViewSample/master/Logo-5.png" >
    <h2>TableView For Android</h2>
    <p align="center">
        <p>TableView is a powerful Android library for displaying complex data structures and rendering tabular data composed of rows, columns and cells. 
           TableView relies on a separate model object to hold and represent the data it displays. This repository also contains a sample app that is
           designed to show you how to create your own TableView in your application.</p>        
        <a href="https://youtu.be/1DWFIqrqrPk">
            <b>Demo Full video »</b>
        </a>
    </p>

</div>

<p align="center">
    <a href="https://youtu.be/1DWFIqrqrPk">
      <img src="https://raw.githubusercontent.com/evrencoskun/TableViewSample/master/TableView-0_8_5_1_2.gif">
    </a>
</p>

## Features

  - [x] Each column width value can be calculated automatically considering the largest one
  - [x] Setting your own model class to displayed in a table view easily.
  - [x] TableView has an action listener interface to listen user touch interaction for each cell.
  - [x] TableView columns can be sorted in ascending or descending order.
  - [x] Hiding & Showing the row and the column is pretty easy. 
  - [x] Filtering by more than one data.
  - [x] Pagination functionality.
    
## What's new

You can check new implementations of TableView on the [release page](https://github.com/evrencoskun/TableView/releases). 
  
## Table of Contents
  - [Installation](#installation)
  - [Implement your item on TableView](#implement-your-item-on-tableview)
    - [1. Create your TableView](#1-create-your-tableview)
       - [XML](#xml)
       - [Programmatically](#programmatically)
    - [2. Create your TableViewAdapter](#2-create-your-tableviewadapter)
    - [3. Set the Adapter to the TableView](#3-set-the-adapter-to-the-tableview)
    - [4. Set Click listener to the TableView](#4-set-click-listener-to-the-tableview)   
  - [Sorting](#sorting)
    - [1. ISortableModel to your Cell Model](#1-isortablemodel-to-your-cell-model)
    - [2. AbstractSorterViewHolder to your Column Header ViewHolder](#2-abstractsorterviewholder-to-your-column-header-viewholder)
    - [3. Helper methods for sorting process](#3-helper-methods-for-sorting-process)
  - [Filtering](#filtering)
    - [Steps to implement filtering functionality into TableView](#steps-to-implement-filtering-functionality-into-tableview)
    - [Filtering notes](#filtering-notes)
  - [Pagination](#pagination)
    - [Steps to implement pagination functionality into TableView](#steps-to-implement-pagination-functionality-into-tableview)
        - [Creating views to control the Pagination](#creating-views-to-control-the-pagination)
        - [Implementation of the pagination](#implementation-of-the-pagination)
    - [Pagination notes](#pagination-notes)
  - [Change your TableView model](#change-your-tableview-model)
  - [Hiding & Showing the Row](#hiding--showing-the-row)
  - [Hiding & Showing the Column](#hiding--showing-the-column)
  - [Advanced Usage](#advanced-usage)
  - [Sample Apps](#sample-apps)
  - [Articles](#Articles)
  - [Communication](#communication)
  - [Contributors](#contributors)
  - [License](#license)

## Installation

To use this library in your android project, just simply add the following dependency into your build.gradle

```
dependencies {
    compile 'com.evrencoskun.library:tableview:0.8.8' 
}
```

## Implement your item on TableView 

### 1. Create your TableView

#### XML 

``` 
<com.evrencoskun.tableview.TableView
    android:id="@+id/content_container"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

As default constants can be set programmatically, it can be set by also using xml attributes of TableView like this:

``` 
<com.evrencoskun.tableview.TableView
    android:id="@+id/content_container"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    
    app:column_header_height="@dimen/column_header_height"
    app:row_header_width="@dimen/row_header_width"
    app:selected_color="@color/selected_background_color"
    app:shadow_color="@color/shadow_background_color"
    app:unselected_color="@color/unselected_background_color"
/>
```

**Note:** To be able use these attributes on xml side, the **xmlns:** namespace below line should be added on layout root view. Otherwise, Android Studio gives you compile error.

``` 
xmlns:app="http://schemas.android.com/apk/res-auto"
```

#### Programmatically 

```
TableView tableView = new TableView(getContext());
```

### 2. Create your TableViewAdapter

Firstly, you must create your custom TableView Adapter  which extends from ```AbstractTableAdapter``` class. 
 ```AbstractTableAdapter``` class requires 3 different lists which represent respectively; **ColumnHeader**, **RowHeader** and **Cell** ViewModels.

**For example:** 

```java
 public class MyTableViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {
  
     public MyTableViewAdapter(Context context) {
         super(context);
 
     }
 
     /**
      * This is sample CellViewHolder class
      * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
      */
      class MyCellViewHolder extends AbstractViewHolder {
          
          public final TextView cell_textview;
      
          public MyCellViewHolder(View itemView) {
              super(itemView);
              cell_textview = (TextView) itemView.findViewById(R.id.cell_data);
          }
      }
     
     
     /**
      * This is where you create your custom Cell ViewHolder. This method is called when Cell
      * RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given type to
      * represent an item.
      *
      * @param viewType : This value comes from #getCellItemViewType method to support different type
      *                 of viewHolder as a Cell item.
      *
      * @see #getCellItemViewType(int);
      */
     @Override
     public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
         // Get cell xml layout 
         View layout = LayoutInflater.from(context).inflate(R.layout.my_cell_layout,
                 parent, false);
         // Create a Custom ViewHolder for a Cell item.
         return new MyCellViewHolder(layout);
     }
 
     /**
      * That is where you set Cell View Model data to your custom Cell ViewHolder. This method is
      * Called by Cell RecyclerView of the TableView to display the data at the specified position.
      * This method gives you everything you need about a cell item.
      *
      * @param holder       : This is one of your cell ViewHolders that was created on
      *                     ```onCreateCellViewHolder``` method. In this example we have created
      *                     "MyCellViewHolder" holder.
      * @param cellItemModel     : This is the cell view model located on this X and Y position. In this
      *                     example, the model class is "Cell".
      * @param columnPosition : This is the X (Column) position of the cell item.
      * @param rowPosition : This is the Y (Row) position of the cell item.
      *
      * @see #onCreateCellViewHolder(ViewGroup, int);
      */
     @Override
     public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int 
             columnPosition, int rowPosition) {
         Cell cell = (Cell) cellItemModel;
 
         // Get the holder to update cell item text
         MyCellViewHolder viewHolder = (MyCellViewHolder) holder;
         viewHolder.cell_textview.setText(cell.getData());
 
         // If your TableView should have auto resize for cells & columns.
         // Then you should consider the below lines. Otherwise, you can ignore them.
 
         // It is necessary to remeasure itself.
         viewHolder.ItemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
         viewHolder.cell_textview.requestLayout();
     }
 
     
     /**
      * This is sample CellViewHolder class. 
      * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
      */
      class MyColumnHeaderViewHolder extends AbstractViewHolder {
           
        public final TextView cell_textview;
   
        public MyColumnHeaderViewHolder(View itemView) {
           super(itemView);
           cell_textview = (TextView) itemView.findViewById(R.id.cell_data);
        }
      }
     
     /**
      * This is where you create your custom Column Header ViewHolder. This method is called when
      * Column Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
      * type to represent an item.
      *
      * @param viewType : This value comes from "getColumnHeaderItemViewType" method to support
      *                 different type of viewHolder as a Column Header item.
      *
      * @see #getColumnHeaderItemViewType(int);
      */
     @Override
     public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
 
         // Get Column Header xml Layout
         View layout = LayoutInflater.from(context).inflate(R.layout
                 .table_view_column_header_layout, parent, false);
 
         // Create a ColumnHeader ViewHolder
         return new MyColumnHeaderViewHolder(layout);
     }
 
     /**
      * That is where you set Column Header View Model data to your custom Column Header ViewHolder.
      * This method is Called by ColumnHeader RecyclerView of the TableView to display the data at
      * the specified position. This method gives you everything you need about a column header
      * item.
      *
      * @param holder   : This is one of your column header ViewHolders that was created on
      *                 ```onCreateColumnHeaderViewHolder``` method. In this example we have created
      *                 "MyColumnHeaderViewHolder" holder.
      * @param columnHeaderItemModel : This is the column header view model located on this X position. In this
      *                 example, the model class is "ColumnHeader".
      * @param position : This is the X (Column) position of the column header item.
      *
      * @see #onCreateColumnHeaderViewHolder(ViewGroup, int) ;
      */
     @Override
     public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int 
             position) {
         ColumnHeader columnHeader = (ColumnHeader) columnHeaderItemModel;
 
         // Get the holder to update cell item text
         MyColumnHeaderViewHolder columnHeaderViewHolder = (MyColumnHeaderViewHolder) holder;
         columnHeaderViewHolder.column_header_textview.setText(columnHeader.getData());
 
         // If your TableView should have auto resize for cells & columns.
         // Then you should consider the below lines. Otherwise, you can ignore them.
 
         // It is necessary to remeasure itself.
         columnHeaderViewHolder.column_header_container.getLayoutParams().width = LinearLayout
                 .LayoutParams.WRAP_CONTENT;
         columnHeaderViewHolder.column_header_textview.requestLayout();
     }
 
     /**
      * This is sample CellViewHolder class. 
      * This viewHolder must be extended from AbstractViewHolder class instead of RecyclerView.ViewHolder.
      */
      class MyRowHeaderViewHolder extends AbstractViewHolder {
            
         public final TextView cell_textview;
    
         public MyRowHeaderViewHolder(View itemView) {
            super(itemView);
            cell_textview = (TextView) itemView.findViewById(R.id.cell_data);
         }
      }
     
     
     /**
      * This is where you create your custom Row Header ViewHolder. This method is called when
      * Row Header RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given
      * type to represent an item.
      *
      * @param viewType : This value comes from "getRowHeaderItemViewType" method to support
      *                 different type of viewHolder as a row Header item.
      *
      * @see #getRowHeaderItemViewType(int);
      */
     @Override
     public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
 
         // Get Row Header xml Layout
         View layout = LayoutInflater.from(context).inflate(R.layout
                 .table_view_row_header_layout, parent, false);
 
         // Create a Row Header ViewHolder
         return new MyRowHeaderViewHolder(layout);
     }
 
 
     /**
      * That is where you set Row Header View Model data to your custom Row Header ViewHolder. This
      * method is Called by RowHeader RecyclerView of the TableView to display the data at the
      * specified position. This method gives you everything you need about a row header item.
      *
      * @param holder   : This is one of your row header ViewHolders that was created on
      *                 ```onCreateRowHeaderViewHolder``` method. In this example we have created
      *                 "MyRowHeaderViewHolder" holder.
      * @param rowHeaderItemModel : This is the row header view model located on this Y position. In this
      *                 example, the model class is "RowHeader".
      * @param position : This is the Y (row) position of the row header item.
      *
      * @see #onCreateRowHeaderViewHolder(ViewGroup, int) ;
      */
     @Override
     public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int 
             position) {
         RowHeader rowHeader = (RowHeader) rowHeaderItemModel;
 
         // Get the holder to update row header item text
         MyRowHeaderViewHolder rowHeaderViewHolder = (MyRowHeaderViewHolder) holder;
         rowHeaderViewHolder.row_header_textview.setText(rowHeader.getData());
     }
 
 
     @Override
     public View onCreateCornerView() {
         // Get Corner xml layout
         return LayoutInflater.from(context).inflate(R.layout.table_view_corner_layout, null);
     }
 
     @Override
     public int getColumnHeaderItemViewType(int columnPosition) {
         // The unique ID for this type of column header item
         // If you have different items for Cell View by X (Column) position, 
         // then you should fill this method to be able create different 
         // type of CellViewHolder on "onCreateCellViewHolder"
         return 0;
     }
 
     @Override
     public int getRowHeaderItemViewType(int rowPosition) {
         // The unique ID for this type of row header item
         // If you have different items for Row Header View by Y (Row) position, 
         // then you should fill this method to be able create different 
         // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
         return 0;
     }
 
     @Override
     public int getCellItemViewType(int columnPosition) {
         // The unique ID for this type of cell item
         // If you have different items for Cell View by X (Column) position, 
         // then you should fill this method to be able create different 
         // type of CellViewHolder on "onCreateCellViewHolder"
         return 0;
     }
 }
```
 
### 3. Set the Adapter to the TableView
 
 > ```AbstractTableAdapter``` class requires 3 different lists which represent respectively; **ColumnHeader**, **RowHeader** and **Cell** ViewModels.

 Assuming that we have the 3 item lists below:
 
 ```java
    private List<RowHeader> mRowHeaderList;
    private List<ColumnHeader> mColumnHeaderList;
    private List<List<Cell>> mCellList;
 ```
Setting data using our TableView adapter like this:
 
 ```java
    TableView tableView = new TableView(getContext());
    
    // Create our custom TableView Adapter
    MyTableViewAdapter adapter = new MyTableViewAdapter(getContext());
    
    // Set this adapter to the our TableView
    tableView.setAdapter(adapter);
    
    // Let's set datas of the TableView on the Adapter
    adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
 ```
 
 ### 4. Set Click listener to the TableView
 
 ```java
     public class MyTableViewListener implements ITableViewListener {
     
         /**
          * Called when user click any cell item.
          *
          * @param cellView  : Clicked Cell ViewHolder.
          * @param columnPosition : X (Column) position of Clicked Cell item.
          * @param rowPosition : Y (Row) position of Clicked Cell item.
          */
         @Override
         public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int columnPosition, int
                 rowPosition) {
             // Do what you want.
         }
    
         /**
          * Called when user long press any cell item.
          *
          * @param cellView : Long Pressed Cell ViewHolder.
          * @param column   : X (Column) position of Long Pressed Cell item.
          * @param row      : Y (Row) position of Long Pressed Cell item.
          */
         @Override
         public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
            // Do What you want
         }
     
         /**
          * Called when user click any column header item.
          *
          * @param columnHeaderView : Clicked Column Header ViewHolder.
          * @param columnPosition        : X (Column) position of Clicked Column Header item.
          */
         @Override
         public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int
                 columnPosition) {
             // Do what you want.
         }
         
         /**
          * Called when user click any column header item.
          *                   
          * @param columnHeaderView : Long pressed Column Header ViewHolder.
          * @param columnPosition        : X (Column) position of Clicked Column Header item.
          * @version 0.8.5.1
          */
         @Override
         public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int
                  columnPosition) {
              // Do what you want.
         }
     
         /**
          * Called when user click any Row Header item.
          *
          * @param rowHeaderView : Clicked Row Header ViewHolder.
          * @param rowPosition     : Y (Row) position of Clicked Row Header item.
          */
         @Override
         public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int
                 rowPosition) {
             // Do what you want.
     
         }    
         
         /**
          * Called when user click any Row Header item.
          *
          * @param rowHeaderView : Long pressed Row Header ViewHolder.
          * @param rowPosition     : Y (Row) position of Clicked Row Header item.
          * @version 0.8.5.1
          */
         @Override
         public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int
                  rowPosition) {
              // Do what you want.
      
         }
    }
```

Setting the listener to the TableView

```java
    tableView.setTableViewListener(new MyTableViewListener());
``` 

## Sorting

TableView has a sorting functionality with 0.8.5.1 version. TableView does not store or copy the data in its TableModel;
instead it maintains a map from the row indexes of the view to the row indexes of the model. 

### 1. ISortableModel to your Cell Model 

To be able use this feature on your TableView. You have to implement **ISortableModel** to your Cell Model. This interface
wants two methods from your cell model. These are:

- To compare sorted items ordered by normal items in terms of "**are _items_ the same**":
```java
    String getId()
```

- To compare sorted items ordered by normal items in terms of  "**are _contents_ the same**":
```java
    Object getContent()
```

As you seen getContent value returns Object. TableView controls the type of the object. And It sorts by considering to the type of class.
So you can sort any type of value. Such as; 

- **Number**
- **String**
- **Date**
- **Boolean**
- **Comparable**

### 2. AbstractSorterViewHolder to your Column Header ViewHolder
 
AbstractSorterViewHolder helps to listen to change of sorting actions. So you can do whatever you want on any sorting state.

- This interface method will be called after each sorting process. *Note* : It will be also called every recycling process.
```java
    onSortingStatusChanged(SortState sortState)
```
- This method gives current Sorting state:
```java
    SortState getSortState()
```

#### Sorting States

```java
    /**
     * Enumeration value indicating the items are sorted in increasing order.
     * For example, the set <code>1, 4, 0</code> sorted in
     * <code>ASCENDING</code> order is <code>0, 1, 4</code>.
     */
     ASCENDING,
    
    /**
     * Enumeration value indicating the items are sorted in decreasing order.
     * For example, the set <code>1, 4, 0</code> sorted in
     * <code>DESCENDING</code> order is <code>4, 1, 0</code>.
     */
    DESCENDING,
    
    /**
     * Enumeration value indicating the items are unordered.
     * For example, the set <code>1, 4, 0</code> in
     * <code>UNSORTED</code> order is <code>1, 4, 0</code>.
     */
    UNSORTED
```

### 3. Helper methods for sorting process

Several helper methods have been inserted on TableView. These are:
- To **sort the TableView according to a specified column**:
```java
    sortColumn(int column, SortState sortState)
```
- To **get the current sorting state of a column**:
```java
    SortState getSortingStatus(int column)
```

## Filtering

As of version **0.8.6**, a **filtering** functionality has been added.
>Filtering, by definition and usage in this context, is displaying a subset of data into the TableView based on a given filter globally. on a specified column or combination.

### Steps to implement filtering functionality into TableView

1. Implement the **IFilterableModel** interface to your Cell Item Model. This interface would require you to implement the method ```getFilterableKeyword()```.
2. The ```getFilterableKeyword()``` requires a **String** value to be returned. Make sure that this item is unique for a specific filter and without conflict to other filter Strings.
3. Create a **Filter** instance passing the created TableView:
```java
    ...
    private TableView   tableView;
    private Filter      tableViewFilter;
    ...    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        tableView = setUpTableView();
        tableViewFilter = new Filter(tableView);
        ...
    }
    ...
```
4. Filtering can be done for the whole table (global), for a specified column or combination. The filtering call is very easy to use once the **Filter** object is created:
```java
    ...
    public void filterWholeTable(String filterKeyword) {
        tableViewFilter.set(filterKeyword);
    }
    
    // assuming that Gender column is the 4th column in the TableView
    public void filterTableForGender(String genderFilterKeyword) {
        tableViewFilter.set(3, genderFilterKeyword);
    }
    
    public void filterTableColumnForKeyword(int column, String keyword) {
        tableViewFilter.set(column, keyword);
    }
    ...
```

#### Filtering notes

1. For clearing a filter, just pass an **empty** String ("" **AND NOT ```null```**) to the ```set``` method.
```java
    ...
    public void clearWholeTableFilter() {
        tableViewFilter.set("");
    }
    
    // assuming that Gender column is the 4th column in the TableView
    public void clearFilterForGender() {
        tableViewFilter.set(3, "");
    }
    ...
```
2. Multiple filtering combinations are supported such as COLUMN + WHOLE TABLE filter or MULTIPLE COLUMNS filter: e.g. "Happy" + "Boy" + all strings with a '-' character.
3. Based on step 2 of the implementation steps above, **FilterableKeyword** for different types of filters must be unique Strings **AND** no common substring. For instance, "**_male_**" and "**_female_**" should not be used together as filter keywords, since the method for processing filters uses ```String.contains(CharSequence s)```, the filtering process will return all data with **male** keyword, thus, **female** is also included in the filtered data set. Better use "boy" and "girl" or the _hashed_ values of your keyword Strings.
4. Advanced usage: **FilterChangedListener** is also available and can be implemented if you want to do something whenever the TableView is filtered. Public listener methods include:
```java
    // FilterChangedListener implementation:
    ...
    tableView.getFilterHandler().addFilterChangedListener(filterChangedListener);
    ...
    
    // The filterChangedListener variable:
    private FilterChangedListener filterChangedListener =
            new FilterChangedListener() {
                /**
                 * Called when a filter has been changed.
                 *
                 * @param filteredCellItems      The list of filtered cell items.
                 * @param filteredRowHeaderItems The list of filtered row items.
                 */
                @Override
                public void onFilterChanged(List<List<T>> filteredCellItems, List<T> filteredRowHeaderItems) {
                    // do something here...
                }
                
                /**
                 * Called when the TableView filters are cleared.
                 *
                 * @param originalCellItems      The unfiltered cell item list.
                 * @param originalRowHeaderItems The unfiltered row header list.
                 */
                @Override
                public void onFilterCleared(List<List<T>> originalCellItems, List<T> originalRowHeaderItems) {
                    // do something here...
                }
    };
```

## Pagination

As of version **0.8.6**, a **pagination** functionality has been added.
>Pagination, by definition and usage in this context, is the division of the whole set of data into subsets called pages and loading the data into the TableView page-by-page and not the whole data directly. This is useful if you have a large amount of data to be displayed.

### Steps to implement pagination functionality into TableView

##### Creating views to control the Pagination

_Depending on your preference, you may not follow the following and create your own implementation._
1. Create a layout with the following components: Two **Button** views to control next and previous page, a **Spinner** if you want to have a customized number of pagination (e.g. 10, 20, 50, All), an **EditText** to have a user input on which page s/he wants to go to, a **TextView** to display details (e.g. _Showing page X, items Y-Z_)
2. Asign the views with the controls and methods which are discussed below.

##### Implementation of the pagination

1. Create a **Pagination** instance passing the created TableView.
```java
    ...
    private TableView   tableView;
    private Pagination  mPagination;
    ...    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        tableView = setUpTableView();
        mPagination = new Pagination(mTableView);
        ...
    }
    ...

```

- The Pagination class has three possible constructors: (1) passing the TableView instance only, (2) TableView and the initial ITEMS_PER_PAGE and (3) TableView, initial ITEMS_PER_PAGE and the OnTableViewPageTurnedListener. By default, if no ITEMS_PER_PAGE specified, the TableView will be paginated into **10** items per page.
```java
    /**
     * Basic constructor, TableView instance is required.
     *
     * @param tableView The TableView to be paginated.
     */
    public Pagination(ITableView tableView) { ... }
    
    /**
     * Applies pagination to the supplied TableView with number of items per page.
     *
     * @param tableView    The TableView to be paginated.
     * @param itemsPerPage The number of items per page.
     */
    public Pagination(ITableView tableView, int itemsPerPage) { ... }
    
    /**
     * Applies pagination to the supplied TableView with number of items per page and an
     * OnTableViewPageTurnedListener for handling changes in the TableView pagination.
     *
     * @param tableView    The TableView to be paginated.
     * @param itemsPerPage The number of items per page.
     * @param listener     The OnTableViewPageTurnedListener for the TableView.
     */
    public Pagination(ITableView tableView, int itemsPerPage, OnTableViewPageTurnedListener listener) { ... }
```
2. **Loading the next page of items** into the TableView using the ```nextPage()``` method. You can assign this to your implementation of nextPageButton onClick action:
```java
    ...
    public void nextTablePage() {
        mPagination.nextPage();
    }
    ...
```

2. **Loading the previous page of items** into the TableView using the ```previousPage()``` method. You can assign this to your implementation of previousPageButton onClick action:
```java
    ...
    public void previousTablePage() {
        mPagination.previousPage();
    }
    ...
```

3. You can navigate through the pages by **going to a specific page directly** using the ```goToPage(int page)``` method. You can assign this to the EditText field TextChanged action (using TextWatcher):
```java
    ...
    public void goToTablePage(int page) {
        mPagination.goToPage(page);
    }
    ...
```

4. You can customize and **set the number of items to be displayed per page** of the TableView using the ```setItemsPerPage(int itemsPerPage)``` method. You can assign this to your Spinner with the number of items per page list:
```java
    ...
    public void setTableItemsPerPage(int itemsPerPage) {
        mPagination.setItemsPerPage(itemsPerPage);
    }
    ...
```

5. Advanced usage: A listener interface (**Pagination.OnTableViewPageTurnedListener**) can also be implemented if you want to do something everytime _a page is turned_ (e.g. previous, next, goToPage or change items per page action is called):
```java
    ...
        mPagination = new Pagination(mTableView);
        mPagination.setOnTableViewPageTurnedListener(onTableViewPageTurnedListener);
    ...
    
    private Pagination.OnTableViewPageTurnedListener onTableViewPageTurnedListener =
            new Pagination.OnTableViewPageTurnedListener() {
                /**
                 * Called when the page is changed in the TableView.
                 *
                 * @param numItems   The number of items currently being displayed in the TableView.
                 * @param itemsStart The starting item currently being displayed in the TableView.
                 * @param itemsEnd   The ending item currently being displayed in the TableView.
                 */
                @Override
                public void onPageTurned(int numItems, int itemsStart, int itemsEnd) {
                    // Do something here...
                    // You can update a TextView to display details (e.g. Showing page X, items Y-Z)
                }
            };
    ...
```

#### Pagination notes
1. Other methods which can be used from Pagination:
```java
    /**
     * Removes the OnTableViewPageTurnedListener for this Pagination.
     */
    void removeOnTableViewPageTurnedListener();
    
    /**
     * @return The current page loaded to the table view.
     */
    int getCurrentPage();
    
    /**
     * @return The number of items per page loaded to the table view.
     */
    int getItemsPerPage();
    
    /**
     * @return The number of pages in the pagination.
     */
    int getPageCount();
    
    /**
     * @return Current pagination state of the table view.
     */
    boolean isPaginated();
```

2. Pagination and Filtering works seamlessly: Filter action <---> Paginate action, e.g. You filter for all "Boy" then paginate by 50 items per page OR You paginate by 25 items per page and go to a specific page and then filter all "Sad", etc.

## Change your TableView model 

As of version **0.8.5.1**, TableView has some helper functions to change desired cell item model easily. These are the following:

- To **add a row**:
```java
    addRow(int rowPosition, YourRowHeaderModel rowHeaderItem, List<YourCellItemModel> cellItems)
```

- To **add a multiple rows**:
```java
    addRowRange(int rowPositionStart, List<YourRowHeaderModel> rowHeaderItem, List<List<YourCellItemModel>> cellItems)
```

- To **remove a row**:
```java
    removeRow(int rowPosition)
``` 

- To **remove multiple rows**:
```java
    removeRowRange(int rowPositionStart, int itemCount)
``` 

- To **update a row header**:
```java
    changeRowHeaderItem(int rowPosition, YourRowHeaderModel rowHeaderModel)
```

- To **update multiple row headers**:
```java
    changeRowHeaderItemRange(int rowPositionStart, List<YourRowHeaderModel> rowHeaderModelList)
``` 

- To **update a cell item**:
```java
    changeCellItem(int columnPosition, int rowPosition, YourCellItemModel cellModel)
``` 

- To **update a column header**:
```java
    changeColumnHeader(int columnPosition, YourColumnHeaderModel columnHeaderModel)
``` 

- To **update multiple column headers**:
```java
    changeColumnHeaderRange(int columnPositionStart, List<YourColumnHeaderModel> columnHeaderModelList)
```

*Note:*[TableViewSample 2 app](https://github.com/evrencoskun/TableViewSample2) also shows usage of these helper methods.

## Hiding & Showing the Row

With 0.8.5.1 version, hiding and showing any of row is pretty easy for TableView.  For that several helper methods have been inserted on TableView. 

- To **show** a row:
```java
    showRow(int row)
```

- To **hide** the row:
```java
    hideRow(int row)
```

- To **show all hidden** rows:
```java
    showAllHiddenRows()
```

- TableView store a map that contains all hidden rows. This method for the time that is necessary to **clear the list of hidden** rows. 
```java
    clearHiddenRowList()
```

- To **check the visibility state** of a row:
```java
    isRowVisible(int row)
```

## Hiding & Showing the Column

With 0.8.5.5 version, hiding and showing any of column is pretty easy for TableView.  For that several helper methods have been inserted on TableView. 

- To **show** a column:
```java
    showColumn(int column)
```

- To **hide** a column:
```java
    hideColumn(int column)
```

- To **show all hidden** columns
```java
    showAllHiddenColumns()
```

- TableView store a map that contains all hidden columns. This method for the time that is necessary to **clear the list of hidden columns**.
```java
    clearHiddenColumnList()
```

- To **check the visibility state** of a column:
```java
    isColumnVisible(int column)
```
## Advanced Usage

- To recalculate the desired column. Sample app shows also its usage as well.

```java
    remeasureColumnWidth(int column);
``` 

- To ignore column width calculation for better performance, the below line can be used.
  
```java
    tableView.setHasFixedWidth(false);
``` 

- To ignore setting selection colors that are displayed by user interaction, the below line can be used.

```java
    tableView.setIgnoreSelectionColors(false);
``` 

- To show or hide separators of the TableView, you can simply use these helper methods.

```java
    setShowHorizontalSeparators(boolean showSeparators)
```

```java
    setShowVerticalSeparators(boolean showSeparators)
```

- There are 2 new helper methods to scroll desired column or row position programmatically.

```java
    scrollToColumnPosition(int column)
```

```java
    scrollToRowPosition(int row)
```

- With 0.8.8 version, Column width values can be set programatically using the below function. 
 
```java
    setColumnWidth(int columnPosition, int width)
```

## Sample Apps

- This repository has an Sample Application of TableView.
- [TableViewSample 2](https://github.com/evrencoskun/TableViewSample2)
- (contact me to mention your app on this page)

## Article

- Coming soon.

## Communication

- If you **need help**, please use [Stack Overflow](https://stackoverflow.com/questions/tagged/tableview+android). (Tag 'TableView' and 'Android')
- If you'd like to **ask a general question**, please use [Stack Overflow](https://stackoverflow.com/questions/tagged/tableview+android).
- If you **found a bug**, please open an issue.
- If you **have a feature request**, please open an issue.
- If you **want to contribute**, please submit a pull request.
- If you **use the control**, please contact me to mention your app on this page.

## Contributors

Contributions of any kind are welcome! I would like to thank the following [contributors](https://github.com/evrencoskun/TableView/blob/master/CONTRIBUTORS.md) for sharing code and 
making TableView library a better product. 

## License

    Copyright (c) 2017 Evren Coşkun
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
