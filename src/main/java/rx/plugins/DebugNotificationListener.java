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

import rx.Observable;
import rx.Observable.Operator;
import rx.Observer;

/**
 * Subclasses of this are passed into the constructor of {@link DebugHook} to receive notification
 * about all activity in Rx.
 * 
 * @author gscampbell
 * 
 * @param <C>
 *            Context type that is returned from start and passed to either complete or error.
 * @see DebugHook
 */
public abstract class DebugNotificationListener<C> {
    /**
     * Override this to change the default behavior of returning the encapsulated value. This will
     * only be invoked when the {@link DebugNotification#getKind()} is
     * {@link DebugNotification.Kind#OnNext} and the value (null or not) is just about to be sent to
     * next {@link Observer}. This can end up being called multiple times for
     * the same value as it passes from {@link Operator} to {@link Operator} in the
     * {@link Observable} chain.
     * <p>
     * This can be used to decorate or replace the values passed into any onNext function or just
     * perform extra logging, metrics and other such things and pass-thru the function.
     * 
     * @param n
     *            {@link DebugNotification} containing the data and context about what is happening.
     * @return the notification's value
     */
    public <T> T onNext(DebugNotification<T> n) {
        return n.getValue();
    }

    /**
     * For each {@link DebugNotification.Kind} start is invoked before the actual method is invoked.
     * <p>
     * This can be used to perform extra logging, metrics and other such things.
     * 
     * @param n
     *            {@link DebugNotification} containing the data and context about what is happening.
     * @return
     *         A contextual object that the listener can use in the {@link #complete(Object) } or
     *         {@link #error(Object, Throwable)} after the actual operation has ended.
     */
    public <T> C start(DebugNotification<T> n) {
        return null;
    }

    /**
     * After the actual operations has completed from {@link #start(DebugNotification)} this is
     * invoked
     * 
     * @param context
     */
    public void complete(C context) {
    }

    /**
     * After the actual operations has thrown an exception from {@link #start(DebugNotification)}
     * this is invoked
     * 
     * @param context
     * @param e
     */
    public void error(C context, Throwable e) {
    }
}
