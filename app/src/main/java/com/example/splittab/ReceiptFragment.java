package com.example.splittab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splittab.Adapters.GalleryAdapter;
import com.example.splittab.FirebaseTemplates.Picture;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ReceiptFragment extends Fragment {

    //TODO om bilden finns i gallerian ska den inte gå att ladda upp igen

    private ImageView receiptImageview;
    private Button takePhotoButton;
    private Button uploadPhotoButton;
    private static final int REQUEST_PICTURE_CAPTURE = 1;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference mStorageRef;
    private Bitmap picture;
    private ArrayList<Picture> picturePathList;
    private static final int NUM_GRID_COLUMNs = 3;
    private GalleryAdapter galleryAdapter;
    private RecyclerView galeryRecycleView;
    private RecyclerView.Adapter adapter;
    private StorageReference firebaseStorageRef = storage.getReference().child("firebasephotos");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_layout, container, false);

        receiptImageview = view.findViewById(R.id.receipt_imageview);
        takePhotoButton = view.findViewById(R.id.take_photo_button);
        uploadPhotoButton = view.findViewById(R.id.upload_photo_button);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        picturePathList = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(picturePathList,getContext());
        galeryRecycleView = view.findViewById(R.id.galery_recycleview);
        galeryRecycleView.setAdapter(galleryAdapter);

        loadImages();
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
    //TODO om bilden finns i gallerian ska den inte gå att ladda upp igen
    private void uploadPhoto(){
        if(picture != null) {
            final ProgressBar progressBar = getView().findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            String path = "firebasephotos/" + UUID.randomUUID() + ".jpeg";
            StorageReference pathRef = storage.getReference(path);

            final byte[] data = outputStream.toByteArray();
            pathRef.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri = uri;
                                    Log.e("HF", "URI: " + downloadUri);
                                    //Ska lägga till linken i arraylinklistan
                                    picturePathList.add(new Picture(downloadUri.toString()));
                                    //notifydatachanges till gridden
                                    galleryAdapter.notifyDataSetChanged();
                                }
                            });
                            Toast.makeText(getActivity().getBaseContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity().getBaseContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(getActivity().getBaseContext(), "There is nothing to upload", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadImages(){
     firebaseStorageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        //for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under firebaseStorageRef
                        //}

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under firebaseStorageRef
                            Task<Uri> oldPictureURL = item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri = uri;
                                    picturePathList.add(new Picture(downloadUri.toString()));
                                    Log.e("HF", "downloadUri.toString(): "+downloadUri.toString());
                                    galleryAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
        galleryAdapter.notifyDataSetChanged();
    }
}