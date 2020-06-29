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
package org.freeeed.Processor;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.lowagie.text.Meta;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Message;
import org.apache.tika.metadata.Metadata;
import org.freeeed.Entity.ProjectFile;
import org.freeeed.mail.EmailDataProvider;
import org.freeeed.mail.EmailUtil;
import org.freeeed.mail.EmlParser;
import org.freeeed.main.DiscoveryFile;
import org.freeeed.main.DocumentMetadata;
import org.freeeed.main.DocumentMetadataKeys;
import org.freeeed.services.ContentTypeMapping;
import org.freeeed.services.JsonParser;
import org.freeeed.util.Util;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;


/**
 * Process email files
 */
//TODO: Use mime message instead of tika?
//TODO: CLEAN UP!
public class EmlFileProcessor extends FileProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmlFileProcessor.class);

    private static final String DOCUMENT_ORIGINAL_PATH = "document_original_path";
    private static final String DOCUMENT_PARENT = "document_parent";
    private static final String DOCUMENT_TEXT = "text";
    private static final String HAS_ATTACHMENTS = "has_attachments";
    private static final String HAS_PARENT = "has_parent";
    private static final String HASH = "Hash";
    //    private static final String PROCESSING_EXCEPTION = "processing_exception";
    private static final String MASTER_DUPLICATE = "master_duplicate";
    private static final String CUSTODIAN = "Custodian";
    private static final String LINK_NATIVE = "native_link";
    private static final String TEXT_LINK = "text_link";
    private static final String LINK_EXCEPTION = "exception_link";
    private static final String SUBJECT = "subject";
    private static final String MESSAGE_FROM = "Message-From";
    private static final String MESSAGE_CREATION_DATE = "Creation-Date";
    private static final String MESSAGE_TO = "Message-To";
    private static final String MESSAGE_CC = "Message-Cc";
    private static final String DATE = "date";
    private static final String DATE_RECEIVED = "Date Received";
    private static final String TIME_RECEIVED = "Time Received";
    private static final String DATE_SENT = "Date Sent";
    private static final String TIME_SENT = "Time Sent";
    public static final String UNIQUE_ID = "UPI";
    private static final String MESSAGE_ID = "message_id";
    private static final String REFERENCES = "references";
    private static final String FILETYPE = "File Type";

    public EmlFileProcessor(ProjectFile projectFile) {
        super(projectFile);
    }

    private static final ContentTypeMapping CONTENT_TYPE_MAPPING = new ContentTypeMapping();

    private void parseDateTimeFields(String date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            Date dateObj = df.parse(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateOnly = dateFormatter.format(dateObj);

            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
            timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String timeOnly = timeFormatter.format(dateObj);

            metadata.set(DATE, dateOnly);
            metadata.set(DATE_RECEIVED, dateOnly);
            metadata.set(TIME_RECEIVED, timeOnly);
        } catch (Exception e) {
            LOGGER.error("Problem extracting date time fields" + e.toString());
        }
    }

    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    @Override
    public void run() {

 /*
        try {
            Properties properties = System.getProperties();
            Session session = Session.getInstance(properties, null);
            InputStream i = new FileInputStream(projectFile.getFile());
            MimeMessage mimeMessage = new MimeMessage(session, i);
            System.out.println(mimeMessage.getFrom()[0].toString());
            System.out.println(mimeMessage.getSubject());
            if (mimeMessage.getContentType().contains("multipart")) {
                Multipart multiPart = (Multipart) mimeMessage.getContent();
                for (int k = 0; k < multiPart.getCount(); k++) {
                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(k);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        System.out.println(part.getFileName());
                       // part.saveFile("E:\\a\\" + part.getFileName());
                    }
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/


        try {
            inputStream = TikaInputStream.get(projectFile.getFile().toPath());
            text = TikaAdapter.getInstance().getTika().parseToString(inputStream, metadata);

            // EmlParser emlParser = new EmlParser(projectFile.getFile());
/*
            EmlParser emlParser = new EmlParser(eml);

            if (emlParser.getFrom() != null) {
                metadata.set(Message.MESSAGE_FROM, EmailUtil.getAddressLine(emlParser.getFrom()));
            }

            if (emlParser.getSubject() != null) {
                metadata.set(SUBJECT, EmailUtil.getAddressLine(emlParser.getFrom()));
            }

            if (emlParser.getMessageId() != null) {
                metadata.set(MESSAGE_ID, emlParser.getMessageId());
            }

            if (emlParser.getReferencedMessageIds() != null) {
                metadata.set(REFERENCES, StringUtils.join(emlParser.getReferencedMessageIds(), ", "));
            }

            if (emlParser.getTo() != null) {
                metadata.set(MESSAGE_TO, EmailUtil.getAddressLine(emlParser.getTo()));
            }

            if (emlParser.getCC() != null) {
                metadata.set(MESSAGE_CC, EmailUtil.getAddressLine(emlParser.getCC()));
            }

            if (emlParser.getDate() != null) {
                metadata.set(MESSAGE_CREATION_DATE, formatDate(emlParser.getDate()));
            }

            if (emlParser.getDate() != null) {
                parseDateTimeFields(emlParser.getDate().toString());
            } else {
                parseDateTimeFields(emlParser.getSentDate().toString());
            }
           */
        } catch (Exception e) {
            LOGGER.error("Error EML {}", projectFile.getFile().getName());
        }
        writeMetadata();
    }

}