package it.mgaido.spark.ml.clustering.gridbased.sting

import org.apache.spark.rdd.RDD
import it.mgaido.spark.ml.clustering.Point
import it.mgaido.spark.ml.stats.MinsMaxs
import it.mgaido.spark.ml.clustering._
import org.apache.spark.SparkContext

/**
 * @author m.gaido
 */
object STING {
  
  def train(sc:SparkContext, data:RDD[Point], settings:STINGSettings):STINGModel = {
    val cachedData = data.persist()
    //TODO
    val minMax = MinsMaxs(cachedData.map { p => p.coordinates })
    val rootCell:Cell = new Cell(
            minMax.mins.zip(minMax.maxs)
              .map(t => new Boundary(t._1,t._2)))
    
    //generate the tree
    val tree:HierarchicalTree=HierarchicalTree.generateTree(rootCell, 
        settings.numOfLevels, settings.numOfSplits)
    
    val broadcastedTree=sc.broadcast(tree)
    
    val relevantCells = data.map( point => broadcastedTree.value.getLeafCellForPoint(point) )
        .filter { x => x.isDefined }
        .map { x => (x.get, 1L) }
        .reduceByKey(_+_)
        .map( t => {
          t._1.setNumberOfPoints(t._2)
          t._1
        })
        .filter { cell => cell.getDensity>=settings.minDensity }
    
    cachedData.unpersist()
    
    
    //merge cells
    
    new STINGModel
  }
 
}