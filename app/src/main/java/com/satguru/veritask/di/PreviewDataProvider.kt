package com.satguru.veritask.di

import com.satguru.veritask.models.User

object PreviewDataProvider {

    val user = User(
        id = "1",
        name = "Jack John",
        role = "Manager",
        email = "jack@corp.in",
        manager = null
    )

    val user1 = User(
        id = "2",
        name = "Michel John",
        role = "Manager",
        email = "michel@corp.in",
        manager = null
    )

    val users = listOf(user, user1)
}