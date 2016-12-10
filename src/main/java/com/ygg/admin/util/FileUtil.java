package com.ygg.admin.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil
{
    public static void deleteFile(String path)
    {
        try
        {
            File file = new File(path);
            if (!file.isDirectory())
            {
                file.delete();
            }
            else if (file.isDirectory())
            {
                File[] fs = file.listFiles();
                for (File f2 : fs)
                {
                    if (f2.isDirectory())
                    {
                        deleteFile(f2.getAbsolutePath());
                    }
                    else
                    {
                        f2.delete();
                    }
                }
                file.delete();
            }
            return;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
    }
    
    public static byte[] getBytesFromFile(File f)
    {
        if (f == null)
        {
            return null;
        }
        try
        {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
            {
                out.write(b, 0, n);
            }
            stream.close();
            out.close();
            return out.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
