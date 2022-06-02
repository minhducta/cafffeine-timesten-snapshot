package com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value;

import lombok.Value;

@Value
public class ScenarioId {
    String value;

    public static ScenarioId from(String id) {
        return new ScenarioId(id);
    }
}
