package test.de.recrutement.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import test.de.recrutement.activities.MainActivity;

/**
 * Created by Rahul Padaliya on 3/10/2017.
 */
public class CompressFile {

    private static final String TAG = CompressFile.class.getSimpleName();
    /**
     * Now This Code Working For Create Zip (Date 9 Sep 2017)
     * @param zipFileName
     * @param dir
     * @throws Exception
     */
    public static void zipDir(String zipFileName, String dir) throws Exception {
        Log.d("Zip >> ", "Dir >> " + dir);
        File dirObj = new File(dir);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        //System.out.println("Creating : " + zipFileName);
        Log.d("ZIP", "Creating : " + zipFileName);
        addDir(dirObj, out);
        out.close();
    }


    static void addDir(File dirObj, ZipOutputStream out) throws IOException {
        try {


        File[] files = dirObj.listFiles();
        byte[] tmpBuf = new byte[1024];

        for (int i = 0; i < files.length; i++) {
            if (!files[i].getName().endsWith(".zip")) {
                if (files[i].isDirectory()) {
                    addDir(files[i], out);
                    continue;
                }
                FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
                System.out.println(" Adding: " + files[i].getAbsolutePath());
                /**
                 * Here Use + 15 Substring Because We Getting Full (Root) Path Of The Directory
                 * So We Need To Spite That On Questionnaire Directory
                 * And Make Zip Contain(Audio + Images Folder)
                 */
                ZipEntry entry = new ZipEntry(files[i].getAbsolutePath().substring(files[i].getAbsolutePath().lastIndexOf("/Questionnaire") + 15));
                out.putNextEntry(entry/*new ZipEntry(files[i].getAbsolutePath())*/);
                int len;
                while ((len = in.read(tmpBuf)) > 0) {
                    out.write(tmpBuf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
        }
        }catch (Exception e) {
            Log.d(TAG,"<----- Zip Compress Error--->");
            e.printStackTrace();
        }
    }
}
