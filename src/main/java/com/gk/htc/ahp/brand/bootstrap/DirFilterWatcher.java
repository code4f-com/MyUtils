package com.gk.htc.ahp.brand.bootstrap;

import java.io.*;

public class DirFilterWatcher implements FileFilter {

    private final String filter;

    public DirFilterWatcher() {
        this.filter = "";
    }

    /**
     * File ter with file Extention
     *
     * @param filter
     */
    public DirFilterWatcher(String filter) {
        this.filter = filter;
    }

//    public File[] listFiles(File dir) {
//        return dir.listFiles(this);
//    }

//    public File[] listFiles(String path) {
//        if (path == null || path.equals("")) {
//            return null;
//        }
//        File dir = new File(path);
//        if (dir.isDirectory()) {
//            return dir.listFiles(this);
//        } else {
//            return null;
//        }
//    }

    @Override
    public boolean accept(File file) {
        if ("".equals(filter)) {
            return true;    // Return all File
        }
        return (file.getName().endsWith(filter));
    }
}
