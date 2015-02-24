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

import rx.Subscriber;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A simple implementation of the {@link DebugNotificationListener} that store all of the events in memory for analysis later.
 * It has unbounded growth so should not be used in a long running system.
 */
public class SimpleDebugNotificationListener extends DebugNotificationListener<SimpleContext<?>> {
    private final Map<Subscriber<?>, Queue<SimpleContext<?>>> notificationsByObservable = new ConcurrentHashMap<Subscriber<?>, Queue<SimpleContext<?>>>();

    @Override
    public <T> SimpleContext<?> start(DebugNotification<T> n) {
        SimpleContext<T> context = new SimpleContext<T>(n);
        Queue<SimpleContext<?>> notifications = notificationsByObservable.get(n.getObserver());
        if (notifications == null) {
            notifications = new LinkedBlockingQueue<SimpleContext<?>>();
            notificationsByObservable.put((Subscriber<?>) n.getObserver(), notifications);
        }
        notifications.add(context);
        return context;
    }

    @Override
    public void complete(SimpleContext<?> context) {
        context.setEnd();
    }

    @Override
    public void error(SimpleContext<?> context, Throwable e) {
        context.setError(e);
    }

    @Override
    public String toString() {
        SortedSet<NotificationsByObservable<?>> notificationsByObservableSnapshot = getNotificationsByObservable();
        return toString(notificationsByObservableSnapshot);
    }

    public String toString(SortedSet<NotificationsByObservable<?>> notificationsByObservableSnapshot) {
        StringBuilder str = new StringBuilder();
        str.append("{\n  ");
        boolean first = true;
        for (NotificationsByObservable<?> notificationsForObservableSnapshot : notificationsByObservableSnapshot) {
            if (!first)
                str.append(",\n  ");
            notificationsForObservableSnapshot.append(str);
            first = false;
        }
        str.append("\n}");
        return str.toString();
    }

    public static class NotificationsByObservable<T> implements Comparable<NotificationsByObservable<T>> {
        public final Subscriber<T> subscriber;
        public final SortedSet<SimpleContext<T>> notifications;

        public NotificationsByObservable(Map.Entry<Subscriber<T>, Queue<SimpleContext<T>>> context) {
            subscriber = context.getKey();
            notifications = new TreeSet<SimpleContext<T>>();
            notifications.addAll(context.getValue());
        }

        @Override
        public int compareTo(NotificationsByObservable<T> o) {
            if (notifications.isEmpty()) {
                return o.notifications.isEmpty() ? 0 : -1;
            }
            if (o.notifications.isEmpty())
                return 1;
            return notifications.first().compareTo(o.notifications.first());
        }

        public void append(StringBuilder str) {
            str.append("\"").append(subscriber.toString()).append("\": [\n    ");
            boolean first = true;
            for (SimpleContext<T> simpleContext : notifications) {
                if (!first)
                    str.append(",\n    ");
                str.append(simpleContext.toString());
                first = false;
            }
            str.append("\n  ]");
        }
        
        public SortedSet<SimpleContext<T>> getNotifications() {
            return notifications;
        }
    }

    /**
     * a copy sorted by time  of the all the state useful for analysis.
     * @return
     */
    public SortedSet<NotificationsByObservable<?>> getNotificationsByObservable() {
        SortedSet<NotificationsByObservable<?>> notificationsByObservableSnapshot = new TreeSet<NotificationsByObservable<?>>();
        for (Entry<Subscriber<?>, Queue<SimpleContext<?>>> notificationsForObservable : notificationsByObservable.entrySet()) {
            notificationsByObservableSnapshot.add(new NotificationsByObservable(notificationsForObservable));
        }
        return notificationsByObservableSnapshot;
    }
}
