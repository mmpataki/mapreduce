# mapreduce

JarHelper is a utility class which can help you build a Jar for your mapreduce code.

### How to use
```java
import ...;
public class Driver {

	public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		...
	}
	
	public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		...
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, Exception {
		JobConf conf = new JobConf();
		conf.set("mapred.remote.os", "Linux");
		conf.set("mapreduce.app-submission.cross-platform", "true");
		Job job = Job.getInstance(conf, "common-friends");
		job.addCacheFile(new Path(args[0]).toUri());
		
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		job.setJarByClass(Driver.class);
		
		//Add this line, that's it
		job.setJar(JarHelper.getJar(Driver.class));
		job.waitForCompletion(true);
	}
}
```
