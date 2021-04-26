package org.krst.app.services.supports.DataProcessors;

@FunctionalInterface
public interface TriConsumer<Row, Object, CellStyle> {
    void accept(Row row, Object obj, CellStyle style);
}
