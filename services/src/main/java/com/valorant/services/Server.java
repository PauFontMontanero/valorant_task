package com.valorant.services;

import com.valorant.services.exception.ServerException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpOptions;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static final int PORT = 80;
    private final RequestRouter requestRouter;
    private volatile boolean isRunning;

    public Server(RequestRouter requestRouter) {
        this.requestRouter = requestRouter;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!isRunning) {
                try (var clientSocket = serverSocket.accept()) {
                    RawHttp rawHttp = new RawHttp(RawHttpOptions.newBuilder().doNotInsertHostHeaderIfMissing().build());
                    var request = rawHttp.parseRequest(clientSocket.getInputStream());
                    var response = requestRouter.execRequest(request);
                    response.writeTo(clientSocket.getOutputStream());
                }
            }
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }
}
