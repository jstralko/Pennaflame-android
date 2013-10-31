package com.pennaflame.app;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

public class MetricActivity extends PennaFlameBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MetricFragment())
                    .commit();
        }
    }

    public static class MetricFragment extends Fragment implements Spinner.OnItemSelectedListener {

        private boolean enabledTextWatcherEvents = true;
        private Map<String, Float> mEnglishMap;
        private Map<String, Float> mEngMetricMap;
        private Map<String, Float> mMetricMap;
        private EditText leftEditText;
        private EditText rightEditText;
        private Spinner leftSpinner;
        private Spinner rightSpinner;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_metric, null);

            //load conversion values into Maps.
            String[] keys = getResources().getStringArray(R.array.metricKey);
            TypedArray values = getResources().obtainTypedArray(R.array.metricUnits);
            mMetricMap = new HashMap<String, Float>();
            int i = 0;
            for (String key : keys) {
                mMetricMap.put(key, values.getFloat(i, 0.0f));
                i++;
            }

            keys = getResources().getStringArray(R.array.englishMetricKeys);
            values = getResources().obtainTypedArray(R.array.englishMetric);
            mEngMetricMap = new HashMap<String, Float>();
            i = 0;
            for (String key : keys) {
                mEngMetricMap.put(key, values.getFloat(i, 0.0f));
                i++;
            }

            keys = getResources().getStringArray(R.array.englishKey);
            values = getResources().obtainTypedArray(R.array.englishUnits);
            mEnglishMap = new HashMap<String, Float>();
            i = 0;
            for (String key : keys) {
                mEnglishMap.put(key, values.getFloat(i, 0.0f));
                i++;
            }

            leftEditText = (EditText)view.findViewById(R.id.leftEditText);
            leftEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!enabledTextWatcherEvents) {
                        return;
                    }

                    String convertee = leftSpinner.getSelectedItem().toString();
                    String converter = rightSpinner.getSelectedItem().toString();
                    float fvalue = 0.0f;
                    try {
                        fvalue = Float.parseFloat(leftEditText.getText().toString());
                    } catch (NumberFormatException nfe) {
                        Log.w("PennaFlame", String.format("Failed to parse %s into Float", leftEditText.getText().toString()));
                    }
                    float value = MetricFragment.this.convertUnit(convertee, converter, fvalue);

                    enabledTextWatcherEvents = false;
                    rightEditText.setText(String.format("%4.2f", value));
                    enabledTextWatcherEvents = true;
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            rightEditText = (EditText)view.findViewById(R.id.rightEditText);
            rightEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!enabledTextWatcherEvents) {
                        return;
                    }

                    String convertee = rightSpinner.getSelectedItem().toString();
                    String converter = leftSpinner.getSelectedItem().toString();
                    float fvalue = 0.0f;
                    try {
                        fvalue = Float.parseFloat(rightEditText.getText().toString());
                    } catch (NumberFormatException nfe) {
                        Log.w("PennaFlame", String.format("Failed to parse %s into Float", rightEditText.getText().toString()));
                    }
                    float value = MetricFragment.this.convertUnit(convertee, converter, fvalue);

                    enabledTextWatcherEvents = false;
                    leftEditText.setText(String.format("%4.2f", value));
                    enabledTextWatcherEvents = true;
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            leftSpinner = (Spinner)view.findViewById(R.id.leftSpinner);
            leftSpinner.setSelection(4);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.units, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            leftSpinner.setAdapter(adapter);
            leftSpinner.setOnItemSelectedListener(this);

            rightSpinner = (Spinner)view.findViewById(R.id.rightSpinner);
            rightSpinner.setAdapter(adapter);
            rightSpinner.setSelection(0);
            rightSpinner.setOnItemSelectedListener(this);

            //float value = convertUnit("Inch", "Centimeter", 1.0f);
            //Log.d("PennaFlame", String.format("value: %f", value));

            return view;
        }

        public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
            float value;
            float fvalue = 0.0f;
            try {
                fvalue = Float.parseFloat(leftEditText.getText().toString());
            } catch (NumberFormatException nfe) {
                Log.w("PennaFlame", String.format("Failed to parse %s into Float", leftEditText.getText().toString()));
            }

            String right;
            String left;
            if (adapter == leftSpinner) {
                left = leftSpinner.getItemAtPosition(pos).toString();
                right = rightSpinner.getSelectedItem().toString();
            } else {
                right = rightSpinner.getItemAtPosition(pos).toString();
                left = leftSpinner.getSelectedItem().toString();
            }

            value = convertUnit(left, right, fvalue);

            enabledTextWatcherEvents = false;
            rightEditText.setText(String.format("%4.2f", value));
            enabledTextWatcherEvents = true;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
                /* no-op */
        }

        private float convertUnit(String convertee, String converter, float value) {
            float converteeBase;
            float converterBase;
            float convertedValue = 0.0f;

            if (!isSameUnitClass(convertee, converter)) {
                converterBase = mEnglishMap.get(converter);

                if (isMetricUnitClass(convertee)) {
                    converteeBase = mMetricMap.get(convertee);
                } else {
                    converteeBase = mEnglishMap.get(convertee);
                }

                boolean isMetric = false;
                float conversionRate;
                if (isEnglishUnitClass(converter)) {
                    conversionRate = mEngMetricMap.get("EnglishToMetric");
                } else {
                    conversionRate = mEngMetricMap.get("MetricToEnglish");
                    isMetric = true;
                }

                float baseValue;
                if (isMetric) {
                    baseValue = converteeBase * value * converterBase;
                } else {
                    baseValue = converteeBase * value / converterBase;
                }
                convertedValue = (baseValue * conversionRate);

            } else {
                converterBase = mMetricMap.get(converter);
                converteeBase = mMetricMap.get(convertee);
                convertedValue = (converteeBase * value) / converterBase;
            }

            return convertedValue;
        }

        private boolean isEnglishUnitClass(String unit) {
            if (unit.equalsIgnoreCase("Inch") ||
                unit.equalsIgnoreCase("Foot") ||
                unit.equalsIgnoreCase("Yard") ||
                unit.equalsIgnoreCase("Mile")) {
                return true;
            }
            return false;
        }

        private boolean isMetricUnitClass(String unit) {
            if (unit.equalsIgnoreCase("Millimeter") ||
                    unit.equalsIgnoreCase("Centimeter") ||
                    unit.equalsIgnoreCase("Meter") ||
                    unit.equalsIgnoreCase("Kilometer")) {
                return true;
            }
            return false;
        }

        private boolean isSameUnitClass(String convertee, String converter) {
            boolean isEnglish = false;
            if (isEnglishUnitClass(convertee)) {
                isEnglish = true;
            }

            if (isEnglishUnitClass(converter)) {
                if (isEnglish)
                    return true;
                return false;
            }

            return !isEnglish ? true : false;
        }
    }
}
