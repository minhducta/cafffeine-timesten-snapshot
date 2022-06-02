package com.tcbs.caffeine.timestensnapshot.hexagonal.framework.adapter.output;

import com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.output.RecordPersistenceOutputPort;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.SnapshotRecord;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.Row;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class FileRecordPersistenceAdapter implements RecordPersistenceOutputPort {
    @Value("${application.record-persistence.file}")
    private String outputFolder;
    @Override
    public void saveRecord(SnapshotRecord snapshotRecord) {
        try {
            Path systemPath = Path.of(outputFolder,
                String.join("-", snapshotRecord.getScenes()),
                snapshotRecord.getScenarioId(),
                snapshotRecord.getId().getValue());
            Files.createDirectories(systemPath);
            for (Table table : snapshotRecord.getResult().getTables()) {
                try {
                    String firstLine = toCsv(table.getMetadata().getColumns());
                    List<String> lines = Stream.concat(
                            Stream.of(firstLine),
                            table.getRows().stream()
                                .map(Row::getColumnData)
                                .map(this::toCsv))
                        .collect(Collectors.toUnmodifiableList());
                    Path filePath = Path.of(systemPath.toString(), table.getMetadata().getName() + ".csv");
                    Files.write(filePath, lines,
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                    log.info("Write to {} has success! {} lines has written.", filePath, lines.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            log.error("Some thing error when saveRecord: {}", snapshotRecord, e);
        }
    }

    private String toCsv(List<String> data) {
        return String.join(",", data);
    }
}
