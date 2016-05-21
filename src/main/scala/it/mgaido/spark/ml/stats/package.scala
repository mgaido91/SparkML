package it.mgaido.spark.ml

/**
 * @author m.gaido
 */
import org.apache.spark.rdd.RDD
package object stats {
  implicit class MinMaxRDD(rdd:RDD[Array[Double]]) {
    private lazy val minAndMaxs = MinsMaxs(rdd)
    def getMins() = minAndMaxs.mins
    def getMaxs() = minAndMaxs.maxs
    
  }
}