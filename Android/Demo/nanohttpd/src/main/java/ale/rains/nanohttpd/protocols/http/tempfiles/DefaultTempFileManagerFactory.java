package ale.rains.nanohttpd.protocols.http.tempfiles;

import ale.rains.nanohttpd.util.IFactory;

/**
 * Default strategy for creating and cleaning up temporary files.
 */
public class DefaultTempFileManagerFactory implements IFactory<ITempFileManager> {

    @Override
    public ITempFileManager create() {
        return new DefaultTempFileManager();
    }
}