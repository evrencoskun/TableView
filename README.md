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
      <img src="https://raw.githubusercontent.com/evrencoskun/TableViewSample/master/TableView-0_8_5_1.gif">
    </a>
</p>


## Features
  - [x] Each column width value can be calculated automatically considering the largest one
  - [x] Setting your own model class to displayed in a table view easily.
  - [x] TableView has an click listener to listen user touch interaction for each cell.
  - [x] TableView columns can be sorted ascendingly or descendingly. (*NEW*)
  - [x] Hiding & Showing the row is pretty easy. (*NEW*)
  
  
## What's new 
You can check new implementations of TableView on <a href="https://github.com/evrencoskun/TableView/releases/tag/v0.8.5.1">release page.</a> 

  
## Table of Contents
  - [Installation](#Installation)
  - [Implement your item on TableView](#Implement your item on TableView)
    1. [Create your TableView](#Create your TableView)
       - [XML](#XML)
       - [Programmatically](#Programmatically)
    2. [Create your TableViewAdapter](#Create your TableViewAdapter)
    3. [Set the Adapter to the TableView](#Set the Adapter to the TableView)
    4. [Set Click listener to the TableView](#Set Click listener to the TableView)   
  - [Sorting](#Sorting)
    1. [ISortableModel to your Cell Model](#ISortableModel to your Cell Model)
    2. [AbstractSorterViewHolder to your Column Header ViewHolder](#AbstractSorterViewHolder to your Column Header ViewHolder)
    3. [Helper methods for sorting process](#Helper methods for sorting process)
  - [Advanced Usage](##Advanced Usage)
  - [Sample Apps](#Sample Apps)
  - [Articles](#Articles)
  - [Communication](#Communication)
  - [License](#License)


## Installation

To use this library in your android project, just simply add the following dependency into your build.gradle

``` 
dependencies {
 compile 'com.evrencoskun.library:tableview:0.8.5.1' 
}
```

## Implement your item on TableView 

###1. Create your TableView
#### XML 

``` 
<com.evrencoskun.tableview.TableView
    android:id="@+id/content_container"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

As default constants can be set programmatically, it can be set by also using  xml attributes of TableView like this;

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
**Note:** To be able use these attributes on xml side, below **xmlns:** line should be added on root view. Otherwise, android studio gives you compile error.

``` 
xmlns:app="http://schemas.android.com/apk/res-auto"
```



#### Programmatically 

```
TableView tableView = new TableView(getContext());
```



### 2. Create your TableViewAdapter
 Firstly, you must create your custom TableView Adapter  which extends from ```AbstractTableAdapter``` class. 
 ```AbstractTableAdapter``` class wants 3 different lists which represent respectively; ColumnHeader, RowHeader and Cell views model.

 **For example;** 
 
 ```java
 public class MyTableViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {
  
     public MyTableViewAdapter(Context p_jContext) {
         super(p_jContext);
 
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
     public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
         // Get cell xml layout 
         View layout = LayoutInflater.from(m_jContext).inflate(R.layout.my_cell_layout,
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
      * @param p_jValue     : This is the cell view model located on this X and Y position. In this
      *                     example, the model class is "Cell".
      * @param p_nXPosition : This is the X (Column) position of the cell item.
      * @param p_nYPosition : This is the Y (Row) position of the cell item.
      *
      * @see #onCreateCellViewHolder(ViewGroup, int);
      */
     @Override
     public void onBindCellViewHolder(AbstractViewHolder holder, Object p_jValue, int 
             p_nXPosition, int p_nYPosition) {
         Cell cell = (Cell) p_jValue;
 
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
     public RecyclerView.ViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
 
         // Get Column Header xml Layout
         View layout = LayoutInflater.from(m_jContext).inflate(R.layout
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
      * @param p_jValue : This is the column header view model located on this X position. In this
      *                 example, the model class is "ColumnHeader".
      * @param position : This is the X (Column) position of the column header item.
      *
      * @see #onCreateColumnHeaderViewHolder(ViewGroup, int) ;
      */
     @Override
     public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int 
             position) {
         ColumnHeader columnHeader = (ColumnHeader) p_jValue;
 
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
     public RecyclerView.ViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
 
         // Get Row Header xml Layout
         View layout = LayoutInflater.from(m_jContext).inflate(R.layout
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
      * @param p_jValue : This is the row header view model located on this Y position. In this
      *                 example, the model class is "RowHeader".
      * @param position : This is the Y (row) position of the row header item.
      *
      * @see #onCreateRowHeaderViewHolder(ViewGroup, int) ;
      */
     @Override
     public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int 
             position) {
         RowHeader rowHeader = (RowHeader) p_jValue;
 
         // Get the holder to update row header item text
         MyRowHeaderViewHolder rowHeaderViewHolder = (MyRowHeaderViewHolder) holder;
         rowHeaderViewHolder.row_header_textview.setText(rowHeader.getData());
     }
 
 
     @Override
     public View onCreateCornerView() {
         // Get Corner xml layout
         return LayoutInflater.from(m_jContext).inflate(R.layout.table_view_corner_layout, null);
     }
 
     @Override
     public int getColumnHeaderItemViewType(int position) {
         // The unique ID for this type of column header item
         // If you have different items for Cell View by X (Column) position, 
         // then you should fill this method to be able create different 
         // type of CellViewHolder on "onCreateCellViewHolder"
         return 0;
     }
 
     @Override
     public int getRowHeaderItemViewType(int pYPosition) {
         // The unique ID for this type of row header item
         // If you have different items for Row Header View by Y (Row) position, 
         // then you should fill this method to be able create different 
         // type of RowHeaderViewHolder on "onCreateRowHeaderViewHolder"
         return 0;
     }
 
     @Override
     public int getCellItemViewType(int pXPosition) {
         // The unique ID for this type of cell item
         // If you have different items for Cell View by X (Column) position, 
         // then you should fill this method to be able create different 
         // type of CellViewHolder on "onCreateCellViewHolder"
         return 0;
     }
 }
```
 
### 3. Set the Adapter to the TableView
 
 > ```AbstractTableAdapter``` class wants 3 different lists which represent respectively; ColumnHeader, RowHeader and Cell views model.

 Assume that we have 3 below list items.
 
 ```java
  private List<RowHeader> mRowHeaderList;
  private List<ColumnHeader> mColumnHeaderList;
  private List<List<Cell>> mCellList;
 ```
Setting datas using our TableView adapter like this;
 
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
      * @param p_jCellView  : Clicked Cell ViewHolder.
      * @param p_nXPosition : X (Column) position of Clicked Cell item.
      * @param p_nYPosition : Y (Row) position of Clicked Cell item.
      */
     @Override
     public void onCellClicked(@NonNull RecyclerView.ViewHolder p_jCellView, int p_nXPosition, int
             p_nYPosition) {
         // Do want you want.
     }
 
     /**
      * Called when user click any column header item.
      *
      * @param p_jColumnHeaderView : Clicked Column Header ViewHolder.
      * @param p_nXPosition        : X (Column) position of Clicked Column Header item.
      */
     @Override
     public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int
             p_nXPosition) {
         // Do want you want.
     }
 
     /**
      * Called when user click any Row Header item.
      *
      * @param p_jRowHeaderView : Clicked Row Header ViewHolder.
      * @param p_nYPosition     : Y (Row) position of Clicked Row Header item.
      */
     @Override
     public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int
             p_nYPosition) {
         // Do want you want.
 
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

To be able use this feature on your TableView. You have to implement ISortableModel to your Cell Model. This interface
wants two method from your cell model. These are ;areItemsTheSame

- ```java String getId()``` : To compare sorted items ordered by normal items in terms of  "are Items The Same"
- ```java Object getContent()``` : To compare sorted items ordered by normal items in terms of  "are Contents The Same"

As you seen getContent value returns Object. TableView controls the type of the object. And It sorts by considering to the type of class.
So you can sort any type of value. Such as; 

- Number
- String
- Date
- Boolean
- Comparable

### 2. AbstractSorterViewHolder to your Column Header ViewHolder
 
AbstractSorterViewHolder helps to listen to change of sorting actions. So you can do whatever you want on any sorting state.

- ```java onSortingStatusChanged(SortState pSortState)``` : It will be called each sorting process. *Note*: It will also calls every recycling process as well.
- ```java SortState getSortState()``` : It gives current Sorting state.

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

Several helper methods have been inserted on TableView. These are;
- ```javasortColumn(int p_nColumnPosition, SortState p_eSortState)``` : To sort
- ```javaSortState getSortingStatus(int column)``` : To get current state of the column

## Advanced Usage

To ignore column width calculation for better performance, the below line can be used.
  
```java
tableView.setHasFixedWidth(false);
``` 

To ignore setting selection colors that are displayed by user interaction, the below line can be used.

```java
tableView.setIgnoreSelectionColors(false);
``` 

To show or hide separators of the TableView, you can simply use these helper methods.

```java
setShowHorizontalSeparators(boolean p_bShowSeparators)
```

```java
setShowVerticalSeparators(boolean p_bShowSeparators)
```

## Sample Apps

- This repository has an Sample Application of TableView.
- <a href="https://github.com/evrencoskun/TableViewSample2"> TableViewSample 2 </a>
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