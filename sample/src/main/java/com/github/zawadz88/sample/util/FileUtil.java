package com.github.zawadz88.sample.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author Piotr Zawadzki (Piotr.Zawadzki@stepstone.com)
 */
public class FileUtil {

    public static File copyFileFromAssetsToCacheDirectory(Context context, String fileName) {
        File result = new File(context.getCacheDir(), fileName);
        if (!result.exists()) {
            try {

                InputStream is = context.getAssets().open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                FileOutputStream fos = new FileOutputStream(result);
                fos.write(buffer);
                fos.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}
