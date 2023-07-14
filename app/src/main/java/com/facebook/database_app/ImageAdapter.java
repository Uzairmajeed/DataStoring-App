package com.facebook.database_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<ImageData> imageDataList;
    private int imageWidth;
    private int imageHeight;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ImageAdapter(Context context, List<ImageData> imageDataList) {
        this.context = context;
        this.imageDataList = imageDataList;
    }

    public void setImageDimensions(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageData imageData = imageDataList.get(position);

        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.width = imageWidth;
        layoutParams.height = imageHeight;
        holder.imageView.setLayoutParams(layoutParams);

        holder.textViewName.setText(imageData.getName());

        if (imageData.getImagePath() == null || imageData.getImagePath().isEmpty()) {
            Glide.with(context)
                    .clear(holder.imageView); // Clear any previous image set with Glide
            holder.imageView.setImageResource(R.drawable.image2); // Set a default image
        } else {
            Glide.with(context)
                    .load(imageData.getImagePath())
                    .placeholder(R.drawable.image2) // Set a placeholder image while loading
                    .error(R.drawable.image2) // Set an error image if loading fails
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }

    public ImageData getImageData(int position) {
        if (position >= 0 && position < imageDataList.size()) {
            return imageDataList.get(position);
        }
        return null;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textViewName;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        }
    }
}
