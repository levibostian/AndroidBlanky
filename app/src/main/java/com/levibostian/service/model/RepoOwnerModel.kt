package com.levibostian.service.model

import com.squareup.moshi.Json

class RepoOwnerModel(@field:Json(name = "login") var name: String = "")