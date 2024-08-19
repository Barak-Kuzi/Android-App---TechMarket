package com.example.techmarket_finalproject.Utilities.States;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpButtonState implements TextWatcher {
    private final AppCompatButton button;
    private final TextInputLayout emailLayout, passwordLayout, rePasswordLayout, usernameLayout, phoneLayout, addressLayout;
    private final TextInputEditText emailInput, passwordInput, rePasswordInput, usernameInput, phoneInput, addressInput;

    public SignUpButtonState(AppCompatButton button, TextInputLayout emailLayout, TextInputLayout passwordLayout, TextInputLayout rePasswordLayout, TextInputLayout usernameLayout, TextInputLayout phoneLayout, TextInputLayout addressLayout, TextInputEditText emailInput, TextInputEditText passwordInput, TextInputEditText rePasswordInput, TextInputEditText usernameInput, TextInputEditText phoneInput, TextInputEditText addressInput) {
        this.button = button;
        this.emailLayout = emailLayout;
        this.passwordLayout = passwordLayout;
        this.rePasswordLayout = rePasswordLayout;
        this.usernameLayout = usernameLayout;
        this.phoneLayout = phoneLayout;
        this.addressLayout = addressLayout;
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
        this.rePasswordInput = rePasswordInput;
        this.usernameInput = usernameInput;
        this.phoneInput = phoneInput;
        this.addressInput = addressInput;
        this.emailInput.addTextChangedListener(this);
        this.passwordInput.addTextChangedListener(this);
        this.rePasswordInput.addTextChangedListener(this);
        this.usernameInput.addTextChangedListener(this);
        this.phoneInput.addTextChangedListener(this);
        this.addressInput.addTextChangedListener(this);
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
        boolean isRePasswordValid = rePasswordLayout.getError() == null && !rePasswordInput.getText().toString().isEmpty();
        boolean isUsernameValid = usernameLayout.getError() == null && !usernameInput.getText().toString().isEmpty();
        boolean isPhoneValid = phoneLayout.getError() == null && !phoneInput.getText().toString().isEmpty();
        boolean isAddressValid = addressLayout.getError() == null && !addressInput.getText().toString().isEmpty();

        button.setEnabled(isEmailValid && isPasswordValid && isRePasswordValid && isUsernameValid && isPhoneValid && isAddressValid);
    }

}
