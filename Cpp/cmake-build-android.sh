rm -rf build_android
mkdir build_android
cd build_android
NDK=/home/spark/android-ndk-r25c
cmake -DCMAKE_TOOLCHAIN_FILE= $NDK/build/cmake/android.toolchain.cmake \
		-DANDROID_NDK=$NDK \
		-DANDROID_ABI=arm64-v8a \
		-DANDROID_PLATFORM=android-33 \
		-DCMAKE_ANDROID_ARCH_ABI=arm64-v8a \
		-DCMAKE_BUILD_TYPE=Release \
		-DCMAKE_SYSTEM_NAME=Android \
		-DCMAKE_SYSTEM_VERSION=33 \
		..
make
cd ..