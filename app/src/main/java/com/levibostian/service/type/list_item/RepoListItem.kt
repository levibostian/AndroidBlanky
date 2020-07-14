package com.levibostian.service.type.list_item

import com.levibostian.service.model.RepoModel
import com.levibostian.view.type.CTA

sealed class RepoListItem {
    data class Cta(val cta: CTA) : RepoListItem()
    object Favorite : RepoListItem()
    data class Repo(val repo: RepoModel) : RepoListItem()

    companion object
}
