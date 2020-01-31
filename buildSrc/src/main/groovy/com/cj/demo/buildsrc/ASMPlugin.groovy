package com.cj.demo.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project;

/**
 * <description>
 *
 * @author xj
 * @date 2020/1/30
 */
public class ASMPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        println("====================")
        println("hello world!")
        println("====================")
    }
}
