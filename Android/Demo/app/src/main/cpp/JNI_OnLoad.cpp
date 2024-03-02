#include <jni.h>
#include <string>
#include <android/log.h>
#define LOG_TAG "Test"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG , LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO , LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN , LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR , LOG_TAG, __VA_ARGS__)

jint test(JNIEnv* env, jobject obj, jint i, jint j) {
    LOGV("%d + %d = %d\n", i, j, i+j);
    return i + j;
}

static const JNINativeMethod methods[] = {
        {"test", "(II)I", (void*)test}
};

static const char* className = "ale/rains/demo/MainActivity";

int JNI_OnLoad(JavaVM* vm, void* reserved) {
    LOGI("JNI_OnLoad\n");
    JNIEnv* env = nullptr;
    if (vm->GetEnv((void**)&env, JNI_VERSION_1_6) != JNI_OK) {
        LOGE("GetEnv failed\n");
        return JNI_ERR;
    }
    jclass clazz = env->FindClass(className);
    env->RegisterNatives(clazz, methods, sizeof(methods)/sizeof(JNINativeMethod));
    return JNI_VERSION_1_6;
}
