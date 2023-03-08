package com.evrencoskun.tableview.model;

public interface IColumnValue<T> {
    Object get(T row);
}
