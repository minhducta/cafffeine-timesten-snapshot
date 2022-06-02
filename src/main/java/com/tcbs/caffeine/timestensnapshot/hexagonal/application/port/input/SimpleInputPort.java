package com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.input;

import com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.output.DataSourceOutputPort;
import com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.output.RecordPersistenceOutputPort;
import com.tcbs.caffeine.timestensnapshot.hexagonal.application.usecase.RecordPersistenceUseCase;
import com.tcbs.caffeine.timestensnapshot.hexagonal.application.usecase.TakeSnapshotUseCase;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.Scenario;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.SnapshotRecord;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.SnapshotRecordId;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.Table;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SimpleInputPort implements TakeSnapshotUseCase, RecordPersistenceUseCase {
    private final Clock clock;
    private final RecordPersistenceOutputPort outputPort;
    private final DataSourceOutputPort dataSource;

    @Override
    public SnapshotRecord takeSnapshot(Scenario scenario) {
        Instant instant = Instant.now(clock);
        List<Table> tables = scenario.getTables().stream()
            .map(dataSource::takeSnapshotOfTable)
            .collect(Collectors.toUnmodifiableList());
        return SnapshotRecord.builder()
            .id(SnapshotRecordId.generate())
            .scenarioId(scenario.getId().getValue())
            .scenes(scenario.getScenes())
            .dateTime(instant)
            .result(SnapshotRecord.Result.of(tables))
            .build();
    }

    @Override
    public void saveRecord(SnapshotRecord snapshotRecord) {
        outputPort.saveRecord(snapshotRecord);
    }

}
