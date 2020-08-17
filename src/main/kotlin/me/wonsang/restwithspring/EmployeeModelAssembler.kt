package me.wonsang.restwithspring

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component

@Component
class EmployeeModelAssembler : RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    override fun toModel(entity: Employee): EntityModel<Employee> {
        return EntityModel.of(entity,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController::class.java).one(entity.id!!)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController::class.java).all()).withRel("employees")
        )
    }
}