package it.mgaido.spark.ml

/**
 * @author m.gaido
 */
package object clustering {
  type ClusterId = Int
  type PointId = Long
  val UndefinedClusterId:ClusterId = -1
  val UndefinedPointId:PointId = -1
  val NoiseClusterId:ClusterId = 0
  
  case class Boundary(lowerBound:Double, upperBound:Double){
    def getLength:Double = (upperBound-lowerBound)
    override def equals(other:Any):Boolean = {
      if( !other.isInstanceOf[Boundary] ) return false 
    
      val otherBoundary = other.asInstanceOf[Boundary]
      
      otherBoundary.lowerBound == this.lowerBound  && otherBoundary.upperBound == this.upperBound
    }
  }
}