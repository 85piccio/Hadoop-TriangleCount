package it.uniroma1.bdc.piccioli.tesi.trianglecount.job1;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		ArrayList<String> cache1 = new ArrayList<String>();


		// first loop and caching
		for (Text u : values) {
			String a = u.toString();
			cache1.add(a);
		}

		
		for (String u : cache1) {
			for (String w : cache1) {
				
				if (!u.equals(w)) {

					Text emit1 = new Text();					
					emit1.set(u.toString() + "$" + w.toString());					
					context.write(key, emit1);
				}
			}
		}

	}
}