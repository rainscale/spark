# Add the standalone toolchain to the search path.
#export PATH=$PATH:`pwd`/my-toolchain/bin
export TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/linux-x86_64
export PATH=$PATH:TOOLCHAIN/bin

export TARGET=aarch64-linux-android

# Set this to your minSdkVersion.
export API=32

# Tell configure what tools to use.
export AR=$TOOLCHAIN/bin/llvm-ar
export CC=$TOOLCHAIN/bin/$TARGET$API-clang
export CXX=$TOOLCHAIN/bin/$TARGET$API-clang++
#export AS=$TOOLCHAIN/bin/llvm-as
export AS=$CC
export LD=$TOOLCHAIN/bin/ld
export STRIP=$TOOLCHAIN/bin/llvm-strip
export RANLIB=$TOOLCHAIN/bin/llvm-ranlib

# Tell configure what flags Android requires.
export CFLAGS="-fPIE -fPIC"
export LDFLAGS="-pie"

make $@