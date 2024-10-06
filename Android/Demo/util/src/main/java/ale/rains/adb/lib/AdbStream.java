package ale.rains.adb.lib;


import java.io.Closeable;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class abstracts the underlying ADB streams
 *
 * @see <a href="https://github.com/cgutman/AdbLib">AdbLib</a>
 */
public class AdbStream implements Closeable {

    /**
     * The AdbConnection object that the stream communicates over
     */
    private AdbConnection adbConn;

    /**
     * The local ID of the stream
     */
    private int localId;

    /**
     * The remote ID of the stream
     */
    private int remoteId;

    /**
     * Indicates whether a write is currently allowed
     */
    private AtomicBoolean writeReady;

    /**
     * A queue of data from the target's write packets
     */
    private ByteQueueInputStream readQueue;

    /**
     * Indicates whether the connection is closed already
     */
    private boolean isClosed;

    /**
     * Creates a new AdbStream object on the specified AdbConnection
     * with the given local ID.
     *
     * @param adbConn AdbConnection that this stream is running on
     * @param localId Local ID of the stream
     */
    public AdbStream(AdbConnection adbConn, int localId) {
        this.adbConn = adbConn;
        this.localId = localId;
        this.readQueue = new ByteQueueInputStream();
        this.writeReady = new AtomicBoolean(false);
        this.isClosed = false;
    }

    /**
     * Called by the connection thread to indicate newly received data.
     *
     * @param payload Data inside the write message
     */
    void addPayload(byte[] payload) {
        readQueue.addBytes(payload);
    }

    public ByteQueueInputStream getInputStream() {
        return readQueue;
    }

    public Queue<byte[]> getReadQueue() {
        return readQueue.readQueue;
    }

    /**
     * Called by the connection thread to send an OKAY packet, allowing the
     * other side to continue transmission.
     *
     * @throws IOException If the connection fails while sending the packet
     */
    void sendReady() throws IOException {
        /* Generate and send a READY packet */
        byte[] packet = AdbProtocol.generateReady(localId, remoteId);
        adbConn.outputStream.write(packet);
        adbConn.outputStream.flush();
    }

    /**
     * Called by the connection thread to update the remote ID for this stream
     *
     * @param remoteId New remote ID
     */
    void updateRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }

    /**
     * Called by the connection thread to indicate the stream is okay to send data.
     */
    void readyForWrite() {
        writeReady.set(true);
    }

    /**
     * Called by the connection thread to notify that the stream was closed by the peer.
     */
    void notifyClose() {
        /* We don't call close() because it sends another CLOSE */
        isClosed = true;

        /* Unwait readers and writers */
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * Sends a write packet with a given String payload.
     *
     * @param payload Payload in the form of a String
     * @throws IOException          If the stream fails while sending data
     * @throws InterruptedException If we are unable to wait to send data
     */
    public void write(String payload) throws IOException, InterruptedException {
        /* ADB needs null-terminated strings */
        //System.out.println("Send CMD:WRTE; arg0 " + localId + "; arg1: " + remoteId + "; data: " + payload);

        // 保持同步写
        specialWrite(payload.getBytes("UTF-8"));
    }

    /**
     * 针对write payload的特殊处理
     *
     * @param payload
     * @throws IOException
     * @throws InterruptedException
     */
    public void specialWrite(byte[] payload) throws IOException, InterruptedException {
        synchronized (this) {
            /* Make sure we're ready for a write */
            while (!isClosed && !writeReady.compareAndSet(true, false))
                wait();

            if (isClosed) {
                throw new IOException("Stream closed");
            }
        }

        /* Generate a WRITE packet and send it */
        byte[] packet = AdbProtocol.generateWrite(localId, remoteId, payload);

        // payload和空内容提交完毕后再flush
        adbConn.outputStream.write(packet);
        adbConn.outputStream.write(AdbProtocol.generateWrite(localId, remoteId, new byte[0]));

        adbConn.outputStream.flush();
    }

    /**
     * Sends a write packet with a given byte array payload.
     *
     * @param payload Payload in the form of a byte array
     * @throws IOException          If the stream fails while sending data
     * @throws InterruptedException If we are unable to wait to send data
     */
    public void write(byte[] payload) throws IOException, InterruptedException {
        write(payload, true);
    }

    /**
     * Queues a write packet and optionally sends it immediately.
     *
     * @param payload Payload in the form of a byte array
     * @param flush   Specifies whether to send the packet immediately
     * @throws IOException          If the stream fails while sending data
     * @throws InterruptedException If we are unable to wait to send data
     */
    public void write(byte[] payload, boolean flush) throws IOException, InterruptedException {
        synchronized (this) {
            /* Make sure we're ready for a write */
            while (!isClosed && !writeReady.compareAndSet(true, false))
                wait();

            if (isClosed) {
                throw new IOException("Stream closed");
            }
        }

        /* Generate a WRITE packet and send it */
        byte[] packet = AdbProtocol.generateWrite(localId, remoteId, payload);
        adbConn.outputStream.write(packet);

        if (flush)
            adbConn.outputStream.flush();
    }

    /**
     * Closes the stream. This sends a close message to the peer.
     *
     * @throws IOException If the stream fails while sending the close message.
     */
    @Override
    public void close() throws IOException {
        synchronized (this) {
            /* This may already be closed by the remote host */
            if (isClosed)
                return;

            /* Notify readers/writers that we've closed */
            notifyClose();
        }

        byte[] packet = AdbProtocol.generateClose(localId, remoteId);
        adbConn.outputStream.write(packet);
        adbConn.outputStream.flush();

        readQueue.close();
    }

    public int getLocalId() {
        return localId;
    }

    /**
     * Retreives whether the stream is closed or not
     *
     * @return True if the stream is close, false if not
     */
    public boolean isClosed() {
        return isClosed;
    }
}
