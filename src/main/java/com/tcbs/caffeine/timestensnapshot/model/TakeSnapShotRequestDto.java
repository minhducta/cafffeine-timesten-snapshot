package com.tcbs.caffeine.timestensnapshot.model;

import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.ScenarioId;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TakeSnapShotRequestDto {
    String scenarioId;
    List<String> scenes;
}
