package com.example.techmarket_finalproject.Util;

import com.example.techmarket_finalproject.Model.User;
import com.google.firebase.database.DatabaseError;

public interface UserCallBack {
    void onSuccess(User user);
    void onFailure(DatabaseError error);
}
