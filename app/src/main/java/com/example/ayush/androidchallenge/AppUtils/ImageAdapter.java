package com.example.ayush.androidchallenge.AppUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ayush.androidchallenge.AndroidLifecycle.App;
import com.example.ayush.androidchallenge.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    @Inject
    Context context;

    private ArrayList<String> links;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(String link);
    }

    public ImageAdapter(OnItemClickListener listener){
        this.links = new ArrayList<>();
        this.listener = listener;
        App.getComponent().inject(this);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.adapter_item, null);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.onBind(links.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    public void addItem(String link){
        links.add(link);
    }

    public void addItems(ArrayList<String> list){
        links.addAll(list);
        notifyDataSetChanged();
    }

    public void clear(){
        links.clear();
        notifyDataSetChanged();
    }

    public void setLinks(ArrayList<String> links){
        this.links = links;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private View itemView;
        ImageViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.image);
        }

        private String getMediumLink(String link){
            int index = link.lastIndexOf('.');
            String mLink;
            if(index == -1) {
                mLink = link;
            }
            else {
                mLink = link.substring(0,index) + "m" + link.substring(index);
            }
            return mLink;
        }

        private void onBind(String link, OnItemClickListener listener){
            itemView.setOnClickListener(v -> listener.onItemClick(link));
            Picasso.with(itemView.getContext()).load(getMediumLink(link)).placeholder(R.drawable.loading).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    // if failed to load medium image, load the original.
                    Picasso.with(itemView.getContext()).load(link).into(imageView);
                }
            });
        }

    }
}
