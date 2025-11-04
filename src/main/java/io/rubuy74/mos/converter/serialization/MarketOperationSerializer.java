package io.rubuy74.mos.converter.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.rubuy74.mos.domain.MarketOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InvalidObjectException;

public class MarketOperationSerializer {

    private final ObjectMapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketOperationSerializer.class);

    public MarketOperationSerializer() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public byte[] serialize(MarketOperation marketOperation) throws InvalidObjectException {
        if(marketOperation == null) {
            throw new InvalidObjectException("Market operation is null");
        }
        byte[] payload;
        try {
            payload = mapper.writeValueAsBytes(marketOperation);
            return payload;
        } catch (Exception e) {
            LOGGER.error("operation=serialize_market_operation, " +
                    "msg=Failed to serialize MarketOperation to JSON, " +
                    "error={}", e.getMessage(), e);
            throw new InvalidObjectException(e.getMessage());
        }
    }
}
