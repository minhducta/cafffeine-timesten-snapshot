package com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.output;

import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.Table;

public interface DataSourceOutputPort {
    Table takeSnapshotOfTable(String tableName);
}
