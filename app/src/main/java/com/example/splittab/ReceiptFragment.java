package com.example.splittab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class ReceiptFragment extends Fragment {
    private ImageView receiptImageview;
    private Button takePhotoButton;
    private Button uploadPhotoButton;
    private static int REQUEST_PICTURE_CAPTURE = 1;
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_layout, container, false);
        receiptImageview = view.findViewById(R.id.receipt_imageview);
        takePhotoButton = view.findViewById(R.id.take_photo_button);
        uploadPhotoButton = view.findViewById(R.id.upload_photo_button);

        takePhotoButton.setOnClickListener(takePhotoLButtonListener);
        //uploadPhotoButton.setOnClickListener(uploadPhotoLButtonListener);

        return view;
    }
    private View.OnClickListener takePhotoLButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //startCamera();
        }
    };

    /*private View.OnClickListener uploadPhotoLButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bitmap capture = Bitmap.createBitmap(receiptImageview.getWidth(),receiptImageview.getHeight(), Bitmap.Config.ARGB_8888);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            capture.compress(Bitmap.CompressFormat.JPEG, 100,outputStream);
            byte[] data = outputStream.toByteArray();

            String path = "firebasephotos/"+ UUID.randomUUID() + ".jpeg";
            StorageReference pathRef = storage.getReference(path);

            UploadTask uploadTask = pathRef.putBytes(data);
        }
    };*/

}