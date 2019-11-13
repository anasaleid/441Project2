import com.typesafe.config.{Config, ConfigFactory}
import myMapReducer.XmlInputFormat
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce._
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.slf4j.LoggerFactory
//import org.apache.log4j.Logger
//<articles>, <inproceedings>
object MapAndReduce {

  def main(args: Array[String]): Unit = {

    val configFile: Config = ConfigFactory.load("config.conf")
    val logger = LoggerFactory.getLogger(this.getClass)

    logger.info("Setting up config")
    //Setting up the XMLInputFormat to parse the specified start and end tag
    val config = new Configuration
    config.set("xmlinput.start", configFile.getString("hw2.startTag"))
    config.set("xmlinput.end", configFile.getString("hw2.endTag"))
    config.set("io.serializations", "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization")

    logger.info("Setting up the job")
    //Creating the job and setting up in mapper/reducer and the input/output formats for each
    val myJob = new Job(config, "UIC authors")
    FileInputFormat.setInputPaths(myJob, new Path(args(1)))
    myJob.setJarByClass(this.getClass)
    myJob.setMapperClass(classOf[MyMapper])
    myJob.setReducerClass(classOf[MyReducer])
    myJob.setInputFormatClass(classOf[XmlInputFormat])
    myJob.setOutputKeyClass(classOf[Text])
    myJob.setOutputValueClass(classOf[IntWritable])
    myJob.setMapOutputKeyClass(classOf[Text])
    myJob.setMapOutputValueClass(classOf[IntWritable])

    logger.info("Deleting output path if it exists")
    //Deleting the output folder if it exists already
    val outPath = new Path(args(2))
    FileOutputFormat.setOutputPath(myJob, outPath)
    val fileSys = FileSystem.get(outPath.toUri, config)
    if(fileSys.exists(outPath)){
      fileSys.delete(outPath, true)
    }

    logger.info("Starting job")
    myJob.waitForCompletion(true)
    logger.info("Job Finished")

    //Doing some post processing on the output of the mapreduce job to add some more specifications that gephi will need
    logger.info("Doing some post processing to create an CSV file you can put into gephi")

    //THIS CODE DOESN'T WORK ON AMAZON EMR SINCE IT CREATES MULTIPLE OUTPUT FILES
    //I WILL DO THESE THINGS MANUALLY ON EMR INSTEAD OF REWRITING THE CODE FOR CLOUDERA AND AMAZON EMR
    /*val outputFile = new Path(args(2) + "/part-r-00000")
    val mapreduceOutput = FileSystem.get(outputFile.toUri, config)
    if(mapreduceOutput.exists(outputFile)) {
      val fs = FileSystem.get(config)
      val nodeFile = fs.create(new Path(args(2) + "/nodeFile.csv"))
      nodeFile.write("Source, Target, Weight, Type\n".getBytes())
      val content = mapreduceOutput.open(outputFile)
      val lines = Stream.cons(content.readLine(), Stream.continually( content.readLine()))
      lines.takeWhile(_ != null).foreach(line => nodeFile.write((line + ", Undirected\n").getBytes()))
      logger.info("Post processing done. The file name is nodeFile.csv and it is in the output directory you specified earlier")
    }
    else logger.info("The file was never created. Something went wrong!!")*/
  }
}
