package ale.rains.nanohttpd.protocols.http.sockets;

import java.io.IOException;
import java.net.ServerSocket;

import ale.rains.nanohttpd.util.IFactoryThrowing;

/**
 * Creates a normal ServerSocket for TCP connections
 */
public class DefaultServerSocketFactory implements IFactoryThrowing<ServerSocket, IOException> {

    @Override
    public ServerSocket create() throws IOException {
        return new ServerSocket();
    }

}
