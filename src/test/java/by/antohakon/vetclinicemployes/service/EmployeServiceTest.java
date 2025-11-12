package by.antohakon.vetclinicemployes.service;

import by.antohakon.vetclinicemployes.dto.CreateEmployeDto;
import by.antohakon.vetclinicemployes.dto.EmployeDto;
import by.antohakon.vetclinicemployes.dto.mapper.EmployeMapper;
import by.antohakon.vetclinicemployes.entity.Employe;
import by.antohakon.vetclinicemployes.entity.Role;
import by.antohakon.vetclinicemployes.exceptions.EmployeNotFoundException;
import by.antohakon.vetclinicemployes.repository.EmployeRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

    @Mock
    private EmployeRepository employeRepository;
    @Mock
    private EmployeMapper employeMapper;

    @InjectMocks
    private EmployeServiceImpl employeService;

    private static final UUID TEST_UUID = UUID.fromString("2e8f5f7b-7dfe-4c60-a03a-c38a1446033b");
    private AutoCloseable mockStatic;

    @BeforeEach
    void setUp() {
        mockStatic = Mockito.mockStatic(UUID.class);
        Mockito.when(UUID.randomUUID()).thenReturn(TEST_UUID);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockStatic.close();
    }

    @SneakyThrows
    @Test
    @DisplayName("возврат врача из бд положительный")
    void findEmployeByEmployeeId_positive() {


        UUID employeeId = UUID.fromString("2e8f5f7b-7dfe-4c60-a03a-c38a1446033b");

        Employe employe = Employe.builder()
                .id(1L)
                .employeeId(employeeId)
                .lastName("test")
                .firstName("test")
                .role(Role.ADMIN)
                .build();

        EmployeDto employeDto = EmployeDto.builder()
                .employeeId(employe.getEmployeeId())
                .lastName(employe.getLastName())
                .firstName(employe.getFirstName())
                .role(employe.getRole())
                .build();

        when(employeRepository.findByEmployeeId(employeeId)).thenReturn(employe);
        when(employeMapper.toDto(employe))
                .thenReturn(employeDto);

        EmployeDto result = employeService.getEmployeById(employeeId);

        assertNotNull(result);
        assertEquals(employeeId, result.employeeId());
        assertEquals(employeDto.lastName(), result.lastName());
        assertEquals(employeDto.firstName(), result.firstName());
        assertEquals(employeDto.role(), result.role());

    }

    @SneakyThrows
    @Test
    @DisplayName("создание врача в БД, положительный")
    void createEmployee_positive() {

        CreateEmployeDto createEmployeDto = CreateEmployeDto.builder()
                .lastName("Pokr")
                .firstName("Anton")
                .role(Role.ADMIN)
                .build();

        Employe newEmploye = Employe.builder()
                .employeeId(TEST_UUID)
                .lastName(createEmployeDto.lastName())
                .firstName(createEmployeDto.firstName())
                .role(createEmployeDto.role())
                .build();

        EmployeDto expectedDto = EmployeDto.builder()
                .employeeId(TEST_UUID)
                .lastName(newEmploye.getLastName())
                .firstName(newEmploye.getFirstName())
                .role(newEmploye.getRole())
                .build();

        when(employeRepository.existsByLastName(createEmployeDto.lastName()))
                .thenReturn(false);
        when(employeMapper.toEntity(createEmployeDto)).thenReturn(newEmploye);
        when(employeRepository.save(newEmploye)).thenReturn(newEmploye);
        when(employeMapper.toDto(newEmploye)).thenReturn(expectedDto);

        EmployeDto result = employeService.createEmploye(createEmployeDto);


        assertNotNull(result);
        assertEquals(expectedDto.employeeId(), result.employeeId());
        assertEquals(expectedDto.lastName(), result.lastName());
        assertEquals(expectedDto.firstName(), result.firstName());
        assertEquals(expectedDto.role(), result.role());

    }

    @Test
    @DisplayName("Обновление данных врача, позитивный")
    void updateEmploye_positive() {

        UUID employeeId = UUID.fromString("2e8f5f7b-7dfe-4c60-a03a-c38a1446033b");

        CreateEmployeDto updateDto = CreateEmployeDto.builder()
                .lastName("НоваяФамилия")
                .firstName("НовоеИмя")
                .role(Role.ADMIN)
                .build();

        Employe existingEmploye = Employe.builder()
                .id(1L)
                .employeeId(employeeId)
                .lastName("СтараяФамилия")
                .firstName("СтароеИмя")
                .role(Role.ADMIN)
                .build();

        EmployeDto expectedDto = EmployeDto.builder()
                .employeeId(employeeId)
                .lastName("НоваяФамилия")
                .firstName("НовоеИмя")
                .role(Role.ADMIN)
                .build();

        when(employeRepository.findByEmployeeId(employeeId))
                .thenReturn(existingEmploye);
        when(employeRepository.save(any(Employe.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(employeMapper.toDto(any(Employe.class)))
                .thenReturn(expectedDto);

        EmployeDto result = employeService.updateEmploye(employeeId, updateDto);

        assertNotNull(result);
        assertEquals(employeeId, result.employeeId());
        assertEquals("НоваяФамилия", result.lastName());
        assertEquals("НовоеИмя", result.firstName());
        assertEquals(Role.ADMIN, result.role());
    }

    @Test
    @DisplayName("Обновление несуществующего врача, негативный")
    void updateEmploye_negative() {

        UUID nonExistentId = UUID.randomUUID();

        CreateEmployeDto updateDto = CreateEmployeDto.builder()
                .lastName("Фамилия")
                .firstName("Имя")
                .role(Role.ADMIN)
                .build();

        when(employeRepository.findByEmployeeId(nonExistentId))
                .thenReturn(null);

        assertThrows(EmployeNotFoundException.class, () -> {
            employeService.updateEmploye(nonExistentId, updateDto);
        });
    }

    @Test
    @DisplayName("Удаление врача, позитивный")
    void deleteEmploye_positive() {

        UUID employeeId = UUID.fromString("2e8f5f7b-7dfe-4c60-a03a-c38a1446033b");

        Employe existingEmploye = Employe.builder()
                .id(1L)
                .employeeId(employeeId)
                .lastName("Фамилия")
                .firstName("Имя")
                .role(Role.ADMIN)
                .build();

        when(employeRepository.findByEmployeeId(employeeId))
                .thenReturn(existingEmploye);
        doNothing().when(employeRepository).delete(existingEmploye);

        assertDoesNotThrow(() -> employeService.deleteEmploye(employeeId));
    }

    @Test
    @DisplayName("Удаление несуществующего врача, негативный")
    void deleteEmploye_NotFound() {
        UUID nonExistentId = UUID.randomUUID();

        when(employeRepository.findByEmployeeId(nonExistentId))
                .thenReturn(null);

        assertThrows(EmployeNotFoundException.class, () -> {
            employeService.deleteEmploye(nonExistentId);
        });
    }
}
