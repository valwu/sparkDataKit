package dataTool.nlp.tokenizer

import java.io.File
import java.io.FileInputStream
import java.io.StringReader
import org.lionsoul.jcseg.tokenizer.core.{DictionaryFactory, IWord, JcsegTaskConfig, SegmentFactory}
import scala.collection.mutable

/**
  * Created by valwu on 2017/2/8.
  * Jcseg分词的scala包装
  */
class JcsegTokenizer {

  // 首先初始化词典
  val config = new JcsegTaskConfig()
  val dic = DictionaryFactory.createSingletonDictionary(config,false)
  dic.loadClassPath()
  // 加载自定义词典,很方便
  val url = getClass.getClassLoader.getResource("resources/nlp/suplementWords")
  val file = new File(url.getFile)
  val fileIS = new FileInputStream(file)
  dic.load(fileIS)
  // 利用词典构建分词器
  val seg = SegmentFactory.createJcseg(JcsegTaskConfig.NLP_MODE,config,dic)

  // 每次分词时调用本函数
  def getSegWordsRslt(text:String)
  :Array[String] = {

    seg.reset(new StringReader(text))
    val wordsBuffer = mutable.ArrayBuffer[String]()
    var continue = true
    while(continue){
      val word = seg.next()
      if(word.isInstanceOf[IWord]){
        wordsBuffer += word.getValue
      }
      else{
        continue = false // 注意如何跳出循环的
      }
    }
    wordsBuffer.toArray

  }

}
