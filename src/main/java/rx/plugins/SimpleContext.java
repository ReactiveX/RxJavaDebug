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

public class SimpleContext<T> {
    public final DebugNotification<T> notification;
    public final long start = System.nanoTime();
    public long end = -1;
    public Throwable err;

    public SimpleContext(DebugNotification<T> notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (end == -1) {
            str.append("(not complete): ");
        } else {
            str.append(end - start).append("ns: ");
        }
        str.append(notification);
        if (err != null) {
            str.append(" ").append(err.getMessage());
        }
        str.append("\n");
        return str.toString();
    }
}
