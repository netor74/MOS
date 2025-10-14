package io.rubuy74.mos.converter.deserialization;

import io.rubuy74.mos.dto.EventDTO;
import io.rubuy74.mos.utils.ValidatorUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EventDTODeserializer {
    private static final List<String> ATTRIBUTE_LIST = List.of("name", "date");

    private static boolean checkDate(long date) {
        try  {
            return LocalDate.now()
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli() > date ;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static Optional<IllegalArgumentException> checkEventValidity(Map<String,Object> rawPayload) {
        try {
            ValidatorUtils.checkArgument(
                    rawPayload == null,
                    "EventDTO payload is null",
                    "deserialize_event_dto");
            ValidatorUtils.checkAttributeList(
                    rawPayload,
                    ATTRIBUTE_LIST);
            ValidatorUtils.checkArgument(
                    !(checkDate((long) rawPayload.get("date"))),
                    "Event DTO Date is invalid",
                    "deserialize_event_dto"
            );
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            return Optional.of(e);
        }
    }

    public static EventDTO deserialize(Map<String,Object> rawPayload) {
        Optional<IllegalArgumentException> InvalidDateError = checkEventValidity(rawPayload);
        if (InvalidDateError.isPresent()) {
            throw InvalidDateError.get();
        }

        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        long date = (long) rawPayload.get("date");
        return new EventDTO(id,name,date);
    }
}
