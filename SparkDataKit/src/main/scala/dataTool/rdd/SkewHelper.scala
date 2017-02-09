package dataTool.rdd

import org.apache.spark.rdd.RDD
import scala.reflect.ClassTag
/**
  * Created by valwu on 2017/02/08.
  */
object SkewHelper {

  def discardRandomSuffix(dataStr: String)
  :String = {
    // 去掉随机化后缀
    dataStr.substring(0, dataStr.indexOf("#"))
  }

  def getRandomSuffix(dataStr: String)
  :String = {
    // 获得随机化后缀
    dataStr.substring(dataStr.indexOf("#"))
  }

  /*
  * input:
  * data:输入的KV型RDD
  * hashStrLen:hash打散时可能的hash串的最大长度,
  *           当key倾斜越严重时，这个数应该越大，直到平均每个hash槽里元素个数10W以下
  * note:目前key的类型必须是string类型的
  */
  def calcCardByKey[V:ClassTag](data:RDD[(String,V)],
                                hashStrLen:Int = 8)
  :RDD[(String,Long)] = {
    // 在存在大量key而且key的value个数倾斜很大的RDD上统计每个key的value去重数,
    val hashMod = Array.tabulate(hashStrLen)(index=>if(index==0) 1 else 0).mkString("").toLong // hash模数
    val myPartitioner = new org.apache.spark.HashPartitioner(100)
    // 下面通过hash打散实现倾斜的均匀化
    data.filter(item => item._1.trim.length > 0) // 扔掉空key
      .map(item => (item._1 + ("#%0"+hashStrLen+"d").format(Math.abs(item._2.hashCode / hashMod)), item._2))
      .partitionBy(myPartitioner) // 确保RDD被重新分布,减少后续suffle时的通信量,另外注意是先形成新key,然后重新划分数据,不能相反!!!
      .combineByKey((item:V) => List(item),
      (TempList: List[V], item: V) => item :: TempList,
      (TempList1: List[V], TempList2: List[V]) => TempList1 ::: TempList2)
      // 注意下面做distinct时以hashcode区分不同的value
      .map(item => (item._1, item._2.map(item=>item.hashCode().toString).toSet[String].size))
      // 然后将每个hash槽上的大小进行累加
      .map(item => (discardRandomSuffix(item._1), item._2.toLong))
      .combineByKey((item: Long) => item,
        (cur: Long, item: Long) => cur + item,
        (cur1: Long, cur2: Long) => cur1 + cur2)

  }


}

