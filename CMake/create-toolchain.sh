NDK=/home/spark/android-ndk-r25c
$NDK/build/tools/make_standalone_toolchain.py --arch arm64 --api 32 --install-dir=my-toolchain


#WARNING:__main__:make_standalone_toolchain.py is no longer necessary. The
#$NDK/toolchains/llvm/prebuilt/linux-x86_64/bin directory contains target-specific scripts that perform
#the same task. For example, instead of:
#
#    $ python $NDK/build/tools/make_standalone_toolchain.py \
#        --arch arm64 --api 32 --install-dir toolchain
#    $ toolchain/bin/clang++ src.cpp
#
#Instead use:
#
#    $ $NDK/toolchains/llvm/prebuilt/linux-x86_64/bin/aarch64-linux-android32-clang++ src.cpp