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
import java.io.IOException;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.freeeed.data.index.LuceneIndex;
import org.freeeed.services.FreeEedUtil;


/**
 * Process email files
 */
public class EmlFileProcessor extends FileProcessor {

    /**
     * Constructor
     * 
     * @param singleFileName
     * @param context
     */
    public EmlFileProcessor(String singleFileName, Context context, LuceneIndex luceneIndex) {
        super(context, luceneIndex);
        setSingleFileName(singleFileName);
    }

    /**
     * Process file
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void process() throws IOException, InterruptedException {
        String emailPath = getSingleFileName();
        String emailName = new File(emailPath).getName();
        // if the file already has an extension - then it is an attachment
        String ext = FreeEedUtil.getExtension(emailName);
        if (ext.isEmpty()) {
            emailName += ".eml";
        } //else {
            //System.out.println("Warning: Processing " + emailName
            //        + ". expected no-extension emails");
        //}
        processFileEntry(emailPath, emailName);
    }

    @Override
    String getOriginalDocumentPath(String tempFile, String originalFileName) {
        String pathToEmail = tempFile.substring(ParameterProcessing.PST_OUTPUT_DIR.length() + 1);
        return new File(pathToEmail).getParent() + File.separator + originalFileName;
    }
}
