/*
 *
 * Copyright SHMsoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.freeeed.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * This class parses file with or without embedded documents
 *
 * @author nehaojha
 */
public class ImageTextParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTextParser.class);
    private static final String EMPTY = "";
    private static volatile ImageTextParser mInstance;

    private ImageTextParser() {
    }

    public static ImageTextParser getInstance() {
        if (mInstance == null) {
            synchronized (ImageTextParser.class) {
                if (mInstance == null) {
                    mInstance = new ImageTextParser();
                }
            }
        }
        return mInstance;
    }

    public String parseImages(String file) {
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        instance.setDatapath("tessdata"); // path to tessdata directory
        try {
            File File = new File(file);
            return instance.doOCR(File);
        } catch (TesseractException e) {
            LOGGER.error("Error during image ORC {}", e.getMessage());
        }
        return EMPTY;
    }
}
