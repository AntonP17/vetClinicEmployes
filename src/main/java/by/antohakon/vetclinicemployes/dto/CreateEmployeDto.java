package by.antohakon.vetclinicemployes.dto;

import by.antohakon.vetclinicemployes.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;


@Builder
public record CreateEmployeDto (

        @NotEmpty(message = "фамилия не должно быть пустым")
        @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
        @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\\s-]+$", message = "Фамилия может содержать только буквы, пробелы и дефисы")
        String lastName,

        @NotEmpty(message = "имя не должно быт пустым")
        @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
        @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\\s-]+$", message = "Имя может содержать только буквы, пробелы и дефисы")
        String firstName,

        @NotNull(message = "Должность должна быть указана")
        Role role) {
}
