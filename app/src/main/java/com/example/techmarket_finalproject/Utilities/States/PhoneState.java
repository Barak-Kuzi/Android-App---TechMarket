package com.example.techmarket_finalproject.Utilities.States;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PhoneState implements TextWatcher {
    private final TextInputEditText phoneInput;
    private final TextInputLayout phoneLayout;

    public PhoneState(TextInputEditText phoneInput, TextInputLayout phoneLayout) {
        this.phoneInput = phoneInput;
        this.phoneLayout = phoneLayout;
        this.phoneInput.addTextChangedListener(this);
    }

    public TextInputEditText getPhoneInput() {
        return phoneInput;
    }

    public TextInputLayout getPhoneLayout() {
        return phoneLayout;
    }

    private boolean validatePhone(String phone) throws Exception {
        if (phone.isEmpty()) {
            throw new Exception("Phone number is required");
        } else if (phone.length() < 10) {
            throw new Exception("Phone number must be at least 10 characters");
        } else {
            return true;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            if (validatePhone(s.toString())) {
                phoneLayout.setError(null);
                phoneLayout.setErrorEnabled(false);
            }
        } catch (Exception e) {
            phoneLayout.setError(e.getMessage());
        }

    }

}
