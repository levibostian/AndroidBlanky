package com.levibostian.androidblanky.fragment;

import com.curiosityio.realmrecyclerview.RealmRecyclerView;

public abstract class BaseListFragment extends BaseFragment {

    public interface ListListener {
        void refreshTriggered();
        void loadMore();
    }

    protected ListListener mListListener;

    protected abstract RealmRecyclerView getRecyclerView();

    public void showRefresh() {
        if (getRecyclerView() != null) {
            getRecyclerView().setRefreshing(true);
        }
    }

    public void setListListener(ListListener listener) {
        mListListener = listener;
    }

    public void hideRefresh() {
        if (getRecyclerView() != null) {
            getRecyclerView().setRefreshing(false);
        }
    }

}
