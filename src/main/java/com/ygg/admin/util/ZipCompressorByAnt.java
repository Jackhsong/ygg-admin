package com.ygg.admin.util;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class ZipCompressorByAnt
{
    private File zipFile;
    
    private static final Project DEFAULT_PROJECT = new Project();
    
    /**
     * 压缩文件构造函数
     * 
     * @param pathName 最终压缩生成的压缩文件：目录+压缩文件名.zip
     */
    public ZipCompressorByAnt(String finalFile)
    {
        zipFile = new File(finalFile);
    }
    
    /**
     * 执行压缩操作
     * 
     * @param srcPathName 需要被压缩的文件/文件夹
     */
    public void compressExe(String srcPathName)
    {
        File srcdir = new File(srcPathName);
        if (!srcdir.exists())
        {
            throw new RuntimeException(srcPathName + "不存在！");
        }
        
        Project prj = new Project();
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(srcdir);
        // fileSet.setIncludes("**/*.java"); //包括哪些文件或文件夹
        // eg:zip.setIncludes("*.java");
        // fileSet.setExcludes(...); //排除哪些文件或文件夹
        zip.addFileset(fileSet);
        zip.execute();
    }
    
    public static void unZip(File orgin, File dest)
    {
        
        Expand expand = new Expand();
        expand.setProject(DEFAULT_PROJECT);
        expand.setSrc(orgin);
        expand.setDest(dest);
        expand.execute();
        
    }
    
    public static void zip(File orgin, File dest)
    {
        
        Zip zip = new Zip();
        zip.setProject(DEFAULT_PROJECT);
        zip.setDestFile(dest);
        
        FileSet fs = new FileSet();
        fs.setProject(DEFAULT_PROJECT);
        fs.setDir(orgin);
        
        zip.addFileset(fs);
        zip.execute();
        
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        File orgin = new File("F:\\zip\\Pictures.zip");
        File dest = new File("f:\\zip\\");
        unZip(orgin, dest);
        System.out.println("----------un zip -----------");
        
        File zip = new File("f:\\zip\\test.zip");
        System.out.println("----------zip starting-----------");
        zip(dest, zip);
        System.out.println("----------zip success-----------");
    }
}
