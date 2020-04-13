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
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.freeeed.services.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * This class parses file with or without embedded documents
 *
 * @author nehaojha
 */
public class PdfTextParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfTextParser.class);
    private static final Tika TIKA = new Tika();
    private static final Parser AUTO_DETECT_PARSER = new AutoDetectParser();
    private static final ParseContext PARSE_CONTEXT = new ParseContext();
    private static final String EMPTY = "";
    private static final Project CURRENT_PROJECT = Project.getCurrentProject();
    private static final String TMP = "/tmp/";
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private static volatile PdfTextParser mInstance;

    //config
    static {
        TesseractOCRConfig config = new TesseractOCRConfig();
        PDFParserConfig pdfConfig = new PDFParserConfig();
        pdfConfig.setExtractInlineImages(true);
        PARSE_CONTEXT.set(TesseractOCRConfig.class, config);
        PARSE_CONTEXT.set(PDFParserConfig.class, pdfConfig);
        PARSE_CONTEXT.set(Parser.class, AUTO_DETECT_PARSER);
    }

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
        String simpleParse = EMPTY;
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
            LOGGER.info("processing pdf with ocr {}", e);
        }
        return EMPTY;
    }

    private String parseImages(String file) {
        try {
            String path = splitPages(file);
            return ocrParallel(path);
        } catch (Exception ex) {
            LOGGER.error("Exception doing ocr ", ex);
        }
        return EMPTY;
    }

    private String ocrParallel(String path) throws IOException, ExecutionException, InterruptedException {
        File root = new File(path);
        File[] files = root.listFiles();
        if (Objects.isNull(files)) {
            return EMPTY;
        }
        int totalFiles = files.length;
        COUNTER.set(0);
        System.out.println(totalFiles);
        for (int i = 0; i <= totalFiles; i++) {
            File file = new File(path + i + ".pdf");
            parseAndPut(totalFiles, file);
        }
        return combineAll(path, totalFiles);
    }

    private void parseAndPut(int totalFiles, File file) {
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        instance.setDatapath("tessdata"); // path to tessdata directory
        try {
            String result = instance.doOCR(file);
            Files.write(Paths.get(file.getPath().replace("pdf", "txt")), Collections.singleton(result));
        } catch (TesseractException | IOException e) {
            LOGGER.error("Problem parsing document {} {}", file, e.getMessage());
        }
        int count = COUNTER.incrementAndGet();
        LOGGER.debug("scanned " + count + " of " + totalFiles + " pages");
    }

    private String combineAll(String path, int totalFiles) throws IOException {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < totalFiles; i++) {
            String filePath = path + i + ".txt";
            Files.readAllLines(Paths.get(filePath))
                    .forEach(line -> content.append(line).append("\n"));
            new File(filePath).delete();
        }
        return content.toString();
    }

    private String splitPages(String filePath) throws IOException {
        File file = new File(filePath);
        String pagePath;
        try (PDDocument document = PDDocument.load(file)) {
            Splitter splitter = new Splitter();
            List<PDDocument> pages = splitter.split(document);
            Iterator<PDDocument> iterator = pages.listIterator();
            int i = 0;
            pagePath = createTempPath();
            //LOGGER.debug("pagePath = " + pagePath);
            while (iterator.hasNext()) {
                PDDocument pd = iterator.next();
                pd.save(pagePath + i++ + ".pdf");
                pd.close();
            }
        }
        return pagePath;
    }

    private String createTempPath() {
        String pagePath = TMP + System.currentTimeMillis() + "/";
        File tempFile = new File(pagePath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        return pagePath;
    }

}
