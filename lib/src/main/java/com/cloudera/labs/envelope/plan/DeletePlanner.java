/*
 * Copyright (c) 2015-2018, Cloudera, Inc. All Rights Reserved.
 *
 * Cloudera, Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"). You may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 */

package com.cloudera.labs.envelope.plan;

import com.cloudera.labs.envelope.load.ProvidesAlias;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.typesafe.config.Config;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.Tuple2;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DeletePlanner implements BulkPlanner, ProvidesAlias {

  @Override
  public List<Tuple2<MutationType, Dataset<Row>>> planMutationsForSet(Dataset<Row> arriving) {
    return Lists.newArrayList(Collections.singleton(new Tuple2<>(MutationType.DELETE, arriving)));
  }

  @Override
  public Set<MutationType> getEmittedMutationTypes() {
    return Sets.newHashSet(MutationType.DELETE);
  }
  
  @Override
  public void configure(Config config) {}

  @Override
  public String getAlias() {
    return "delete";
  }
  
}
