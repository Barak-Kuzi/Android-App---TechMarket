package com.example.techmarket_finalproject.Utilities.States;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddressState implements TextWatcher {
    private final TextInputEditText addressInput;
    private final TextInputLayout AddressLayout;

    public AddressState(TextInputEditText addressInput, TextInputLayout AddressLayout) {
        this.addressInput = addressInput;
        this.AddressLayout = AddressLayout;
        this.addressInput.addTextChangedListener(this);
    }

    public TextInputEditText getAddressInput() {
        return addressInput;
    }

    public TextInputLayout getAddressLayout() {
        return AddressLayout;
    }

    private boolean validateAddress(String address) throws Exception {
        if (address.isEmpty()) {
            throw new Exception("Address is required");
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
            if (validateAddress(s.toString())) {
                AddressLayout.setError(null);
                AddressLayout.setErrorEnabled(false);
            }

        } catch (Exception e) {
            AddressLayout.setError(e.getMessage());
        }
    }

}
