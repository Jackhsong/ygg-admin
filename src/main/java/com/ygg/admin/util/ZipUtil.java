package com.ygg.admin.util;

import com.google.common.io.Files;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil
{
    private static String imagePostfix = "(.jpg)|(.png)|(.gif)|(.bmp)|(.jpeg)";
    
    public static Map<String, Object> readImageZipFile(File file)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int status = 1;
        String msg = "";
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        int total = 0;
        List<Integer> fileNameList = new ArrayList<Integer>();
        boolean isOk = true;
        while ((ze = zin.getNextEntry()) != null)
        {
            if (ze.isDirectory())
            {
                status = 0;
                msg = "压缩文件中不能包含子文件夹";
                break;
            }
            else
            {
                total++;
                if (total > 29)
                {
                    status = 0;
                    msg = "图片数量最大为29个";
                    isOk = false;
                    break;
                }
                String fileName = ze.getName();
                long fileSize = ze.getSize() / 1024;
                if (fileSize > 510)
                {
                    status = 0;
                    msg = "图片大小不得超过400KB";
                    isOk = false;
                    break;
                }
                int index = fileName.lastIndexOf(".");
                String postfix = fileName.substring(index).toLowerCase();
                String fileNameWithoutPostfix = fileName.substring(0, index);
                if (!Pattern.matches(imagePostfix, postfix))
                {
                    status = 0;
                    msg = "图片后缀只允许(*.jpg)|(*.png)|(*.gif)|(*.bmp)|(*.jpeg)";
                    isOk = false;
                    break;
                }
                if (!Pattern.matches("\\d+", fileNameWithoutPostfix))
                {
                    status = 0;
                    msg = "图片名字只能是数字。";
                    isOk = false;
                    break;
                }
                if (!StringUtils.isNumeric(fileNameWithoutPostfix) || (Integer.parseInt(fileNameWithoutPostfix) > 29)
                    || (Integer.parseInt(fileNameWithoutPostfix) < 1))
                {
                    status = 0;
                    msg = "图片文件名有误：" + fileName;
                    isOk = false;
                    break;
                }
                if (fileNameList.contains(fileNameWithoutPostfix))
                {
                    status = 0;
                    msg = "图片文件名重复：" + fileName;
                    isOk = false;
                    break;
                }
                fileNameList.add(Integer.parseInt(fileNameWithoutPostfix));
            }
        }
        if (isOk)
        {
            for (Integer it : fileNameList)
            {
                if (it >= 2 && it <= 5)
                {
                    if (!fileNameList.contains(it - 1))
                    {
                        status = 0;
                        msg = "文件名1-5必须连续。";
                        break;
                    }
                }
                if (it >= 9 && it <= 29)
                {
                    if (!fileNameList.contains(it - 1))
                    {
                        status = 0;
                        msg = "文件名8-29必须连续。";
                        break;
                    }
                }
            }
        }
        result.put("status", status);
        result.put("msg", msg);
        zin.closeEntry();
        zin.close();
        return result;
    }

    /**
     *
     * @param zipFileNameWithPath  zip文件的名字 包含路径  如 /tmp/xx.zip
     */
    public static File zipFilesByAnt(String zipFileNameWithPath, List<File> files)
            throws IOException{
//        String downloadBasePath = YggAdminProperties.getInstance().getPropertie("order_zip_download_dir");
//        if(StringUtils.isEmpty(downloadBasePath))
        String downloadBasePath = ".";
        File downloadDir = new File(downloadBasePath);
        File dir = new File(downloadDir, "zip"+UUID.randomUUID().toString().substring(0,10));
        dir.mkdir();
        String basePath = dir.getAbsolutePath().endsWith("/") ? dir.getAbsolutePath() : dir.getAbsolutePath() + "/";
        for(File file : files){
            Files.copy(file, new File(basePath + file.getName()));
        }
        ZipCompressorByAnt zca = new ZipCompressorByAnt(zipFileNameWithPath);
        zca.compressExe(dir.getAbsolutePath());
        FileUtil.deleteFile(dir.getAbsolutePath());
        return new File(zipFileNameWithPath);
    }


}
