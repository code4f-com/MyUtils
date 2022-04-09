package com.tuanpla.bootstrap;

import java.io.*;
import java.util.*;

public abstract class DirWatcher extends TimerTask {

    private String path;
//    private File filesArray[];
    private final HashMap dir = new HashMap();
    private FileFilter dfw;

    public static enum ACTION {
        add, modify, delete;
    }

    /**
     * Watch all File and Folder
     *
     * @param path
     */
    public DirWatcher(String path) {
        this(path, "");
    }

    public DirWatcher(String path, String filter) {
        this.path = path;
        dfw = new DirFilterWatcher(filter);
        File[] fArrStart = new File(path).listFiles(dfw);
        if (fArrStart != null && fArrStart.length > 0) {
            for (File oneFile : fArrStart) {
                dir.put(oneFile, oneFile.lastModified());
            }
        }
    }

    @Override
    public final void run() {
        try {
            Set checkedFiles = new HashSet();
            File[] fArrCurrent = new File(path).listFiles(dfw);

            for (File oneFile : fArrCurrent) {
                Long current = (Long) dir.get(oneFile);
                checkedFiles.add(oneFile);
                if (current == null) {
                    // new file
                    dir.put(oneFile, oneFile.lastModified());
                    onChange(oneFile, ACTION.add.name());
                } else if (current != oneFile.lastModified()) {
                    // modified file
                    dir.put(oneFile, oneFile.lastModified());
                    onChange(oneFile, ACTION.modify.name());
                } else {
                    // File is not change
                }
            }
            // now check for deleted files
            Set ref = ((HashMap) dir.clone()).keySet();
            ref.removeAll(checkedFiles);
            Iterator it = ref.iterator();
            while (it.hasNext()) {
                File deletedFile = (File) it.next();
                dir.remove(deletedFile);
                onChange(deletedFile, ACTION.delete.name());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected abstract void onChange(File file, String action);

//    public static void main(String[] args) {
//        DirWatcher dw = new DirWatcher("D:\\DevDirWatch", "") {
//            @Override
//            protected void onChange(File file, String action) {
//                System.out.println("File name: [" + file.getName() + "] is " + action);
//            }
//        };
//        long period = 5000L;
//        Timer timer = new Timer("Timer");
//        timer.schedule(dw, 0, period);
//    }
}
