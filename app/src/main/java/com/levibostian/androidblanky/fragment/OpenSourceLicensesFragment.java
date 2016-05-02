package com.levibostian.androidblanky.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.levibostian.androidblanky.R;
import com.levibostian.androidblanky.adapter.recyclerview.OpenSourceLicensesAdapter;

import java.util.ArrayList;

public class OpenSourceLicensesFragment extends BaseFragment {

    private RecyclerView mOpenSourceLicensesRecyclerView;

    public static OpenSourceLicensesFragment newInstance() {
        OpenSourceLicensesFragment fragment = new OpenSourceLicensesFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_source_licenses, container, false);

        mOpenSourceLicensesRecyclerView = (RecyclerView) view.findViewById(R.id.open_source_licenses_recycler_view);

        setupViews();

        return view;
    }

    private void setupViews() {
        ArrayList<OpenSourceLicenseVo> licenses = new ArrayList<>();
        licenses.add(new OpenSourceLicenseVo("Android Open Source Project", "https://source.android.com/source/licenses.html"));
        licenses.add(new OpenSourceLicenseVo("Android support library", "https://developer.android.com/tools/support-library/index.html"));
        licenses.add(new OpenSourceLicenseVo("bumptech/glide", "https://github.com/bumptech/glide"));
        licenses.add(new OpenSourceLicenseVo("square/retrofit", "https://github.com/square/retrofit"));
        licenses.add(new OpenSourceLicenseVo("JakeWharton/NineOldAndroids", "https://github.com/JakeWharton/NineOldAndroids"));
        licenses.add(new OpenSourceLicenseVo("daimajia/AndroidViewAnimations", "https://github.com/daimajia/AndroidViewAnimations"));
        licenses.add(new OpenSourceLicenseVo("daimajia/AnimationEasingFunctions", "https://github.com/daimajia/AnimationEasingFunctions"));
        licenses.add(new OpenSourceLicenseVo("Pkmmte/CircularImageView", "https://github.com/Pkmmte/CircularImageView"));
        licenses.add(new OpenSourceLicenseVo("greenrobot/EventBus", "https://github.com/greenrobot/EventBus"));
        licenses.add(new OpenSourceLicenseVo("afollestad/material-dialogs", "https://github.com/afollestad/material-dialogs"));
        licenses.add(new OpenSourceLicenseVo("google/dagger", "https://github.com/google/dagger"));
        licenses.add(new OpenSourceLicenseVo("whinc/RatingBar", "https://github.com/whinc/RatingBar"));
        licenses.add(new OpenSourceLicenseVo("curiosityio/Realm-RecyclerView", "https://github.com/curiosityio/Realm-RecyclerView"));
        licenses.add(new OpenSourceLicenseVo("realm/realm-java", "https://github.com/realm/realm-java"));
        licenses.add(new OpenSourceLicenseVo("ReactiveX/RxJava", "https://github.com/ReactiveX/RxJava"));

        mOpenSourceLicensesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOpenSourceLicensesRecyclerView.setAdapter(new OpenSourceLicensesAdapter(licenses, new OpenSourceLicensesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(OpenSourceLicenseVo data, int position) {
                getBaseActivity().openWebpage(data.link);
            }
        }));
    }

    @Override
    public String getTitle() {
        return getString(R.string.open_source_licenses);
    }

    public class OpenSourceLicenseVo {

        public String title;
        public String link;

        public OpenSourceLicenseVo(String title, String link) {
            this.title = title;
            this.link = link;
        }
    }


}
