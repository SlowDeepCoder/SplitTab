package com.example.splittab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
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
    private static final int REQUEST_PICTURE_CAPTURE = 1;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference mStorageRef;
    private Bitmap picture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_layout, container, false);

        receiptImageview = view.findViewById(R.id.receipt_imageview);
        takePhotoButton = view.findViewById(R.id.take_photo_button);
        uploadPhotoButton = view.findViewById(R.id.upload_photo_button);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });

        return view;
    }
    private void startCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,REQUEST_PICTURE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PICTURE_CAPTURE && resultCode == -1){
            Bundle extras = data.getExtras();
            picture = (Bitmap)extras.get("data");
            receiptImageview.setImageBitmap(picture);
        }
    }

    private void uploadPhoto(){
        final ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 100,outputStream);

        String path = "firebasephotos/"+ UUID.randomUUID() + ".jpeg";
        StorageReference pathRef = storage.getReference(path);

        byte[] data = outputStream.toByteArray();
        pathRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);

                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUri = uri;
                            }
                        });
                        Toast.makeText(getActivity().getBaseContext(),"Photo Uploaded",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}