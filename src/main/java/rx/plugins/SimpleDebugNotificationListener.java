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

import rx.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple implementation of the {@link DebugNotificationListener} that store all of the events in memory for analysis later.
 * It has unbounded growth so should not be used in a long running system.
 */
public class SimpleDebugNotificationListener extends DebugNotificationListener<SimpleContext<?>> {
    private final Map<Observer<?>, List<SimpleContext<?>>> notificationsByObservable = new ConcurrentHashMap<Observer<?>, List<SimpleContext<?>>>();

    @Override
    public <T> SimpleContext<?> start(DebugNotification<T> n) {
        SimpleContext<T> context = new SimpleContext<T>(n);
        List<SimpleContext<?>> notifications = notificationsByObservable.get(n.getObserver());
        if (notifications == null) {
            notifications = new ArrayList<SimpleContext<?>>();
            notificationsByObservable.put(n.getObserver(), notifications);
        }
        notifications.add(context);
        return context;
    }

    @Override
    public void complete(SimpleContext<?> context) {
        context.end = System.nanoTime();
    }

    @Override
    public void error(SimpleContext<?> context, Throwable e) {
        context.end = System.nanoTime();
        context.err = e;
    }

    public Map<Observer<?>, List<SimpleContext<?>>> getNotificationsByObservable() {
        return notificationsByObservable;
    }
}
