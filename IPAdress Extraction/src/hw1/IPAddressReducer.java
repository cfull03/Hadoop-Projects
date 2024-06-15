package hw1;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddressReducer extends Reducer<Text, Text, Text, Text> {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile("(\\d{2}/\\w{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2})");

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        long earliestTimestamp = Long.MAX_VALUE;
        String earliestDate = "";

        // Iterate through all timestamps for this IP address
        for (Text value : values) {
            String timestampStr = value.toString();
            // Remove the timezone offset
            timestampStr = getTime(timestampStr);

            // Parse the timestamp
            try {
                Date timestamp = DATE_FORMAT.parse(timestampStr);
                long timestampMillis = timestamp.getTime();
                if (timestampMillis < earliestTimestamp && is2009(timestamp)) {
                    earliestTimestamp = timestampMillis;
                    earliestDate = DATE_FORMAT.format(timestamp); // Format the timestamp
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Emit the IP address along with its earliest request time in 2009
        if (!earliestDate.isEmpty()) {
            context.write(key, new Text(earliestDate));
        }
    }

    // Method to Extract the time from the log entry
    private String getTime(String timestamp) {
        Matcher matcher = TIMESTAMP_PATTERN.matcher(timestamp);
        if (matcher.find()) {
            return matcher.group(1); // Extract timestamp without timezone offset
        }
        return timestamp; // Return original timestamp if pattern not found
    }

    // Method to check if the year on the log entry is in 2009
    private boolean is2009(Date timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        int year = calendar.get(Calendar.YEAR);
        return year == 2009;
    }
}




