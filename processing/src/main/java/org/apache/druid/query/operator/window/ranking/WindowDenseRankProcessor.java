/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.druid.query.operator.window.ranking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.druid.query.rowsandcols.RowsAndColumns;
import org.apache.druid.query.rowsandcols.StartAndEnd;
import org.apache.druid.query.rowsandcols.column.IntArrayColumn;

import java.util.Arrays;
import java.util.List;

/**
 * This Processor assumes that data has already been sorted for it.  It does not re-sort the data and if it is given
 * data that is not in the correct sorted order, its operation is undefined.
 */
public class WindowDenseRankProcessor extends WindowRankingProcessorBase
{
  @JsonCreator
  public WindowDenseRankProcessor(
      @JsonProperty("group") List<String> groupingCols,
      @JsonProperty("outputColumn") String outputColumn
  )
  {
    super(groupingCols, outputColumn);
  }

  @Override
  public RowsAndColumns process(RowsAndColumns incomingPartition)
  {
    return processInternal(incomingPartition, groupings -> {
      final int[] ranks = new int[incomingPartition.numRows()];
      for (int i = 0; i < groupings.size(); ++i) {
        StartAndEnd startAndEnd = groupings.get(i);
        Arrays.fill(ranks, startAndEnd.getStart(), startAndEnd.getEnd(), i + 1);
      }

      return new IntArrayColumn(ranks);
    });
  }
}
