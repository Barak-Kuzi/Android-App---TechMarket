package com.example.techmarket_finalproject.Utilities.States;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EmailState implements TextWatcher {
    private final TextInputEditText emailInput;
    private final TextInputLayout emailLayout;

    public EmailState(TextInputEditText emailInput, TextInputLayout emailLayout) {
        this.emailInput = emailInput;
        this.emailLayout = emailLayout;
        this.emailInput.addTextChangedListener(this);
    }

    public TextInputEditText getEmailInput() {
        return emailInput;
    }

    public TextInputLayout getEmailLayout() {
        return emailLayout;
    }

    private boolean validateEmail(String email) throws Exception {
        if (email.isEmpty()) {
            throw new Exception("Email is required");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new Exception("Invalid email address");
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
            if (validateEmail(s.toString())) {
                emailLayout.setError(null);
                emailLayout.setErrorEnabled(false);
            }
        } catch (Exception e) {
            emailLayout.setError(e.getMessage());
        }
    }

}
