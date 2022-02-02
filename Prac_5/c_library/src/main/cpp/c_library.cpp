#include "c_library.h"

/*******************************************************
* Author: George Aziz
* Purpose: Implementation of Java methods in C/C++
* Date Last Modified: 19/09/2021
*******************************************************/

// This construct is needed to make the C++ compiler generate C-compatible compiled code.
extern "C" 
{
    //Function that takes user input and checks if input is a real number or not
    JNIEXPORT jdouble JNICALL Java_comp3003_jni_ConsoleIO_read(JNIEnv *env, jclass cls, jdouble inputValue)
    {
        int check;
        float realNumber;
        check = scanf("%f", &realNumber); //User input
        fflush(stdout); //Flushes stdout so output from Java/C comes in order
        if(check != 1)
        {
             return inputValue;
        }
        else
        {
            return realNumber;
        }
    }

    //Function that prints imported string
    JNIEXPORT void JNICALL Java_comp3003_jni_ConsoleIO_printStr(JNIEnv *env, jclass cls, jstring stringInput)
    {
        const char* str = (*env).GetStringUTFChars(stringInput, 0);
        printf("Returned String: %s\n", str); //Print Default Value
        fflush(stdout); //Flushes stdout so output from Java/C comes in order
        (*env).ReleaseStringUTFChars(stringInput, str); //Releases memory to prevent memory leaks
    }

    //Function that prints imported list of strings
    JNIEXPORT void JNICALL Java_comp3003_jni_ConsoleIO_printList(JNIEnv *env, jclass cls, jobject stringList)
    {
        jclass list = (*env).GetObjectClass(stringList);

        //Getting method calls from Java to get size and get element
        jmethodID sizeCall = (*env).GetMethodID(list,"size","()I");
        jmethodID getCall = (*env).GetMethodID(list,"get","(I)Ljava/lang/Object;");

        //Gets size of list
        jint size = (*env).CallIntMethod(stringList, sizeCall);
        printf("Returned Output: ");
        for (jint i = 0; i < size; i++)
        {
            //Gets string element
            jstring curJString = (jstring)(*env).CallObjectMethod(stringList, getCall, i);
            const char* str = (*env).GetStringUTFChars(curJString, 0); //Coverts jstring into c string
            if(i == size-1) //Last element
            {
                printf("%s\n", str);
            }
            else //Not last element
            {
                printf("%s, ", str);
            }
            (*env).ReleaseStringUTFChars(curJString, str); //Releases memory to prevent memory leaks
            fflush(stdout); //Flushes stdout so output from Java/C comes in order
        }
    }
}
