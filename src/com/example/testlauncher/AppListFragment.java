package com.example.testlauncher;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.ArrayList;

/**
 * Created by Arnab Chakraborty
 */
public class AppListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<AppModel>> {
    AppListAdapter mAdapter;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No Applications");

        mAdapter = new AppListAdapter(getActivity());
        setListAdapter(mAdapter);

        // till the data is loaded display a spinner
        setListShown(false);

        // create the loader to load the apps list in background
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<AppModel>> onCreateLoader(final int id, final Bundle bundle) {
        return new AppsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(final Loader<ArrayList<AppModel>> loader, final ArrayList<AppModel> apps) {
        mAdapter.setData(apps);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(final Loader<ArrayList<AppModel>> loader) {
        mAdapter.setData(null);
    }
}
