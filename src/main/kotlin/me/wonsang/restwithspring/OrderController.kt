package me.wonsang.restwithspring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.parsing.Problem
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class OrderController {
    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var assembler: OrderModelAssembler

    @GetMapping("/orders")
    fun all(): CollectionModel<EntityModel<Orders>> {
        val orders = orderRepository
                .findAll()
                .asSequence()
                .map(assembler::toModel)
                .toList()

        return CollectionModel.of(orders,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController::class.java).all()).withSelfRel()
        )
    }

    @GetMapping("/orders/{id}")
    fun one(@PathVariable id: Long): EntityModel<Orders> {
        val order = orderRepository.findById(id).get()
        return assembler.toModel(order)
    }

    @PostMapping("/orders")
    fun newOrder(@RequestBody order: Orders): ResponseEntity<Any> {
        order.orderStatus = Status.IN_PROGRESS
        val newOrder = orderRepository.save(order)

        return ResponseEntity
                .created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController::class.java).one(newOrder.id!!)).toUri())
                .body(assembler.toModel(newOrder))
    }

    @DeleteMapping("/orders/{id}/cancel")
    fun cancel(@PathVariable id: Long): ResponseEntity<Any> {
        val order = orderRepository.findById(id).get()

        if (order.orderStatus == Status.IN_PROGRESS) {
            order.orderStatus = Status.CANCELLED
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)))
        }

        return ResponseEntity
                .badRequest()
                .body("You can't cancel an order that is in the " + order.orderStatus + " status")
    }

    @PutMapping("/orders/{id}/complete")
    fun complete(@PathVariable id: Long): ResponseEntity<Any> {
        val order = orderRepository.findById(id).get()

        if (order.orderStatus == Status.IN_PROGRESS) {
            order.orderStatus = Status.COMPLETED
            return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)))
        }

        return ResponseEntity
                .badRequest()
                .body("You can't complete an order that is in the " + order.orderStatus + " status")
    }
}