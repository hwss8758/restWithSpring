package me.wonsang.restwithspring

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Employee(
        @Id
        @GeneratedValue
        var id: Long? = null,
        var name: String,
        var role: String
) {
}