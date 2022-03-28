package com.gk.htc.ahp.brand.bootstrap;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

public class Bootstrap {

    private final String CLASS_TO_EXEC = "com.gk.htc.ahp.brand.app.AppStart";
    private final String methodExec = "main";
    ClassLoader cl;
    Class<?> mainClass;

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start(args);
    }

    public void start(final String[] argsExec) throws Exception {
        this.cl = this.loadLib();
        if (cl != null) {
            final Class<?>[] classes = new Class[]{argsExec.getClass()};
            final Object[] methodArgs = new Object[]{argsExec};
            mainClass = cl.loadClass(CLASS_TO_EXEC);
            final Method method = mainClass.getMethod(methodExec, classes);
            Runnable execer = new Runnable() {
                @Override
                public void run() {
                    try {
                        method.invoke(null, methodArgs);
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            };
            Thread bootstrapper = new Thread(execer, "main");
            bootstrapper.setContextClassLoader(cl);
            bootstrapper.start();
        }
        File parent = LibLoader.findBootstrapHome();
        System.out.println("parent:" + parent.getAbsolutePath());
        String bundle_dir = parent.getParentFile().getPath() + File.separator + "bundles";
        System.out.println("bundle_dir :" + bundle_dir);
    }

    private ClassLoader loadLib() {
        File parent = LibLoader.findBootstrapHome();
        if (parent != null) {
            System.out.println("Parent Path:" + parent.getPath());
            String lib_dir = parent.getPath() + File.pathSeparator
                    + parent.getParentFile().getPath() + File.separator + "bundles";
//        lib_dir = lib_dir + File.pathSeparator + parent.getParentFile().getPath() + File.separator + "config";
            System.out.println("lib Path:" + lib_dir);
            System.out.println("************AAA*****************");
            try {
                ClassLoader _cl = LibLoader.loadClasses(lib_dir, false);
                return _cl;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
