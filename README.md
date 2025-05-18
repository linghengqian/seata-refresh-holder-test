# seata-refresh-holder-test

- For https://github.com/apache/shardingsphere/pull/35427 .

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

- The log is as follows.

```shell
PS D:\TwinklingLiftWorks\git\public\seata-refresh-holder-test> ./mvnw clean test -T 1C
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
[INFO] Copying 4 resources from src\test\resources to target\test-classes
[INFO]
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ seata-refresh-holder-test ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 1 source file with javac [debug release 21] to target\test-classes
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ seata-refresh-holder-test ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.github.linghengqian.SimpleTest
[ERROR] 2025-05-18 18:47:38.278 [main] o.a.s.config.ConfigurationFactory - failed to load non-spring configuration :not found service provider for : org.apache.seata.config.ConfigurationProvider
org.apache.seata.common.loader.EnhancedServiceNotFoundException: not found service provider for : org.apache.seata.config.ConfigurationProvider
[ERROR] 2025-05-18 18:47:40.294 [main] o.a.s.config.ConfigurationFactory - failed to load non-spring configuration :not found service provider for : org.apache.seata.config.ConfigurationProvider
org.apache.seata.common.loader.EnhancedServiceNotFoundException: not found service provider for : org.apache.seata.config.ConfigurationProvider
[ERROR] 2025-05-18 18:47:59.252 [main] o.a.s.config.ConfigurationFactory - failed to load non-spring configuration :not found service provider for : org.apache.seata.config.ConfigurationProvider
org.apache.seata.common.loader.EnhancedServiceNotFoundException: not found service provider for : org.apache.seata.config.ConfigurationProvider
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 44.59 s -- in com.github.linghengqian.SimpleTest
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  47.340 s (Wall Clock)
[INFO] Finished at: 2025-05-18T18:48:17+08:00
[INFO] ------------------------------------------------------------------------
```