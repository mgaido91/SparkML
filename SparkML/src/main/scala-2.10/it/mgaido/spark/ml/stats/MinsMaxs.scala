package it.mgaido.spark.ml.stats

import org.apache.spark.rdd.RDD

/**
 * @author m.gaido
 */
class MinsMaxs(val mins:Array[Double], val maxs:Array[Double]) {
  
}

object MinsMaxs {
  def apply(data:RDD[Array[Double]]):MinsMaxs = {
    
    //computing min and max
    val minAndMax = data.aggregate(Array.fill(data.first().length)(BigDecimal(Double.MaxValue), BigDecimal(Double.MinValue)))(
          (minsMaxs,current) => {
            minsMaxs.zip(current).map( (x)=> (if(x._1._1<=x._2) x._1._1 else BigDecimal(x._2), if(x._1._2>=x._2) x._1._2 else BigDecimal(x._2) ) )
          }, 
          (minsMaxs1, minsMaxs2) => {
            minsMaxs1.zip(minsMaxs2).map({
              case (a, b) => ( if(a._1<b._1) a._1 else b._1, if(a._2>b._2) a._2 else b._2)
            })
          })
    new MinsMaxs(minAndMax.map( x => x._1.toDouble), minAndMax.map( x => x._2.toDouble))
  }
}