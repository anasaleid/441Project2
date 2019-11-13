import com.typesafe.config.{Config, ConfigFactory}
import org.apache.hadoop.classification.InterfaceAudience.Public
import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import scala.collection.JavaConversions._
import scala.xml.NodeSeq
import scala.xml.XML

@Public class MyMapper extends Mapper[LongWritable, Text, Text, IntWritable] {
  private val authorName = new Text()
  private val one = new IntWritable(1)
  val configFile: Config = ConfigFactory.load("config.conf")
  val listOfUICProfNames: List[String] = configFile.getStringList("hw2.profnames").toList

  //Recursively create a list of all UIC authors in this split
  def getUICAuthors(allAuthors: List[String]): List[String] = {
    if(allAuthors.isEmpty) Nil
    else if(listOfUICProfNames.contains(allAuthors.head)) allAuthors.head :: getUICAuthors(allAuthors.tail)
    else getUICAuthors(allAuthors.tail)

  }
  //Recursively create a list of tuples that contain a author and a coauthor
  def connectCoauthors(allAuthors : List[String], currentAuthor : String): List[String] ={
    if(allAuthors.isEmpty) Nil
    else if(allAuthors.head.toString != currentAuthor) (currentAuthor +  "," + allAuthors.head.toString + ",") :: connectCoauthors(allAuthors.tail, currentAuthor)
    else connectCoauthors(allAuthors.tail, currentAuthor)

  }
   override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, IntWritable]#Context) {
    //val xml = "<!DOCTYPE tmp PUBLIC \n\"-//CMP//DTD dblp 1.0//EN\"\n\"dblp.dtd\">" + value.toString + "</dblp>"
    //Doing this replace all adds a lot of overhead to the time it takes for the job to finished but I couldn't find another way
     //With the time I had left
     val xmlFormat = XML.loadString(value.toString.replaceAll("&", ""))
    //Extracting all authors from the xml string
     val authors = xmlFormat \\ "author"
     //Removing the <author> tag from each element
     val authorsList = authors.map(_.text)
     //The names of the methods below should be enough to figure out what is going on
    val UICAuthors = getUICAuthors(authorsList.toList)
    UICAuthors.foreach{x =>
      val coauthorRelations = connectCoauthors(UICAuthors, x)
      coauthorRelations.foreach { y =>
        authorName.set(y)
        context.write(authorName, one)
      }
    }


  }




}