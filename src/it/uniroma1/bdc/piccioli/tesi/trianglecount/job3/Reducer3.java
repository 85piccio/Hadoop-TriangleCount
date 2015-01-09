package it.uniroma1.bdc.piccioli.tesi.trianglecount.job3;

import java.io.IOException;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer3 extends Reducer<Text, Text, Text, Text> {
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		Double tot = Double.valueOf(0);
		for(Text value : values){
			tot += Double.parseDouble(value.toString());
		}
		
		context.write(new Text("Tot"), new Text(tot.toString()));
		
	}
}