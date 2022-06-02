package com.tcbs.caffeine.timestensnapshot.hexagonal.application.usecase;

import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.Scenario;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.SnapshotRecord;

public interface TakeSnapshotUseCase {
    SnapshotRecord takeSnapshot(Scenario scenario);
}
