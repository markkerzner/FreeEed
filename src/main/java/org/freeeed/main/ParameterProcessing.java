/*    
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
package org.freeeed.main;

import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.freeeed.services.History;

/**
 * Default application parameters
 *
 * @author mark
 */
public class ParameterProcessing {

    public static final String DEFAULT_PARAMETER_FILE = "config/default.freeeed.properties";
    public static final String CURRENT_DIR = "current-dir";
    public static final String RECENT_PROJECTS = "recent-projects";
    public static final String NEW_PROJECT_NAME = "new-project-name";
    public static final String GIGS_PER_ZIP_STAGING = "gigs-per-zip-staging";
    //public static final String S3BUCKET = "s3bucket";
    public static final String LAST_PROJECT_CODE = "last-project-code";
    public static final String PROJECT_CODE = "project-code";
    public static final String PROJECT_NAME = "project-name";
    public static final String PROJECT_FILE_NAME = "project-file-name";
    public static final String PROJECT_FILE_PATH = "project-file-path";
    public static final String PROJECT_INPUTS = "input";
    public static final String PROJECT_CUSTODIANS = "custodian";
    public static final String PROCESS_WHERE = "process-where";
    public static final String FILE_SYSTEM = "file-system";
    public static final String STAGE = "stage";
    public static final String CULLING = "culling";
    public static final String CONTENT = "content";
    public static final String TITLE = "title";
    public static final String NATIVE = "native";
    public static final String PDF_FOLDER = "pdf";
    public static final String NATIVE_AS_PDF = "native-as-pdf";
    public static final String TEXT = "text";
    public static final String OUTPUT_DIR = "freeeed-output";
    public static final String OUTPUT_DIR_HADOOP = "output-dir-hadoop";
    public static final String TMP_DIR = "tmp" + File.separator;
    // tmp dir for Hadoop environment - which means Unix, will also work on EC2
    // TODO think this through
    // public static final String TMP_DIR_HADOOP = "/mnt/tmp/shm";
    public static final String TMP_DIR_HADOOP = TMP_DIR;
    public static final String DOWNLOAD_DIR = "data_downloads";
    public static final String PST_OUTPUT_DIR = "pst_output";
    public static final String NSF_OUTPUT_DIR = "nsf_output";
    public static final String USE_JPST = "use_jpst";
    public static final String CREATE_PDF = "create-pdf";
    public static final String PROJECT = "project";
    public static final String WORK_AREA = "/freeeed_work_area";
    public static final String METADATA_OPTION = "metadata";
    public static final String FIELD_SEPARATOR = "field-separator";
    public static final String METADATA_FILE = "metadata-file";
    public static final String METADATA_FILE_EXT = ".txt";
    public static final String HADOOP_DEBUG = "hadoop-debug";
    public static final String MULTIPLE_CLUSTERS = "multiple-clusters";
    public static final String NUMBER_CLUSTERS = "number-clusters";
    public static final String SKIP = "skip";
    public static final String RUN_PARAMETERS_FILE = "run-parameters-file";
    public static final String RUN = "run";
    public static final String REMOVE_SYSTEM_FILES = "remove-system-files";
    public static final String METADATA_COLLECTION = "metadata";
    public static final String TEXT_IN_METADATA = "text-in-metadata";
    public static final String ACCESS_KEY_ID = "access-key-id";
    public static final String SECRET_ACCESS_KEY = "secret-access-key";
    public static final String PROJECT_BUCKET = "project-bucket";
    public static final String SECURITY_GROUP = "security-group";
    public static final String KEY_PAIR = "key-pair";
    public static final String PEM_CERTIFICATE_NAME = "freeeed.pem";
    public static final String CLUSTER_SIZE = "cluster-size";
    public static final String INSTANCE_TYPE = "instance-type";
    public static final String CLUSTER_USER_NAME = "ubuntu";
    public static final String AVAILABILITY_ZONE = "availability-zone";       
    public static long ONE_GIG = 1073741824L;
    public static final String NL = System.getProperty("line.separator");
    public static final char TM = '\u2122';
    public static final String CLUSTER_TIMEOUT = "cluster-timeout";
    public static final String MANUAL_PAGE = "manual-page";
    public static final String APP_NAME = "FreeEed";
    public static final String DEFAULT_SETTINGS = "settings.properties";
    public static final String SETTINGS_STR = "settings-string";
    public static final String NUM_REDUCE = "number-reducers";
    public static final String DOWNLOAD_LINK = "download-link";
    public static final String ITEMS_PER_MAPPER = "items-per-mapper";
    public static final String BYTES_PER_MAPPER = "bytes-per-mapper";
    public static final String LOAD_BALANCE = "load_balance";
    public static final String CLUSTER_AMI="ami";
    public static final String OCR_ENABLED="ocr_enabled";
    public static final String OCR_OUTPUT="ocr_output";
    public static final String LUCENE_INDEX_DIR="lucene_index";
    public static final String LUCENE_FS_INDEX_ENABLED="lucene_fs_index_enabled";
    public static final String SEND_INDEX_SOLR_ENABLED="send_index_solr_enabled";
    public static final String SOLR_ENDPOINT="solr_endpoint";
    public static final String EXTERNAL_PROCESSING_MACHINE_ENDPOINT="ep_endpoint";
    public static final String NO_IMAGE_FILE = "no_photo.gif";
    public static final String NO_PDF_IMAGE_FILE = "no_pdf_image.pdf";
    public static final String EML_HTML_TEMPLATE_FILE = "eml_html_template.html";
    public static final String SKIP_INSTANCE_CREATION = "skip_instance_creation";
    public static final String OCR_MAX_IMAGES_PER_PDF = "ocr_max_images_per_pdf";
    
    public static final String SOLRCLOUD_REPLICA_COUNT = "solrcloud_replica_count";
    public static final String SOLRCLOUD_SHARD_COUNT = "solrcloud_shard_count";
    
    /**
     * Custom configuration / processing parameters
     *
     * @param customParametersFile file path of properties file
     * @return
     */
    public static Configuration collectProcessingParameters(String customParametersFile) {

        // apache.commons configuration class
        CompositeConfiguration cc = new CompositeConfiguration();

        try {
            // custom parameter file is first priority
            if (customParametersFile != null) {
                // read file
                Configuration customProperties = new SHMcloudConfiguration(customParametersFile);
                // add to configuration
                cc.addConfiguration(customProperties);
            }

            // default parameter file is last priority

            // read file
            Configuration defaults = new SHMcloudConfiguration(DEFAULT_PARAMETER_FILE);
            // add to configuration
            cc.addConfiguration(defaults);

            // set project file name
            cc.setProperty(PROJECT_FILE_NAME, customParametersFile);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            // follow the "fail-fast" design pattern
            System.exit(0);
        }
        return cc;
    }

    /**
     * Default configuration / processing parameters
     *
     * @return
     */
    public static Configuration setDefaultParameters() {
        CompositeConfiguration cc = new CompositeConfiguration();
        try {
            Configuration defaults = new SHMcloudConfiguration(DEFAULT_PARAMETER_FILE);
            cc.addConfiguration(defaults);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            // follow the "fail-fast" design pattern
            System.exit(0);
        }
        return cc;
    }

    /**
     * Echo configuration, save configuration, and update application log
     *
     * @param configuration processing parameters
     * @throws ConfigurationException
     * @throws MalformedURLException
     */
    public static void echoProcessingParameters(Configuration configuration)
            throws ConfigurationException, MalformedURLException {
        SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyMMdd_HHmmss");
        String runParameterFileName = "freeeed.parameters."
                + fileNameFormat.format(new Date()) + ".project";

        // save configuration
        SHMcloudConfiguration configToSave = new SHMcloudConfiguration();
        configToSave.cleanup();
        configToSave.append(configuration);
        configToSave.setProperty("processed_by ", Version.getVersionAndBuild());
        String paramPath = SHMcloudLogging.logDir + "/" + runParameterFileName;
        configToSave.save(paramPath);
        configToSave.restore();

        // update application log
        History.appendToHistory("Processing parameters were saved to " + paramPath);
        configuration.setProperty(ParameterProcessing.RUN_PARAMETERS_FILE, paramPath);
    }
}
