package com.tcbs.caffeine.timestensnapshot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TakeSnapShotResponseDto {
    boolean success;
    String recordId;
}
