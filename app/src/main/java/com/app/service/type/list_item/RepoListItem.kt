package com.app.service.type.list_item

import com.app.service.model.RepoModel
import com.app.view.type.CTA

sealed class RepoListItem {
    data class Cta(val cta: CTA) : RepoListItem()
    object Favorite : RepoListItem()
    data class Repo(val repo: RepoModel) : RepoListItem()

    companion object
}
