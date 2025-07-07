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
15:40:36.804 [main] INFO  org.testcontainers.images.PullPolicy - Image pull policy will be performed by: DefaultPullPolicy()
15:40:36.808 [main] INFO  org.testcontainers.utility.ImageNameSubstitutor - Image name substitution will be performed by: DefaultImageNameSubstitutor (composite of 'ConfigurationFileImageNameSubstitutor' and 'PrefixingImageNameSubstitutor')
15:40:36.829 [main] INFO  org.testcontainers.DockerClientFactory - Testcontainers version: 1.21.3
15:40:36.996 [main] INFO  org.testcontainers.dockerclient.DockerClientProviderStrategy - Loaded org.testcontainers.dockerclient.NpipeSocketClientProviderStrategy from ~/.testcontainers.properties, will try it first
15:40:37.217 [main] INFO  org.testcontainers.dockerclient.DockerClientProviderStrategy - Found Docker environment with local Npipe socket (npipe:////./pipe/docker_engine)
15:40:37.218 [main] INFO  org.testcontainers.DockerClientFactory - Docker host IP address is localhost
15:40:37.234 [main] INFO  org.testcontainers.DockerClientFactory - Connected to docker: 
  Server Version: 27.3.1
  API Version: 1.47
  Operating System: Rancher Desktop WSL Distribution
  Total Memory: 15588 MB
15:40:37.277 [main] INFO  tc.testcontainers/ryuk:0.12.0 - Creating container for image: testcontainers/ryuk:0.12.0
15:40:37.319 [main] INFO  org.testcontainers.utility.RegistryAuthLocator - Credential helper/store (docker-credential-wincred) does not have credentials for https://index.docker.io/v1/
15:40:37.400 [main] INFO  tc.testcontainers/ryuk:0.12.0 - Container testcontainers/ryuk:0.12.0 is starting: e69af64d09d6d82057b9b240a074a57264c5efb8c209e42435f1635bce720b61
15:40:37.876 [main] INFO  tc.testcontainers/ryuk:0.12.0 - Container testcontainers/ryuk:0.12.0 started in PT0.5994741S
15:40:37.880 [main] INFO  org.testcontainers.utility.RyukResourceReaper - Ryuk started - will monitor and terminate Testcontainers containers on JVM exit
15:40:37.880 [main] INFO  org.testcontainers.DockerClientFactory - Checking the system...
15:40:37.881 [main] INFO  org.testcontainers.DockerClientFactory - ✔︎ Docker server version should be at least 1.6.0
15:40:37.881 [main] INFO  tc.apache/seata-server:2.4.0 - Creating container for image: apache/seata-server:2.4.0
15:40:37.920 [main] INFO  tc.apache/seata-server:2.4.0 - Container apache/seata-server:2.4.0 is starting: f370474ea51c862d8bc990c143c3e39db929dffe5251a4be14effaadce2a9018
15:40:38.206 [main] INFO  org.testcontainers.containers.wait.strategy.HttpWaitStrategy - /unruffled_williamson: Waiting for 60 seconds for URL: http://localhost:32880/health (where port 32880 maps to container port 7091)
15:40:42.342 [main] INFO  tc.apache/seata-server:2.4.0 - Container apache/seata-server:2.4.0 started in PT4.4612956S
15:40:42.342 [main] INFO  tc.postgres:17.5-bookworm - Creating container for image: postgres:17.5-bookworm
15:40:42.404 [main] INFO  tc.postgres:17.5-bookworm - Container postgres:17.5-bookworm is starting: c57b59bae6092ced75e4607952f5d3bb1f73a8813ebd25039f188998c71b97c2
15:40:44.113 [main] INFO  tc.postgres:17.5-bookworm - Container postgres:17.5-bookworm started in PT1.7709435S
15:40:44.114 [main] INFO  tc.postgres:17.5-bookworm - Container is started (JDBC URL: jdbc:postgresql://localhost:32882/test?loggerLevel=OFF)
15:40:44.152 [main] INFO  org.apache.seata.common.loader.EnhancedServiceLoader$InnerEnhancedServiceLoader - Ignore load compatible class io.seata.config.ExtConfigurationProvider, because is not assignable from origin type org.apache.seata.config.ExtConfigurationProvider
15:40:44.157 [main] INFO  org.apache.seata.common.loader.EnhancedServiceLoader$InnerEnhancedServiceLoader - Load compatible class io.seata.config.ConfigurationProvider
15:40:44.161 [main] ERROR org.apache.seata.config.ConfigurationFactory - failed to load non-spring configuration :not found service provider for : org.apache.seata.config.ConfigurationProvider
org.apache.seata.common.loader.EnhancedServiceNotFoundException: not found service provider for : org.apache.seata.config.ConfigurationProvider
15:40:44.275 [main] INFO  org.apache.seata.common.loader.EnhancedServiceLoader$InnerEnhancedServiceLoader - Load compatible class io.seata.core.auth.AuthSigner
15:40:44.283 [main] INFO  org.apache.seata.core.rpc.netty.NettyClientBootstrap - NettyClientBootstrap has started
15:40:44.285 [main] INFO  org.apache.seata.discovery.registry.RegistryFactory - use registry center type: file
15:40:44.288 [main] INFO  org.apache.seata.common.loader.EnhancedServiceLoader$InnerEnhancedServiceLoader - Load compatible class io.seata.discovery.registry.RegistryProvider
15:40:44.291 [main] INFO  org.apache.seata.core.rpc.netty.NettyClientChannelManager - will connect to 127.0.0.1:32881
15:40:44.295 [main] INFO  org.apache.seata.core.rpc.netty.NettyPoolableFactory - NettyPool create channel to transactionRole:TMROLE,address:127.0.0.1:32881,msg:< RegisterTMRequest{version='2.4.0', applicationId='test-first', transactionServiceGroup='default_tx_group', extraData='ak=null
digest=default_tx_group,192.168.20.1,1751874044294
timestamp=1751874044294
authVersion=V4
vgroup=default_tx_group
ip=192.168.20.1
'} >
15:40:44.370 [NettyClientSelector_TMROLE_1_1_32] INFO  org.apache.seata.common.loader.EnhancedServiceLoader$InnerEnhancedServiceLoader - Load compatible class io.seata.core.serializer.Serializer
15:40:44.445 [main] INFO  org.apache.seata.core.rpc.netty.TmNettyRemotingClient - register TM success. client version:2.4.0, server version:2.4.0,channel:[id: 0xa6a3f4da, L:/127.0.0.1:58894 - R:/127.0.0.1:32881]
15:40:44.449 [main] INFO  org.apache.seata.core.rpc.netty.NettyPoolableFactory - register success, cost 97 ms, version:2.4.0,role:TMROLE,channel:[id: 0xa6a3f4da, L:/127.0.0.1:58894 - R:/127.0.0.1:32881]
15:40:44.478 [main] INFO  org.apache.seata.common.loader.EnhancedServiceLoader$InnerEnhancedServiceLoader - Load compatible class io.seata.core.model.ResourceManager
15:40:44.479 [main] INFO  org.apache.seata.rm.datasource.AsyncWorker - Async Commit Buffer Limit: 10000
15:40:44.480 [main] INFO  org.apache.seata.rm.datasource.xa.ResourceManagerXA - ResourceManagerXA init ...
15:40:44.483 [main] INFO  org.apache.seata.core.rpc.netty.NettyClientBootstrap - NettyClientBootstrap has started
15:40:44.490 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
15:40:44.629 [main] INFO  com.zaxxer.hikari.pool.HikariPool - HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@6a818392
15:40:44.631 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
15:40:44.709 [main] INFO  org.apache.seata.core.rpc.netty.NettyClientChannelManager - will connect to 127.0.0.1:32881
15:40:44.709 [main] INFO  org.apache.seata.core.rpc.netty.RmNettyRemotingClient - RM will register :jdbc:postgresql://127.0.0.1:32882/demo_ds_0
15:40:44.709 [main] INFO  org.apache.seata.core.rpc.netty.NettyPoolableFactory - NettyPool create channel to transactionRole:RMROLE,address:127.0.0.1:32881,msg:< RegisterRMRequest{resourceIds='jdbc:postgresql://127.0.0.1:32882/demo_ds_0', version='2.4.0', applicationId='test-first', transactionServiceGroup='default_tx_group', extraData='null'} >
15:40:44.719 [main] INFO  org.apache.seata.core.rpc.netty.RmNettyRemotingClient - register RM success. client version:2.4.0, server version:2.4.0,channel:[id: 0x15912f30, L:/127.0.0.1:58896 - R:/127.0.0.1:32881]
15:40:44.719 [main] INFO  org.apache.seata.core.rpc.netty.NettyPoolableFactory - register success, cost 8 ms, version:2.4.0,role:RMROLE,channel:[id: 0x15912f30, L:/127.0.0.1:58896 - R:/127.0.0.1:32881]
15:40:44.723 [main] INFO  org.apache.seata.common.loader.EnhancedServiceLoader$InnerEnhancedServiceLoader - Load compatible class io.seata.core.context.ContextCore
15:40:44.957 [NettyClientSelector_TMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.AbstractNettyRemotingClient - channel inactive: [id: 0xa6a3f4da, L:/127.0.0.1:58894 ! R:/127.0.0.1:32881]
15:40:44.957 [NettyClientSelector_TMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.NettyClientChannelManager - return to pool, rm channel:[id: 0xa6a3f4da, L:/127.0.0.1:58894 ! R:/127.0.0.1:32881]
15:40:44.957 [NettyClientSelector_TMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.NettyPoolableFactory - channel valid false,channel:[id: 0xa6a3f4da, L:/127.0.0.1:58894 ! R:/127.0.0.1:32881]
15:40:44.957 [NettyClientSelector_TMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.NettyPoolableFactory - will destroy channel:[id: 0xa6a3f4da, L:/127.0.0.1:58894 ! R:/127.0.0.1:32881]
15:40:44.957 [NettyClientSelector_TMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.AbstractNettyRemotingClient - ChannelHandlerContext(AbstractNettyRemotingClient$ClientHandler#0, [id: 0xa6a3f4da, L:/127.0.0.1:58894 ! R:/127.0.0.1:32881]) will closed
15:40:44.957 [NettyClientSelector_TMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.AbstractNettyRemotingClient - ChannelHandlerContext(AbstractNettyRemotingClient$ClientHandler#0, [id: 0xa6a3f4da, L:/127.0.0.1:58894 ! R:/127.0.0.1:32881]) will closed
15:40:44.960 [NettyClientSelector_RMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.AbstractNettyRemotingClient - channel inactive: [id: 0x15912f30, L:/127.0.0.1:58896 ! R:/127.0.0.1:32881]
15:40:44.960 [NettyClientSelector_RMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.NettyClientChannelManager - return to pool, rm channel:[id: 0x15912f30, L:/127.0.0.1:58896 ! R:/127.0.0.1:32881]
15:40:44.960 [NettyClientSelector_RMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.NettyPoolableFactory - channel valid false,channel:[id: 0x15912f30, L:/127.0.0.1:58896 ! R:/127.0.0.1:32881]
15:40:44.960 [NettyClientSelector_RMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.NettyPoolableFactory - will destroy channel:[id: 0x15912f30, L:/127.0.0.1:58896 ! R:/127.0.0.1:32881]
15:40:44.960 [NettyClientSelector_RMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.AbstractNettyRemotingClient - ChannelHandlerContext(AbstractNettyRemotingClient$ClientHandler#0, [id: 0x15912f30, L:/127.0.0.1:58896 ! R:/127.0.0.1:32881]) will closed
15:40:44.961 [NettyClientSelector_RMROLE_1_1_32] INFO  org.apache.seata.core.rpc.netty.AbstractNettyRemotingClient - ChannelHandlerContext(AbstractNettyRemotingClient$ClientHandler#0, [id: 0x15912f30, L:/127.0.0.1:58896 ! R:/127.0.0.1:32881]) will closed
15:40:44.967 [main] ERROR org.apache.seata.config.ConfigurationFactory - failed to load non-spring configuration :not found service provider for : org.apache.seata.config.ConfigurationProvider
org.apache.seata.common.loader.EnhancedServiceNotFoundException: not found service provider for : org.apache.seata.config.ConfigurationProvider
15:40:46.269 [main] INFO  tc.postgres:17.5-bookworm - Creating container for image: postgres:17.5-bookworm
15:40:46.306 [main] INFO  tc.postgres:17.5-bookworm - Container postgres:17.5-bookworm is starting: c895d75c2179bac97d38005560b1e018b7fec83ec5d5acb9c00a1427c091ee73
15:40:47.692 [main] INFO  tc.postgres:17.5-bookworm - Container postgres:17.5-bookworm started in PT1.423793S
15:40:47.692 [main] INFO  tc.postgres:17.5-bookworm - Container is started (JDBC URL: jdbc:postgresql://localhost:32883/test?loggerLevel=OFF)
15:40:47.693 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-2 - Starting...
15:40:47.705 [main] INFO  com.zaxxer.hikari.pool.HikariPool - HikariPool-2 - Added connection org.postgresql.jdbc.PgConnection@24eb65e3
15:40:47.705 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-2 - Start completed.
15:40:48.433 [main] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling docker image: firebirdsql/firebird:5.0.2. Please be patient; this may take some time but only needs to be done once.
15:40:53.587 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Starting to pull image
15:40:53.693 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  0 pending,  0 downloaded,  0 extracted, (0 bytes/0 bytes)
15:40:55.987 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  5 pending,  1 downloaded,  0 extracted, (32 bytes/? MB)
15:40:57.987 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  4 pending,  2 downloaded,  0 extracted, (25 MB/? MB)
15:40:59.487 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  3 pending,  3 downloaded,  0 extracted, (44 MB/? MB)
15:40:59.887 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  3 pending,  3 downloaded,  1 extracted, (51 MB/? MB)
15:41:00.387 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  2 pending,  4 downloaded,  1 extracted, (59 MB/? MB)
15:41:01.287 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  2 pending,  4 downloaded,  2 extracted, (59 MB/? MB)
15:41:01.287 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  2 pending,  4 downloaded,  3 extracted, (59 MB/? MB)
15:41:01.386 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  2 pending,  4 downloaded,  4 extracted, (59 MB/? MB)
15:41:01.887 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  1 pending,  5 downloaded,  4 extracted, (59 MB/? MB)
15:41:01.887 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  1 pending,  5 downloaded,  5 extracted, (59 MB/? MB)
15:41:02.880 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  0 pending,  6 downloaded,  5 extracted, (59 MB/59 MB)
15:41:02.880 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pulling image layers:  0 pending,  6 downloaded,  6 extracted, (59 MB/59 MB)
15:41:02.881 [docker-java-stream-731037058] INFO  tc.firebirdsql/firebird:5.0.2 - Pull complete. 6 layers, pulled in 9s (downloaded 59 MB at 6 MB/s)
15:41:02.881 [main] INFO  tc.firebirdsql/firebird:5.0.2 - Image firebirdsql/firebird:5.0.2 pull took PT14.4473597S
15:41:02.890 [main] INFO  tc.firebirdsql/firebird:5.0.2 - Creating container for image: firebirdsql/firebird:5.0.2
15:41:03.028 [main] INFO  tc.firebirdsql/firebird:5.0.2 - Container firebirdsql/firebird:5.0.2 is starting: cfc073be8631fe2b46b5ced16a626589bc9990dce92c2ce42f621a29700908eb
15:41:03.513 [main] INFO  tc.firebirdsql/firebird:5.0.2 - Container firebirdsql/firebird:5.0.2 started in PT0.6227747S
15:41:03.803 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-3 - Starting...
15:41:03.821 [main] INFO  com.zaxxer.hikari.pool.HikariPool - HikariPool-3 - Added connection org.firebirdsql.jdbc.FBConnection@2596d7f4
15:41:03.822 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-3 - Start completed.
```