package com.levibostian.androidblanky.module;

import com.levibostian.androidblanky.fragment.MainFragment;
import dagger.Module;

@Module(injects = {MainFragment.class}, library = false, complete = false)
public class FragmentModule {

    public FragmentModule() {
    }

}
