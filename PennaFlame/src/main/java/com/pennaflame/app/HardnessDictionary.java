package com.pennaflame.app;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by stralko on 10/23/13.
 */
public class HardnessDictionary extends HashMap <String, ArrayList<CharSequence>> {

    private String mRowHeaderTitles[];

    private Context mContext;

    public HardnessDictionary(Context context) {
        mContext = context;
        Resources res = mContext.getResources();

        mRowHeaderTitles = res.getStringArray(R.array.row_header_titles);
        TypedArray myArray = res.obtainTypedArray(R.array.keys);
        int columns = myArray.length();
        for(int i = 0; i < columns; i++) {
            int rowid = myArray.peekValue(i).resourceId;
            TypedArray row = res.obtainTypedArray(rowid);

            int values = row.length();
            for(int k = 0; k < values; k++) {
                if (!containsKey(mRowHeaderTitles[i])) {
                    put(mRowHeaderTitles[i], new ArrayList<CharSequence>());
                }

                List<CharSequence> list = get(mRowHeaderTitles[i]);
                list.add(row.peekValue(k).coerceToString());
            }
        }
    }


}
