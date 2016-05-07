package it.mgaido.spark.ml.clustering

/**
 * @author m.gaido
 */
class Point(val coordinates:Array[Double]) {
  private[this] var pointId:Option[PointId]=None
  private[clustering] var clusterId:Option[ClusterId]=None
  
  def this(pointId:PointId, coordinates:Array[Double]) = {
    this(coordinates)
    this.pointId=Option(pointId)
  }
  /**
   * @return The point ID set for the point
   *          or [[it.mgaido.spark.ml.clustering.UndefinedPointId]] if no ID has been set
   */
  def getPointId():PointId = pointId.getOrElse(UndefinedPointId)
  
  /**
   * @return The cluster ID to which the point has been assigned
   *          or [[it.mgaido.spark.ml.clustering.UndefinedClusterId]] if it has not yet been clustered
   */
  def getClusterId():ClusterId = clusterId.getOrElse(UndefinedClusterId)
  
}