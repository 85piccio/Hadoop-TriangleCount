package it.uniroma1.bdc.piccioli.tesi.trianglecount.job2;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer2 extends Reducer<Text, Text, Text, Text> {
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		boolean isOk = false;

		ArrayList<String> cache1 = new ArrayList<String>();

		// first loop and caching
		for (Text u : values) {
			String a = u.toString();
			cache1.add(a);
		}

		for (String u : cache1) {// check se è presente $ tra i values
			if (u.equals("£"))
				isOk = true;
		}

		if (isOk) {

			float T = 0;

			for (String u : cache1) {
				if (!u.equals("£")) {
					T++;
				}
			}

			float triangle = T / 6;
			if (triangle != 0)
				context.write(key, new Text(new Float(triangle).toString()));
		}
	}
}