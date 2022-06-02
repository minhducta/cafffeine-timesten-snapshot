package com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity;

import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.SnapshotRecordId;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SnapshotRecord {
    @Setter(AccessLevel.PRIVATE)
    SnapshotRecordId id;
    String scenarioId;
    List<String> scenes;
    Instant dateTime;
    Result result;

    @Value(staticConstructor = "of")
    public static class Result {
        List<Table> tables;
    }
}
