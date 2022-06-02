package com.tcbs.caffeine.timestensnapshot.hexagonal.framework.adapter.input;

import com.tcbs.caffeine.timestensnapshot.hexagonal.application.usecase.RecordPersistenceUseCase;
import com.tcbs.caffeine.timestensnapshot.hexagonal.application.usecase.TakeSnapshotUseCase;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.Scenario;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.SnapshotRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Component
public class SimpleInputAdapter {
    private final TakeSnapshotUseCase takeSnapshotUseCase;
    private final RecordPersistenceUseCase recordPersistenceUseCase;

    public SnapshotRecord doSnapshot(Scenario scenario) {
        SnapshotRecord record = takeSnapshotUseCase.takeSnapshot(scenario);
        recordPersistenceUseCase.saveRecord(record);
        return record;
    }
}
