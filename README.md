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