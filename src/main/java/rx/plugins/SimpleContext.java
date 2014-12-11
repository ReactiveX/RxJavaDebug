/**
 * Copyright 2014 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package rx.plugins;

import java.util.concurrent.atomic.AtomicLong;

public class SimpleContext<T> implements Comparable<SimpleContext<T>> {
    private final DebugNotification<T> notification;
    private final long threadId = Thread.currentThread().getId();
    private final long start = System.nanoTime();
    private AtomicLong end = new AtomicLong(-1);
    private volatile Throwable err;

    public SimpleContext(DebugNotification<T> notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        toString(str);
        return str.toString();
    }

    private void toString(StringBuilder str) {
        str.append("{");
        long e = end.get();
        if (e != -1) {
            str.append("\"ns_duration\": ").append(String.format("%10d", e - start)).append(", ");
        }
        str.append("\"threadId\": ").append(String.format("%3d", threadId)).append(", ");
        str.append("\"notification\": ").append(notification).append("}");
    }

    @Override
    public int compareTo(SimpleContext<T> o) {
        return Long.compare(start, o.start);
    }

    public long getEnd() {
        return end.get();
    }

    public void setEnd() {
        if (!this.end.compareAndSet(-1, System.nanoTime())) {
            throw new IllegalStateException("The context was already completed");
        }
    }

    public Throwable getError() {
        return err;
    }

    public void setError(Throwable err) {
        if (this.end.compareAndSet(-1, System.nanoTime())) {
            this.err = err;
        } else {
            throw new IllegalStateException("The context was already completed");
        }
    }

    public DebugNotification<T> getNotification() {
        return notification;
    }

    public long getThreadId() {
        return threadId;
    }

    public long getStart() {
        return start;
    }
}
