package by.antohakon.vetclinicemployes.service;

import by.antohakon.vetclinicemployes.dto.CreateEmployeDto;
import by.antohakon.vetclinicemployes.dto.EmployeDto;
import by.antohakon.vetclinicemployes.dto.mapper.EmployeMapper;
import by.antohakon.vetclinicemployes.entity.Employe;
import by.antohakon.vetclinicemployes.exceptions.EmployeNotFoundException;
import by.antohakon.vetclinicemployes.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeServiceImpl implements EmployeService {

     private final EmployeRepository employeRepository;
     private final EmployeMapper employeMapper;

    @Override
    public Page<EmployeDto> getAllEmployes(Pageable pageable) {

        return employeRepository.findAll(pageable)
                .map(employe -> EmployeDto.builder()
                        .id(employe.getId())
                        .lastName(employe.getLastName())
                        .firstNme(employe.getFirstName())
                        .role(employe.getRole())
                .build());
    }

    @Override
    public EmployeDto getEmployeById(Long id) {
        Employe findEmploye = employeRepository.findById(id)
                .orElseThrow(() -> new EmployeNotFoundException("Emplye not found with id " + id));

        return employeMapper.toDto(findEmploye);
    }

    @Override
    public EmployeDto createEmploye(CreateEmployeDto newEmploye) {
        return null;
    }

    @Override
    public EmployeDto updateEmploye(Long id, CreateEmployeDto newEmploye) {
        return null;
    }

    @Override
    public void deleteEmploye(Long id) {

    }
}
