package br.com.nglauber.nextlevelapps;

import android.content.Context;
import android.graphics.Color;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MyListAdapter extends WearableListView.Adapter {
    private String[] mDataset;
    private int[] mImages;
    private final LayoutInflater mInflater;

    public MyListAdapter(Context context, String[] dataset, int[] images) {
        mInflater = LayoutInflater.from(context);
        mDataset = dataset;
        mImages = images;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.list_item, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        MyViewHolder itemHolder = (MyViewHolder) holder;

        itemHolder.textView.setText(mDataset[position]);
        itemHolder.imageView.setImageResource(mImages[position]);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class MyViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        private CircledImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.name);
            imageView = (CircledImageView) itemView.findViewById(R.id.circle);
        }
    }
}