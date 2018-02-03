/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private MainFragment mainFragment;
    private EditText searchField;
    private Spinner moodFilter, genderFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        searchField = findViewById(R.id.query_string);
        moodFilter = findViewById(R.id.mood_spinner);
        genderFilter = findViewById(R.id.gender_spinner);
        searchField.addTextChangedListener(onSearchTextChange);
        moodFilter.setOnItemSelectedListener(onMoodSelectedListener);
        genderFilter.setOnItemSelectedListener(onGenderSelectedListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.activity_container,
                    mainFragment, mainFragment.getClass().getSimpleName()).commit();
        }

    }

    @Override
    protected void onDestroy() {
        mainFragment.onDestroy();
        super.onDestroy();
    }

    private TextWatcher onSearchTextChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mainFragment.filterTable(String.valueOf(s));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private AdapterView.OnItemSelectedListener onMoodSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mainFragment.filterTableForMood(parent.getItemAtPosition(position).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener onGenderSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String filter = "";
            switch (parent.getItemAtPosition(position).toString()) {
                case "":
                    filter = "";
                    break;
                case "Male":
                    filter = "boy";
                    break;
                case "Female":
                    filter = "girl";
                    break;
            }

            mainFragment.filterTableForGender(filter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
