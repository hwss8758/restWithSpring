package me.wonsang.restwithspring

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors


@RestController
class EmployeeController(val employeeRepository: EmployeeRepository,
                         val employeeModelAssembler: EmployeeModelAssembler) {

    //    @GetMapping("/employees")
    //    fun all(): CollectionModel<EntityModel<Employee>> {
    //
    //        // stream 함수보다는 kotlin에 맞게 아래와 같이 asSequence를 사용해 준다.
    //        val employees = employeeRepository.findAll().asSequence().map {
    //            EntityModel.of(it,
    //                    WebMvcLinkBuilder.linkTo(methodOn(EmployeeController::class.java).one(it.id!!)).withSelfRel(),
    //                    WebMvcLinkBuilder.linkTo(methodOn(EmployeeController::class.java).all()).withRel("employees"))
    //        }.toList()
    //        //        val employees = employeeRepository.findAll().stream().map { employee ->
    //        //            EntityModel.of(employee,
    //        //                    WebMvcLinkBuilder.linkTo(methodOn(EmployeeController::class.java).one(employee.id!!)).withSelfRel(),
    //        //                    WebMvcLinkBuilder.linkTo(methodOn(EmployeeController::class.java).all()).withRel("employees"))
    //        //        }.collect(Collectors.toList())
    //
    //        return CollectionModel.of(employees, WebMvcLinkBuilder.linkTo(methodOn(EmployeeController::class.java).all()).withSelfRel())
    //    }
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // 위의 함수를 RepresentationModelAssembler를 사용하여 아래와 같이 변경 할 수 있다. 코드가 간략해진다!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @GetMapping("/employees")
    fun all(): CollectionModel<EntityModel<Employee>> {

        //        val employees = employeeRepository
        //                .findAll()
        //                .asSequence()
        //                .map {
        //                    employeeModelAssembler.toModel(it)
        //                }.toList()

        val employees = employeeRepository
                .findAll()
                .asSequence()
                .map(employeeModelAssembler::toModel)
                .toList()

        return CollectionModel.of(employees, WebMvcLinkBuilder.linkTo(methodOn(EmployeeController::class.java).all()).withSelfRel())
    }

    @PostMapping("/employees")
    fun newEmployee(@RequestBody newEmployee: Employee): ResponseEntity<Any> {
        val entityModel: EntityModel<Employee> = employeeModelAssembler.toModel(employeeRepository.save(newEmployee))
        return ResponseEntity
                .created(entityModel
                        .getRequiredLink(IanaLinkRelations.SELF)
                        .toUri())
                .body(entityModel)
    }

    //    @GetMapping("/employees/{id}")
    //    fun one(@PathVariable id: Long): EntityModel<Employee> {
    //        //return employeeRepository.findById(id).orElseThrow { EmployeeNotFoundException(id) }
    //        //return employeeRepository.findById(id).get()
    //        val employee = employeeRepository.findById(id).orElseThrow { EmployeeNotFoundException(id) }
    //
    //        return EntityModel.of(employee,
    //                WebMvcLinkBuilder.linkTo(methodOn(EmployeeController::class.java).one(id)).withSelfRel(),
    //                WebMvcLinkBuilder.linkTo(methodOn(EmployeeController::class.java).all()).withRel("employees"))
    //    }
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // 위의 함수를 RepresentationModelAssembler를 사용하여 아래와 같이 변경 할 수 있다. 코드가 간략해진다!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @GetMapping("/employees/{id}")
    fun one(@PathVariable id: Long): EntityModel<Employee> {
        val employee = employeeRepository.findById(id).orElseThrow { EmployeeNotFoundException(id) }
        return employeeModelAssembler.toModel(employee)
    }

    @PutMapping("/employees/{id}")
    fun replaceEmployee(@RequestBody newEmployee: Employee,
                        @PathVariable id: Long): ResponseEntity<Any> {
        val getEmployee: Employee? = employeeRepository.findById(id).get()

        lateinit var returnEntity: Employee

        if (getEmployee != null) {
            getEmployee.name = newEmployee.name
            getEmployee.role = newEmployee.role
            returnEntity = employeeRepository.save(getEmployee)

        } else {
            newEmployee.id = id
            returnEntity = employeeRepository.save(newEmployee)
        }

        val entityModel: EntityModel<Employee> = employeeModelAssembler.toModel(returnEntity)

        return ResponseEntity
                .created(entityModel
                        .getRequiredLink(IanaLinkRelations.SELF)
                        .toUri())
                .body(entityModel)
    }

    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(@PathVariable id: Long): ResponseEntity<Any> {
        employeeRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}