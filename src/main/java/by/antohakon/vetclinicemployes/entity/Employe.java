package by.antohakon.vetclinicemployes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "employes", indexes = {
        @Index(columnList = "employeeId", name = "employe_uuid_index"),
        @Index(columnList = "role", name = "employe_role_index")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID employeeId;
    @Column(unique = true, nullable = false)
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
