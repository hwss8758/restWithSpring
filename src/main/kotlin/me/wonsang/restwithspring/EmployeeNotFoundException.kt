package me.wonsang.restwithspring

class EmployeeNotFoundException(id: Long) : RuntimeException("Could not find employee $id")