package com.cj.demo

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project;

public class CrashDefensePlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        AppExtension appExtension = project.getExtensions().getByType(AppExtension)
        appExtension.registerTransform(new CrashDefenseTransform());
    }

}
