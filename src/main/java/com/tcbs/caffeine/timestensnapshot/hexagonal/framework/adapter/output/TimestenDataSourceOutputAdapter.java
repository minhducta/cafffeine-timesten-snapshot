package com.tcbs.caffeine.timestensnapshot.hexagonal.framework.adapter.output;

import com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.output.DataSourceOutputPort;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.Row;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.Table;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.TableMetaData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class TimestenDataSourceOutputAdapter implements DataSourceOutputPort {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Table takeSnapshotOfTable(String tableName) {
        return selectAllFrom(tableName);
    }

    private List<Row> fetchAllRow(ResultSet resultSet) throws SQLException {
        int colCount = resultSet.getMetaData().getColumnCount();
        List<Row> rows = new ArrayList<>(colCount);

        while (resultSet.next()) {
            List<String> arr = new ArrayList<>(colCount);
            for (int i = 1; i <= colCount; i++) {
                arr.add(resultSet.getString(i));
            }
            Row row =  Row.of(Collections.unmodifiableList(arr));
            rows.add(row);
        }
        return Collections.unmodifiableList(rows);
    }

    private TableMetaData createTableMetaData(String tableName, ResultSetMetaData metaData) throws SQLException {
        List<String> colNames = parseColNames(metaData);
        return TableMetaData.of(tableName, colNames);
    }

    private List<String> parseColNames(ResultSetMetaData metaData) throws SQLException {
        int colCount = metaData.getColumnCount();
        List<String> arr = new ArrayList<>(colCount);
        for (int i = 1; i <= colCount; i++) {
            arr.add(metaData.getColumnName(i));
        }
        return Collections.unmodifiableList(arr);
    }

    private Table selectAllFrom(String tableName) {
        String sql = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.query(sql, (rs) -> {
            TableMetaData tableMetaData = createTableMetaData(tableName, rs.getMetaData());
            List<Row> rows = fetchAllRow(rs);
            return Table.of(tableMetaData, rows);
        });
    }
}
