package dataTool.nlp.tokenizer

import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation
import edu.stanford.nlp.util.CoreMap
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation
import edu.stanford.nlp.ling.CoreLabel
import scala.collection.JavaConversions._
import scala.collection.mutable
/**
  * Created by valwu on 2017/2/8.
  * StanfordCoreNLP的scala包装
  * stanford中文NLP
  * 单个对象至少占用1G内存，并且初始需要约20s载入模型
  * 对新词的支持不是很好，对流行的网络用语区分不佳。但是对正式文本区分很好。
  */
class stanfordChieseTookit {

  val pipeline = new StanfordCoreNLP("StanfordCoreNLP-chinese.properties") //这里载入的是中文模型

  def getSegWordsRslt(text:String)
  :Array[String] = {
    val wordsBuffer = mutable.ArrayBuffer[String]()
    val document = new Annotation(text)
    pipeline.annotate(document)
    // 下面注意尽量显示指出类型,scala推断java类型时无法万能
    val sentences:java.util.List[CoreMap] = document.get(classOf[SentencesAnnotation])
    for(sentence:CoreMap<-sentences){ // 注意这里实在遍历java的list而不是scala的list
      for(token:CoreLabel<-sentence.get(classOf[TokensAnnotation])){
        val word:String  = token.get(classOf[TextAnnotation])           // 获取分词
        val pos:String  = token.get(classOf[PartOfSpeechAnnotation])     // 获取词性标注
        val ner:String  = token.get(classOf[NamedEntityTagAnnotation])   // 获取命名实体识别结果
        wordsBuffer += word
      }
    }
    wordsBuffer.toArray
  }
}
