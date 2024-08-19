package com.example.techmarket_finalproject.Utilities.States;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ConfirmButtonState implements TextWatcher {
    private final AppCompatButton button;
    private final TextInputLayout passwordLayout, rePasswordLayout, usernameLayout, phoneLayout, addressLayout;
    private final TextInputEditText passwordInput, rePasswordInput, usernameInput, phoneInput, addressInput;

    public ConfirmButtonState(AppCompatButton button, TextInputLayout passwordLayout, TextInputLayout rePasswordLayout, TextInputLayout usernameLayout, TextInputLayout phoneLayout, TextInputLayout addressLayout,
                              TextInputEditText passwordInput, TextInputEditText rePasswordInput, TextInputEditText usernameInput, TextInputEditText phoneInput, TextInputEditText addressInput) {
        this.button = button;
        this.passwordLayout = passwordLayout;
        this.rePasswordLayout = rePasswordLayout;
        this.usernameLayout = usernameLayout;
        this.phoneLayout = phoneLayout;
        this.addressLayout = addressLayout;
        this.passwordInput = passwordInput;
        this.rePasswordInput = rePasswordInput;
        this.usernameInput = usernameInput;
        this.phoneInput = phoneInput;
        this.addressInput = addressInput;
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
        boolean isPasswordValid = passwordLayout.getError() == null && !passwordInput.getText().toString().isEmpty();
        boolean isRePasswordValid = rePasswordLayout.getError() == null && !rePasswordInput.getText().toString().isEmpty();
        boolean isUsernameValid = usernameLayout.getError() == null && !usernameInput.getText().toString().isEmpty();
        boolean isPhoneValid = phoneLayout.getError() == null && !phoneInput.getText().toString().isEmpty();
        boolean isAddressValid = addressLayout.getError() == null && !addressInput.getText().toString().isEmpty();

        button.setEnabled(isPasswordValid && isRePasswordValid && isUsernameValid && isPhoneValid && isAddressValid);
    }

}
