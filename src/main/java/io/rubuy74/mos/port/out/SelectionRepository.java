package io.rubuy74.mos.port.out;

import io.rubuy74.mos.domain.Selection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectionRepository extends JpaRepository<Selection, String> {
}
