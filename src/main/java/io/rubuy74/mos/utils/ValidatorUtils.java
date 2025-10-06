package io.rubuy74.mos.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidatorUtils {


    public static List<String> checkAttributeList(
            Map<String,Object> rawPayload,
            List<String> attributeList
    ) {
        List<String> messages = new ArrayList<>();
        attributeList.forEach(attribute -> {
            if(!rawPayload.containsKey(attribute)) {
                messages.add("attribute '" + attribute + "' doesn't exist");
            }
        });
        return messages;
    }
}
