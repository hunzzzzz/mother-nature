package com.nature.mother.userservice.domain.user.model

import com.nature.mother.common.model.UserType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "name", nullable = false)
    var name: String,

    nickname: String,

    @Column(name = "phone", nullable = false)
    var phone: String
) : BaseUser(type = UserType.NORMAL, email = email, nickname = nickname)