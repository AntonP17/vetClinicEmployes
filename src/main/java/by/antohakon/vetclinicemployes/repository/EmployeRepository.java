package by.antohakon.vetclinicemployes.repository;

import by.antohakon.vetclinicemployes.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.UUID;

@Repository
public interface EmployeRepository extends JpaRepository<Employe,Long> {
     boolean existsByLastName(String lastName);
     Employe findByEmployeeId(UUID employeeId);
}
