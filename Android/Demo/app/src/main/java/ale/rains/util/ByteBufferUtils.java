package ale.rains.util;

/*
 * Copyright 2020 Adrien Grand and the lz4-java contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

public enum ByteBufferUtils {
  ;

  public static void checkRange(ByteBuffer buf, int off, int len) {
    SafeUtils.checkLength(len);
    if (len > 0) {
      checkRange(buf, off);
      checkRange(buf, off + len - 1);
    }
  }

  public static void checkRange(ByteBuffer buf, int off) {
    if (off < 0 || off >= buf.capacity()) {
      throw new ArrayIndexOutOfBoundsException(off);
    }
  }

  public static ByteBuffer inLittleEndianOrder(ByteBuffer buf) {
    if (buf.order().equals(ByteOrder.LITTLE_ENDIAN)) {
      return buf;
    } else {
      return buf.duplicate().order(ByteOrder.LITTLE_ENDIAN);
    }
  }

  public static ByteBuffer inNativeByteOrder(ByteBuffer buf) {
    if (buf.order().equals(Utils.NATIVE_BYTE_ORDER)) {
      return buf;
    } else {
      return buf.duplicate().order(Utils.NATIVE_BYTE_ORDER);
    }
  }

  public static byte readByte(ByteBuffer buf, int i) {
    return buf.get(i);
  }

  public static void writeInt(ByteBuffer buf, int i, int v) {
    assert buf.order() == Utils.NATIVE_BYTE_ORDER;
    buf.putInt(i, v);
  }

  public static int readInt(ByteBuffer buf, int i) {
    assert buf.order() == Utils.NATIVE_BYTE_ORDER;
    return buf.getInt(i);
  }

  public static int readIntLE(ByteBuffer buf, int i) {
    assert buf.order() == ByteOrder.LITTLE_ENDIAN;
    return buf.getInt(i);
  }

  public static void writeLong(ByteBuffer buf, int i, long v) {
    assert buf.order() == Utils.NATIVE_BYTE_ORDER;
    buf.putLong(i, v);
  }

  public static long readLong(ByteBuffer buf, int i) {
    assert buf.order() == Utils.NATIVE_BYTE_ORDER;
    return buf.getLong(i);
  }

  public static long readLongLE(ByteBuffer buf, int i) {
    assert buf.order() == ByteOrder.LITTLE_ENDIAN;
    return buf.getLong(i);
  }

  public static void writeByte(ByteBuffer dest, int off, int i) {
    dest.put(off, (byte) i);
  }

  public static void writeShortLE(ByteBuffer dest, int off, int i) {
    dest.put(off, (byte) i);
    dest.put(off + 1, (byte) (i >>> 8));
  }

  public static void checkNotReadOnly(ByteBuffer buffer) {
    if (buffer.isReadOnly()) {
      throw new ReadOnlyBufferException();
    }
  }

  public static int readShortLE(ByteBuffer buf, int i) {
    return (buf.get(i) & 0xFF) | ((buf.get(i+1) & 0xFF) << 8);
  }
}
