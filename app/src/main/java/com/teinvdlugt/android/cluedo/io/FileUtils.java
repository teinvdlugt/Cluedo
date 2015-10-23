package com.teinvdlugt.android.cluedo.io;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class FileUtils {
    public static void saveFile(Context context, String fileName, String file) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            StringBuilder file = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                file.append(line).append("\n");
            }
            return file.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
