CMake
Android NDK 支持使用 CMake 编译应用的 C 和 C++ 代码。本页介绍了如何通过 Android Gradle 插件的 ExternalNativeBuild 或通过直接调用 CMake 将 CMake 与 NDK 搭配使用。

注意：如果您使用的是 Android Studio，请参阅向您的项目添加 C 和 C++ 代码，了解以下方面的基础知识：向项目中添加原生源代码，创建 CMake 构建脚本，将 CMake 项目添加为 Gradle 依赖项，以及使用比 SDK 中的 CMake 版本更高的 CMake。
CMake 工具链文件
NDK 通过工具链文件支持 CMake。工具链文件是用于自定义交叉编译工具链行为的 CMake 文件。用于 NDK 的工具链文件位于 NDK 中以下位置：<NDK>/build/cmake/android.toolchain.cmake。

注意：如果安装了 Android SDK，则 NDK 安装在 SDK 目录的以下位置：ndk/version/ 或 ndk-bundle/。
在调用 cmake 时，命令行会提供 ABI 和 minSdkVersion 等构建参数。如需查看支持的参数的列表，请参阅工具链参数部分。

警告：CMake 拥有自己的内置 NDK 支持。在 CMake 3.21 之前，此工作流程不受 Android 支持，并且经常会被新的 NDK 版本中断。从 CMake 3.21 开始，这些实现将被合并。CMake 的内置支持与 NDK 工具链文件具有类似的行为，但变量名称有所不同。从 Android NDK r23 开始，工具链文件将在使用 CMake 3.21 或更高版本时委托给 CMake 的内置支持。 如需了解详情，请参阅问题 463。请注意，Android Gradle 插件仍会自动使用 NDK 的工具链文件。
用法
Gradle
命令行
使用 externalNativeBuild 时，系统会自动使用 CMake 工具链文件。详情请参阅 Android Studio 的向您的项目添加 C 和 C++ 代码指南。
工具链参数
以下参数可以传递给 CMake 工具链文件。如果使用 Gradle 进行构建，请按照 ExternalNativeBuild 文档中所述向 android.defaultConfig.externalNativeBuild.cmake.arguments 添加参数。如果通过命令行进行构建，请使用 -D 将参数传递给 CMake。例如，要强制 armeabi-v7a 始终使用 Neon 支持进行构建，请传递 -DANDROID_ARM_NEON=TRUE。

注意：任何必需参数均由 Gradle 自动传递，只有在通过命令行构建时才需明确传递。

ANDROID_ABI
注意：这是必需参数。
目标 ABI。如需了解支持的 ABI，请参阅 Android ABI。
Gradle 会自动提供此参数。请勿在您的 build.gradle 文件中明确设置此参数。如需控制 ABI Gradle 的目标，请按照 Android ABI 中所述使用 abiFilters。
对于每个 build，CMake 都只针对一个目标进行构建。如需以多个 Android ABI 为目标，您必须为每个 ABI 构建一次。建议对每个 ABI 使用不同的构建目录，以避免 build 之间发生冲突。
值										备注
armeabi-v7a	
armeabi-v7a with NEON					与 -DANDROID_ABI=armeabi-v7a -DANDROID_ARM_NEON=ON 相同。
arm64-v8a	
x86	
x86_64	

ANDROID_ARM_MODE
指定是为 armeabi-v7a 生成 arm 还是 thumb 指令。对其他 ABI 没有影响。如需了解详情，请参阅 Android ABI 文档。
值				备注
arm	
thumb			默认行为。

ANDROID_ARM_NEON
为 armeabi-v7a 启用或停用 NEON。对其他 ABI 没有影响。对于 API 级别（minSdkVersion 或 ANDROID_PLATFORM）23 或更高版本，默认为 true，否则为 false。如需了解详情，请参阅 Neon 支持文档。
值				备注
TRUE			API 级别 23 或更高版本的默认值。
FALSE			API 级别 22 或更低版本的默认值。

ANDROID_LD
选择要使用的链接器。lld 目前处于 NDK 实验阶段，可通过此参数启用。
值					备注
lld					启用 lld。
default				对于给定的 ABI 使用默认链接器。

ANDROID_NATIVE_API_LEVEL
ANDROID_PLATFORM 的别名。

ANDROID_PLATFORM
指定应用或库所支持的最低 API 级别。此值对应于应用的 minSdkVersion。
使用 Android Gradle 插件时，此值会自动设置为与应用的 minSdkVersion 相匹配，因此不应手动设置。
当直接调用 CMake 时，此值默认为所使用的 NDK 支持的最低 API 级别。例如，对于 NDK r20，此值默认为 API 级别 16。
警告：NDK 库无法在 API 级别低于构建代码所用的 ANDROID_PLATFORM 值的设备上运行。
此参数支持多种格式：
android-$API_LEVEL
$API_LEVEL
android-$API_LETTER
$API_LETTER 格式允许您指定 android-N，而无需确定与该版本关联的编号。请注意，对于某些版本，API 只是编号增加了，但字母次序没有增加。您可以通过附加 -MR1 后缀来指定这些 API。例如，API 级别 25 为 android-N-MR1。

ANDROID_STL
指定要为此应用使用的 STL。如需了解详情，请参阅 C++ 库支持。默认情况下将使用 c++_static。
注意：默认行为只适用于部分应用。选择 STL 之前，请务必先阅读 C++ 库支持指南，尤其是有关静态运行时的部分。
值					备注
c++_shared			libc++ 的共享库变体。
c++_static			libc++ 的静态库变体。
none				不支持 C++ 标准库。
system				系统 STL

了解 CMake 构建命令
在调试 CMake 构建问题时，了解 Gradle 在为 Android 交叉编译时使用的具体构建参数会很有帮助。
Android Gradle 插件会将用于为每对 ABI 和构建类型执行 CMake 构建的构建参数保存至 build_command.txt。这些文件位于以下目录中：
<project-root>/<module-root>/.cxx/cmake/<build-type>/<ABI>/
以下代码段举例说明了如何使用 CMake 参数构建面向 armeabi-v7a 架构的 hello-jni 示例的可调试版本。
                    Executable : ${HOME}/Android/Sdk/cmake/3.10.2.4988404/bin/cmake
arguments :
-H${HOME}/Dev/github-projects/googlesamples/ndk-samples/hello-jni/app/src/main/cpp
-DCMAKE_FIND_ROOT_PATH=${HOME}/Dev/github-projects/googlesamples/ndk-samples/hello-jni/app/.cxx/cmake/universalDebug/prefab/armeabi-v7a/prefab
-DCMAKE_BUILD_TYPE=Debug
-DCMAKE_TOOLCHAIN_FILE=${HOME}/Android/Sdk/ndk/22.1.7171670/build/cmake/android.toolchain.cmake
-DANDROID_ABI=armeabi-v7a
-DANDROID_NDK=${HOME}/Android/Sdk/ndk/22.1.7171670
-DANDROID_PLATFORM=android-23
-DCMAKE_ANDROID_ARCH_ABI=armeabi-v7a
-DCMAKE_ANDROID_NDK=${HOME}/Android/Sdk/ndk/22.1.7171670
-DCMAKE_EXPORT_COMPILE_COMMANDS=ON
-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=${HOME}/Dev/github-projects/googlesamples/ndk-samples/hello-jni/app/build/intermediates/cmake/universalDebug/obj/armeabi-v7a
-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=${HOME}/Dev/github-projects/googlesamples/ndk-samples/hello-jni/app/build/intermediates/cmake/universalDebug/obj/armeabi-v7a
-DCMAKE_MAKE_PROGRAM=${HOME}/Android/Sdk/cmake/3.10.2.4988404/bin/ninja
-DCMAKE_SYSTEM_NAME=Android
-DCMAKE_SYSTEM_VERSION=23
-B${HOME}/Dev/github-projects/googlesamples/ndk-samples/hello-jni/app/.cxx/cmake/universalDebug/armeabi-v7a
-GNinja
jvmArgs :

                    Build command args: []
                    Version: 1

使用预构建库
如果您需要导入的预构建库是作为 AAR 分发的，请按照 Studio 的依赖项文档进行导入和使用。如果您未使用 AGP，可以按照 https://google.github.io/prefab/example-workflow.html 中的说明操作，但迁移到 AGP 可能要容易得多。
对于未作为 AAR 分发的库，如需了解有关如何将预构建库与 CMake 搭配使用的说明，请参阅 CMake 手册中关于 IMPORTED 目标的 add_library 文档。

构建第三方代码
将第三方代码作为 CMake 项目的一部分进行构建有多种方式，至于哪个方法最适合，则取决于您的具体情况。通常，最好的选择是别这样做。相反，请为相应的库构建 AAR，并在您的应用中使用该 AAR。倒不一定非要发布该 AAR。它可以作为 Gradle 项目的内部资源。
如果不行，请执行以下操作：
将第三方源代码提供（即复制）到您的代码库中，然后使用 add_subdirectory 构建相应源代码。只有在另一个库也使用 CMake 构建的情况下，这种方法才有效。
定义一个 ExternalProject。
独立于您的项目构建该库，然后按照使用预构建库中的说明将其导入为预构建库。


CMake 中的 YASM 支持
NDK 为构建 YASM 汇编代码提供 CMake 支持，以便在 x86 和 x86-64 架构上运行。YASM 是 x86 和 x86-64 架构的开源汇编程序，它基于 NASM 汇编程序。
如需使用 CMake 构建汇编代码，请在项目的 CMakeLists.txt 中进行以下更改：
调用 enable_language，并将值设置为 ASM_NASM。
如果要构建共享库，则调用 add_library；如果要构建可执行的二进制文件，则调用 add_executable。在参数中传入源文件列表，该列表包含用于 YASM 中的汇编程序的 .asm 文件和用于相关 C 库或函数的 .c 文件。
以下代码段展示了如何配置 CMakeLists.txt 来将 YASM 程序构建为共享库。
cmake_minimum_required(VERSION 3.6.0)
enable_language(ASM_NASM)
add_library(test-yasm SHARED jni/test-yasm.c jni/print_hello.asm)
如需查看如何将 YASM 程序构建为可执行文件的示例，请参阅 NDK git 代码库中的 yasm 测试。



概览
只需完成定义目标环境所需的极少配置工作，即可使用 NDK 中的 Clang 编译器。

注意：在 NDK r19 之前，NDK 默认安装的工具链无法直接使用，需要改为使用 make_standalone_toolchain.py。这种情况不会再发生了。
为了确保针对正确的架构进行构建，请在调用 Clang 时使用 -target 传递适当的目标，或调用具有目标前缀的 Clang。例如，若要使用 API 级别 21 的 minSdkVersion 针对 64 位 ARM Android 进行编译，可使用以下两种方法，您可以选择其中最方便的一种：
$ $NDK/toolchains/llvm/prebuilt/$HOST_TAG/bin/clang++ \
    -target aarch64-linux-android21 foo.cpp

$ $NDK/toolchains/llvm/prebuilt/$HOST_TAG/bin/aarch64-linux-android21-clang++ \
    foo.cpp
在这两种情况下，请将 $NDK 替换为指向 NDK 的路径，将 $HOST_TAG 替换为指向根据下表所下载的 NDK 的路径：

NDK操作系统变体		主机标记
macOS				darwin-x86_64
Linux				linux-x86_64
64位Windows			windows-x86_64

注意：虽然 Darwin 名称中包含 x86_64 标记，但这些是包含 M1 支持的胖二进制文件。路径并未更新以反映这项支持，因为这样做会导致编码这些路径的现有 build 损坏。
这里的前缀或目标参数的格式是目标三元组，带有表示 minSdkVersion 的后缀。该后缀仅与 clang/clang++ 一起使用；binutils 工具（例如 ar 和 strip）不需要后缀，因为它们不受 minSdkVersion 影响。Android 支持的目标三元组如下：

ABI				三元组
armeabi-v7a		armv7a-linux-androideabi
arm64-v8a		aarch64-linux-android
x86				i686-linux-android
x86-64			x86_64-linux-android
注意：对于32位ARM，编译器会使用前缀armv7a-linux-androideabi，但binutils工具会使用前缀 arm-linux-androideabi。对于其他架构，所有工具的前缀都相同。
许多项目的构建脚本都预计使用 GCC 风格的交叉编译器，其中每个编译器仅针对一种操作系统/架构组合，因此可能无法正常处理 -target。在这些情况下，您通常可以在编译器定义中包含 -target 参数（例如 CC="clang -target aarch64-linux-android21），或者使用三元组前缀的 Clang 二进制文件。



Autoconf
注意：通常无法在 Windows 上构建 Autoconf 项目。Windows 用户可以使用 Linux 虚拟机中的 Linux NDK 构建这些项目。适用于 Linux 的 Windows 子系统或许也能正常运行，但不受官方支持。目前已知 WSL1无法正常运行。
Autoconf 项目允许您指定与环境变量一起使用的工具链。例如，以下示例展示了如何在 Linux 上使用 API 级别 21 的 minSdkVersion 为 Android x86-64 构建 libpng。
# Check out the source.
git clone https://github.com/glennrp/libpng -b v1.6.37
cd libpng
# Only choose one of these, depending on your build machine...
export TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/darwin-x86_64
export TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/linux-x86_64
# Only choose one of these, depending on your device...
export TARGET=aarch64-linux-android
export TARGET=armv7a-linux-androideabi
export TARGET=i686-linux-android
export TARGET=x86_64-linux-android
# Set this to your minSdkVersion.
export API=21
# Configure and build.
export AR=$TOOLCHAIN/bin/llvm-ar
export CC=$TOOLCHAIN/bin/$TARGET$API-clang
export AS=$CC
export CXX=$TOOLCHAIN/bin/$TARGET$API-clang++
export LD=$TOOLCHAIN/bin/ld
export RANLIB=$TOOLCHAIN/bin/llvm-ranlib
export STRIP=$TOOLCHAIN/bin/llvm-strip
./configure --host $TARGET
make
此示例中选择的工具适用于 NDK r22 及更高版本。旧版 NDK 可能需要不同的工具。



非 Autoconf make 项目
注意：并非所有的make项目都支持交叉编译，且并非所有make 项目都以相同的方式支持交叉编译。如果不进行修改，很可能无法构建项目。
一些 makefile 项目允许通过覆盖与在 autoconf 项目中覆盖的变量相同的变量进行交叉编译。以下示例展示了如何使用 API 级别 21 的 minSdkVersion 为 Android x86-64 构建 libbzip2。
# Check out the source.
git clone https://gitlab.com/bzip/bzip2.git
cd bzip2

# Only choose one of these, depending on your build machine...
export TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/darwin-x86_64
export TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/linux-x86_64

# Only choose one of these, depending on your device...
export TARGET=aarch64-linux-android
export TARGET=armv7a-linux-androideabi
export TARGET=i686-linux-android
export TARGET=x86_64-linux-android

# Set this to your minSdkVersion.
export API=21

# Build.
make \
    CC=$TOOLCHAIN/bin/$TARGET$API-clang \
    AR=$TOOLCHAIN/bin/llvm-ar \
    RANLIB=$TOOLCHAIN/bin/llvm-ranlib \
    bzip2