package com.example.techmarket_finalproject.Interfaces;

import com.google.firebase.database.DatabaseError;

public interface GenericCallBack<T> {
    void onResponse(T response);
    void onFailure(DatabaseError databaseError);
}
