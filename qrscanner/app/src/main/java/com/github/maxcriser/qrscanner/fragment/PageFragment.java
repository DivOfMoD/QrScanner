package com.github.maxcriser.qrscanner.fragment;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.maxcriser.qrscanner.R;
import com.github.maxcriser.qrscanner.adapter.ItemAdapter;
import com.github.maxcriser.qrscanner.database.models.ItemModel;
import com.github.maxcriser.qrscanner.loader.ItemLoader;

public class PageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_PAGE = "ARG_PAGE";
    private RecyclerView recyclerItems;
    private static final int LOADER_ID = 1;
    private ItemAdapter adapter;
    private static LoaderManager getSupportLoaderManager;
    private static Application getApplication;
    private static Context context;


    private int mPage;

    public static PageFragment newInstance(final int page, LoaderManager pGetSupportLoaderManager, Application pGetApplication, Context pContext) {
        getSupportLoaderManager = pGetSupportLoaderManager;
        getApplication = pGetApplication;
        context = pContext;
        final Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        final PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        final LoaderManager supportLoaderManager = getSupportLoaderManager;
        if (supportLoaderManager.getLoader(LOADER_ID) != null) {
            supportLoaderManager.getLoader(LOADER_ID).forceLoad();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new ItemLoader(context, "", ItemModel.class, getApplication);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        adapter = new ItemAdapter(data, context, R.layout.item_database);
    //    recyclerItems.setAdapter(adapter);
        recyclerItems.swapAdapter(adapter, true);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        recyclerItems.swapAdapter(null, true);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        View view;

        switch (getArguments().getInt(ARG_PAGE))
        {
            case 1: {
                return inflater.inflate(R.layout.fragment_home, container, false);
            }
            default: {
                view = inflater.inflate(R.layout.fragment_database, container, false);

        recyclerItems = (RecyclerView) view.findViewById(R.id.recycler_view);

        getSupportLoaderManager.restartLoader(LOADER_ID, null, this);

        recyclerItems.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerItems.setLayoutManager(layoutManager);
                return view;
            }
        }
    }
}