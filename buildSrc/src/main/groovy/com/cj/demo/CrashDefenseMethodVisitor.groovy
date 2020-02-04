package com.cj.demo

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes


public class CrashDefenseMethodVisitor extends MethodVisitor {

    CrashDefenseMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor)
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (owner.equals("android/graphics/Color") && name.equals("parseColor")) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/cj/demo/CrashDefense", "parseColor",
                    "(Ljava/lang/String;)I", false);
        } else {
            super.visitMethodInsn(opcode, owner, name, desc, itf)
        }
    }
}
