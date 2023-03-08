package com.evrencoskun.tableview.modell;

public interface IColumnValue<T> {
    Object get(T row);
}
