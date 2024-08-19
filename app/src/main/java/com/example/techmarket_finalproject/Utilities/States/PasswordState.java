package com.example.techmarket_finalproject.Utilities.States;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PasswordState implements TextWatcher {
    private final TextInputEditText passwordInput;
    private final TextInputLayout passwordLayout;

    public PasswordState(TextInputEditText passwordInput, TextInputLayout passwordLayout) {
        this.passwordInput = passwordInput;
        this.passwordLayout = passwordLayout;
        this.passwordInput.addTextChangedListener(this);
    }

    public TextInputEditText getPasswordInput() {
        return passwordInput;
    }

    public TextInputLayout getPasswordLayout() {
        return passwordLayout;
    }

    private boolean validatePassword(String password) throws Exception {
        if (password.isEmpty()) {
            throw new Exception("Password is required");
        } else if (password.length() < 6) {
            throw new Exception("Password must be at least 6 characters");
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
            if (validatePassword(s.toString())) {
                passwordLayout.setError(null);
                passwordLayout.setErrorEnabled(false);
            }
        } catch (Exception e) {
            passwordLayout.setError(e.getMessage());
        }
    }

}
