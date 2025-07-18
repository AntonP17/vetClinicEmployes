package by.antohakon.vetclinicemployes.dto.mapper;

import by.antohakon.vetclinicemployes.dto.CreateEmployeDto;
import by.antohakon.vetclinicemployes.dto.EmployeDto;
import by.antohakon.vetclinicemployes.entity.Employe;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeMapper {

    EmployeDto toDto(Employe emp);
    Employe toEntity(CreateEmployeDto empDto);

    List<EmployeDto> toDto(List<Employe> employeList);
}
