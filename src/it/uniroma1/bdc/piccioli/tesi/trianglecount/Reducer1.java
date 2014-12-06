package it.uniroma1.bdc.piccioli.tesi.trianglecount;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		ArrayList<String> cache1 = new ArrayList<String>();
//		ArrayList<String> cache2 = new ArrayList<String>();


		// first loop and caching
		for (Text u : values) {
			String a = u.toString();
//			System.out.println("add: " + a);
			cache1.add(a);
//			cache2.add(a);
		}

		
		for (String u : cache1) {
			for (String w : cache1) {

//				 System.out.print(u);
//				 System.out.print(" / ");
//				 System.out.print(w);
//				 System.out.print("\n");

				if (!u.equals(w)) {

//					System.out.println("if");

					Text emit1 = new Text();
					Text emit2 = new Text();
					emit1.set(u.toString() + "$" + w.toString());
					emit2.set(w.toString() + "$" + u.toString());

//					System.out.println(emit1.toString());
//					System.out.println(emit2.toString());

					context.write(key, emit1);
					context.write(key, emit2);
				}
			}
		}

	}
}