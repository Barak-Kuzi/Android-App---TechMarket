package com.example.techmarket_finalproject.Utilities;

public class ValidationManagement {
    private boolean emailIsValid, passwordIsValid, rePasswordIsValid, usernameIsValid, phoneIsValid, addressIsValid;
    private String emailErrorMessage, passwordErrorMessage, rePasswordErrorMessage, usernameErrorMessage, phoneErrorMessage, addressErrorMessage;

    public ValidationManagement() {
        this.emailIsValid = true;
        this.passwordIsValid = true;
        this.rePasswordIsValid = true;
        this.usernameIsValid = true;
        this.phoneIsValid = true;
        this.addressIsValid = true;
        this.emailErrorMessage = "";
        this.passwordErrorMessage = "";
        this.rePasswordErrorMessage = "";
        this.usernameErrorMessage = "";
        this.phoneErrorMessage = "";
        this.addressErrorMessage = "";
    }

    public boolean emailIsValid() {
        return emailIsValid;
    }

    public boolean passwordIsValid() {
        return passwordIsValid;
    }

    public boolean rePasswordIsValid() {
        return rePasswordIsValid;
    }

    public boolean usernameIsValid() {
        return usernameIsValid;
    }

    public boolean phoneIsValid() {
        return phoneIsValid;
    }

    public boolean addressIsValid() {
        return addressIsValid;
    }

    public String getEmailErrorMessage() {
        return emailErrorMessage;
    }

    public String getPasswordErrorMessage() {
        return passwordErrorMessage;
    }

    public String getRePasswordErrorMessage() {
        return rePasswordErrorMessage;
    }

    public String getUsernameErrorMessage() {
        return usernameErrorMessage;
    }

    public String getPhoneErrorMessage() {
        return phoneErrorMessage;
    }

    public String getAddressErrorMessage() {
        return addressErrorMessage;
    }

    public boolean allSingUpFieldsAreValid(String email, String password, String rePassword, String username, String phone, String address) {
        validateEmail(email);
        validatePassword(password);
        validateRePassword(password, rePassword);
        validateUsername(username);
        validatePhone(phone);
        validateAddress(address);
        return this.emailIsValid && this.passwordIsValid && this.rePasswordIsValid && this.usernameIsValid && this.phoneIsValid && this.addressIsValid;
    }

    public boolean allSingInFieldsAreValid(String email, String password) {
        validateEmail(email);
        validatePassword(password);
        return this.emailIsValid && this.passwordIsValid;
    }

    public boolean allEditProfileFieldsAreValid(String username, String address, String phone, String password, String rePassword) {
        validateUsername(username);
        validateAddress(address);
        validatePhone(phone);
        validatePassword(password);
        validateRePassword(password, rePassword);
        return this.passwordIsValid && this.rePasswordIsValid && this.usernameIsValid && this.phoneIsValid && this.addressIsValid;
    }

    public boolean emailFieldIsValid(String email) {
        validateEmail(email);
        return this.emailIsValid;
    }

    public void validateEmail(String email) {
        if (email.isEmpty()) {
            this.emailIsValid = false;
            this.emailErrorMessage = "Email is required";
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.emailIsValid = false;
            this.emailErrorMessage = "Invalid email address";
        } else {
            this.emailIsValid = true;
            this.emailErrorMessage = "";
        }
    }

    public void validatePassword(String password) {
        if (password.isEmpty()) {
            this.passwordIsValid = false;
            this.passwordErrorMessage = "Password is required";
        } else if (password.length() < 6) {
            this.passwordIsValid = false;
            this.passwordErrorMessage = "Password must be at least 6 characters";
        } else {
            this.passwordIsValid = true;
            this.passwordErrorMessage = "";
        }
    }

    public void validateRePassword(String password, String rePassword) {
        if (rePassword.isEmpty()) {
            this.rePasswordIsValid = false;
            this.rePasswordErrorMessage = "Re-enter password is required";
        } else if (!rePassword.equals(password)) {
            this.rePasswordIsValid = false;
            this.rePasswordErrorMessage = "Passwords do not match";
        } else {
            this.rePasswordIsValid = true;
            this.rePasswordErrorMessage = "";
        }
    }

    public void validateUsername(String username) {
        if (username.isEmpty()) {
            this.usernameIsValid = false;
            this.usernameErrorMessage = "Username is required";
        } else {
            this.usernameIsValid = true;
            this.usernameErrorMessage = "";
        }
    }

    public void validatePhone(String phone) {
        if (phone.isEmpty()) {
            this.phoneIsValid = false;
            this.phoneErrorMessage = "Phone number is required";
        } else if (phone.length() < 10) {
            this.phoneIsValid = false;
            this.phoneErrorMessage = "Phone number must be at least 10 characters";
        } else {
            this.phoneIsValid = true;
            this.phoneErrorMessage = "";
        }
    }

    public void validateAddress(String address) {
        if (address.isEmpty()) {
            this.addressIsValid = false;
            this.addressErrorMessage = "Address is required";
        } else {
            this.addressIsValid = true;
            this.addressErrorMessage = "";
        }
    }
}
