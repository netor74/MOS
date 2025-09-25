package io.rubuy74.mos.application;

import io.rubuy74.mos.domain.*;
import io.rubuy74.mos.port.in.MarketChangeHandler;
import io.rubuy74.mos.port.out.MarketChangeLogger;
import io.rubuy74.mos.port.out.MarketChangePublisher;
import io.rubuy74.mos.adapter.out.database.EventService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MarketChangeProcessor implements MarketChangeHandler {

    private final MarketChangeLogger marketChangeLogger;
    private final MarketChangePublisher marketChangePublisher;
    private final EventService eventService;

    @Autowired
    public MarketChangeProcessor(
            MarketChangeLogger marketChangeLogger,
            MarketChangePublisher marketChangePublisher,
            EventService eventService) {
        this.marketChangeLogger = marketChangeLogger;
        this.marketChangePublisher = marketChangePublisher;
        this.eventService = eventService;
    }


    private Market createMarket(MarketOperation marketOperation) {
        String marketId = marketOperation.marketRequest.marketId;
        String marketName = marketOperation.marketRequest.marketName;
        List<Selection> marketSelections = marketOperation.marketRequest.selections;

        return new Market(marketId,marketName,marketSelections);
    }

    private void logChanges(String message, MarketOperation marketOperation) {
        marketChangeLogger.log(
                message,
                marketOperation
        );
        marketChangePublisher.publish(
                message,
                marketOperation
        );
    }

    @Override
    @Transactional
    public void handle(MarketOperation marketOperation) {
        Optional<Event> optionalEvent = eventService.getEvent(marketOperation.marketRequest.eventDTO.id);
        switch (marketOperation.operationType) {
            case ADD:
                if (optionalEvent.isPresent()) {
                    Event event = optionalEvent.get();
                    String marketId = marketOperation.marketRequest.marketId;
                    List<String> marketIds = event.markets.stream().map(market -> market.id).toList();

                    if(!marketIds.contains(marketId)) {
                        Market newMarket = createMarket(marketOperation);
                        event.markets.add(newMarket);
                        eventService.updateEvent(event);

                        logChanges(
                                String.format("[SUCCESS] - Added market to event %s",event.id),
                                marketOperation
                        );

                    } else {
                        logChanges(
                                String.format("[FAILED] - Market %s already exists",marketId),
                                marketOperation
                        );
                        return;
                    }
                } else {
                    EventDTO eventDTO = marketOperation.marketRequest.eventDTO;
                    Event event = new Event(eventDTO.id,eventDTO.name,eventDTO.date);
                    Market newMarket = createMarket(marketOperation);
                    event.markets.add(newMarket);

                    logChanges(
                            String.format("[SUCCESS] - Created new event %s with market %s", event.id, newMarket.id),
                            marketOperation
                    );

                    eventService.createEvent(event);
                }
                return;
            case EDIT:
                if (optionalEvent.isPresent()) {
                    Event event = optionalEvent.get();
                    String marketId = marketOperation.marketRequest.marketId;
                    List<String> marketIds = event.markets.stream().map(market -> market.id).toList();
                    if(marketIds.contains(marketId)) {
                        Market newMarket = createMarket(marketOperation);
                        event.markets.add(newMarket);
                        eventService.updateEvent(event);

                        logChanges(
                                String.format("[SUCCESS] - Updated market %s on event %s",newMarket.id,event.id),
                                marketOperation
                        );
                    } else {
                        logChanges(
                                String.format("[FAILED] - Market %s does not exist",marketId),
                                marketOperation
                        );
                    }
                } else {
                    String eventId = marketOperation.marketRequest.eventDTO.id;
                    logChanges(
                            String.format("[FAILED] - Event %s does not exist",eventId),
                            marketOperation
                    );
                }
                return;
            case DELETE:
                if (optionalEvent.isPresent()) {
                    Event event = optionalEvent.get();
                    String marketId = marketOperation.marketRequest.marketId;
                    List<String> marketIds = event.markets.stream().map(market -> market.id).toList();
                    if(marketIds.contains(marketId)) {
                        Market market = createMarket(marketOperation);
                        event.markets.remove(market);
                        eventService.updateEvent(event);

                        logChanges(
                                String.format("[SUCCESS] - Market %s deleted from event %s",marketId,event.id),
                                marketOperation
                        );
                    } else {
                        logChanges(
                                String.format("[FAILED] - Market %s does not exist",marketId),
                                marketOperation
                        );
                    }
                } else {
                    String eventId = marketOperation.marketRequest.eventDTO.id;
                    logChanges(
                            String.format("[FAILED] - Event %s does not exist",eventId),
                            marketOperation
                    );
                }
                return;
            default:
        }
    }
}