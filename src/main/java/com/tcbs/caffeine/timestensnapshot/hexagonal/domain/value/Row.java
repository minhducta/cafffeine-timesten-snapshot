package com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class Row {
    List<String> columnData;
}
