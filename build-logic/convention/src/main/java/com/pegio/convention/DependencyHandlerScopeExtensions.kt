package com.pegio.convention

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.scriptImplementation(dependency: Any) {
    "implementation"(dependency)
}

fun DependencyHandlerScope.scriptKapt(dependency: Any) {
    "kapt"(dependency)
}

fun DependencyHandlerScope.scriptKsp(dependency: Any) {
    "ksp"(dependency)
}

fun DependencyHandlerScope.scriptTestImplementation(dependency: Any) {
    "testImplementation"(dependency)
}

fun DependencyHandlerScope.scriptAndroidTestImplementation(dependency: Any) {
    "androidTestImplementation"(dependency)
}

fun DependencyHandlerScope.scriptDebugImplementation(dependency: Any) {
    "debugImplementation"(dependency)
}