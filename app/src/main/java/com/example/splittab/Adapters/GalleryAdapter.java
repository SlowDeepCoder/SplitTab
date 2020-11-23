package com.example.splittab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splittab.FirebaseTemplates.Picture;
import com.example.splittab.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ContactHolder> {
    private ArrayList<Picture> picturePathList;
    private Context context;
    public ImageView galleryImageView;

    public GalleryAdapter(ArrayList<Picture> picturePathList, Context context) {
        this.picturePathList = picturePathList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.gallery_view_item, parent,false);
        //logga för check
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Picture pictureLinkPath = picturePathList.get(position);
        //Sätt gallaryimmagen till den bitmappen som kommit fram av URIn
        //holder.galleryImageView.setImageBitmap();
        Picasso.with(context).load(pictureLinkPath.getImgURL()).into(galleryImageView);
        //loopas på possition
        //logga för check
    }

    @Override
    public int getItemCount() {
        return picturePathList.size();
    }

    public class ContactHolder extends RecyclerView.ViewHolder{



        public ContactHolder(@NonNull View itemView) {
            super(itemView);

            galleryImageView = (ImageView)itemView.findViewById(R.id.galery_image_view);

        }
    }
}
