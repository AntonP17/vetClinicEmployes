package by.antohakon.vetclinicemployes.service;

import by.antohakon.vetclinicemployes.dto.CreateEmployeDto;
import by.antohakon.vetclinicemployes.dto.EmployeDto;
import by.antohakon.vetclinicemployes.entity.Employe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EmployeService {

    Page<EmployeDto> getAllEmployes(Pageable pageable);
    EmployeDto getEmployeById(UUID employeeId);
    EmployeDto createEmploye(CreateEmployeDto newEmploye);
    EmployeDto updateEmploye(UUID id, CreateEmployeDto newEmploye);
    void deleteEmploye(UUID id);

}
