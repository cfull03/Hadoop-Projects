package hw1;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class IPAddressMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private static final Pattern LOG_PATTERN = Pattern.compile("^(\\S+) .*?\\[(\\S+\\s+-\\d{4})].*");

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
    	
        String line = value.toString();
        
        for(String entry : line.split("\n")){
        	// Creating a regular expression to extract the IP address and timestamp
        	// using a more precise pattern to ensure correct extraction
        	Matcher matcher = LOG_PATTERN.matcher(entry);

        	if (matcher.find()) {
        		String ip = matcher.group(1);
        		String timestamp = matcher.group(2);
        		context.write(new Text(ip), new Text(timestamp));
        	}
        }
    }
}




