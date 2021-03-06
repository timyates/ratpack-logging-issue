## Example of locking up under contention

I'm probably doing something stupid...  But you only learn by being stupid ;-)

So, get this going with:

```bash
./gradlew run
```

Then, from the same folder in a new shell, the following should lock (eventually)

```bash
$ ab -n 2000 -c 25 -p dashboard_log.json -T application/json  http://localhost:5050/log
This is ApacheBench, Version 2.3 <$Revision: 1663405 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 200 requests
Completed 400 requests
Completed 600 requests
Completed 800 requests
Completed 1000 requests
Completed 1200 requests
Completed 1400 requests
Completed 1600 requests
Completed 1800 requests
apr_pollset_poll: The timeout specified has expired (70007)
Total of 1976 requests completed
```

At the top of the logs that Ratpack outputs are loads of:

```bash
21:17:36.836 [ratpack-compute-1-6] DEBUG i.n.channel.DefaultChannelPipeline - Discarded inbound message DefaultLastHttpContent(data: SlicedByteBuf(ridx: 0, widx: 139, cap: 139/139, unwrapped: PooledUnsafeDirectByteBuf(ridx: 278, widx: 278, cap: 1024)), decoderResult: success) that reached at the tail of the pipeline. Please check your pipeline configuration.
```

Which might (or might not be) indicative of the problem?

If you change `build.gradle` (line 7) like so:

```diff
- classpath "io.ratpack:ratpack-gradle:1.1.1"
+ classpath "io.ratpack:ratpack-gradle:1.0.0"
```

Then the `ab` test passes as expected...

Hope this makes sense!

PS:  Versions of stuff:

```bash
$ uname -v
Darwin Kernel Version 15.0.0: Sat Sep 19 15:53:46 PDT 2015; root:xnu-3247.10.11~1/RELEASE_X86_64

$ java -version
java version "1.8.0_65"
Java(TM) SE Runtime Environment (build 1.8.0_65-b17)
Java HotSpot(TM) 64-Bit Server VM (build 25.65-b01, mixed mode)

$ ./gradlew -version

------------------------------------------------------------
Gradle 2.9
------------------------------------------------------------

Build time:   2015-11-17 07:02:17 UTC
Build number: none
Revision:     b463d7980c40d44c4657dc80025275b84a29e31f

Groovy:       2.4.4
Ant:          Apache Ant(TM) version 1.9.3 compiled on December 23 2013
JVM:          1.8.0_65 (Oracle Corporation 25.65-b01)
OS:           Mac OS X 10.11.1 x86_64
```
