package com.tcbs.caffeine.timestensnapshot.hexagonal.domain.repo;

import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.Scenario;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.ScenarioId;

import java.util.Optional;

public interface ScenarioRepository {
    Optional<Scenario> find(ScenarioId id);
}
