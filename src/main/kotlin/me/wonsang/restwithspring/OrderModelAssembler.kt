package me.wonsang.restwithspring

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component

@Component
class OrderModelAssembler : RepresentationModelAssembler<Orders, EntityModel<Orders>> {
    override fun toModel(entity: Orders): EntityModel<Orders> {
        val orderModel = EntityModel.of(entity,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController::class.java).one(entity.id!!)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController::class.java).all()).withRel("orders")
        )

        if (entity.orderStatus == Status.IN_PROGRESS) {
            orderModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController::class.java).cancel(entity.id!!)).withRel("cancle"))
            orderModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController::class.java).complete(entity.id!!)).withRel("complete"))
        }

        return orderModel
    }
}