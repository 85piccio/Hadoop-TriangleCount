package it.uniroma1.bdc.piccioli.tesi.trianglecount;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper1 extends Mapper<Text, Text, Text, Text> {

	@Override
	protected void map(Text key, Text value, Context context)
			throws IOException, InterruptedException {

//		System.out.print(key);
//		System.out.print(" / ");
//		System.out.print(value);
//		System.out.print("\n");
		
		context.write(key, value);
		context.write(value, key);
	}

}
