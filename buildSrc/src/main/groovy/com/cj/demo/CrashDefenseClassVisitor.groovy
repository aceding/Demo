package com.cj.demo

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes;

class CrashDefenseClassVisitor extends ClassVisitor {

    String clsName;

    CrashDefenseClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM6, cv);
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        //记录输入的class的name
        clsName = name
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
        //如果是替换为的方法，不能再替换
        if (clsName.equals("com/cj/demo/CrashDefense")) {
            return mv;
        }
        //使用MethodVisitor对Method进行编辑
        return new CrashDefenseMethodVisitor(mv);
    }
}
