package by.antohakon.vetclinicemployes.service;

import by.antohakon.vetclinicemployes.dto.CreateEmployeDto;
import by.antohakon.vetclinicemployes.dto.EmployeDto;
import by.antohakon.vetclinicemployes.entity.Employe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeService {

    Page<EmployeDto> getAllEmployes(Pageable pageable);
    EmployeDto getEmployeById(Long id);
    EmployeDto createEmploye(CreateEmployeDto newEmploye);
    EmployeDto updateEmploye(Long id, CreateEmployeDto newEmploye);
    void deleteEmploye(Long id);

}
