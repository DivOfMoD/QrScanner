package com.github.maxcriser.qrscanner.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.maxcriser.qrscanner.database.models.ItemModel;
import com.github.maxcriser.qrscanner.viewHolder.ItemHolder;

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

    private final Cursor mCursor;
    private final Context mContext;
    private final Object mView;

    public ItemAdapter(final Cursor pCursor, final Context pContext, final Object mObject) {
        mCursor = pCursor;
        mContext = pContext;
        mView = mObject;
    }

    @Override
    public ItemHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(mContext).inflate((Integer) mView, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        if (mCursor.moveToPosition(position)) {
            holder.mTitle.setText(mCursor.getString(mCursor.getColumnIndex(ItemModel.TEXT)));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}