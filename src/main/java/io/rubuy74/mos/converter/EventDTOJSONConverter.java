package io.rubuy74.mos.converter;

import io.rubuy74.mos.dto.EventDTO;
import io.rubuy74.mos.utils.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class EventDTOJSONConverter {
    public static Logger logger = LoggerFactory.getLogger(EventDTOJSONConverter.class);
    public static EventDTO fromJson(Map<String,Object> rawPayload) {
        if (rawPayload == null) {
            logger.error("operation=deserialize_event_dto, " +
                    "msg=Raw payload is null");
            return null;
        }

        List<String> attributeList = List.of("name", "date");
        List<String> errorMessages = ValidatorUtils.checkAttributeList(rawPayload,attributeList);
        if(!errorMessages.isEmpty()) {
            errorMessages.forEach((errorMessage) -> {
                logger.error("operation=deserialize_event_dto, msg={}", errorMessage);
            });
            return null;
        }

        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse((String) rawPayload.get("date"), formatter);

        return new EventDTO(id,name,date);
    }
}
