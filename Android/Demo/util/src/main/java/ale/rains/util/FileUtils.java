package ale.rains.util;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String EMPTY_STRING = "";
    private static final String FILE_EXTENSION_SEPARATOR = ".";

    public static void arraycopy(byte[] src, int srcPos, byte[] dst, int dstPos, int length) {
        try {
            Method method = System.class.getDeclaredMethod("arraycopy", byte[].class, int.class, byte[].class, int.class, int.class);
            method.setAccessible(true);
            method.invoke(System.class, src, srcPos, dst, dstPos, length);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    public static void closeIO(Closeable... closeables) {
        if (closeables == null)
            return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    Logger.e(e.toString());
                }
            }
        }
    }

    public static boolean copyFile(String srcPath, String dstPath) {
        InputStream is = null;
        try {
            is = new FileInputStream(srcPath);
            return writeFile(dstPath, is, false);
        } catch (FileNotFoundException e) {
            Logger.e(e.getMessage());
            return false;
        } finally {
            closeIO(is);
        }
    }

    public static void copyFolder(String srcPath, String dstPath) {
        copyFolder(srcPath, dstPath, false);
    }

    public static void copyFolder(String srcPath, String dstPath, boolean delete) {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(dstPath)) {
            return;
        }
        File f = new File(srcPath);
        if (!f.exists()) {
            Logger.i(srcPath + " does not exist");
            return;
        }
        if (!f.isDirectory()) {
            Logger.i(srcPath + " is not directory");
            return;
        }
        try {
            File srcFile = new File(srcPath);
            String[] fileList = srcFile.list();
            File dstFile = new File(dstPath);
            if (!dstFile.exists()) {
                dstFile.mkdir();
            }
            if (fileList == null) {
                return;
            }
            for (String filePath : fileList) {
                String childPath = srcPath + File.separator + filePath;
                File childFile = new File(childPath);
                if (childFile.isDirectory()) {
                    copyFolder(childPath, dstPath + File.separator + filePath, delete);
                    if (delete) {
                        deleteFile(childPath);
                    }
                } else if (childFile.isFile()) {
                    copyFile(childPath, dstPath + File.separator + filePath);
                    if (delete) {
                        deleteFile(childPath);
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    public static boolean createFile(String filePath, long length, String type) {
        long sizeKb = 1024L;
        long sizeMb = 1024L * 1024L;
        long size10Mb = 10 * 1024L * 1024L;
        FileOutputStream fos = null;
        File f = new File(filePath);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            long blockSize = 0;
            long size = getFileSize(type, length);
            if (size > sizeKb) {
                blockSize = sizeKb;
            }
            if (size > sizeMb) {
                blockSize = sizeMb;
            }
            if (size > size10Mb) {
                blockSize = size10Mb;
            }
            long count = size / blockSize;
            long remainder = size % blockSize;
            fos = new FileOutputStream(f);
            FileChannel fc = fos.getChannel();
            byte[] byteArray = getByte((int) blockSize);
            for (int i = 0; i < count; i++) {
                ByteBuffer buffer = ByteBuffer.wrap(byteArray);
                fc.write(buffer);
            }
            if (remainder != 0) {
                ByteBuffer buffer = ByteBuffer.allocate((int) remainder);
                fc.write(buffer);
            }
            return true;
        } catch (FileNotFoundException e) {
            Logger.e(e.getMessage());
            return false;
        } catch (IOException e) {
            Logger.e(e.getMessage());
            return false;
        } finally {
            closeIO(fos);
        }
    }

    public static boolean deleteAllFile(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return true;
        File f = new File(filePath);
        if (!f.exists())
            return true;
        if (f.isFile())
            return f.delete();
        if (!f.isDirectory())
            return false;
        File[] fileList = f.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    deleteAllFile(file.getAbsolutePath());
                }
            }
        }
        return f.delete();
    }

    public static boolean deleteFile(String paramString) {
        File f = new File(paramString);
        return f.exists() ? f.delete() : false;
    }

    public static int getAllFilesCount(File dirFile, int depth) {
        ArrayList<File> files = getAllFiles(dirFile, depth, EMPTY_STRING);
        return files.size();
    }

    public static int getAllFilesCount(File dirFile, String suffix) {
        ArrayList<File> files = getAllFiles(dirFile, Integer.MAX_VALUE, suffix);
        return files.size();
    }

    public static ArrayList<File> getAllFiles(File dirFile) {
        return getAllFiles(dirFile, Integer.MAX_VALUE, EMPTY_STRING);
    }

    public static ArrayList<File> getAllFiles(File dirFile, int depth, String suffix) {
        ArrayList<File> files = new ArrayList();
        if (dirFile == null || dirFile.isFile() || depth <= 0) {
            return files;
        }
        File[] fileList = dirFile.listFiles();
        if (fileList == null) {
            return files;
        }
        for (File f : fileList) {
            if (f.isFile() && TextUtils.isEmpty(suffix)) {
                files.add(f);
            } else if (f.isFile() && f.getAbsolutePath().endsWith(suffix)) {
                files.add(f);
            }
            if (f.isDirectory()) {
                ArrayList<File> list = getAllFiles(f, depth - 1, suffix);
                files.addAll(list);
            }
        }
        return files;
    }

    public static int getAllFilesCount(File dirFile, int depth, String suffix) {
        ArrayList<File> files = getAllFiles(dirFile, depth, suffix);
        return files.size();
    }

    public static byte[] getByte(int size) {
        byte[] src = "abcdefg\n".getBytes();
        byte[] dst = new byte[size];
        int count = size / src.length;
        if (count > 0) {
            for (int i = 0; i < count; i++)
                arraycopy(src, 0, dst, i * src.length, src.length);
            if (size % src.length != 0)
                arraycopy(src, 0, dst, count * src.length, size % src.length);
            return dst;
        } else {
            return src;
        }
    }

    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return filePath;
        int lastDotIndex = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        if (lastDotIndex == -1) {
            return EMPTY_STRING;
        }
        int lastSeparatorIndex = filePath.lastIndexOf(File.separator);
        return (lastSeparatorIndex >= lastDotIndex) ? EMPTY_STRING : filePath.substring(lastDotIndex + 1);
    }

    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return filePath;
        int lastSeparatorIndex = filePath.lastIndexOf(File.separator);
        return (lastSeparatorIndex == -1) ? filePath : filePath.substring(lastSeparatorIndex + 1);
    }

    public static long getFileSize(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return -1L;
        File f = new File(filePath);
        return (f.exists() && f.isFile()) ? f.length() : -1L;
    }

    public static long getFileSize(String type, long length) {
        long size = length;
        switch (type) {
            case "KB":
                size = size * 1024L;
                break;
            case "MB":
                size = size * 1024L * 1024L;
                break;
            case "GB":
                size = size * 1024L * 1024L * 1024L;
                break;
            case "TB":
                size = size * 1024L * 1024L * 1024L * 1024L;
                break;
            default:
                break;
        }
        return size;
    }

    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return filePath;
        int lastSeparatorIndex = filePath.lastIndexOf(File.separator);
        return (lastSeparatorIndex == -1) ? EMPTY_STRING : filePath.substring(0, lastSeparatorIndex);
    }

    public static long getFolderSize(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return -1L;
        File f = new File(filePath);
        if (!f.exists()) {
            return -1L;
        }
        if (f.isFile()) {
            return f.length();
        }
        long size = 0L;
        File[] fileList = f.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                size += getFolderSize(file.getAbsolutePath());
            }
        }
        return size;
    }

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;
        File f = new File(filePath);
        return f.exists() && f.isFile();
    }

    public static boolean isFolderExist(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;
        File f = new File(filePath);
        return f.exists() && f.isDirectory();
    }

    public static boolean makeDirsByDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath))
            return false;
        File f = new File(dirPath);
        if (f.exists() && f.isDirectory()) {
            return true;
        } else {
            return f.mkdirs();
        }
    }

    public static boolean makeDirsByFilePath(String filePath) {
        String folderPath = getFolderName(filePath);
        if (TextUtils.isEmpty(folderPath))
            return false;
        File f = new File(folderPath);
        if (f.exists() && f.isDirectory()) {
            return true;
        } else {
            return f.mkdirs();
        }
    }

    public static void makeFile(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                Logger.e(e.getMessage());
            }
        }
    }

    public static void moveFile(File srcFile, File dstFile) {
        if (!srcFile.renameTo(dstFile)) {
            copyFile(srcFile.getAbsolutePath(), dstFile.getAbsolutePath());
            deleteAllFile(srcFile.getAbsolutePath());
        }
    }

    public static void moveFile(String srcPath, String dstPath) {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(dstPath)) {
            Logger.e("Both srcPath and dstPath cannot be null");
        }
        moveFile(new File(srcPath), new File(dstPath));
    }

    /**
     * 读取指定文件的内容
     *
     * @param filePath    文件路径
     * @param charsetName 字符编码
     * @return 文件内容
     */
    public static String readFile(String filePath, String charsetName) {
        String result = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            int length = fis.available();
            byte[] buffer = new byte[length];
            int count = fis.read(buffer);
            if (count != -1) {
                result = new String(buffer, charsetName);
            }
        } catch (FileNotFoundException e) {
            Logger.e(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Logger.e(e.getMessage());
        } catch (IOException e) {
            Logger.e(e.getMessage());
        } finally {
            closeIO(fis);
        }
        return result;
    }

    public static List<String> readFileToList(String filePath, String charsetName) {
        File f = new File(filePath);
        List<String> list = new ArrayList<>();
        if (!f.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(f), charsetName);
            reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            return list;
        } catch (FileNotFoundException e) {
            Logger.e(e);
            return null;
        } catch (UnsupportedEncodingException e) {
            Logger.e(e);
            return null;
        } catch (IOException e) {
            Logger.e(e);
            return null;
        } finally {
            closeIO(reader);
        }
    }

    public static String readNode(String filePath) {
        String result = null;
        Reader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
            bufferedReader = new BufferedReader(reader);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
        } catch (FileNotFoundException e) {
            Logger.e(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Logger.e(e.getMessage());
        } catch (IOException e) {
            Logger.e(e.getMessage());
        } finally {
            closeIO(reader, bufferedReader);
        }
        return result;
    }

    public static boolean writeFile(File paramFile, InputStream paramInputStream) {
        return writeFile(paramFile, paramInputStream, true);
    }

    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream os = null;
        try {
            makeDirsByFilePath(file.getAbsolutePath());
            os = new FileOutputStream(file, append);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            os.flush();
            return true;
        } catch (FileNotFoundException e) {
            Logger.e(e.getMessage());
            return false;
        } catch (IOException e) {
            Logger.e(e.getMessage());
            return false;
        } finally {
            closeIO(os, stream);
        }
    }

    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, true);
    }

    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        if (filePath == null) {
            return false;
        }
        return writeFile(new File(filePath), stream, append);
    }

    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, true);
    }

    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        BufferedWriter writer = null;
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, append)));
            char[] charArray = content.toCharArray();
            writer.write(charArray, 0, charArray.length);
            return true;
        } catch (IOException e) {
            Logger.e(e.getMessage());
            return false;
        } finally {
            closeIO(writer);
        }
    }
}