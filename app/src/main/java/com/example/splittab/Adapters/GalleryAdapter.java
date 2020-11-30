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
    private OnImageListener onImageListener;

    public GalleryAdapter(ArrayList<Picture> picturePathList, Context context, OnImageListener onImageListener) {
        this.picturePathList = picturePathList;
        this.context = context;
        this.onImageListener = onImageListener;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.gallery_view_item, parent,false);
        return new ContactHolder(view, onImageListener);
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

    public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnImageListener onImageListener;

        public ContactHolder(@NonNull View itemView, OnImageListener onImageListener) {
            super(itemView);
            galleryImageView = (ImageView)itemView.findViewById(R.id.galery_image_view);
            this.onImageListener = onImageListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onImageListener.onItemClick(getAdapterPosition());
        }
    }
    public interface OnImageListener{
        void onItemClick(int position);
    }
}
