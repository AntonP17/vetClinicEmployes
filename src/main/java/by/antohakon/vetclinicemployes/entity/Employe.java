package by.antohakon.vetclinicemployes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employes")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String firstName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public Employe(String lastName, String firstName, Role role) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
    }
}
