/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
#include <stdio.h>
/* Header for class comp3003_jni_ConsoleIO */

#ifndef _Included_comp3003_jni_ConsoleIO
#define _Included_comp3003_jni_ConsoleIO
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     comp3003_jni_ConsoleIO
 * Method:    read
 * Signature: (D)D
 */
JNIEXPORT jdouble JNICALL Java_comp3003_jni_ConsoleIO_read
  (JNIEnv *, jclass, jdouble);

/*
 * Class:     comp3003_jni_ConsoleIO
 * Method:    printStr
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_comp3003_jni_ConsoleIO_printStr
  (JNIEnv *, jclass, jstring);

/*
 * Class:     comp3003_jni_ConsoleIO
 * Method:    printList
 * Signature: (Ljava/util/List;)V
 */
JNIEXPORT void JNICALL Java_comp3003_jni_ConsoleIO_printList
  (JNIEnv *, jclass, jobject);

#ifdef __cplusplus
}
#endif
#endif
