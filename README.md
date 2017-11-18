<!-- ![Image](https://raw.githubusercontent.com/evrencoskun/TableViewSample/master/Logo.png) -->
# TableView for Android
TableView is a powerful Android library for displaying complex data structures and rendering tabular data composed of rows, columns and cells. 
TableView relies on a separate model object to hold and represent the data it displays.
This repository also contains a **TableViewSample** that is
designed to show you how to create your own TableView in your application.

[![TableView v0.8.2 screen shot](https://raw.githubusercontent.com/evrencoskun/TableViewSample/master/tableview-0_8_2_screenshot.png)](https://www.youtube.com/watch?v=Bui84mc5Xco)


## Installation

To use this library in your android project, just simply add the following dependency into your build.gradle

``` 
dependencies {
 compile 'com.evrencoskun.library:tableview:0.8.2' 
}
```

## Features
  - [x] Each column width value can be calculated automatically considering the largest one
  - [x] Setting your own model class to displayed in a table view easily.
  - [x] TableView has an click listener to listen user touch interaction for each cell.
  
## Documentation

### XML 

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

### List of Default Constants

Constants            |  Type           | Explanation
---------------------|-----------------|------------
column_header_height | Dimension       | Even if it's name is column header height, This value is taken into consideration for all cells, column headers and row Headers height. 
row_header_width     | Dimension       | Unlike Cells  and Column headers, width of Row Headers is constant. So this value uses for this purpose.
selected_color       | Color int       | Selected background color of Cells / Row Headers / Column Headers
unselected_color     | Color int       | Default background color of Cells / Row Headers / Column Headers
shadow_color         | Color int       | When a cell view is selected, both the column header and the row header which are located on x and y position of Cell view are colored to this value.


### Programmatically 

```
TableView tableView = new TableView(getContext());
```



###  Implement your item on TableView 
 To be able show your items on TableView, you must follow the below steps.



####  1. Create your TableViewAdapter
 Firstly, you must create your custom TableView Adapter  which extends from ```AbstractTableAdapter``` class. 
 ```AbstractTableAdapter``` class wants 3 different lists which represent respectively; ColumnHeader, RowHeader and Cell views model.

 **For example;** 
 
 
 Assume that we have 3 below list items.

     private List<MyRowHeaderModel> mRowHeaderList;
     private List<MyColumnHeaderModel> mColumnHeaderList;
     private List<List<MyCellModel>> mCellList;
    
 For these lists, Your custom TableView Adapter should be created like this;
      
     public class MyTableViewAdapter extends AbstractTableAdapter<MyColumnHeaderModel, MyRowHeaderModel, MyCellModel> {
          ....
    
    }
    
    
 ``` AbstractTableAdapter``` class has some abstract methods that you must fill them. 
 
 
 
 > If you familiar with RecyclerView,
 these methods will familiar to you as well. Because TableView comes from three powerful & talented RecyclerViews.
 
 
 
 **onCreateCellViewHolder :** That is where you create your custom Cell ViewHolder. This method is called when 
 Cell RecyclerView of the TableView needs a new RecyclerView.ViewHolder of the given type to represent an item.


 Assume that your Cell ViewHolder xml layout like this. 
 In res/layout folder, let's give a name like  ```my_TableView_cell_layout.xml ```
 
     <?xml version="1.0" encoding="utf-8"?>
     <LinearLayout
         xmlns:android="http://schemas.android.com/apk/res/android"
         xmlns:tools="http://schemas.android.com/tools"
         android:layout_width="wrap_content"
         android:layout_height="@dimen/cell_height"
         android:background="@color/cell_background_color"
         android:orientation="vertical">
     
     
         <TextView
             android:id="@+id/cell_data"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             android:layout_gravity="center_vertical"
             android:layout_marginStart="10dp"
             android:layout_marginEnd="10dp"
             android:gravity="center"
             android:maxLines="1"
             android:textColor="@color/cell_text_color"
             android:textSize="@dimen/text_size"
             />
     
     </LinearLayout>
 
 
 
 Let's create simple ViewHolder. However, **Don't forget** This ViewHolder must be extended from ```AbstractViewHolder``` which has some protected methods to help you on selection.


     class MyCellViewHolder extends AbstractViewHolder {
     
         public final TextView cell_textview;
     
         public CellViewHolder(View itemView) {
             super(itemView);
             cell_textview = (TextView) itemView.findViewById(R.id.cell_data);
         }
     }


 Now Let's fill ```onCreateCellViewHolder``` method like this;  
 
 
    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        // Getting custome Cell View Layout from xml 
        View layout = LayoutInflater.from(m_jContext).inflate(R.layout.my_TableView_cell_layout,
                parent, false);
                
        // And creating sample custom CellViewHolder
        return new MyCellViewHolder(layout);
    }


**onBindCellViewHolder :** That is where you set Cell View Model data to your custom Cell ViewHolder. This method is Called by Cell RecyclerView of the TableView to display the data at the specified position.
This method gives you everything you need about a cell item. 

Parameter    |      Type                              | Explanation
-------------|----------------------------------------|-----------------------------------------------------------------------------------------------
holder       |  AbstractViewHolder (MyCellViewHolder) | This is one of ```MyCellViewHolder``` that was created on ```onCreateCellViewHolder``` method.
pValue       |  Object (MyCellModel)                  | This is the cell view model located on this position.
pXPosition   |  int                                   | This is the X (Column) position of the cell item.
pYPosition   |  int                                   | This is the Y (Row) position of the cell item.


    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object pValue, int pXPosition, int pYPosition) {
            
        MyCellModel cell = (MyCellModel) pValue;

        MyCellViewHolder viewHolder = (MyCellViewHolder) holder;
        viewHolder.cell_textview.setText(cell.getData());

        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.
        
        // It is necessary to remeasure itself 
        viewHolder.ItemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.cell_textview.requestLayout();
    }




to be continued.. 




## Communication

- If you **need help**, please use [Stack Overflow](https://stackoverflow.com/questions/tagged/tableview+android). (Tag 'TableView' and 'Android')
- If you'd like to **ask a general question**, please use [Stack Overflow](https://stackoverflow.com/questions/tagged/tableview+android).
- If you **found a bug**, please open an issue.
- If you **have a feature request**, please open an issue.
- If you **want to contribute**, please submit a pull request.
- If you **use the control**, please contact me to mention your app on this page.


## License

TableView is released under the MIT license. See LICENSE for details.