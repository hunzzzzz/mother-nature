package com.nature.mother.userservice.domain.user.repository

import com.nature.mother.userservice.domain.user.model.KakaoUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface KakaoUserRepository : JpaRepository<KakaoUser, UUID>