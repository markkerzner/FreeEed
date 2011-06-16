package org.freeeed.main;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MD5Hash;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class Map extends Mapper<LongWritable, Text, MD5Hash, MapWritable> {
    static int maps = 0;
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String zipFile = value.toString();
        System.out.println("Ready to process file: " + zipFile);
        ZipFileProcessor processor = new ZipFileProcessor(zipFile, context);
        processor.process();

    }
}
