package io.rubuy74.mos.port.in;

import io.rubuy74.mos.domain.event.Event;

import java.util.List;

public interface MarketListHandler {
    List<Event> list();
}
