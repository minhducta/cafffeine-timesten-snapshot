package com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity;

import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.ScenarioId;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Builder
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Scenario {
    @Setter(AccessLevel.PRIVATE)
    @With
    ScenarioId id;
    @With
    List<String> scenes;
    String description;
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.NONE)
    List<String> tables;

    public List<String> getTables() {
        return Collections.unmodifiableList(tables);
    }
}
