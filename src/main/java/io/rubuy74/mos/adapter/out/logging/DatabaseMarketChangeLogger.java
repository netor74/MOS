package io.rubuy74.mos.adapter.out.logging;

import io.rubuy74.mos.port.out.MarketChangeLogger;
import io.rubuy74.mos.domain.MarketOperation;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMarketChangeLogger  implements MarketChangeLogger {
    @Override
    // TODO: Log changes to a database for persistency of logs
    public void log(String message, MarketOperation marketOperation) {

    }
}
