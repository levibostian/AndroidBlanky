package com.levibostian.androidblanky.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.levibostian.androidblanky.AppConstants;
import com.levibostian.androidblanky.R;
import com.levibostian.androidblanky.adapter.recyclerview.AboutListAdapter;
import com.levibostian.androidblanky.util.ResourceUtil;

public class AboutFragment extends BaseFragment {

    private static final int PRIVACY_POLICY_INDEX = 0;
    private static final int TERMS_AND_CONDITIONS_INDEX = 1;
    private static final int OPEN_SOURCE_LICENSES_INDEX = 2;
    private static final int ABOUT_INDEX = 3;

    private RecyclerView mAboutListRecyclerView;

    private ListListener mListListener;

    @Override
    public String getTitle() {
        return getString(R.string.about);
    }

    public interface ListListener {
        void openSourceLicensesClicked();
    }

    public static AboutFragment newInstance(ListListener listListener) {
        AboutFragment fragment = new AboutFragment();

        fragment.setListListener(listListener);

        return fragment;
    }

    public void setListListener(ListListener listListener) {
        mListListener = listListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mAboutListRecyclerView = (RecyclerView) view.findViewById(R.id.about_list_recycler_view);

        setupViews();

        return view;
    }

    private void setupViews() {
        mAboutListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAboutListRecyclerView.setAdapter(new AboutListAdapter(ResourceUtil.getArrayOfStringRes(getActivity(), R.array.about_list_options), new AboutListAdapter.ItemClickListener() {

            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case PRIVACY_POLICY_INDEX:
                        getBaseActivity().openWebpage(AppConstants.getPrivacyPolicyUrl());
                        break;
                    case TERMS_AND_CONDITIONS_INDEX:
                        getBaseActivity().openWebpage(AppConstants.getTermsAndConditionsUrl());
                        break;
                    case OPEN_SOURCE_LICENSES_INDEX:
                        mListListener.openSourceLicensesClicked();
                        break;
                    case ABOUT_INDEX:
                        getBaseActivity().openWebpage(AppConstants.getAboutUrl());
                        break;
                    default:
                        break;
                }
            }
        }));
    }

}
