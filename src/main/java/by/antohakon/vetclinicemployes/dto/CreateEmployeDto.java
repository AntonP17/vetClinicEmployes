package by.antohakon.vetclinicemployes.dto;

import by.antohakon.vetclinicemployes.entity.Role;

public record CreateEmployeDto (String lastName, String firstNme, Role role) {
}
