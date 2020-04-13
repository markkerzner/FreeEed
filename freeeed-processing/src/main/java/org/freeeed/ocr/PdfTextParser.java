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
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.freeeed.services.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;

public class PdfTextParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfTextParser.class);
    private static final Tika TIKA = new Tika();
    private static final String EMPTY = "";
    private static final Project CURRENT_PROJECT = Project.getCurrentProject();
    private static volatile PdfTextParser mInstance;

    private PdfTextParser() {
    }

    public static PdfTextParser getInstance() {
        if (mInstance == null) {
            synchronized (PdfTextParser.class) {
                if (mInstance == null) {
                    mInstance = new PdfTextParser();
                }
            }
        }
        return mInstance;
    }

    public String parseContent(String file) {
        String simpleParse;
        simpleParse = parseText(file);
        if (CURRENT_PROJECT.isOcrEnabled()) {
            if (simpleParse.trim().isEmpty()) {
                LOGGER.info("processing PDF with ocr");
                simpleParse = parseImages(file);
            }
        }
        return simpleParse;
    }

    private String parseText(String filePath) {
        try {
            return TIKA.parseToString(new File(filePath));
        } catch (IOException | TikaException e) {
            LOGGER.info("processing pdf with ocr {}", e.getMessage());
        }
        return EMPTY;
    }

    private String parseImages(String pdfFile) {
        String result = null;
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        instance.setDatapath("tessdata"); // path to tessdata directory
        File file = new File(pdfFile);
        try {
            result = instance.doOCR(file);
        } catch (TesseractException e) {
            LOGGER.error("Problem parsing document {} {}", file, e.getMessage());
        }
        return result;
    }
}
