package com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value;

import lombok.Value;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Value
public class SnapshotRecordId {
    private static final String ID_FORMAT = "%04d-%02d-%02d-%02d-%02d-%02d-%09d";
    String value;

    public static SnapshotRecordId from(String id) {
        return new SnapshotRecordId(id);
    }

    public static SnapshotRecordId generate() {
        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        return new SnapshotRecordId(String.format(ID_FORMAT,
            offsetDateTime.getYear(),
            offsetDateTime.getMonthValue(),
            offsetDateTime.getDayOfMonth(),
            offsetDateTime.getHour(),
            offsetDateTime.getMinute(),
            offsetDateTime.getSecond(),
            offsetDateTime.getNano()));
    }
}
