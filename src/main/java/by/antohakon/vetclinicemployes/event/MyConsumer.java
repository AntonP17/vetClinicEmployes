package by.antohakon.vetclinicemployes.event;

import by.antohakon.vetclinicemployes.dto.EmployeEvent;
import by.antohakon.vetclinicemployes.dto.ExceptionNotFoundDto;
import by.antohakon.vetclinicemployes.dto.VisitInfoDto;
import by.antohakon.vetclinicemployes.entity.Employe;
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
    public void listenDoctors(String message)  {

        VisitInfoDto visitInfoDto = null;
        try {
            log.info("Take message {}", message);
            visitInfoDto = objectMapper.readValue(message, VisitInfoDto.class);

            log.info("after parsing {}", visitInfoDto.toString());
        } catch (JsonProcessingException e) {
            log.error("Failed to parse order from JSON: {}", message, e);
        }

        log.info("try find doctor by id : {}", visitInfoDto.doctorId());

        try {

            Employe employee = employeRepository.findByEmployeeId(visitInfoDto.doctorId());

            EmployeEvent employeEvent = EmployeEvent.builder()
                    .visitId(visitInfoDto.visitId())
                    .fullName(employee.getFirstName() + " " + employee.getLastName())
                    .build();

            log.info("return doctor : {}", employeEvent.toString());

            String json = objectMapper.writeValueAsString(employeEvent);
            kafkaTemplate.send("doctors_response", json);

        } catch (NullPointerException e) {

            String errorMessage = "doctor not found with id " + visitInfoDto.doctorId();
            ExceptionNotFoundDto exceptionNotFoundDto = new ExceptionNotFoundDto(errorMessage, visitInfoDto.visitId());
            String errorResponse = null;

            try {

                errorResponse = objectMapper.writeValueAsString(exceptionNotFoundDto);

            } catch (JsonProcessingException ex) {

                log.error("Failed to serialize order: {}", e.getMessage());
            }

            kafkaTemplate.send("exceptions", errorResponse);
            log.error(errorMessage);

        } catch (JsonProcessingException e) {

            log.error("Failed to serialize order: {}", e.getMessage());

        }

    }

}

// if (employee == null) {
//String errorMessage = "doctor not found with id " + visitInfoDto.doctorId();
//ExceptionNotFoundDto exceptionNotFoundDto = new ExceptionNotFoundDto(errorMessage, visitInfoDto.visitId());
//String errorResponse = objectMapper.writeValueAsString(exceptionNotFoundDto);
//            kafkaTemplate.send("exceptions", errorResponse);
//            log.error(errorMessage);
//        }
//    try {
//String json = objectMapper.writeValueAsString(employeEvent);
//            kafkaTemplate.send("doctors_response", json);
//        } catch (JsonProcessingException e) {
//        log.error("Failed to serialize order: {}", e.getMessage());
//        }