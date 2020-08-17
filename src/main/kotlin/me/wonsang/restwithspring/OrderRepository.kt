package me.wonsang.restwithspring

import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Orders, Long> {
}