package com.pennaflame.app;

/**
 * Created by stralko on 11/2/13.
 * Interface to communicate between inner static fragment classes
 * in FractionDecimalActivity class
 */
public interface OnFractionDecimalListener {
    void decimalValueChanged(float value);
    void fractionValueChanged(int numerator, int denominator);
}
