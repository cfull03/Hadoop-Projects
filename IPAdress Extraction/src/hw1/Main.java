package hw1;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // Create a Hadoop configuration object
        Configuration conf = new Configuration();

        // Create a new MapReduce job instance
        Job job = Job.getInstance(conf, "2009 IP Request");

        // Set the main class JAR file
        job.setJarByClass(Main.class);

        // Set the input and output paths
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Set the mapper and reducer classes
        job.setMapperClass(IPAddressMapper.class);
        job.setReducerClass(IPAddressReducer.class);

        // Set the output key and value classes
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Wait for the job to complete and exit
        boolean success = job.waitForCompletion(true);
        System.exit(success ? 0 : 1);
    }
}

