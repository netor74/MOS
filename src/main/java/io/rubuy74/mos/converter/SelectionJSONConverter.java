package io.rubuy74.mos.converter;

import io.rubuy74.mos.domain.Selection;
import io.rubuy74.mos.utils.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class SelectionJSONConverter {
    private static final Logger logger = LoggerFactory.getLogger(SelectionJSONConverter.class);

    public static Selection fromJson(Map<String,Object> rawPayload) {

        List<String> attributeList = List.of("id","name","odd");
        List<String> errorMessages = ValidatorUtils.checkAttributeList(rawPayload, attributeList);
        if(!errorMessages.isEmpty()) {
            errorMessages.forEach((errorMessage) -> {
                logger.error("operation=deserialize_market_operation, msg={}", errorMessage);
            });
            return null;
        }

        String id = (String) rawPayload.get("id");
        String name = (String) rawPayload.get("name");
        Double odd = (Double) rawPayload.get("odd");
        return new Selection(id, name, odd);
    }
}
