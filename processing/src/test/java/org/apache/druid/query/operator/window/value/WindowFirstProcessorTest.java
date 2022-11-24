package org.apache.druid.query.operator.window.value;

import org.apache.druid.query.operator.window.ComposingProcessor;
import org.apache.druid.query.operator.window.RowsAndColumnsHelper;
import org.apache.druid.query.rowsandcols.RowsAndColumns;
import org.apache.druid.query.rowsandcols.column.Column;
import org.apache.druid.query.rowsandcols.column.DoubleArrayColumn;
import org.apache.druid.query.rowsandcols.column.IntArrayColumn;
import org.apache.druid.query.rowsandcols.column.ObjectArrayColumn;
import org.apache.druid.query.rowsandcols.frame.MapOfColumnsRowsAndColumns;
import org.apache.druid.segment.column.ColumnType;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class WindowFirstProcessorTest
{
  @Test
  public void testFirstProcessing()
  {
    Map<String, Column> map = new LinkedHashMap<>();
    map.put("intCol", new IntArrayColumn(new int[]{88, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
    map.put("doubleCol", new DoubleArrayColumn(new double[]{0.4728, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
    map.put("objectCol", new ObjectArrayColumn(
        new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"},
        ColumnType.STRING
    ));
    map.put("nullFirstCol", new ObjectArrayColumn(
        new String[]{null, "b", "c", "d", "e", "f", "g", "h", "i", "j"},
        ColumnType.STRING
    ));

    MapOfColumnsRowsAndColumns rac = MapOfColumnsRowsAndColumns.fromMap(map);

    ComposingProcessor processor = new ComposingProcessor(
        new WindowFirstProcessor("intCol", "FirstIntCol"),
        new WindowFirstProcessor("doubleCol", "FirstDoubleCol"),
        new WindowFirstProcessor("objectCol", "FirstObjectCol"),
        new WindowFirstProcessor("nullFirstCol", "NullFirstCol")
    );

    final RowsAndColumns results = processor.process(rac);
    RowsAndColumnsHelper.assertEquals(results, "intCol", new int[]{88, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    RowsAndColumnsHelper.assertEquals(results, "doubleCol", new double[]{0.4728, 1, 2, 3, 4, 5, 6, 7, 8, 9});

    final RowsAndColumnsHelper helper = new RowsAndColumnsHelper(results);
    helper.forColumn("FirstIntCol", ColumnType.LONG)
          .setExpectation(new int[]{88, 88, 88, 88, 88, 88, 88, 88, 88, 88})
          .validate();

    helper.forColumn("FirstDoubleCol", ColumnType.DOUBLE)
          .setExpectation(new double[]{0.4728, 0.4728, 0.4728, 0.4728, 0.4728, 0.4728, 0.4728, 0.4728, 0.4728, 0.4728})
          .validate();

    helper.forColumn("FirstObjectCol", ColumnType.STRING)
          .setExpectation(new String[]{"a", "a", "a", "a", "a", "a", "a", "a", "a", "a"})
          .validate();

    helper.forColumn("NullFirstCol", ColumnType.STRING)
          .setNulls(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
          .validate();
  }
}