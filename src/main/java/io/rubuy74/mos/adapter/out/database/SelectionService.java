package io.rubuy74.mos.adapter.out.database;

import io.rubuy74.mos.domain.Selection;
import io.rubuy74.mos.port.out.SelectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectionService {
    @Autowired
    SelectionRepository selectionRepository;

    private static final Logger logger = LoggerFactory.getLogger(SelectionService.class);

    public List<Selection> getManagedSelections(List<Selection> incomingSelections) {

        return incomingSelections
                .stream()
                .map(incomingSelection -> {
                    String selectionId = incomingSelection.getId();
                    return selectionRepository.findById(selectionId)
                            .orElseGet(() -> {
                                logger.info("No selection found with id {}", selectionId);
                                return selectionRepository.save(incomingSelection);
                            });
                }).toList();
    }
}
