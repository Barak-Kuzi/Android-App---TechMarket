package com.example.techmarket_finalproject.Interfaces;

import com.example.techmarket_finalproject.Models.User;
import com.google.firebase.database.DatabaseError;

public interface UserCallBack {
    void onSuccess(User user);
    void onFailure(DatabaseError error);
}
