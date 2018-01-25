package com.evrencoskun.tableviewsample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableviewsample.tableview.TableViewAdapter;
import com.evrencoskun.tableviewsample.tableview.TableViewListener;
import com.evrencoskun.tableviewsample.tableview.model.Cell;
import com.evrencoskun.tableviewsample.tableview.model.ColumnHeader;
import com.evrencoskun.tableviewsample.tableview.model.RowHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public static final int COLUMN_SIZE = 100;
    public static final int ROW_SIZE = 100;

    private List<RowHeader> m_jRowHeaderList;
    private List<ColumnHeader> m_jColumnHeaderList;
    private List<List<Cell>> m_jCellList;

    private TableViewAdapter m_iTableViewAdapter;
    private TableView m_iTableView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

        //setFullScreenMode();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        RelativeLayout fragment_container = (RelativeLayout) view.findViewById(R.id
                .fragment_container);

        // Create Table view
        m_iTableView = createTableView();
        fragment_container.addView(m_iTableView);

        loadData();
        return view;
    }

    private TableView createTableView() {
        TableView tableView = new TableView(getContext());

        // Set adapter
        m_iTableViewAdapter = new TableViewAdapter(getContext());
        tableView.setAdapter(m_iTableViewAdapter);

        // Set layout params
        FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                .MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        tableView.setLayoutParams(tlp);

        // Set TableView listener
        tableView.setTableViewListener(new TableViewListener(tableView));

        //Setting ghost columns to hide
        m_iTableViewAdapter.setGhostColumns("HiddenColumn");

        return tableView;
    }


    private void initData() {
        m_jRowHeaderList = new ArrayList<>();
        m_jColumnHeaderList = new ArrayList<>();
        m_jCellList = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            m_jCellList.add(new ArrayList<Cell>());
        }
    }

    private void loadData() {
        List<RowHeader> rowHeaders = getRowHeaderList();
        List<List<Cell>> cellList = getCellListForSorting(); // getCellList();
        // getRandomCellList(); //
        List<ColumnHeader> columnHeaders = getColumnHeaderList(); //getRandomColumnHeaderList(); //

        m_jRowHeaderList.addAll(rowHeaders);
        for (int i = 0; i < cellList.size(); i++) {
            m_jCellList.get(i).addAll(cellList.get(i));
        }

        // Load all data
        m_jColumnHeaderList.addAll(columnHeaders);
        m_iTableViewAdapter.setAllItems(m_jColumnHeaderList, m_jRowHeaderList, m_jCellList);

    }

    private List<RowHeader> getRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            RowHeader header = new RowHeader(String.valueOf(i), "row " + i);
            list.add(header);
        }

        return list;
    }

    private List<ColumnHeader> getColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();

        //Ghost column
        list.add(new ColumnHeader("HiddenColumnId", "HiddenColumn"));

        for (int i = 0; i < COLUMN_SIZE; i++) {
            String strTitle = "column " + i;
            if (i % 6 == 2) {
                strTitle = "large column " + i;
            }
            ColumnHeader header = new ColumnHeader(String.valueOf(i), strTitle);
            list.add(header);
        }


        return list;
    }

    private List<ColumnHeader> getRandomColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();

        for (int i = 0; i < COLUMN_SIZE; i++) {
            String strTitle = "column " + i;
            int nRandom = new Random().nextInt();
            if (nRandom % 4 == 0 || nRandom % 3 == 0 || nRandom == i) {
                strTitle = "large column " + i;
            }

            ColumnHeader header = new ColumnHeader(String.valueOf(i), strTitle);
            list.add(header);
        }

        return list;
    }

    private List<List<Cell>> getCellList() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < COLUMN_SIZE; j++) {
                String strText = "cell " + j + " " + i;
                if (j % 4 == 0 && i % 5 == 0) {
                    strText = "large cell " + j + " " + i + ".";
                }
                String strID = j + "-" + i;

                Cell cell = new Cell(strID, strText);
                cellList.add(cell);
            }
            list.add(cellList);
        }

        return list;
    }

    private List<List<Cell>> getCellListForSorting() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < COLUMN_SIZE; j++) {
                Object strText = "cell " + j + " " + i;

                if (j == 0) {
                    strText = i;
                } else if (j == 1) {
                    int nRandom = new Random().nextInt();
                    strText = nRandom;
                }

                String strID = j + "-" + i;

                Cell cell = new Cell(strID, strText);
                cellList.add(cell);
            }
            list.add(cellList);
        }

        return list;
    }


    private List<List<Cell>> getRandomCellList() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            list.add(cellList);
            for (int j = 0; j < COLUMN_SIZE; j++) {
                String strText = "cell " + j + " " + i;
                int nRandom = new Random().nextInt();
                if (nRandom % 2 == 0 || nRandom % 5 == 0 || nRandom == j) {
                    strText = "large cell  " + j + " " + i + getRandomString() + ".";
                }

                String strID = j + "-" + i;

                Cell cell = new Cell(strID, strText);
                cellList.add(cell);
            }
        }

        return list;
    }


    private String getRandomString() {
        Random r = new Random();
        String str = " a ";
        for (int i = 0; i < r.nextInt(); i++) {
            str = str + " a ";
        }

        return str;
    }

    private void setFullScreenMode() {
        // Set full screen mode
        this.getActivity().getWindow().getDecorView().setSystemUiVisibility(View
                .SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View
                .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
                // nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
