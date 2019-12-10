package mirrormirror.swen302.mirrormirrorandroid.adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import mirrormirror.swen302.mirrormirrorandroid.R;
import mirrormirror.swen302.mirrormirrorandroid.utilities.ImageStorageManager;

/**
 * Created by hayandr1 on 17/08/17.
 */

public class HorizontalAdapter extends RecyclerView.Adapter {

    public List<String> filePaths;
    Activity context;
    RelativeLayout highlightedPath;
    int highlightedPosition;

    public HorizontalAdapter(List<String> filePaths, Activity context){
        this.filePaths = filePaths;
        this.context = context;
        this.highlightedPath = null;
        this.highlightedPosition = -1;

    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        RelativeLayout imageViewParent;
        TextView textView;
        public ImageViewHolder(View view) {
            super(view);
            imageViewParent = (RelativeLayout) view.findViewById(R.id.recycler_imageview_parent);
            imageView=(ImageView) view.findViewById(R.id.recycler_imageview);
            textView=(TextView) view.findViewById(R.id.date_stamp);
        }
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_image_layout, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(itemLayoutView);
        return  imageViewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ImageViewHolder imageHolder = (ImageViewHolder)holder;
        final RelativeLayout parent = (RelativeLayout) imageHolder.imageViewParent;
        TextView dateStamp = imageHolder.textView;
        dateStamp.setText(filePaths.get(position));
        //imageHolder.imageView.setImageBitmap(ImageStorageManager.loadImageByName(filePaths.get(position), context));
        Glide.with(context).load(ImageStorageManager.loadImageByName(filePaths.get(position), context)).into(imageHolder.imageView);

        if(position == highlightedPosition){
            parent.setBackgroundColor(context.getResources().getColor(R.color.imageSelectedColor));
        }
        imageHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView mainImage = (ImageView) HorizontalAdapter.this.context.findViewById(R.id.main_image);
                Glide.with(context).load(ImageStorageManager.loadImageByName(filePaths.get(position),context)).into(mainImage);
                parent.setBackgroundColor(context.getResources().getColor(R.color.imageSelectedColor));
                if(highlightedPosition != -1 && highlightedPosition != position) {
                    highlightedPath.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                }
                highlightedPath = parent;
                highlightedPosition = position;

                //mainImage.setImageBitmap(ImageStorageManager.loadImageByName(filePaths.get(position), context));

            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        final ImageViewHolder imageHolder = (ImageViewHolder)holder;

        RelativeLayout parent = (RelativeLayout) imageHolder.imageViewParent;
        parent.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
    }

    @Override
    public int getItemCount() {
        if(filePaths != null){
            return filePaths.size();

        }
        else{
            return 0;
        }
    }
}
