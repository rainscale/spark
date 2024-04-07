#include <jni.h>
#include <string>
#include <sys/resource.h>    /* setpriority */
#include <sys/system_properties.h>    /* __system_property_get */
#include <unistd.h>    /* nice */
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

#define NUM_THREADS_MAX    64
volatile bool isExit = false;
pthread_t threads[NUM_THREADS_MAX];
int ids[NUM_THREADS_MAX];
int num_threads;

void* func(void* args) {
    int res;
    char* key = (char*)malloc(PROP_NAME_MAX);
    char* value = (char*)malloc(PROP_VALUE_MAX);
    int id = (int)(*((int*)args));
    pthread_t tid = gettid();
    LOGI("thread %ld-%d run\n", tid, id);
    sprintf(key, "debug.thread%d.exit", id);
    while (!isExit) {
        res = __system_property_get(key, value);
        if (res > 0 && !strcmp(value, "1")) {
            LOGI("%s is 1\n", key);
            __system_property_set(key, "0");
            break;
        }
        sleep(1);
    };
    num_threads--;
    LOGI("thread %ld-%d exit\n", tid, id);
    return NULL;
}

void thread_test(JNIEnv*, jclass, jlong time, jint n) {
    int res;
    long t = 0;
    res = setpriority(PRIO_PROCESS, 0, -20);
    if (res) {
        LOGE("setpriority failed, error: %s\n", strerror(errno));
    }
    res = nice(-20);
    if (res) {
        LOGE("nice failed, error: %s\n", strerror(errno));
    }
    num_threads = n;
    for (int i = 0; i < num_threads; i++) {
        ids[i] = i + 1;
    }
    for (int i = 0; i < n; i++) {
        res = pthread_create(&threads[i], NULL, func, &ids[i]);
        if (res) {
            LOGI("pthread_create %i failed, error: %s\n", i, strerror(res));
            num_threads--;
        } else {
            LOGI("pthread_create %i success\n", i);
            pthread_detach(threads[i]);
        }
    }
    while (true) {
        if (isExit) {
            break;
        }
        sleep(1);
        if (time > 0) {
            t++;
            if (t > time) {
                isExit = true;
                break;
            }
        }
        if (num_threads <= 0) {
            break;
        }
    }
    LOGI("thread_test exit\n");
}

static const JNINativeMethod methods[] = {
        {"test", "(II)I", (void*)test},
        {"thread_test", "(JI)V", (void*)thread_test}
};

static const char* className = "ale/rains/demo/activity/MainActivity";

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
