# RxJava Debug Plugin

A execution plugin implementation for debugging [RxJava](https://github.com/ReactiveX/RxJava). RxJava gives you low level plugin hooks for things like create, subscribe and lift. The DebugHook defined in this project uses those to give you hooks for the start/end/error of `Subscribe`, `OnNext`, `OnError`, `OnComplete` and `Unsubscribe`. For many of the `rx.plugins.DebugNotification` events you also get the operator where the notification is coming from and going to. It's like having a detailed materialized copy of every `rx.Observable` in the system.

## Master Build Status

<a href='https://travis-ci.org/ReactiveX/RxJavaDebug/builds'><img src='https://travis-ci.org/ReactiveX/RxJavaDebug.svg?branch=0.x'></a>

## Communication

- Google Group: [RxJava](http://groups.google.com/d/forum/rxjava)
- Twitter: [@RxJava](http://twitter.com/RxJava)
- [GitHub Issues](https://github.com/ReactiveX/RxJavaDebug/issues)


## Binaries

Binaries and dependency information for Maven, Ivy, Gradle and others can be found at [http://search.maven.org](http://search.maven.org/#search%7Cga%7C1%7Cio.reactivex.rxjava-debug).

Example for Maven:

```xml
<dependency>
    <groupId>io.reactivex</groupId>
    <artifactId>rxjava-debug</artifactId>
    <version>x.y.z</version>
</dependency>
```
and for Ivy:

```xml
<dependency org="io.reactivex" name="rxjava-debug" rev="x.y.z" />
```

## Build

To build:

```
$ git clone git@github.com:ReactiveX/RxJavaDebug.git
$ cd RxJavaDebug/
$ ./gradlew build
```

## Bugs and Feedback

For bugs, questions and discussions please use the [Github Issues](https://github.com/ReactiveX/RxJavaDebug/issues).

 
## LICENSE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
