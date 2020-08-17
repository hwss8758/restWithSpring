package me.wonsang.restwithspring

import javax.persistence.*

@Entity

data class Orders (
        @Id
        @GeneratedValue
        var id: Long? = null,
        var orderDescription: String,
        @Enumerated(EnumType.STRING)
        var orderStatus: Status
)