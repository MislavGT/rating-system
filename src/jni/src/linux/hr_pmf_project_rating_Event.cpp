#define EPSILON 0.0000000001
#include <cmath>
#include "hr_pmf_project_rating_Event.h"

using namespace std;
long placement, length;
double* deltas, *priorMeans, *m, *d;
long* placements;
double parameter, start;

double shorthand(double x){
    return x*sqrt(3)/M_PI;
}

double newtonMethod(double mean, double (*func)(double), double (*der)(double)){
    double x = mean;
    double y = x - (func(x) / der(x));
    while(abs(x - y) > EPSILON){
        x = y;
        y = x - (func(x) / der(x));
    }
    return y;
}

double firstFunction(double x){
    double sum = 0;
    for(int i = 0; i < length; i++){
        if(placement <= placements[i]){
            sum += (tanh((x - priorMeans[i]) / (2 * shorthand(deltas[i])) - 1) / deltas[i]);
        }
        if(placement >= placements[i]){
            sum += (tanh((x - priorMeans[i]) / (2 * shorthand(deltas[i])) + 1) / deltas[i]);
        }
    }
    return sum;
}

double firstFunctionDerivative(double x){
    double sum = 0;
    for(int i = 0; i < length; i++){
        if(placement <= placements[i]){
            sum += (1 / (2 * shorthand(deltas[i]) * deltas[i]
                * pow(cosh(1 - (x - priorMeans[i])/(2*shorthand(deltas[i]))), 2)));
        }
        if(placement >= placements[i]){
            sum += (1 / (2 * shorthand(deltas[i]) * deltas[i]
                * pow(cosh(1 + (x - priorMeans[i])/(2*shorthand(deltas[i]))), 2)));
        }
    }
    return sum;
}

double secondFunction(double x){
    double sum = d[0] * (x - m[0]);
    for(int i = 1; i < length; i++){
        sum += ((d[i] * M_PI * parameter / sqrt(3))
            * tanh((x - m[i]) / (2 * shorthand(parameter))));
    }
    return sum;
}

double secondFunctionDerivative(double x){
    double sum = d[0];
    for(int i = 1; i < length; i++){
        sum += ((d[i] * pow(parameter, 2)) / (2 * pow(shorthand(parameter), 2)
            * pow(cosh((m[i] - x) / (2 * shorthand(parameter))), 2)));
    }
    return sum;
}


JNIEXPORT jdouble JNICALL Java_hr_pmf_project_rating_Event_firstSolveZero(
JNIEnv *env, jobject self, jint _placement, jobject _deltas, jobject _priorMeans,
jobject _placements, jdouble _start){

    jclass arrayList = env->FindClass("java/util/ArrayList");
    jclass integerClass = env->FindClass("java/lang/Integer");
    jclass doubleClass= env->FindClass("java/lang/Double");

    jmethodID arrayListGetId  = env->GetMethodID(arrayList, "get", "(I)Ljava/lang/Object;");
    jmethodID arrayListSizeId = env->GetMethodID(arrayList, "size", "()I");
    jmethodID toInt = env->GetMethodID(integerClass, "intValue", "()I");
    jmethodID toDouble = env->GetMethodID(doubleClass, "doubleValue", "()D");

    length = (env->CallIntMethod(_placements, arrayListSizeId));
    placement = _placement;
    start = _start;

    deltas = (double*)malloc(length * sizeof(double));
    priorMeans = (double*)malloc(length * sizeof(double));
    placements = (long*)malloc(length * sizeof(long));

    for (int i = 0; i < length; ++i) {
        jobject o1 = env->CallObjectMethod(_deltas, arrayListGetId, i);
        deltas[i] = static_cast<double>(env->CallDoubleMethod(o1, toDouble));
        jobject o2 = env->CallObjectMethod(_priorMeans, arrayListGetId, i);
        priorMeans[i] = static_cast<double>(env->CallDoubleMethod(o2, toDouble));
        jobject o3 = env->CallObjectMethod(_placements, arrayListGetId, i);
        placements[i] = static_cast<long>(env->CallIntMethod(o3, toInt));
        env->DeleteLocalRef(o1);
        env->DeleteLocalRef(o2);
        env->DeleteLocalRef(o3);
    }

    double solution = newtonMethod(start, &firstFunction, &firstFunctionDerivative);

    free(deltas);
    free(priorMeans);
    free(placements);
    env->DeleteLocalRef(arrayList);
    env->DeleteLocalRef(integerClass);
    env->DeleteLocalRef(doubleClass);

    return solution;
}


JNIEXPORT jdouble JNICALL Java_hr_pmf_project_rating_Event_secondSolveZero
  (JNIEnv *env, jobject self, jobject _m, jobject _d, jdouble _parameter, jdouble _start){

    jclass arrayList = env->FindClass("java/util/ArrayList");
    jclass doubleClass= env->FindClass("java/lang/Double");

    jmethodID arrayListGetId  = env->GetMethodID(arrayList, "get", "(I)Ljava/lang/Object;");
    jmethodID arrayListSizeId = env->GetMethodID(arrayList, "size", "()I");
    jmethodID toDouble = env->GetMethodID(doubleClass, "doubleValue", "()D");

    length = (env->CallIntMethod(_m, arrayListSizeId));
    parameter = _parameter;
    start = _start;

    d = (double*)malloc(length * sizeof(double));
    m = (double*)malloc(length * sizeof(double));

    for (int i = 0; i < length; ++i) {
        jobject o1 = env->CallObjectMethod(_d, arrayListGetId, i);
        d[i] = static_cast<double>(env->CallDoubleMethod(o1, toDouble));
        jobject o2 = env->CallObjectMethod(_m, arrayListGetId, i);
        m[i] = static_cast<double>(env->CallDoubleMethod(o2, toDouble));
        env->DeleteLocalRef(o1);
        env->DeleteLocalRef(o2);
    }

    double solution = newtonMethod(start, &secondFunction, &secondFunctionDerivative);

    free(d);
    free(m);
    env->DeleteLocalRef(arrayList);
    env->DeleteLocalRef(doubleClass);

    return solution;
}
