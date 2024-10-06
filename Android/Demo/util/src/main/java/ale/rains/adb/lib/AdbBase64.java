package ale.rains.adb.lib;

/**
 * This interface specifies the required functions for AdbCrypto to
 * perform Base64 encoding of its public key.
 *
 * @see <a href="https://github.com/cgutman/AdbLib">AdbLib</a>
 */
public interface AdbBase64 {
    /**
     * This function must encoded the specified data as a base 64 string, without
     * appending any extra newlines or other characters.
     *
     * @param data Data to encode
     * @return String containing base 64 encoded data
     */
    public String encodeToString(byte[] data);
}