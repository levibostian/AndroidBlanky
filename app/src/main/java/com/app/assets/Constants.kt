package com.app.assets

import android.net.Uri
import com.app.Env

object Constants {

    val remoteAssets = RemoteAssets(
        programsFavoritesImage = Uri.parse("${Env.assetsEndpoint}/programs/ic_programs_favorites.jpg"),
        programsUnlockFullImage = Uri.parse("${Env.assetsEndpoint}/programs/ic_programs_unlockfull.jpg")
    )

    data class RemoteAssets(val programsFavoritesImage: Uri, val programsUnlockFullImage: Uri)
}
