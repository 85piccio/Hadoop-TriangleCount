package it.uniroma1.bdc.piccioli.tesi.trianglecount;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper2 extends Mapper<Text, Text, Text, Text> {

	@Override
	protected void map(Text key, Text value, Context context)
			throws IOException, InterruptedException {
		
		if(value.toString().contains("$")){ //input round precedente
			context.write(value,key);
			System.out.println(value + " "+ key);
		}else{//input originale
			Text keuw = new Text();
			Text kewu = new Text();
			
			keuw.set(key.toString() + "$" + value.toString());
			kewu.set(value.toString() + "$" + key.toString());

//			System.out.println(keuw.toString());
//			System.out.println(kewu.toString());
			
			context.write(keuw, new Text("£"));//£ is empty set
			context.write(keuw, new Text("£"));//£ is empty set
		}
	}

}