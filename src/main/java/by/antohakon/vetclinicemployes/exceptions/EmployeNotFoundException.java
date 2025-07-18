package by.antohakon.vetclinicemployes.exceptions;

public class EmployeNotFoundException extends RuntimeException {
    public EmployeNotFoundException(String message) {
        super(message);
    }
}
