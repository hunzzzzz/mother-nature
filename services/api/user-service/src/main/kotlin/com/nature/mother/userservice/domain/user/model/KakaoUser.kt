package com.nature.mother.userservice.domain.user.model

import com.nature.mother.common.model.UserType
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "kakao_users")
class KakaoUser(
    email: String,
    nickname: String
) : BaseUser(type = UserType.KAKAO, email = email, nickname = nickname)