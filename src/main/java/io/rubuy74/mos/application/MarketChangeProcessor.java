package io.rubuy74.mos.application;

import io.rubuy74.mos.adapter.out.database.SelectionService;
import io.rubuy74.mos.domain.*;
import io.rubuy74.mos.domain.internal.MarketOperationResult;
import io.rubuy74.mos.domain.internal.OperationType;
import io.rubuy74.mos.domain.internal.ResultType;
import io.rubuy74.mos.dto.EventDTO;
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
        String marketId = marketOperation.getMarketRequest().getMarketId();
        String marketName = marketOperation.getMarketRequest().getMarketName();
        List<Selection> detachedSelections = marketOperation.getMarketRequest().getSelections();
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
        String eventID = marketOperation.getMarketRequest().getEventDTO().getId();
        Optional<Event> optionalEvent = eventService.getEventById(eventID);
        
        if(optionalEvent.isEmpty() && !marketOperation.getOperationType().equals(OperationType.ADD)) {
            logChanges(
                    ResultType.FAILURE,
                    String.format("Event %s does not exist",eventID),
                    marketOperation
            );
        }
        if(optionalEvent.isPresent()) {

        }
        switch (marketOperation.getOperationType()) {
            case ADD:
                if (optionalEvent.isPresent()) {
                    Event event = optionalEvent.get();
                    String marketId = marketOperation.getMarketRequest().getMarketId();
                    List<String> marketIds = event.getMarkets().stream().map(Market::getId).toList();

                    if(!marketIds.contains(marketId)) {
                        Market newMarket = createMarket(marketOperation);
                        event.getMarkets().add(newMarket);
                        eventService.updateEvent(event);

                        logChanges(
                                ResultType.SUCCESS,
                                String.format("Added market %s to event %s",marketId, event.getId()),
                                marketOperation
                        );

                    } else {
                        logChanges(
                                ResultType.FAILURE,
                                String.format("Market %s already exists in event %s",marketId, event.getId()),
                                marketOperation
                        );
                        return;
                    }
                } else {
                    EventDTO eventDTO = marketOperation.getMarketRequest().getEventDTO();
                    Event event = new Event(eventDTO.getId(), eventDTO.getName(),eventDTO.getEpochMilliseconds());
                    Market newMarket = createMarket(marketOperation);
                    event.getMarkets().add(newMarket);

                    logChanges(
                            ResultType.SUCCESS,
                            String.format("Created new event %s with market %s", event.getId(), newMarket.getId()),
                            marketOperation
                    );

                    eventService.createEvent(event);
                }
                return;
            case EDIT:
                if (optionalEvent.isPresent()) {
                    Event event = optionalEvent.get();
                    String marketId = marketOperation.getMarketRequest().getMarketId();
                    Optional<Market> existingMarketOptional = event.getMarkets().stream()
                            .filter(market -> market.getId().equals(marketId))
                            .findFirst();

                    if(existingMarketOptional.isPresent()) {
                        Market existingMarket = existingMarketOptional.get();
                        Market newMarket = createMarket(marketOperation);
                        existingMarket.setName(newMarket.getName());
                        existingMarket.selections = newMarket.selections;

                        event.getMarkets().forEach(market -> {
                            logger.error("Market ID: {} - Selections Class: {}",
                                    market.getId(), market.selections.getClass().getName());
                        });
                        eventService.updateEvent(event);

                        logChanges(
                                ResultType.SUCCESS,
                                String.format("Updated market %s on event %s", newMarket.getId(), event.getId()),
                                marketOperation
                        );
                    } else {
                        logChanges(
                                ResultType.FAILURE,
                                String.format("Market %s does not exist in event %s",marketId, event.getId()),
                                marketOperation
                        );
                    }
                }
                return;
            case DELETE:
                if (optionalEvent.isPresent()) {
                    Event event = optionalEvent.get();
                    String marketId = marketOperation.getMarketRequest().getMarketId();

                    Optional<Market> existingMarketOptional = event.getMarkets().stream()
                            .filter(market -> market.getId().equals(marketId))
                            .findFirst();

                    if(existingMarketOptional.isPresent()) {
                        Market market = existingMarketOptional.get();
                        event.getMarkets().remove(market);
                        eventService.updateEvent(event);

                        logChanges(
                                ResultType.SUCCESS,
                                String.format("Market %s deleted from event %s",marketId, event.getId()),
                                marketOperation
                        );
                    } else {
                        logChanges(
                                ResultType.FAILURE,
                                String.format("Market %s does not exist in event %s",marketId, event.getId()),
                                marketOperation
                        );
                    }
                }
                return;
            default:
        }
    }
}