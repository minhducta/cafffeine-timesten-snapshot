package com.tcbs.caffeine.timestensnapshot.hexagonal.domain.policy;

import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.Scenario;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.repo.ScenarioRepository;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.ScenarioId;

public class ScenarioParser {
    public Scenario parse(ScenarioId id, ScenarioRepository scenarioRepository) {
        return scenarioRepository
            .find(id)
            .map(scenario -> scenario.withId(id))
            .orElseGet(() -> null);
    }
}
