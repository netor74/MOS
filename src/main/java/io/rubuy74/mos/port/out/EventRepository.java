package io.rubuy74.mos.port.out;

import io.rubuy74.mos.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> { }
