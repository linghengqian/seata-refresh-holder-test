package com.github.linghengqian;

import org.apache.shardingsphere.infra.metadata.database.resource.unit.StorageUnit;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.proxy.Bootstrap;
import org.apache.shardingsphere.proxy.backend.context.ProxyContext;
import org.awaitility.Awaitility;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


public final class ProxyTestingServer {

    private final int proxyPort;

    private final CompletableFuture<Void> completableFuture;

    public int getProxyPort() {
        return proxyPort;
    }

    public ProxyTestingServer(final String configAbsolutePath) {
        try (ServerSocket server = new ServerSocket(0)) {
            server.setReuseAddress(true);
            proxyPort = server.getLocalPort();
        } catch (IOException ex) {
            throw new Error(ex);
        }
        completableFuture = CompletableFuture.runAsync(() -> {
            try {
                Bootstrap.main(new String[]{String.valueOf(proxyPort), configAbsolutePath, "0.0.0.0"});
            } catch (final IOException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void close(final List<String> logicDataBaseNameList) {
        ContextManager contextManager = ProxyContext.getInstance().getContextManager();
        logicDataBaseNameList.forEach(logicDataBaseName -> contextManager.getStorageUnits(logicDataBaseName)
                .values()
                .stream()
                .map(StorageUnit::getDataSource)
                .forEach(dataSource -> {
                    if (dataSource instanceof AutoCloseable) {
                        try {
                            ((AutoCloseable) dataSource).close();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }));
        contextManager.close();
        completableFuture.cancel(false);
        Awaitility.await().atMost(1L, TimeUnit.MINUTES).until(completableFuture::isDone);
    }
}
