package by.antohakon.vetclinicemployes.dto;

import by.antohakon.vetclinicemployes.entity.Role;
import lombok.Builder;

@Builder
public record CreateEmployeDto (String lastName, String firstName, Role role) {
}
