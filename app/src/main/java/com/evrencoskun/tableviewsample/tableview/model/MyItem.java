package com.evrencoskun.tableviewsample.tableview.model;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.sort.ISortableModel;

import java.util.Random;

public class MyItem implements ISortableModel {
    // Columns indexes
    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_RANDOM_LONG = 1;
    public static final int COLUMN_INDEX_RAMDOM_SHORT = 2;
    public static final int COLUMN_INDEX_MOOD_HAPPY = 3;
    public static final int COLUMN_INDEX_GENDER_MALE = 4;

    private final Integer mRandom;
    private final Integer mRandomShort;
    private final Object[] columns;
    @NonNull
    private final String mId;
    @Nullable
    private final String mText;
    public final boolean mGenderMale;
    public final boolean mMoodHappy;

    public MyItem(@NonNull String id) {
        this.mId = id;
        this.mText = "cell " + mId + " 1";
        mGenderMale = new Random().nextBoolean();
        mMoodHappy = new Random().nextBoolean();
        mRandom = abs(new Random().nextInt());
        mRandomShort = mRandom % 100;
        columns = new Object[]{mRandom, mRandomShort, mText, mGenderMale, mMoodHappy};
    }

    /**
     * This is necessary for sorting process. Id must be unique per data row.
     * See {@link ISortableModel}.
     */
    @NonNull
    @Override
    public String getId() {
        return mId;
    }

    /**
     * This is necessary for sorting process.
     * See {@link ISortableModel}.
     */
    @Nullable
    @Override
    public Object getContent(int column) {
        if (column >= 0 && column < columns.length) {
            return columns[column];
        }
        return  "cell " + mId + " " + column;
    }
}
