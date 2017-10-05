//package com.taxprinter.boot
//
//import com.google.inject.Injector
//import com.google.inject.Module
//import com.google.inject.Stage
//import com.hubspot.dropwizard.guice.InjectorFactory
//import com.netflix.governator.guice.LifecycleInjector
//
//
//class GovernatorInjectorFactory : InjectorFactory {
//    override fun create(stage: Stage, modules: List<Module>): Injector {
//        return LifecycleInjector.builder().inStage(stage).withModules(modules).build()
//                .createInjector()
//    }
//}