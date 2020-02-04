package com.cj.demo

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter;

public class CrashDefenseTransform extends Transform {
    @Override
    String getName() {
        return getClass().getSimpleName()
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        //目标输入为CLASS
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        //目标范围为整个工程
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException,
            IOException {
        super.transform(transformInvocation)
        //输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
        Collection<TransformInput> inputs = transformInvocation.getInputs()
        //OutputProvider管理输出路径，如果输入为空，你会发现OutputProvider == null
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()
        //遍历输入
        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                //目标输出文件
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR)
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                transformJar(jarInput.getFile(), dest)
            }
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                //目标输出文件
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
                        Format.DIRECTORY)
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                transformDir(directoryInput.getFile(), dest)
            }
        }
    }

    @Override
    boolean isIncremental() {
        return false
    }

    private static void transformJar(File input, File dest) {
        FileUtils.copyFile(input, dest)
    }

    private static void transformDir(File input, File dest) {
        if (dest.exists()) {
            FileUtils.forceDelete(dest)
        }
        FileUtils.forceMkdir(dest)
        String srcDirPath = input.getAbsolutePath()
        String destDirPath = dest.getAbsolutePath()
        for (File file : input.listFiles()) {
            String destFilePath = file.absolutePath.replace(srcDirPath, destDirPath)
            File destFile = new File(destFilePath)
            if (file.isDirectory()) {
                transformDir(file, destFile)
            } else if (file.isFile()) {
                FileUtils.touch(destFile)
                transformSingleFile(file, destFile)
            }
        }
    }

    private static void transformSingleFile(File input, File dest) {
        weave(input.getAbsolutePath(), dest.getAbsolutePath())
    }

    /**
     * 对.class文件进行编辑
     * @param inputPath 输入的.class文件
     * @param outputPath 输出的.class文件
     */
    private static void weave(String inputPath, String outputPath) {
        try {
            FileInputStream is = new FileInputStream(inputPath)
            //格式化.class文件为ClassReader
            ClassReader cr = new ClassReader(is)
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS)
            CrashDefenseClassVisitor classVisitor = new CrashDefenseClassVisitor(cw);
            //通过ClassVisitor编辑.class的字节码
            cr.accept(classVisitor, 0)
            FileOutputStream fos = new FileOutputStream(outputPath)
            fos.write(cw.toByteArray())
            fos.close()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }
}
