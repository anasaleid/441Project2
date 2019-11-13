import java.lang
import org.apache.hadoop.classification.InterfaceAudience.Public
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import scala.collection.JavaConversions._

@Public class MyReducer extends Reducer[Text, IntWritable, Text, IntWritable]{

  override def reduce(key: Text, values: lang.Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
    //Adding up the count of each coauthor relation
    val count = values.toList.reduce((sum1, sum2) => new IntWritable(sum1.get() + sum2.get()))
    context.write(key, new IntWritable(count.get()))
  }

}