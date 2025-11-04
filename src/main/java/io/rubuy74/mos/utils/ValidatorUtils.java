package io.rubuy74.mos.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidatorUtils {
    public static void checkAttributeList(
            Map<String,Object> rawPayload,
            List<String> attributeList
    ) {
        List<String> errorMessages = new ArrayList<>();
        attributeList.forEach(attribute -> {
            if(!rawPayload.containsKey(attribute)) {
                errorMessages.add("attribute '" + attribute + "' doesn't exist");
            }
        });

        if(!errorMessages.isEmpty()) {
            throw new IllegalArgumentException(errorMessages.toString());
        }
    }

    public static void checkArgument(boolean expression, String errorMessage, String operation) {
        if (expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
