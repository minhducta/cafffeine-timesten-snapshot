package com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.output;

import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.SnapshotRecord;

public interface RecordPersistenceOutputPort {
    void saveRecord(SnapshotRecord snapshotRecord);
}
