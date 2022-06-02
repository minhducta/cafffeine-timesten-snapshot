package com.tcbs.caffeine.timestensnapshot.api.web;

import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.Scenario;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.SnapshotRecord;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.policy.ScenarioParser;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.repo.ScenarioRepository;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.ScenarioId;
import com.tcbs.caffeine.timestensnapshot.hexagonal.framework.adapter.input.SimpleInputAdapter;
import com.tcbs.caffeine.timestensnapshot.model.TakeSnapShotRequestDto;
import com.tcbs.caffeine.timestensnapshot.model.TakeSnapShotResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/snapshot")
@RequiredArgsConstructor
@Setter
public class SimpleController {
    private final SimpleInputAdapter inputAdapter;
    private final ScenarioParser scenarioParser;
    private final ScenarioRepository scenarioRepository;

    @PostMapping
    public TakeSnapShotResponseDto takeSnapshot(@RequestBody TakeSnapShotRequestDto requestDto) {
        Scenario scenario = scenarioParser.parse(ScenarioId.from(requestDto.getScenarioId()), scenarioRepository);
        if (scenario == null)
            return TakeSnapShotResponseDto.builder()
                    .success(false)
                    .build();
        SnapshotRecord record = inputAdapter.doSnapshot(scenario.withScenes(requestDto.getScenes()));
        return TakeSnapShotResponseDto.builder()
            .success(true)
            .recordId(record.getId().getValue())
            .build();
    }
}
