package ale.rains.nanohttpd.protocols.http.threading;

import ale.rains.nanohttpd.protocols.http.ClientHandler;

/**
 * Pluggable strategy for asynchronously executing requests.
 */
public interface IAsyncRunner {

    void closeAll();

    void closed(ClientHandler clientHandler);

    void exec(ClientHandler code);
}
