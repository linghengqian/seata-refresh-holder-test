# seata-refresh-holder-test

- For https://github.com/apache/shardingsphere/pull/35427 and https://github.com/apache/incubator-seata/issues/7523 .

- Execute the following command on the `Windows 11 24H2` instance with `PowerShell/PowerShell`,
  `version-fox/vfox`, `git-for-windows/git` and `rancher-sandbox/rancher-desktop` installed.

```shell
vfox add java
vfox install java@21.0.7-ms
vfox use --global java@21.0.7-ms

git clone git@github.com:linghengqian/seata-refresh-holder-test.git
cd ./seata-refresh-holder-test/
./mvnw clean test -T 1C
```

- Can be tested individually.

```shell
cd ./seata-refresh-holder-test/
./mvnw -T 1C "-Dtest=ShardingSphereTest" clean test
```

- The log is as follows.

```shell
PS D:\TwinklingLiftWorks\git\public\seata-refresh-holder-test> ./mvnw -T 1C "-Dtest=ShardingSphereTest" clean test
[INFO] Scanning for projects...
[INFO] 
[INFO] Using the MultiThreadedBuilder implementation with a thread count of 16
[INFO] 
[INFO] ---------< com.github.linghengqian:seata-refresh-holder-test >----------
[INFO] Building seata-refresh-holder-test 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- clean:3.2.0:clean (default-clean) @ seata-refresh-holder-test ---
[INFO] Deleting D:\TwinklingLiftWorks\git\public\seata-refresh-holder-test\target
[INFO] 
[INFO] --- resources:3.3.1:resources (default-resources) @ seata-refresh-holder-test ---
[INFO] skip non existing resourceDirectory D:\TwinklingLiftWorks\git\public\seata-refresh-holder-test\src\main\resources
[INFO]
[INFO] --- compiler:3.13.0:compile (default-compile) @ seata-refresh-holder-test ---
[INFO] No sources to compile
[INFO]
[INFO] --- resources:3.3.1:testResources (default-testResources) @ seata-refresh-holder-test ---
[INFO] Copying 6 resources from src\test\resources to target\test-classes
[INFO]
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ seata-refresh-holder-test ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 4 source files with javac [debug release 21] to target\test-classes
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ seata-refresh-holder-test ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.github.linghengqian.ShardingSphereTest
[ERROR] 2025-07-11 16:05:07.688 [ForkJoinPool.commonPool-worker-1] o.a.s.config.ConfigurationFactory - failed to load non-spring configuration :not found service provider for : org.apache.seata.config.ConfigurationProvider
org.apache.seata.common.loader.EnhancedServiceNotFoundException: not found service provider for : org.apache.seata.config.ConfigurationProvider
[ERROR] 2025-07-11 16:05:09.252 [Connection-2-ThreadExecutor] o.a.s.config.ConfigurationFactory - failed to load non-spring configuration :not found service provider for : org.apache.seata.config.ConfigurationProvider
org.apache.seata.common.loader.EnhancedServiceNotFoundException: not found service provider for : org.apache.seata.config.ConfigurationProvider
[ERROR] 2025-07-11 16:05:09.516 [Connection-3-ThreadExecutor] o.a.s.config.ConfigurationFactory - failed to load non-spring configuration :not found service provider for : org.apache.seata.config.ConfigurationProvider
org.apache.seata.common.loader.EnhancedServiceNotFoundException: not found service provider for : org.apache.seata.config.ConfigurationProvider
[ERROR] 2025-07-11 16:05:16.683 [main] o.a.s.config.ConfigurationFactory - failed to load non-spring configuration :not found service provider for : org.apache.seata.config.ConfigurationProvider
org.apache.seata.common.loader.EnhancedServiceNotFoundException: not found service provider for : org.apache.seata.config.ConfigurationProvider
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 137.6 s <<< FAILURE! -- in com.github.linghengqian.ShardingSphereTest
[ERROR] com.github.linghengqian.ShardingSphereTest.test -- Time elapsed: 137.4 s <<< ERROR!
java.lang.NullPointerException: Cannot read field "tableMetaRefreshQueue" because "x0" is null
        at org.apache.seata.rm.datasource.sql.struct.TableMetaCacheFactory$TableMetaRefreshHolder.access$000(TableMetaCacheFactory.java:112)
        at org.apache.seata.rm.datasource.sql.struct.TableMetaCacheFactory.tableMetaRefreshEvent(TableMetaCacheFactory.java:98)
        at org.apache.seata.rm.datasource.sql.struct.TableMetaCacheFactory$TableMetaRefreshHolder.lambda$new$0(TableMetaCacheFactory.java:131)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        at java.base/java.lang.Thread.run(Thread.java:1570)

[INFO] 
[INFO] Results:
[INFO]
[ERROR] Errors: 
[ERROR]   ShardingSphereTest.test ? NullPointer Cannot read field "tableMetaRefreshQueue" because "x0" is null
[INFO]
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  02:22 min (Wall Clock)
[INFO] Finished at: 2025-07-11T16:07:18+08:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.2.5:test (default-test) on project seata-refresh-holder-test:
[ERROR]
[ERROR] Please refer to D:\TwinklingLiftWorks\git\public\seata-refresh-holder-test\target\surefire-reports for the individual test results.
[ERROR] Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.
[ERROR] -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
```