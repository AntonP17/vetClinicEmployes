package by.antohakon.vetclinicemployes.service;

import by.antohakon.vetclinicemployes.dto.CreateEmployeDto;
import by.antohakon.vetclinicemployes.dto.EmployeDto;
import by.antohakon.vetclinicemployes.dto.mapper.EmployeMapper;
import by.antohakon.vetclinicemployes.entity.Employe;
import by.antohakon.vetclinicemployes.exceptions.EmployeDuplicationException;
import by.antohakon.vetclinicemployes.exceptions.EmployeNotFoundException;
import by.antohakon.vetclinicemployes.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeServiceImpl implements EmployeService {

    private final EmployeRepository employeRepository;
    private final EmployeMapper employeMapper;

    @Override
    public Page<EmployeDto> getAllEmployes(Pageable pageable) {

        log.info("getAllEmployes");
        return employeRepository.findAll(pageable)
                .map(employe -> EmployeDto.builder()
                        .doctorId(employe.getEmployeeId())
                        .lastName(employe.getLastName())
                        .firstName(employe.getFirstName())
                        .role(employe.getRole())
                        .build());
    }

    @Override
    @Cacheable(value = "employe_cache", key = "#emploeId")
    public EmployeDto getEmployeById(UUID emploeId) {

        log.info("getEmployeById");
        Employe findEmploye = employeRepository.findByEmployeeId(emploeId);
        if (findEmploye == null) {
            log.error("getEmployeById: employeId not found");
            throw new  EmployeNotFoundException("Emplye not found with id " + emploeId);
        }

      //  return employeMapper.toDto(findEmploye);
        return EmployeDto.builder()
                .doctorId(findEmploye.getEmployeeId())
                .lastName(findEmploye.getLastName())
                .firstName(findEmploye.getFirstName())
                .role(findEmploye.getRole())
                .build();
    }

    @Override
    @CachePut(value = "employe_cache", key = "#result.doctorId")
    public EmployeDto createEmploye(CreateEmployeDto employe) {

        log.info("createEmploye");
        if (employeRepository.existsByLastName(employe.lastName())) {
            log.error("lastName already exists");
            throw new EmployeDuplicationException("Employee already exists with last name " + employe.lastName());
        }

        // Employe newEmployee = employeMapper.toEntity(employe);
        Employe newEmployee = Employe.builder()
                .employeeId(UUID.randomUUID())
                .lastName(employe.lastName())
                .firstName(employe.firstName())
                .role(employe.role())
                .build();

        employeRepository.save(newEmployee);

  //    return employeMapper.toDto(newEmployee);
        return EmployeDto.builder()
                .doctorId(newEmployee.getEmployeeId())
                .lastName(newEmployee.getLastName())
                .firstName(newEmployee.getFirstName())
                .role(newEmployee.getRole())
                .build();

    }

    @Override
    @CachePut(value = "employe_cache", key = "#employeId")
    public EmployeDto updateEmploye(UUID employeId, CreateEmployeDto newEmploye) {

        log.info("updateEmploye");
        Employe findEmploye = employeRepository.findByEmployeeId(employeId);
        if (findEmploye == null) {
            log.error("Employee not found with id " + employeId);
           throw new EmployeNotFoundException("Emplye not found with id " + employeId);
        }

        findEmploye.setLastName(newEmploye.lastName());
        findEmploye.setFirstName(newEmploye.firstName());
        findEmploye.setRole(newEmploye.role());
        employeRepository.save(findEmploye);

       // return employeMapper.toDto(findEmploye);
        EmployeDto employeDto = EmployeDto.builder()
                .doctorId(findEmploye.getEmployeeId())
                .lastName(findEmploye.getLastName())
                .firstName(findEmploye.getFirstName())
                .role(findEmploye.getRole())
                .build();

        return employeDto;
    }

    @Override
    @CacheEvict(value = "employe_cache", key = "#employeId")
    public void deleteEmploye(UUID employeId) {

        log.info("deleteEmploye");
        Employe findEmploye = employeRepository.findByEmployeeId(employeId);
        if (findEmploye == null) {
            log.error("Employee not found with id " + employeId);
           throw new EmployeNotFoundException("Emplye not found with id " + employeId);
        }

        employeRepository.delete(findEmploye);

    }
}
