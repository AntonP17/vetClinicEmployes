package by.antohakon.vetclinicemployes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lastName;

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
