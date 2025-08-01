package by.antohakon.vetclinicemployes.controller;

import by.antohakon.vetclinicemployes.dto.CreateEmployeDto;
import by.antohakon.vetclinicemployes.dto.EmployeDto;
import by.antohakon.vetclinicemployes.entity.Employe;
import by.antohakon.vetclinicemployes.service.EmployeService;
import by.antohakon.vetclinicemployes.service.EmployeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/employes")
public class EmployeController {

    private final EmployeServiceImpl employeService;

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    public Page<EmployeDto> getAllEmployees(@PageableDefault(size = 5) Pageable pageable) {
        return employeService.getAllEmployes(pageable);
    }

    @GetMapping("/{employeId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EmployeDto getEmployee(@PathVariable Long employeId) {
        return employeService.getEmployeById(employeId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EmployeDto addEmployee(@RequestBody CreateEmployeDto employeDto) {
        return employeService.createEmploye(employeDto);
    }

    @PutMapping("/{employeId}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public EmployeDto updateEmployee(@PathVariable Long employeId, @RequestBody CreateEmployeDto employeDto) {
        return employeService.updateEmploye(employeId, employeDto);
    }

    @DeleteMapping("/{employeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long employeId) {
        employeService.deleteEmploye(employeId);
    }

}
