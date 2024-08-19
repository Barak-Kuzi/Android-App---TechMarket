package com.example.techmarket_finalproject.Utilities.States;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RePasswordState implements TextWatcher {
    private final TextInputEditText rePasswordInput;
    private final TextInputLayout rePasswordLayout;
    private final TextInputEditText passwordInput;

    public RePasswordState(TextInputEditText rePasswordInput, TextInputLayout rePasswordLayout, TextInputEditText passwordInput) {
        this.rePasswordInput = rePasswordInput;
        this.rePasswordLayout = rePasswordLayout;
        this.rePasswordInput.addTextChangedListener(this);
        this.passwordInput = passwordInput;
    }

    public TextInputEditText getRePasswordInput() {
        return rePasswordInput;
    }

    public TextInputLayout getRePasswordLayout() {
        return rePasswordLayout;
    }

    private boolean validateRePassword(String password, String rePassword) throws Exception {
        if (rePassword.isEmpty()) {
            throw new Exception("Re-enter password is required");
        } else if (!rePassword.equals(password)) {
            throw new Exception("Passwords do not match");
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
            if (validateRePassword(s.toString(), passwordInput.getText().toString())) {
                rePasswordLayout.setError(null);
                rePasswordLayout.setErrorEnabled(false);
            }
        } catch (Exception e) {
            rePasswordLayout.setError(e.getMessage());
        }

    }

}
