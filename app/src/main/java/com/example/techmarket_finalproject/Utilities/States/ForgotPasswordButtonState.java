package com.example.techmarket_finalproject.Utilities.States;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordButtonState implements TextWatcher {
    private final AppCompatButton button;
    private final TextInputLayout emailLayout;
    private final TextInputEditText emailInput;

    public ForgotPasswordButtonState(AppCompatButton button, TextInputLayout emailLayout, TextInputEditText emailInput) {
        this.button = button;
        this.emailLayout = emailLayout;
        this.emailInput = emailInput;
        this.emailInput.addTextChangedListener(this);
    }

    public AppCompatButton getButton() {
        return button;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean isEmailValid = emailLayout.getError() == null && !emailInput.getText().toString().isEmpty();
        button.setEnabled(isEmailValid);


    }

}
