package by.antohakon.vetclinicemployes.dto.mapper;

import by.antohakon.vetclinicemployes.dto.CreateEmployeDto;
import by.antohakon.vetclinicemployes.dto.EmployeDto;
import by.antohakon.vetclinicemployes.entity.Employe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeMapper {

    @Mapping(source = "employeeId", target = "employeeId")
    EmployeDto toDto(Employe emp);

    @Mapping(target = "employeeId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "id", ignore = true)
    Employe toEntity(CreateEmployeDto empDto);

    List<EmployeDto> toDto(List<Employe> employeList);
}
