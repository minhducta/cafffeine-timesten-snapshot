package com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value(staticConstructor = "of")
public class Table {
    TableMetaData metadata;
    @Getter(AccessLevel.NONE)
    List<Row> rows;

    public List<Row> getRows() {
        return Collections.unmodifiableList(rows);
    }
}
