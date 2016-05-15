package it.mgaido.spark.ml.clustering.gridbased.sting

import it.mgaido.spark.ml.clustering._
import org.apache.spark.rdd.RDD

/**
 * @author m.gaido
 */
class STINGModel(val clusters:Iterable[Cluster]) {
    
  def predict(point:Point):ClusterId = {
    STINGModel.predict(clusters, point) 
  }
  
  def predict(points:RDD[Point]):RDD[(Point, ClusterId)] = {
    val broadcastedClusters = points.sparkContext.broadcast(clusters)
    points.map( p => (p, STINGModel.predict(broadcastedClusters.value, p)))
  }
}

object STINGModel{
  private def predict(clusters:Iterable[Cluster], point:Point):ClusterId = {
    clusters.find { x => x.cell.contains(point) }
            .map { c => c.id }
            .getOrElse(NoiseClusterId)    
  }
}