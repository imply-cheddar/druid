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

package org.apache.druid.query.operator;

import java.util.List;

/**
 * A naive sort operator is an operation that sorts a stream of data in-place.  Generally speaking this means
 * that it has to accumulate all of the data of its child operator first before it can sort.  This limitation
 * means that hopefully this operator is only planned in a very small number of circumstances.
 */
public class NaiveSortOperator implements Operator
{
  private final Operator child;
  private final List<String> sortColumns;

  public NaiveSortOperator(
      Operator child,
      List<String> sortColumns
  ) {
    this.child = child;
    this.sortColumns = sortColumns;
  }

  @Override
  public void go(Receiver receiver)
  {
    throw new UnsupportedOperationException();
  }
}
