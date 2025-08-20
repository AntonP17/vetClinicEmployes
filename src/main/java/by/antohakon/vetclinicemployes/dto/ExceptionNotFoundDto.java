package by.antohakon.vetclinicemployes.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ExceptionNotFoundDto(String errorMessage, UUID visitId) {
}
