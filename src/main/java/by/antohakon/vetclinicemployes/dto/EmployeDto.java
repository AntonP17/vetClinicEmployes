package by.antohakon.vetclinicemployes.dto;

import by.antohakon.vetclinicemployes.entity.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record EmployeDto(UUID doctorId, String lastName, String firstName, Role role) {
}
