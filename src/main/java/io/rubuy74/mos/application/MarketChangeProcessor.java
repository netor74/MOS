package io.rubuy74.mos.application;

import io.rubuy74.mos.adapter.out.database.SelectionService;
import io.rubuy74.mos.domain.*;
import io.rubuy74.mos.domain.event.Event;
import io.rubuy74.mos.domain.event.EventDTO;
import io.rubuy74.mos.domain.http.ResultType;
import io.rubuy74.mos.domain.market.Market;
import io.rubuy74.mos.domain.market.MarketOperation;
import io.rubuy74.mos.domain.http.MarketOperationResult;
import io.rubuy74.mos.port.in.MarketChangeHandler;
import io.rubuy74.mos.port.out.MarketChangePublisher;
import io.rubuy74.mos.adapter.out.database.EventService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MarketChangeProcessor implements MarketChangeHandler {

    private final MarketChangePublisher marketChangePublisher;
    private final EventService eventService;
    private final SelectionService selectionService;
    private static final Logger logger = LoggerFactory.getLogger(MarketChangeProcessor.class);

    @Autowired
    public MarketChangeProcessor(
            MarketChangePublisher marketChangePublisher,
            EventService eventService,
            SelectionService selectionService) {
        this.marketChangePublisher = marketChangePublisher;
        this.eventService = eventService;
        this.selectionService = selectionService;
    }


    private Market createMarket(MarketOperation marketOperation) {
        String marketId = marketOperation.marketRequest.marketId;
        String marketName = marketOperation.marketRequest.marketName;
        List<Selection> detachedSelections = marketOperation.marketRequest.selections;
        List<Selection> marketSelections = selectionService.getManagedSelections(detachedSelections);

        return new Market(marketId,marketName,marketSelections);
    }

    private void logChanges(ResultType resultType, String message, MarketOperation marketOperation) {
        logger.info(message);
        MarketOperationResult marketOperationResult = new MarketOperationResult(resultType,message,marketOperation);
        marketChangePublisher.publish(marketOperationResult);
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
                                ResultType.SUCCESS,
                                String.format("Added market %s to event %s",marketId,event.id),
                                marketOperation
                        );

                    } else {
                        logChanges(
                                ResultType.FAILURE,
                                String.format("Market %s already exists in event %s",marketId,event.id),
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
                            ResultType.SUCCESS,
                            String.format("Created new event %s with market %s", event.id, newMarket.id),
                            marketOperation
                    );

                    eventService.createEvent(event);
                }
                return;
            case EDIT:
                if (optionalEvent.isPresent()) {

                    Event event = optionalEvent.get();
                    String marketId = marketOperation.marketRequest.marketId;
                    Optional<Market> existingMarketOptional = event.markets.stream()
                            .filter(market -> market.id.equals(marketId))
                            .findFirst();

                    if(existingMarketOptional.isPresent()) {
                        Market existingMarket = existingMarketOptional.get();
                        Market newMarket = createMarket(marketOperation);
                        existingMarket.name = newMarket.name;
                        existingMarket.selections = newMarket.selections;

                        event.markets.forEach(market -> {
                            logger.error("Market ID: {} - Selections Class: {}",
                                    market.id, market.selections.getClass().getName());
                        });
                        eventService.updateEvent(event);

                        logChanges(
                                ResultType.SUCCESS,
                                String.format("Updated market %s on event %s",newMarket.id,event.id),
                                marketOperation
                        );
                    } else {
                        logChanges(
                                ResultType.FAILURE,
                                String.format("Market %s does not exist in event %s",marketId,event.id),
                                marketOperation
                        );
                    }
                } else {
                    String eventId = marketOperation.marketRequest.eventDTO.id;
                    logChanges(
                            ResultType.FAILURE,
                            String.format("Event %s does not exist",eventId),
                            marketOperation
                    );
                }
                return;
            case DELETE:
                if (optionalEvent.isPresent()) {
                    Event event = optionalEvent.get();
                    String marketId = marketOperation.marketRequest.marketId;

                    Optional<Market> existingMarketOptional = event.markets.stream()
                            .filter(market -> market.id.equals(marketId))
                            .findFirst();

                    if(existingMarketOptional.isPresent()) {
                        Market market = existingMarketOptional.get();
                        event.markets.remove(market);
                        eventService.updateEvent(event);

                        logChanges(
                                ResultType.SUCCESS,
                                String.format("Market %s deleted from event %s",marketId,event.id),
                                marketOperation
                        );
                    } else {
                        logChanges(
                                ResultType.FAILURE,
                                String.format("Market %s does not exist in event %s",marketId,event.id),
                                marketOperation
                        );
                    }
                } else {
                    String eventId = marketOperation.marketRequest.eventDTO.id;
                    logChanges(
                            ResultType.FAILURE,
                            String.format("Event %s does not exist",eventId),
                            marketOperation
                    );
                }
                return;
            default:
        }
    }
}