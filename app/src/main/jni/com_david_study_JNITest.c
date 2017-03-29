//
// Created by DavidChen on 2017/1/5.
//

#include "com_david_study_JNITest.h"
#include <stdio.h>
#include <stdlib.h>

#ifdef __cplusplus
extern "C"
{
#endif

    /*
     * Class:     com_david_study_JNITest
     * Method:    printJNI
     * Signature: ()Ljava/lang/String;
     */
    JNIEXPORT jstring JNICALL Java_com_david_study_JNITest_printJNI
      (JNIEnv *env, jclass arg)
      {
        jstring str = (*env)->NewStringUTF(env, "Hello JNI !!!");
        return str;
      }

#ifdef __cplusplus
}
#endif