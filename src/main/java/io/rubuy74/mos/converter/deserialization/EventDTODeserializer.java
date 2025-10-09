package io.rubuy74.mos.converter.deserialization;

import io.rubuy74.mos.dto.EventDTO;
import io.rubuy74.mos.utils.ValidatorUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EventDTODeserializer {
    private static final List<String> ATTRIBUTE_LIST = List.of("name", "date");

    private static boolean checkDateValidity(String date) {
        if(!date.matches("\\d\\d\\d\\d([/\\-])\\d\\d([/\\-])\\d\\d")) {
            return false;
        }
        return LocalDate.parse(date).isAfter(LocalDate.now());
    }

    private static void checkEventValidity(Map<String,Object> rawPayload) {
        ValidatorUtils.checkArgument(
                rawPayload == null,
                "EventDTO payload is null",
                "deserialize_event_dto");
        ValidatorUtils.checkAttributeList(
                rawPayload,
                ATTRIBUTE_LIST);
        ValidatorUtils.checkArgument(
                !(
                        rawPayload.get("date") instanceof String &&
                        checkDateValidity((String) rawPayload.get("date"))
                ),
                "Event DTO Date is invalid",
                "deserialize_event_dto"
        );
    }

    public static EventDTO deserialize(Map<String,Object> rawPayload) {
        checkEventValidity(rawPayload); // throws exception if not valid

        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        LocalDate date = LocalDate.parse((String) rawPayload.get("date"));

        return new EventDTO(id,name,date);
    }
}
