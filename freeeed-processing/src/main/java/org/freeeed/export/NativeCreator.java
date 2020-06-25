package org.freeeed.export;

import org.freeeed.services.ProcessingStats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NativeCreator {

    private File metadataFile;
    private String tmpFolder;
    private static volatile NativeCreator mInstance;

    private NativeCreator() {
    }

    public static NativeCreator getInstance() {
        if (mInstance == null) {
            synchronized (NativeCreator.class) {
                if (mInstance == null) {
                    mInstance = new NativeCreator();
                }
            }
        }
        return mInstance;
    }


    public void packNative() {
        ProcessingStats.getInstance().taskIsNative();
/*

        int indexNativeLink = 0, indexStageFile = 0, indexExceptionLink = 0;
        boolean checkingHeader = true;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(metadataFile));
            String line = reader.readLine();
            while (line != null) {
                ArrayList<String> headers = new ArrayList<>();
                Collections.addAll(headers, line.split("\\|"));
                if (checkingHeader) {
                    if (headers.contains("native_link")) {
                        indexNativeLink = headers.indexOf("native_link");
                    }
                    if (headers.contains("exception_link")) {
                        indexExceptionLink = headers.indexOf("exception_link");
                    }
                    if (headers.contains("Source Path")) {
                        indexStageFile = headers.indexOf("Source Path");
                    }
                }
                if (!checkingHeader) {
                    copyNativeFile(indexStageFile, indexNativeLink, headers);
                    copyNativeFile(indexStageFile, indexExceptionLink, headers);
                }
                checkingHeader = false;
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProcessingStats.getInstance().taskIsCompressing();
        ResultCompressor.getInstance().process();
*/
    }

    private void copyNativeFile(int indexStageFile, int indexExceptionLink, ArrayList<String> headers) throws IOException {
/*
        String newFile;
        File f;
        File stage;
        if (!(newFile = headers.get(indexExceptionLink)).equals("")) {
            f = new File(tmpFolder + System.getProperty("file.separator") + newFile);
            stage = new File(headers.get(indexStageFile));
            f.getParentFile().mkdirs();
            FileUtils.copyFile(stage, f);
            ProcessingStats.getInstance().addNativeCopied(stage.length());
        }
*/
    }
}
