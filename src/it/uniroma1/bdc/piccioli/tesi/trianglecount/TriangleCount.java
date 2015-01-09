package it.uniroma1.bdc.piccioli.tesi.trianglecount;

import it.uniroma1.bdc.piccioli.tesi.trianglecount.job1.Mapper1;
import it.uniroma1.bdc.piccioli.tesi.trianglecount.job1.Reducer1;
import it.uniroma1.bdc.piccioli.tesi.trianglecount.job2.Mapper2;
import it.uniroma1.bdc.piccioli.tesi.trianglecount.job2.Reducer2;
import it.uniroma1.bdc.piccioli.tesi.trianglecount.job3.Mapper3;
import it.uniroma1.bdc.piccioli.tesi.trianglecount.job3.Reducer3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class TriangleCount extends Configured implements Tool {

	public final static String KEEP_ABOVE = "KEEP_ABOVE";

	public int run(String[] args) throws Exception {

		
		Configuration conf = this.getConf();
		int keepAbove = -1;
		if (args.length > 2) {
			keepAbove = Integer.parseInt(args[2]);
		}
		conf.setInt(KEEP_ABOVE, keepAbove);
		Job job1 = Job.getInstance(conf);

		job1.setJobName("TriangleCount-step1");

		job1.setMapperClass(Mapper1.class);
		job1.setReducerClass(Reducer1.class);
		job1.setJarByClass(TriangleCount.class);

		Path in = new Path(args[0]);
		Path out = new Path(args[1]);
		Path output1 = new Path("/result_job1");//temp results directory
		Path output2 = new Path("/result_job2");//temp results directory

		job1.setInputFormatClass(KeyValueTextInputFormat.class);
		job1.setOutputFormatClass(TextOutputFormat.class);

		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job1, in);
		FileOutputFormat.setOutputPath(job1, output1);	
		
		job1.waitForCompletion(true);
		

		Job job2 = Job.getInstance(conf);
		job2.setJobName("TriangleCount-step2");
		

		job2.setReducerClass(Reducer2.class);
		job2.setJarByClass(TriangleCount.class);

		MultipleInputs.addInputPath(job2, in, KeyValueTextInputFormat.class,Mapper2.class);
		MultipleInputs.addInputPath(job2, output1, KeyValueTextInputFormat.class,Mapper2.class);

		job2.setOutputFormatClass(TextOutputFormat.class);			

		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job2, output1);
		FileOutputFormat.setOutputPath(job2, output2);	
		
		int fine = job2.waitForCompletion(true) ? 0 : -1;

		// Delete temp file
		FileSystem fs = FileSystem.get(conf);
		// delete file, true for recursive
		fs.delete(new Path("/result_job1/"), true);
		
		
		//ultimo job - calcolo totale triangoli
		Job job3 = Job.getInstance(conf);
		job3.setJobName("TriangleCount-step3");

		job3.setReducerClass(Reducer3.class);
		job3.setMapperClass(Mapper3.class);
		job3.setJarByClass(TriangleCount.class);


		job3.setInputFormatClass(KeyValueTextInputFormat.class);
		job3.setOutputFormatClass(TextOutputFormat.class);			

		job3.setOutputKeyClass(Text.class);
		job3.setOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job3, output2);
		FileOutputFormat.setOutputPath(job3, out);	
		
		fine = job3.waitForCompletion(true) ? 0 : -1;

		// delete file, true for recursive
		fs.delete(new Path("/result_job2/"), true);

		return fine;

	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.exit(printUsage());
		}

		Configuration conf = new Configuration();

		conf.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator"," ");
		conf.set("mapreduce.output.textoutputformat.separator", " ");

		TriangleCount dc = new TriangleCount();
		dc.setConf(conf);
		int res = ToolRunner.run(dc, args);
		System.exit(res);

	}

	static int printUsage() {
		System.out.println("TriangleCount <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}

}