package org.freeeed.Processor;

import org.apache.tika.Tika;

class TikaAdapter {

    private Tika tika;

    private static volatile TikaAdapter mInstance;

    private TikaAdapter() {
        tika = new Tika();
        tika.setMaxStringLength(10 * 1024 * 1024);
    }

    public static TikaAdapter getInstance() {
        if (mInstance == null) {
            synchronized (TikaAdapter.class) {
                if (mInstance == null) {
                    mInstance = new TikaAdapter();
                }
            }
        }
        return mInstance;
    }

    public Tika getTika() {
        return tika;
    }
}
