package com.pennaflame.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FractionDecimalActivity extends PennaFlameBaseActivity implements OnFractionDecimalListener {

    private float mCurrentDecimalValue = 0.0f;
    private int[] mCurrentFractionValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fractionDecimal, R.layout.fd_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.fd_spinner_item);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                if (i == 0) {
                    // to prevent fragment re-selection and loosing early saved state
                    if (getSupportFragmentManager().findFragmentByTag("FractionFragment") != null) {
                        return true;
                    }

                    FractionFragment f;
                    if (mCurrentFractionValue != null) {
                        f = FractionFragment.newInstance(mCurrentFractionValue[0], mCurrentFractionValue[1]);
                    } else {
                        f = new FractionFragment();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, f, "FractionFragment")
                            .commit();
                } else {
                    // to prevent fragment re-selection and loosing early saved state
                    if (getSupportFragmentManager().findFragmentByTag("DecimalFragment") != null) {
                        return true;
                    }
                    DecimalFragment f;
                    if (mCurrentFractionValue != null) {
                        f = DecimalFragment.newInstance(mCurrentFractionValue[0], mCurrentFractionValue[1]);
                    } else {
                        f = new DecimalFragment();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, f, "DecimalFragment")
                            .commit();
                }
                return true;
            }
        });
    }

    public void fractionValueChanged(int numerator, int denominator, float decimal) {
        if (mCurrentFractionValue == null) {
            mCurrentFractionValue = new int[2];
        }
        mCurrentFractionValue[0] = numerator;
        mCurrentFractionValue[1] = denominator;
        mCurrentDecimalValue = decimal;
    }

    public static class DecimalFragment extends Fragment {

        private OnFractionDecimalListener mListener;
        private EditText mDecimalEditText;
        private Button mDecimalAddButton;
        private Button mDecimalMinusButton;
        private TextView mNumeratorText;
        private TextView mDenominatorText;
        private boolean mEnableDecimalEditEvent = true;

        public DecimalFragment() {

        }

        public static DecimalFragment newInstance(int numerator, int denominator) {
            Bundle args = new Bundle();
            args.putInt("numerator", numerator);
            args.putInt("denominator", denominator);
            DecimalFragment f = new DecimalFragment();
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_fd_decimal, container, false);
            mNumeratorText = (TextView) rootView.findViewById(R.id.numeratorTextView);
            mDenominatorText = (TextView) rootView.findViewById(R.id.denominatorTextView);

            mDecimalEditText = (EditText) rootView.findViewById(R.id.decimalEditText);
            mDecimalEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!mEnableDecimalEditEvent) {
                        return;
                    }

                    float decimal = getFloatValue(s.toString());
                    if (decimal <= 0.0f && mDecimalMinusButton.isEnabled()) {
                        mDecimalMinusButton.setEnabled(false);
                    } else if (decimal > 0.0f && !mDecimalMinusButton.isEnabled()) {
                        mDecimalMinusButton.setEnabled(true);
                    }
                    int[] fraction = floatToFraction(decimal);
                    mNumeratorText.setText(String.format("%d", fraction[0]), TextView.BufferType.EDITABLE);
                    mDenominatorText.setText(String.format("%d", fraction[1]), TextView.BufferType.EDITABLE);
                    mListener.fractionValueChanged(fraction[0], fraction[1], decimal);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mDecimalAddButton = (Button) rootView.findViewById(R.id.decimalAddButton);
            mDecimalAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float decimal = getFloatValue(mDecimalEditText.getText().toString());
                    decimal += 0.01;
                    if (!mDecimalMinusButton.isEnabled()) {
                        mDecimalMinusButton.setEnabled(true);
                    }
                    DecimalFormat df = new DecimalFormat("@@##");
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        df.setRoundingMode(RoundingMode.UP);
                    }
                    mDecimalEditText.setText(df.format(decimal));
                }
            });

            mDecimalMinusButton = (Button) rootView.findViewById(R.id.decimalMinusButton);
            float decimal = getFloatValue(mDecimalEditText.getText().toString());
            if (decimal > 0.0f && !mDecimalMinusButton.isEnabled()) {
                mDecimalMinusButton.setEnabled(true);
            } else if (decimal <= 0.0f) {
                mDecimalMinusButton.setEnabled(false);
            }
            mDecimalMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float decimal = getFloatValue(mDecimalEditText.getText().toString());
                    decimal -= 0.01;
                    if (decimal <= 0.00f) {
                        decimal = 0.00f;
                        mDecimalMinusButton.setEnabled(false);
                    } else if (!mDecimalMinusButton.isEnabled()) {
                        mDecimalMinusButton.setEnabled(true);
                    }

                    DecimalFormat df = new DecimalFormat("@@##");
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        df.setRoundingMode(RoundingMode.UP);
                    }
                    mDecimalEditText.setText(df.format(decimal));
                }
            });

            Bundle bundleToUse = null;
            Bundle bundle = getArguments();
            if (bundle != null && bundle.containsKey("numerator")) {
                bundleToUse = bundle;
            } else if (savedInstanceState != null && savedInstanceState.containsKey("numerator")) {
                bundleToUse = savedInstanceState;
            }

            if (bundleToUse != null) {
                int numerator = bundleToUse.getInt("numerator");
                int denominator = bundleToUse.getInt("denominator");
                mNumeratorText.setText(String.format("%d", numerator));
                mDenominatorText.setText(String.format("%d", denominator));

                DecimalFormat df = new DecimalFormat("@@##");
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    df.setRoundingMode(RoundingMode.HALF_UP);
                }
                mEnableDecimalEditEvent = false;
                mDecimalEditText.setText(df.format(FractionFragment.fractionToDecimal(numerator, denominator)));
                mEnableDecimalEditEvent = true;
            }

            return rootView;
        }

        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString("denominator", mDenominatorText.getText().toString());
            outState.putString("numerator", mNumeratorText.getText().toString());
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
                mListener = (OnFractionDecimalListener) activity;
            } catch (ClassCastException cce) {
                throw new ClassCastException(activity.toString() + " must implement OnFractionDecimalListener");
            }
        }

        /**
         * Set the callback to null so we don't accidentally leak the
         * Activity instance.
         */
        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

        public static float getFloatValue(String str) {
            float value = 0.0f;
            try {
                value = Float.valueOf(str);
            } catch (NumberFormatException nfe) {
                Log.w("PennaFlame", String.format("Could not parse %s into float", str));

            }
            return value;
        }

        private static int gcdForNumber1(int m, int n) {
            while (m != n) // execute loop until m == n
            {
                if (m > n)
                    m = m - n; // large - small , store the results in large variable<br>
                else
                    n = n - m;
            }
            return (m); // m or n is GCD
        }

        private static int tenRaisedTopower(int decimalLength) {
            int answer = 10;
            while (decimalLength != 1) {
                answer *= 10;
                decimalLength--;
            }
            return answer;
        }

        public static int[] floatToFraction(float decimalNumber) {
            int[] fraction = new int[]{0, 0};
            String decimalString = String.format("%f", decimalNumber);
            String[] components = decimalString.split("\\.");
            if (components.length > 1) {
                int decimalLength = components[1].length();
                int n = tenRaisedTopower(decimalLength);
                int m = Integer.valueOf(components[1]);
                if (m != 0) {
                    int gcd = gcdForNumber1(m, n);
                    int numer = m / gcd;
                    int deno = n / gcd;
                    int fractionnumer = (Integer.valueOf(components[0]) * deno) + numer;
                    fraction[0] = fractionnumer;
                    fraction[1] = deno;
                }
            }
            return fraction;
        }
    }

    public static class FractionFragment extends Fragment {

        private OnFractionDecimalListener mListener;
        private EditText numeratorText;
        private EditText denominatorText;
        private Button numeratorAddButton;
        private Button numeratorMinusButton;
        private Button denominatorAddButton;
        private Button denominatorMinusButton;
        private TextView decimalText;

        public FractionFragment() {
        }

        public static FractionFragment newInstance(int numerator, int denominator) {
            Bundle args = new Bundle();
            args.putInt("numerator", numerator);
            args.putInt("denominator", denominator);
            FractionFragment f = new FractionFragment();
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                // We have different layouts, and in one of them this
                // fragment's containing frame doesn't exist.  The fragment
                // may still be created from its saved state, but there is
                // no reason to try to create its view hierarchy because it
                // won't be displayed.  Note this is not needed -- we could
                // just run the code below, where we would create and return
                // the view hierarchy; it would just never be used.
                return null;
            }

            View rootView = inflater.inflate(R.layout.fragment_fd_fraction, container, false);
            numeratorText = (EditText) rootView.findViewById(R.id.topEditText);
            denominatorText = (EditText) rootView.findViewById(R.id.bottomEditText);
            decimalText = (TextView) rootView.findViewById(R.id.decimalTextView);

            numeratorText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int numvalue = FractionFragment.getIntValue(s.toString());
                    int demValue = FractionFragment.getIntValue(denominatorText.getText().toString());
                    if (numvalue > 0 && !numeratorMinusButton.isEnabled()) {
                        numeratorMinusButton.setEnabled(true);
                    }
                    if (numvalue == 0 && numeratorMinusButton.isEnabled()) {
                        numeratorMinusButton.setEnabled(false);
                    }
                    updateDecimal(numvalue, demValue, decimalText);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            denominatorText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int demValue = FractionFragment.getIntValue(s.toString());
                    int numvalue = FractionFragment.getIntValue(numeratorText.getText().toString());
                    if (demValue > 0 && !denominatorMinusButton.isEnabled()) {
                        denominatorMinusButton.setEnabled(true);
                    }
                    if (demValue == 0 && denominatorMinusButton.isEnabled()) {
                        denominatorMinusButton.setEnabled(false);
                    }
                    updateDecimal(numvalue, demValue, decimalText);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            numeratorAddButton = (Button) rootView.findViewById(R.id.topAddButton);
            numeratorMinusButton = (Button) rootView.findViewById(R.id.topMinusButton);
            numeratorMinusButton.setEnabled(false);
            denominatorAddButton = (Button) rootView.findViewById(R.id.addBottomButton);
            denominatorMinusButton = (Button) rootView.findViewById(R.id.bottomMinuButton);
            denominatorMinusButton.setEnabled(false);

            numeratorAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int numvalue = FractionFragment.getIntValue(numeratorText.getText().toString());
                    numvalue++;
                    if (numvalue == 1 && !numeratorMinusButton.isEnabled()) {
                        numeratorMinusButton.setEnabled(true);
                    }
                    int demValue = FractionFragment.getIntValue(denominatorText.getText().toString());
                    updateDecimal(numvalue, demValue, decimalText);
                    numeratorText.setText(String.format("%d", numvalue), TextView.BufferType.EDITABLE);
                }
            });

            numeratorMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int numvalue = FractionFragment.getIntValue(numeratorText.getText().toString());
                    if (numvalue == 0) {
                        return;
                    }
                    numvalue--;
                    if (numvalue == 0) {
                        numeratorMinusButton.setEnabled(false);
                    }

                    int demValue = FractionFragment.getIntValue(denominatorText.getText().toString());
                    updateDecimal(numvalue, demValue, decimalText);
                    numeratorText.setText(String.format("%d", numvalue), TextView.BufferType.EDITABLE);
                }
            });

            denominatorAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int demvalue = FractionFragment.getIntValue(denominatorText.getText().toString());
                    demvalue++;
                    if (demvalue == 1 && !denominatorMinusButton.isEnabled()) {
                        denominatorMinusButton.setEnabled(true);
                    }
                    int numvalue = FractionFragment.getIntValue(numeratorText.getText().toString());
                    updateDecimal(numvalue, demvalue, decimalText);
                    denominatorText.setText(String.format("%d", demvalue), TextView.BufferType.EDITABLE);
                }
            });

            denominatorMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int demvalue = FractionFragment.getIntValue(denominatorText.getText().toString());
                    if (demvalue == 0) {
                        return;
                    }
                    demvalue--;
                    if (demvalue == 0) {
                        denominatorMinusButton.setEnabled(false);
                    }

                    int numvalue = FractionFragment.getIntValue(numeratorText.getText().toString());
                    updateDecimal(numvalue, demvalue, decimalText);
                    denominatorText.setText(String.format("%d", demvalue), TextView.BufferType.EDITABLE);
                }
            });

            //Save state and rotation change stuff
            Bundle bundle = getArguments();
            if (bundle != null && bundle.containsKey("numerator")) {
                int numerator = bundle.getInt("numerator");
                numeratorText.setText(String.format("%d", numerator), TextView.BufferType.EDITABLE);
                int denominator = bundle.getInt("denominator");
                denominatorText.setText(String.format("%d", denominator), TextView.BufferType.EDITABLE);
            } else if (savedInstanceState != null && savedInstanceState.containsKey("numerator")) {
                numeratorText.setText(savedInstanceState.getString("numerator"), TextView.BufferType.EDITABLE);
                denominatorText.setText(savedInstanceState.getString("denominator"), TextView.BufferType.EDITABLE);
            }

            return rootView;
        }

        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString("numerator", numeratorText.getText().toString());
            outState.putString("denominator", denominatorText.getText().toString());
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
                mListener = (OnFractionDecimalListener) activity;
            } catch (ClassCastException cce) {
                throw new ClassCastException(activity.toString() + " must implement OnFractionDecimalListener");
            }
        }

        /**
         * Set the callback to null so we don't accidentally leak the
         * Activity instance.
         */
        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

        private void updateDecimal(int numvalue, int demvalue, TextView decimalText) {

            if (demvalue == 0) {
                decimalText.setText("0.0");
                mListener.fractionValueChanged(0, 0, 0.0f);
                return;
            }

            float decimal = FractionFragment.fractionToDecimal(numvalue, demvalue);
            mListener.fractionValueChanged(numvalue, demvalue, decimal);
            DecimalFormat df = new DecimalFormat("@@##");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                df.setRoundingMode(RoundingMode.HALF_UP);
            }
            decimalText.setText(df.format(decimal));
        }

        /*
         * Math Util methods (all static)
         */
        public static int getIntValue(String str) {
            int value = 0;
            try {
                value = Integer.valueOf(str);
            } catch (NumberFormatException nfe) {
                Log.w("PennaFlame", String.format("Could not parse %s into integer", str));

            }
            return value;
        }

        public static float fractionToDecimal(int numInt, int demInt) {
            if (demInt != 0) {
                Log.d("PennaFlame",
                        String.format("%d / %d = %4.2f",
                                numInt, demInt, (numInt / (demInt * 1.0f))));
                return numInt / (demInt * 1.0f);
            }
            return 0.0f;
        }
    }
}
