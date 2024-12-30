package com.nature.mother.userservice.domain.user.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseUser(
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: UserType,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "nickname", nullable = false, unique = true)
    var nickname: String
) {
    @Id
    @UuidGenerator
    @Column(name = "user_id", nullable = false, unique = true)
    val id: UUID? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: UserStatus = UserStatus.NORMAL

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole = UserRole.USER

    @Column(name = "created_at", nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: LocalDateTime

    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
        updatedAt = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}