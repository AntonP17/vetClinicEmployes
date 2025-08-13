package by.antohakon.vetclinicemployes.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record EmployeEvent(UUID visitId, String fullName) {
}
