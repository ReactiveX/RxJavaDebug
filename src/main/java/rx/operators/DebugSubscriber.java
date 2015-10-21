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
package rx.operators;

import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.plugins.DebugNotification;
import rx.plugins.DebugNotificationListener;

public final class DebugSubscriber<T, C> extends Subscriber<T> {
    private DebugNotificationListener<C> listener;
    private final Subscriber<? super T> o;
    private Operator<? extends T, ?> from = null;
    private Operator<?, ? super T> to = null;

    public DebugSubscriber(DebugNotificationListener<C> listener, Subscriber<? super T> _o, Operator<? extends T, ?> _out, Operator<?, ? super T> _in) {
        super(_o);
        this.listener = listener;
        this.o = _o;
        this.from = _out;
        this.to = _in;
        this.add(new DebugSubscription<T, C>(this, listener));
    }

    @Override
    public void onStart() {
        final DebugNotification<T> n = DebugNotification.createStart(o, from, to);
        C context = listener.start(n);
        try {
            o.onStart();
            listener.complete(context);
        } catch (Throwable e) {
            listener.error(context, e);
            throw Exceptions.propagate(e);
        }
    }

    @Override
    public void onCompleted() {
        final DebugNotification<T> n = DebugNotification.createOnCompleted(o, from, to);
        C context = listener.start(n);
        try {
            o.onCompleted();
            listener.complete(context);
        } catch (Throwable e) {
            listener.error(context, e);
            throw Exceptions.propagate(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        final DebugNotification<T> n = DebugNotification.createOnError(o, from, e, to);
        C context = listener.start(n);
        try {
            o.onError(e);
            listener.complete(context);
        } catch (Throwable e2) {
            listener.error(context, e2);
            throw Exceptions.propagate(e);
        }
    }

    @Override
    public void onNext(T t) {
        final DebugNotification<T> n = DebugNotification.createOnNext(o, from, t, to);
        t = (T) listener.onNext(n);

        C context = listener.start(n);
        try {
            o.onNext(t);
            listener.complete(context);
        } catch (Throwable e) {
            listener.error(context, e);
            throw Exceptions.propagate(e);
        }
    }

    @Override
    public void setProducer(final Producer producer) {
        o.setProducer(new Producer() {
            @Override
            public void request(long n) {
                final DebugNotification<T> dn = DebugNotification.createRequest(o, from, to, n);

                C context = listener.start(dn);
                try {
                    producer.request(n);
                    listener.complete(context);
                } catch (Throwable e) {
                    listener.error(context, e);
                    throw Exceptions.propagate(e);
                }
            }
        });
    }

    public Operator<? extends T, ?> getFrom() {
        return from;
    }

    public void setFrom(Operator<? extends T, ?> bind) {
        this.from = bind;
    }

    public Operator<?, ? super T> getTo() {
        return to;
    }

    public void setTo(Operator<?, ? super T> op) {
        this.to = op;
    }

    public Subscriber<? super T> getActual() {
        return o;
    }
}
