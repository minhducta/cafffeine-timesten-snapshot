package com.tcbs.caffeine.timestensnapshot.hexagonal.application.usecase;

import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.SnapshotRecord;

public interface RecordPersistenceUseCase {
    void saveRecord(SnapshotRecord snapshotRecord);
}
