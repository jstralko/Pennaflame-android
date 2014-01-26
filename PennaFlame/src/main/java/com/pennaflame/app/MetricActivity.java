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
import android.widget.Button;
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

    public static class MetricFragment extends Fragment implements Spinner.OnItemSelectedListener, Button.OnClickListener {

        private boolean enabledTextWatcherEvents = true;
        private Map<String, Float> mEnglishMap;
        private Map<String, Float> mEngMetricMap;
        private Map<String, Float> mMetricMap;
        private EditText leftEditText;
        private EditText rightEditText;
        private Spinner leftSpinner;
        private Spinner rightSpinner;
        private Button mAddLeftButton;
        private Button mMinusLeftButton;
        private Button mAddRightButton;
        private Button mMinusRightButton;

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

            leftEditText = (EditText) view.findViewById(R.id.leftEditText);
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
                    if (fvalue <= 0.0f) {
                        mMinusLeftButton.setEnabled(false);
                    } else if (fvalue > 0.0f && !mMinusLeftButton.isEnabled()) {
                        mMinusLeftButton.setEnabled(true);
                    }

                    float value = MetricFragment.this.convertUnit(convertee, converter, fvalue);
                    if (value <= 0.0f) {
                        mMinusRightButton.setEnabled(false);
                    } else if (fvalue > 0.0f && !mMinusRightButton.isEnabled()) {
                        mMinusRightButton.setEnabled(true);
                    }

                    enabledTextWatcherEvents = false;
                    rightEditText.setText(String.format("%4.2f", value));
                    enabledTextWatcherEvents = true;
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            rightEditText = (EditText) view.findViewById(R.id.rightEditText);
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
                    if (fvalue <= 0.0f) {
                        mMinusRightButton.setEnabled(false);
                    } else if (fvalue > 0.0f && !mMinusRightButton.isEnabled()) {
                        mMinusRightButton.setEnabled(true);
                    }

                    float value = MetricFragment.this.convertUnit(convertee, converter, fvalue);
                    if (value <= 0.0f) {
                        mMinusLeftButton.setEnabled(false);
                    } else if (value > 0.0f && !mMinusLeftButton.isEnabled()) {
                        mMinusLeftButton.setEnabled(true);
                    }

                    enabledTextWatcherEvents = false;
                    leftEditText.setText(String.format("%4.2f", value));
                    enabledTextWatcherEvents = true;
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            leftSpinner = (Spinner) view.findViewById(R.id.leftSpinner);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.units, R.layout.spinner_layout);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            // Apply the adapter to the spinner
            leftSpinner.setAdapter(adapter);
            leftSpinner.setSelection(4);
            leftSpinner.setOnItemSelectedListener(this);

            rightSpinner = (Spinner) view.findViewById(R.id.rightSpinner);
            rightSpinner.setAdapter(adapter);
            rightSpinner.setSelection(1);
            rightSpinner.setOnItemSelectedListener(this);

            mAddLeftButton = (Button) view.findViewById(R.id.addLeftButton);
            mAddLeftButton.setOnClickListener(this);
            mMinusLeftButton = (Button) view.findViewById(R.id.minusLeftButton);
            mMinusLeftButton.setOnClickListener(this);
            mMinusLeftButton.setEnabled(false);
            mAddRightButton = (Button) view.findViewById(R.id.addRightButton);
            mAddRightButton.setOnClickListener(this);
            mMinusRightButton = (Button) view.findViewById(R.id.minusRightButton);
            mMinusRightButton.setOnClickListener(this);
            mMinusRightButton.setEnabled(false);

            return view;
        }

        public void onClick(View v) {
            float value = 0.0f;
            if (v == mAddLeftButton) {
                String strValue = leftEditText.getText().toString();
                try {
                    value = Float.parseFloat(strValue);
                } catch (NumberFormatException nfe) {
                    Log.w("PennaFlame", String.format("error parsing %s", strValue));
                }
                value = incrementValue(value, mAddLeftButton);
                leftEditText.setText(String.format("%4.2f", value));
            } else if (v == mMinusLeftButton) {
                String strValue = leftEditText.getText().toString();
                try {
                    value = Float.parseFloat(strValue);
                } catch (NumberFormatException nfe) {
                    Log.w("PennaFlame", String.format("error parsing %s", strValue));
                }
                value = decrementValue(value, mMinusLeftButton);
                leftEditText.setText(String.format("%4.2f", value));
            } else if (v == mAddRightButton) {
                String strValue = rightEditText.getText().toString();
                try {
                    value = Float.parseFloat(strValue);
                } catch (NumberFormatException nfe) {
                    Log.w("PennaFlame", String.format("error parsing %s", strValue));
                }
                value = incrementValue(value, mAddRightButton);
                rightEditText.setText(String.format("%4.2f", value));
            } else if (v == mMinusRightButton) {
                String strValue = rightEditText.getText().toString();
                try {
                    value = Float.parseFloat(strValue);
                } catch (NumberFormatException nfe) {
                    Log.w("PennaFlame", String.format("error parsing %s", strValue));
                }
                value = decrementValue(value, mMinusRightButton);
                rightEditText.setText(String.format("%4.2f", value));
            } else {
                Log.w("PennaFlame", String.format("ignoring event from %s", v.toString()));
            }
        }

        private static float decrementValue(float value, Button button) {
            value--;
            if (value < 0.0f) {
                value = 0.0f;
                button.setEnabled(false);
            }
            return value;
        }

        private static float incrementValue(float value, Button button) {
            value++;
            //no max
            if (value > 0.0f && !button.isEnabled()) {
                button.setEnabled(true);
            }
            return value;
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
