package by.antohakon.vetclinicemployes.repository;

import by.antohakon.vetclinicemployes.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface EmployeRepository extends JpaRepository<Employe,Long> {

}
