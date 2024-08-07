package com.example.techmarket_finalproject.Utilities;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagePicker {

    public interface ImageFetchCallback {
        void onSuccess(List<Uri> imageUris);

        void onFailure(String errorMessage);
    }

    public static void fetchAllImages(Context context, ImageFetchCallback callback) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads");
        storageReference.listAll().addOnSuccessListener(listResult -> {
            List<Uri> imageUris = new ArrayList<>();
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUris.add(uri);
                    if (imageUris.size() == listResult.getItems().size()) {
                        callback.onSuccess(imageUris);
                    }
                }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
            }
        }).addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}