changelog.md

TableView is a powerful and beatifull looking Android library for displaying complex 
data structures and rendering tabular data composed of rows, columns and cells.

The current implementation at https://github.com/evrencoskun/TableView
requires a lot of copy&paste from example code to use the TableView in a clientapp.

Product vision: reduce the amount of (copied) code to integrate TableView into a clientapp.

* v done: replaced the table header modell with simple Strings.
* v done: replaced the must-customize Cell.java with a templated Cell<Pojo> where customisation takes place in the pojo
* v done: keep demo app intact using MySamplePojo
* v done: customized TableViewAdapter inherits from new generic TableViewAdapterBase<Pojo> 
* v done: replace List<List<Cell<Pojo>>> with List<IRow<Pojo>> 
* . (planed): additional lib containing the generic gui classes and resources
