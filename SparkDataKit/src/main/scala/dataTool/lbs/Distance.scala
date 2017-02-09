package dataTool.lbs

/**
  * Created by valwu on 2017/2/9.
  * 地理距离计算
  */
object Distance {

  val earthR = 6371393 ////球体模型下地球半径,单位米

  case class LbsPoint(latitude:Double,longitude:Double){
    ////// 注意构造函数中将角度转换为弧度
    val latitudeD = math.toRadians(latitude)
    val longitudeD = math.toRadians(longitude)
  }

  def haversine(theta:Double):Double={
    math.pow(math.sin(theta/2),2)
  }
  def lbsDistanceBySphereModel(point1:LbsPoint,point2:LbsPoint)
  :Double={
    ///////标准球体模型下利用Haversine公式计算地球两点之间的距离,输入是十进制的经纬度坐标
    val h = haversine(point1.latitudeD- point2.latitudeD) +
      math.cos(point1.latitudeD)*math.cos(point2.latitudeD)*haversine(point1.longitudeD- point2.longitudeD)
    //////注意 2 * EarthR*math.atan2(math.sqrt(h), math.sqrt(1-h))和 2*EarthR*math.asin(math.sqrt(h)) 在数学上是等价的,但是计算机运算精度不一样
    val d = 2 * earthR*math.atan2(math.sqrt(h), math.sqrt(1-h))
    d
  }

  def main(args: Array[String]): Unit = {

    println(math.toDegrees(math.Pi/4)) ///角度转换为弧度
    println(math.pow(2,4)) ///乘幂运算

    /////成都-北京之间的距离1,520km，参考网址如下:http://www.jisuan.info/%E6%88%90%E9%83%BD/%E5%8C%97%E4%BA%AC,认为计算正确
    println("成都-北京",lbsDistanceBySphereModel(LbsPoint(30.67,104.06), LbsPoint(39.92,116.46)))

    /////广州-北京之间的距离1,891km，参考网址如下:http://www.jisuan.info/%E5%B9%BF%E5%B7%9E/%E5%8C%97%E4%BA%AC,认为计算正确
    println("广州-北京",lbsDistanceBySphereModel(LbsPoint(23.16,113.23), LbsPoint(39.92,116.46)))

  }
}
