package com.levibostian.androidblanky.service.statedata

import com.levibostian.androidblanky.service.model.RepoModel
import io.realm.RealmResults

class ReposStateData private constructor(val state: StateData.State, val repos: RealmResults<RepoModel>?, val error: Throwable?) : StateData<RealmResults<RepoModel>>(state, repos, error) {

    companion object {
        fun success(repos: RealmResults<RepoModel>): ReposStateData = ReposStateData(StateData.State.DATA, repos, null)
        fun error(error: Throwable): ReposStateData = ReposStateData(StateData.State.ERROR, null, error)
        fun loading(): ReposStateData = ReposStateData(StateData.State.LOADING, null, null)
        fun empty(): ReposStateData = ReposStateData(StateData.State.EMPTY, null, null)
    }

}