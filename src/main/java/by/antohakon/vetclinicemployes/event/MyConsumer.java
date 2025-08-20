package by.antohakon.vetclinicemployes.event;

import by.antohakon.vetclinicemployes.dto.EmployeEvent;
import by.antohakon.vetclinicemployes.dto.ExceptionNotFoundDto;
import by.antohakon.vetclinicemployes.dto.VisitInfoDto;
import by.antohakon.vetclinicemployes.entity.Employe;
import by.antohakon.vetclinicemployes.exceptions.EmployeNotFoundException;
import by.antohakon.vetclinicemployes.repository.EmployeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyConsumer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EmployeRepository employeRepository;


    @KafkaListener(
            topics = "doctors",
            groupId = "doctorsGroup",
            containerFactory = "doctorsKafkaListenerContainerFactory"
    )
    public void listenDoctors(String message) {
        try {
            VisitInfoDto visitInfoDto = parseMessage(message);
            processDoctorRequest(visitInfoDto);
        } catch (JsonProcessingException e) {
            handleParsingError(message, e);
        } catch (EmployeNotFoundException e) {
            handleDoctorNotFound(e, message);
        } catch (Exception e) {
            handleGenericError(e, message);
        }
    }

    private VisitInfoDto parseMessage(String message) throws JsonProcessingException {
        log.info("Received message: {}", message);
        VisitInfoDto visitInfoDto = objectMapper.readValue(message, VisitInfoDto.class);
        log.info("Parsed successfully: {}", visitInfoDto);
        return visitInfoDto;
    }

    private void processDoctorRequest(VisitInfoDto visitInfoDto) throws JsonProcessingException {
        Employe employee = findDoctorById(visitInfoDto.doctorId());
        EmployeEvent employeEvent = createEmployeEvent(visitInfoDto, employee);
        sendSuccessResponse(employeEvent);
    }

    private Employe findDoctorById(UUID doctorId) {
        log.info("Searching doctor by ID: {}", doctorId);
        return Optional.ofNullable(employeRepository.findByEmployeeId(doctorId))
                .orElseThrow(() -> new EmployeNotFoundException("Doctor not found with id: " + doctorId));
    }

    private EmployeEvent createEmployeEvent(VisitInfoDto visitInfoDto, Employe employee) {
        return EmployeEvent.builder()
                .visitId(visitInfoDto.visitId())
                .fullName(employee.getFirstName() + " " + employee.getLastName())
                .build();
    }

    private void sendSuccessResponse(EmployeEvent employeEvent) throws JsonProcessingException {
        log.info("Sending response: {}", employeEvent);
        String json = objectMapper.writeValueAsString(employeEvent);
        kafkaTemplate.send("doctors_response", json);
    }

    private void handleDoctorNotFound(EmployeNotFoundException e, String originalMessage) {
        try {
            VisitInfoDto visitInfoDto = objectMapper.readValue(originalMessage, VisitInfoDto.class);
            String errorMessage = "Doctor not found with id " + visitInfoDto.doctorId();
            ExceptionNotFoundDto exceptionNotFoundDto = new ExceptionNotFoundDto(errorMessage, visitInfoDto.visitId());
            sendErrorResponse(exceptionNotFoundDto);
            log.error(errorMessage);
        } catch (JsonProcessingException ex) {
            log.error("Failed to parse original message for error handling: {}", originalMessage, ex);
        }
    }

    private void sendErrorResponse(ExceptionNotFoundDto errorDto) {
        try {
            String errorResponse = objectMapper.writeValueAsString(errorDto);
            kafkaTemplate.send("exceptions", errorResponse);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize error response: {}", e.getMessage());
        }
    }

    private void handleParsingError(String message, JsonProcessingException e) {
        log.error("Failed to parse message from JSON: {}", message, e);
        ExceptionNotFoundDto errorDto = new ExceptionNotFoundDto("Invalid JSON format", null);
        sendErrorResponse(errorDto);
    }

    private void handleGenericError(Exception e, String message) {
        log.error("Unexpected error processing message: {}", message, e);
        ExceptionNotFoundDto errorDto = new ExceptionNotFoundDto("Internal server error", null);
        sendErrorResponse(errorDto);
    }

}