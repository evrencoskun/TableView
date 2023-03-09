/*
 * MIT License
 *
 * Copyright (c) 2023 K3b
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.evrencoskun.tableviewsample.tableview;

import com.evrencoskun.tableviewsample.tableview.model.MySamplePojo;
import com.evrencoskun.tableviewutil.ColumnDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This is where all Testdata for the demo app comes from.
 *
 * Created by k3b on 2023-03-09
 */

public class TestData {
    // Columns indexes
    public static final int COLUMN_INDEX_MOOD_HAPPY = 3;
    public static final int COLUMN_INDEX_GENDER_MALE = 4;
    // Constant size for dummy data sets
    private static final int COLUMN_SIZE = 500;
    private static final int ROW_SIZE = 500;

    public static List<MySamplePojo> createSampleData() {
        List<MySamplePojo> sampleData = new ArrayList<>();
        for(int i = 0; i < ROW_SIZE; i++) {
            MySamplePojo header = new MySamplePojo(String.valueOf(i));
            sampleData.add(header);
        }
        return sampleData;
    }

    public static List<ColumnDefinition<MySamplePojo>> createColumnDefinitions() {
        List<ColumnDefinition<MySamplePojo>> definitions = new ArrayList<>();
        definitions.addAll(
                Arrays.asList(
                        new ColumnDefinition<>("Random 0", r -> r.mRandom),
                        new ColumnDefinition<>("Short 1", r -> r.mRandomShort),
                        new ColumnDefinition<>("Text 2", r1 -> r1.mText),
                        new ColumnDefinition<>("Gender 3", r -> r.mGenderMale),
                        new ColumnDefinition<>("Mood 4", r -> r.mMoodHappy)));
        for (int i = 5; i < COLUMN_SIZE;i++) {
            final int columnNumber = i;
            boolean large = new Random().nextBoolean();
            definitions.add(new ColumnDefinition<>(
                    (large ? "Lage Column " : "Column ") + i,
                    r -> r.getColumnValue(columnNumber)));
        }
        return definitions;
    }

}
