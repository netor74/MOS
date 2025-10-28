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

import static io.rubuy74.mos.utils.MarketCreator.createMarket;

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


    private void logChanges(ResultType resultType, String message, MarketOperation marketOperation) {
        logger.info(message);
        MarketOperationResult marketOperationResult = new MarketOperationResult(marketOperation.getRequestId(), resultType,message,marketOperation);
        marketChangePublisher.publish(marketOperationResult);
    }


    @Override
    @Transactional
    public void handle(MarketOperation marketOperation) {
        OperationType operationType = marketOperation.getOperationType();
        String eventID = marketOperation.getMarketRequest().getEventDTO().getId();
        Optional<Event> optionalEvent = eventService.getEventById(eventID);
        
        if(optionalEvent.isEmpty()) {
            if(marketOperation.getOperationType().equals(OperationType.ADD)) {
                EventDTO eventDTO = marketOperation.getMarketRequest().getEventDTO();
                String marketId = marketOperation.getMarketRequest().getMarketId();
                eventService.addEvent(selectionService,marketOperation,eventDTO);
                logChanges(
                        ResultType.SUCCESS,
                        String.format("Created new event %s with market %s", eventDTO.getId(),marketId),
                        marketOperation
                );
            } else {
                logChanges(
                        ResultType.FAILURE,
                        String.format("Event %s does not exist",eventID),
                        marketOperation
                );
            }
            return;
        }

        Event event = optionalEvent.get();
        String marketId = marketOperation.getMarketRequest().getMarketId();
        List<String> marketIds = event.getMarkets().stream().map(Market::getId).toList();

        if(operationType.equals(OperationType.ADD)) {
            if(!marketIds.contains(marketId)) {
                eventService.addMarket(selectionService,marketOperation, event);
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
            }
        } else {
            Optional<Market> existingMarketOptional = event.getMarkets().stream()
                    .filter(market -> market.getId().equals(marketId))
                    .findFirst();

            if(existingMarketOptional.isPresent()) {
                Market existingMarket = existingMarketOptional.get();
                if(operationType.equals(OperationType.DELETE)) {
                    eventService.deleteMarket(event,existingMarket);
                    logChanges(
                            ResultType.SUCCESS,
                            String.format("Market %s deleted from event %s",marketId, event.getId()),
                            marketOperation
                    );
                } else if (operationType.equals(OperationType.EDIT)) {
                    Market newMarket = createMarket(selectionService,marketOperation);
                    eventService.updateMarket(event,existingMarket,newMarket);
                    logChanges(
                            ResultType.SUCCESS,
                            String.format("Updated market %s on event %s", newMarket.getId(), event.getId()),
                            marketOperation
                    );
                }
            } else {
                logChanges(
                        ResultType.FAILURE,
                        String.format("Market %s does not exist in event %s",marketId, event.getId()),
                        marketOperation
                );
            }
        }
    }
}