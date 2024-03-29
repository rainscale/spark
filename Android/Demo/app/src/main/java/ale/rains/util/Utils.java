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

import java.nio.ByteOrder;

public enum Utils {
  ;

  public static final ByteOrder NATIVE_BYTE_ORDER = ByteOrder.nativeOrder();

  private static final boolean unalignedAccessAllowed;
  static {
    String arch = System.getProperty("os.arch");
    unalignedAccessAllowed = arch.equals("i386") || arch.equals("x86")
            || arch.equals("amd64") || arch.equals("x86_64")
            || arch.equals("aarch64") || arch.equals("ppc64le");
  }

  public static boolean isUnalignedAccessAllowed() {
    return unalignedAccessAllowed;
  }

}
