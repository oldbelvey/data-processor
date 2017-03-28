package belvey.fs.processor;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.NewCookie;

import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.iq80.leveldb.DB;

import belvey.fs.processor.model.DataEntity;
import belvey.fs.processor.util.DateUtils;

/**
 * word count
 *
 */
public class DataProcessor {
	public static class TokenizerMapper extends Mapper<Object, Text, Text, DataEntity> {

		private Text k1 = new Text();
		private Text v1 = new Text();

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer st = new StringTokenizer(value.toString());
			DataEntity dn = loaddn(st);
			k1.set(dn.getName());
			v1.set(dn.toString());
			context.write(k1, dn);
		}
	}
	private static DataEntity loaddn(StringTokenizer st) {
		DataEntity de = null;
		if(st.countTokens() == 5){
			de = new DataEntity();
			try {
				de.setId(st.nextToken());
				de.setStartTime(st.nextToken());
				de.setEndTime(st.nextToken());
				de.setName(st.nextToken());
				de.setAge(st.nextToken());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return de;
	}

	public static class IntSumReducer extends Reducer<Text, DataEntity, NullWritable, Text> {
		
		Text result = new Text();
		
		@Override
		public void reduce(Text key, Iterable<DataEntity> values, Context context) throws IOException, InterruptedException {
			DataEntity ent = new DataEntity();
			
			Map<Long, DataEntity> map = new TreeMap<Long,DataEntity>();
			
			for (DataEntity de : values) {
				try {
					map.put(DateUtils.getTimeStamp(de.getStartTime()), de);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			Iterator<Entry<Long, DataEntity>> iterator = map.entrySet().iterator();
			Entry<Long, DataEntity> before = iterator.next();  //ent初始化
			while(iterator.hasNext()){
				Entry<Long, DataEntity> next = iterator.next();
				Long timeStamp;
				try {
					timeStamp = DateUtils.getTimeStamp(before.getValue().getEndTime());
					if(next.getKey() >= timeStamp + 120000){
						ent.setId(before.getValue().getId());
						ent.setStartTime(before.getValue().getStartTime());
						ent.setEndTime(next.getValue().getEndTime());
						ent.setName(before.getValue().getName());
						ent.setAge(addAge(before.getValue().getAge(), next.getValue().getAge()));
						before.setValue(ent);  //key不变，value变为ent
					}else{
						result.set(before.getValue().toString());
						context.write(NullWritable.get(), result);
						before = next;
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
		}
		public String addAge(String str1,String str2){
			Integer a = Integer.valueOf(str1) + Integer.valueOf(str2);
			return a.toString();
		}
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		ResourceBundle bundle = ResourceBundle.getBundle("mr");  //参数是后缀为properties的文件名
		Integer ReduceTasksNum = Integer.valueOf(bundle.getString("ReduceTasksNum"));
		
		Configuration conf = new Configuration();
		conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
		Job job = Job.getInstance(conf, "word count");	// job NAME
		job.setJarByClass(DataProcessor.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
				
		job.setNumReduceTasks(ReduceTasksNum);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}



