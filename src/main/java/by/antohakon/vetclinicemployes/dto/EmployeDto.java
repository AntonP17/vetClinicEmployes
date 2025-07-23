package by.antohakon.vetclinicemployes.dto;

import by.antohakon.vetclinicemployes.entity.Role;
import lombok.Builder;

@Builder
public record EmployeDto(Long id, String lastName, String firstName, Role role) {
}
