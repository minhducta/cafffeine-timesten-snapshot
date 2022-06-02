package com.tcbs.caffeine.timestensnapshot.hexagonal.framework.adapter.output;

import com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.output.RecordPersistenceOutputPort;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.SnapshotRecord;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.Row;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class FtpRecordPersistenceAdapter implements RecordPersistenceOutputPort {
    @Value("${application.record-persistence.ftp.host}")
    private String ftpHost;
    @Value("${application.record-persistence.ftp.user}")
    private String ftpUser;
    @Value("${application.record-persistence.ftp.pwd}")
    private String ftpPassword;
    @Value("${application.record-persistence.ftp.working-dir}")
    private String ftpWorkingDir;


    @Override
    public void saveRecord(SnapshotRecord snapshotRecord) {
        for (Table table : snapshotRecord.getResult().getTables()) {
            log.info("Prepare processing table: {} dataSize: {}", table.getMetadata().getName(), table.getRows().size());
            try {
                String firstLine = toCsv(table.getMetadata().getColumns());
                List<String> lines = Stream.concat(
                        Stream.of(firstLine),
                        table.getRows().stream()
                            .map(Row::getColumnData)
                            .map(this::toCsv))
                    .collect(Collectors.toUnmodifiableList());
                List<String> filePath = List.of(
                    snapshotRecord.getScenarioId(),
                    String.join("-", snapshotRecord.getScenes()),
                    snapshotRecord.getId().getValue(),
                    table.getMetadata().getName() + ".csv");
                InputStream is = toInputStream(lines);
                save(filePath, is);
                log.info("Write to {} has success! {} lines has written.", String.join("/", filePath), lines.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private InputStream toInputStream(List<String> lines) throws IOException {
        int bufferSize = 2048;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);

        PrintWriter writer = new PrintWriter(baos, false);
        for (String line : lines) {
            writer.println(line);
            writer.flush();
        }

        return new ByteArrayInputStream(baos.toByteArray());
    }

    private String toCsv(List<String> data) {
        return String.join(",", data);
    }

    private void save(List<String> filePath, InputStream inputStream) {
        try {
            FTPClient client = new FTPClient();
            client.connect(ftpHost);
            client.enterLocalPassiveMode();
            client.login(ftpUser, ftpPassword);
            client.changeWorkingDirectory(ftpWorkingDir);
            ftpMkdirsThenCd(client, filePath);
            boolean successfully = client.storeFile(filePath.get(filePath.size() - 1), inputStream);
            log.info("is successfully: {}", successfully);
            client.logout();
        } catch (IOException e) {
            log.error("Error!", e);
        }

    }

    private void ftpMkdirsThenCd(FTPClient client, List<String> filePath) throws IOException {
        List<String> dirPath = filePath.subList(0, filePath.size() - 1);
        for (String dirName : dirPath) {
            client.makeDirectory(dirName);
            client.changeWorkingDirectory(dirName);
            log.info("ftpMkdirs: {}", dirName);
            log.info("printWorkingDirectory: {}", client.printWorkingDirectory());
        }
    }
}
