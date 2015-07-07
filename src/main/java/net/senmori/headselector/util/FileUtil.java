package net.senmori.headselector.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil 
{
	public static void copy(InputStream in, File file) 
	{
        try 
        {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[4096];
            int len = 0;
            
            while((len = in.read(buf)) != -1)
            {
                out.write(buf,0,len);
            }
            
            out.close();
            in.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}