package com.longway.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by longway on 16/8/6.
 * Email:longway1991117@sina.com
 */

public class ZipUtils {
    private ZipUtils() {
        throw new AssertionError("ZipUtils not instance");
    }

    public static void unzip(String zip, String outPath) {
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new FileInputStream(new File(zip)));
            ZipEntry zipEntry;
            String name;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                name = zipEntry.getName();
                if (name.contains("../")) {
                    continue;
                }
                if (zipEntry.isDirectory()) {
                    name = name.substring(0, name.length() - 1);
                    File folder = new File(outPath + File.separator + name);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                } else {
                    File file = new File(outPath + File.separator + name);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    int len;
                    byte[] buf = new byte[8192];
                    while ((len = zipInputStream.read(buf)) != -1) {
                        outputStream.write(buf, 0, len);
                        outputStream.flush();
                    }
                    outputStream.close();
                }
                zipInputStream.closeEntry();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zipInputStream);
        }
    }

    private static void zipDir(File dir, String name, ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(name + File.separator);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.closeEntry();
        File[] files = dir.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                zipDir(file, name + File.separator + fileName, zipOutputStream);
            } else {
                zipFile(file, name + File.separator + fileName, zipOutputStream);
            }
        }
    }

    private static void zipFile(File file, String name, ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(name);
        zipOutputStream.putNextEntry(zipEntry);
        int len;
        byte[] buf = new byte[8192];
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        while ((len = inputStream.read(buf)) != -1) {
            zipOutputStream.write(buf, 0, len);
        }
        inputStream.close();
        zipOutputStream.closeEntry();
    }

    private static String[] filter(String... path) {
        final ArrayList<String> dir = new ArrayList<>();
        final ArrayList<String> files = new ArrayList<>();
        for (String p : path) {
            File file = new File(p);
            if (file.isDirectory()) {
                dir.add(p);
            } else {
                files.add(p);
            }
        }
        if (files.isEmpty() || dir.isEmpty()) {
            return path;
        }
        ArrayList<String> filters = new ArrayList<>();
        for (String d : dir) {
            for (String f : files) {
                if (f.startsWith(d)) { // filter /a/b/c /a/b/c/d.txt
                    filters.add(f);
                }
            }
        }
        if (filters.isEmpty()) {
            return path;
        }
        files.removeAll(filters);
        final Object[] _d = dir.toArray();
        final Object[] _f = files.toArray();
        final String[] combine = new String[_d.length + _f.length];
        System.arraycopy(_d, 0, combine, 0, _d.length);
        System.arraycopy(_f, 0, combine, _d.length, _f.length);
        return combine;
    }

    public static void zip(String outPath, String... path) {
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(outPath)));
            final String[] pure = filter(path); // filter
            for (String p : pure) {
                File f = new File(p);
                String fileName = f.getName();
                if (f.isDirectory()) {
                    zipDir(f, fileName, zipOutputStream); // zip dir
                } else {
                    zipFile(f, fileName, zipOutputStream); // zip file
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zipOutputStream);
        }
    }
}
