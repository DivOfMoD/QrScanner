package com.github.maxcriser.qrscanner.viewHolder;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.maxcriser.qrscanner.R;

public class ItemHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public AppCompatImageButton mClose;

    public ItemHolder(final View itemView) {
        super(itemView);
        mClose = (AppCompatImageButton) itemView.findViewById(R.id.item_close);
        mTitle = (TextView) itemView.findViewById(R.id.item_title);
    }
}
