package me.wonsang.restwithspring

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AppRunner(val employeeRepository: EmployeeRepository,
                val orderRepository: OrderRepository) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        employeeRepository.save(Employee(name = "Bilbo Baggins", role = "burglar"))
        employeeRepository.save(Employee(name = "Frodo Baggins", role = "thief"))

        orderRepository.save(Orders(orderDescription = "iPhone", orderStatus = Status.IN_PROGRESS))
        orderRepository.save(Orders(orderDescription = "MacBook Pro", orderStatus = Status.COMPLETED))
    }
}