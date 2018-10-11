/**
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudera.labs.envelope.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZooKeeperConnection implements Watcher {

  private ZooKeeper zk;
  private CountDownLatch latch;

  private String connection;
  private int sessionTimeoutMs;

  public static final String CONNECTION_CONFIG = "connection";

  private static final int DEFAULT_SESSION_TIMEOUT_MS = 1000;

  public ZooKeeperConnection(String connection) {
    this(connection, DEFAULT_SESSION_TIMEOUT_MS);
  }

  public ZooKeeperConnection(String connection, int sessionTimeoutMs) {
    this.connection = connection;
    this.sessionTimeoutMs = sessionTimeoutMs;
  }

  @Override
  public void process(WatchedEvent event) {
    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
      latch.countDown();
    }
  }

  public synchronized ZooKeeper getZooKeeper() throws IOException, InterruptedException {
    if (zk == null || zk.getState() != ZooKeeper.States.CONNECTED) {
      latch = new CountDownLatch(1);
      zk = new ZooKeeper(connection, sessionTimeoutMs, this);

      boolean done = latch.await(2, TimeUnit.SECONDS);
      if (!done) {
        throw new InterruptedException("Did not connect to ZooKeeper within 2 seconds");
      }
    }

    return zk;
  }

}