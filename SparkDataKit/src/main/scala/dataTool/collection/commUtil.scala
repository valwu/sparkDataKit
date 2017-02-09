package dataTool.collection

import scala.collection.mutable

/**
  * Created by jianjunwu on 2017/2/9.
  * 通用小型集合操作
  */
object commUtil {

  def cartesian[T,M](iterable1: Iterable[T],
                     iterable2: Iterable[M])
  :Array[(T,M)]={
    // 两个集合对象做笛卡尔乘积,注意参数集合不能太大
    val tempArrayBuffer = mutable.ArrayBuffer[(T,M)]()
    for(iter1<-iterable1) {
      for(iter2<-iterable2) {
        val item = (iter1,iter2)
        tempArrayBuffer += item
      }
    }
    tempArrayBuffer.toArray
  }

  def mergeHashMap[K,T](resHM:mutable.HashMap[K,T],
                        otherHM:mutable.HashMap[K,T],
                        merger:(T,T)=>T) // 合并操作符
  :Unit = {
    // 合并两个hashmap,key相同则value进行指定的合并操作,否则新增kv对
    for((k,v)<- otherHM)
    {
      if(resHM.contains(k)){
        resHM(k) = merger(resHM(k),v)
      }
      else{
        resHM(k) = v
      }
    }
  }

  def main(args: Array[String]): Unit = {

    val resHM = mutable.HashMap[String,String]("dage"->"xx","hunhn"->"yy")
    val otherHM = mutable.HashMap[String,String]("dage"->"mm","laojiang"->"nn","hunhn"->"zz")
    mergeHashMap[String,String](resHM,otherHM,_+_)
    for((k,v) <- resHM){
      println(k,v)
    }

  }

}
