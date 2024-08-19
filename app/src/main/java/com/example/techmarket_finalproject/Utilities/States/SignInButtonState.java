package com.example.techmarket_finalproject.Utilities.States;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignInButtonState implements TextWatcher {
    private final AppCompatButton button;
    private final TextInputLayout emailLayout, passwordLayout;
    private final TextInputEditText emailInput, passwordInput;

    public SignInButtonState(AppCompatButton button, TextInputLayout emailLayout, TextInputLayout passwordLayout, TextInputEditText emailInput, TextInputEditText passwordInput) {
        this.button = button;
        this.emailLayout = emailLayout;
        this.passwordLayout = passwordLayout;
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
        this.emailInput.addTextChangedListener(this);
        this.passwordInput.addTextChangedListener(this);
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
        boolean isPasswordValid = passwordLayout.getError() == null && !passwordInput.getText().toString().isEmpty();
        button.setEnabled(isEmailValid && isPasswordValid);


    }

}
