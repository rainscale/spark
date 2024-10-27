#!/bin/bash
set -eu

function usage() {
    echo -e "Usage:${BASH_SOURCE[0]} target_os target_arch
        target_os: android/linux/windows
        target_arch: arm/arm64/x86/x86_64
    "
}

main() {
    if [[ $# -lt 2 ]]; then
        echo -e "less than 2 parameters, exit without do anythings!"
        usage
        return 0
    fi

    export target_os=$1
    export target_arch=$2
    
    case ${target_os} in
        android)
            local NDK=/home/spark/android-ndk-r25c
            local NDKBIN=${NDK}/toolchains/llvm/prebuilt/linux-x86_64/bin
            local SYSROOT=${NDK}/toolchains/llvm/prebuilt/linux-x86_64/sysroot
            local OPTIONS="--sysroot=${SYSROOT}"
            case "${target_arch}" in
                "arm")
                    TARGET=armv7a-linux-androideabi33
                    ;;
                "arm64")
                    TARGET=aarch64-linux-android33
                    ;;
                *)
                    echo -e "target arch ${target_arch} does not support!"
                    usage
                    return 0
                    ;;
            esac
            
            OPTIONS="${OPTIONS} --target=${TARGET}"
            export ADDR2LINE=${NDKBIN}/llvm-addr2line
            export AR=${NDKBIN}/llvm-ar
            export CC=${NDKBIN}/clang
            export CXX=${NDKBIN}/clang++
            export LD=${NDKBIN}/ld.lld
            export NM=${NDKBIN}/llvm-nm
            export OBJCOPY=${NDKBIN}/llvm-objcopy
            export OBJDUMP=${NDKBIN}/llvm-objdump
            export RANLIB=${NDKBIN}/llvm-ranlib
            export READELF=${NDKBIN}/llvm-readelf
            export STRIP=${NDKBIN}/llvm-strip
            export CFLAGS="${OPTIONS} -DANDROID -fPIC -DNDEBUG"
            export CPPFLAGS="${OPTIONS} -DANDROID -fPIC -DNDEBUG"
            export LDFLAGS="-landroid"
            ;;

        linux)
            case "${target_arch}" in
                "x86")
                    export CFLAGS="-m32"
                    export CPPFLAGS="-m32"
                    export LDFLAGS="-m32"
                    ;;
                "x86_64")
                    ;;
                *)
                    echo -e "target arch ${target_arch} does not support!"
                    usage
                    return 0
                    ;;
            esac
            export CC=gcc
            export CXX=g++
            ;;
        
        windows)
            ;;

        *)
            echo -e "target os ${target_os} does not support!"
            usage
            return 0
            ;;
    esac
        
    if [[ $# -gt 2 ]]; then
        case $3 in
            clean|remake)
                make clean
                ;;
            -C)
                make -C $4
                return 0
                ;;
        esac
    fi

    make -j8
}

main $@