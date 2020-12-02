package com.example.splittab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splittab.Adapters.GalleryAdapter;
import com.example.splittab.FirebaseTemplates.Credit;
import com.example.splittab.FirebaseTemplates.Group;
import com.example.splittab.FirebaseTemplates.Participant;
import com.example.splittab.FirebaseTemplates.Payment;
import com.example.splittab.FirebaseTemplates.Picture;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReceiptFragment extends Fragment  implements GalleryAdapter.OnImageListener {

    private ImageView receiptImageview;
    private Button takePhotoButton;
    private Button uploadPhotoButton;
    private static final int REQUEST_PICTURE_CAPTURE = 1;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference mStorageRef;
    private Bitmap picture;
    private ArrayList<Picture> picturePathList;
    private GalleryAdapter galleryAdapter;
    private RecyclerView galeryRecycleView;
    private StorageReference firebaseStorageRef = storage.getReference("firebasephotos");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_layout, container, false);

        receiptImageview = view.findViewById(R.id.receipt_imageview);
        takePhotoButton = view.findViewById(R.id.take_photo_button);
        uploadPhotoButton = view.findViewById(R.id.upload_photo_button);
        uploadPhotoButton.setVisibility(View.GONE);
        mStorageRef = FirebaseStorage.getInstance().getReference("firebasephotos");

        picturePathList = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(picturePathList,getContext(), this);
        galeryRecycleView = view.findViewById(R.id.galery_recycleview);
        galeryRecycleView.setAdapter(galleryAdapter);

        loadSampleImages();
        if(GroupManager.getInstance().getCurrentGroup() != null){loadImagesRealtimeDatabase();}
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
            uploadPhotoButton.setVisibility(View.VISIBLE);
        }
    }

    private void uploadPhoto(){
        if(picture != null) {
            final ProgressBar progressBar = getView().findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            String path = "photos/" + UUID.randomUUID() + ".jpeg";
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
                                    String downloadURL = downloadUri.toString();
                                    picturePathList.add(new Picture(downloadURL));

                                    DatabaseReference imageStore = FirebaseDatabase.getInstance().getReference().child("groups").child(GroupManager.getInstance().getCurrentGroup().getKey()).child("photos").push();
                                    HashMap<String,String> hashMap = new HashMap<>();
                                    hashMap.put(GroupManager.generateKey(),downloadURL);
                                    imageStore.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    galleryAdapter.notifyDataSetChanged();
                                }
                            });
                            Toast.makeText(getActivity().getBaseContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show();
                            uploadPhotoButton.setVisibility(View.GONE);
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

    public void loadSampleImages(){
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
                                    galleryAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
        galleryAdapter.notifyDataSetChanged();
    }

    public void loadImagesRealtimeDatabase(){
        final DatabaseReference realtimeDatabaseImage = FirebaseDatabase.getInstance().getReference().child("groups").child(GroupManager.getInstance().getCurrentGroup().getKey()).child("photos");
        realtimeDatabaseImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String imageURLFull;
                    for (DataSnapshot ds2 : ds.getChildren()) {
                        imageURLFull = ds2.getValue(String.class);
                        Log.d("HF", "ds2.getValue(String.class "+ imageURLFull);
                        picturePathList.add(new Picture(imageURLFull));
                    }
                }
                galleryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Picture clickedImage = picturePathList.get(position);
        String image = clickedImage.getImgURL();
        Picasso.with(getContext()).load(image).into(receiptImageview);
        uploadPhotoButton.setVisibility(View.GONE);
    }
}