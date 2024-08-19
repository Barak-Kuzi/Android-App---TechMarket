package com.example.techmarket_finalproject.Utilities.States;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class UsernameState implements TextWatcher {
    private final TextInputEditText usernameInput;
    private final TextInputLayout usernameLayout;

    public UsernameState(TextInputEditText usernameInput, TextInputLayout usernameLayout) {
        this.usernameInput = usernameInput;
        this.usernameLayout = usernameLayout;
        this.usernameInput.addTextChangedListener(this);
    }

    public TextInputEditText getUsernameInput() {
        return usernameInput;
    }

    public TextInputLayout getUsernameLayout() {
        return usernameLayout;
    }

    private boolean validateUsername(String username) throws Exception {
        if (username.isEmpty()) {
            throw new Exception("Username is required");
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
            if (validateUsername(s.toString())) {
                usernameLayout.setError(null);
                usernameLayout.setErrorEnabled(false);
            }

        } catch (Exception e) {
            usernameLayout.setError(e.getMessage());
        }
    }

}
