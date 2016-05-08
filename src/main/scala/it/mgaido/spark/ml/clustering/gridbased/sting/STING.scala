package it.mgaido.spark.ml.clustering.gridbased.sting

import org.apache.spark.rdd.RDD
import it.mgaido.spark.ml.clustering.Point
import it.mgaido.spark.ml.stats.MinsMaxs
import it.mgaido.spark.ml.clustering._
import org.apache.spark.SparkContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author m.gaido
 */
object STING {
  
  private val LOG = LoggerFactory.getLogger("STING")
  
  /***
   * Since multiple passes on the dataset are required
   * [[data]] should be persisted to improve performance
   */
  def train(sc:SparkContext, data:RDD[Point], settings:STINGSettings):STINGModel = {
    //val cachedData = data.persist()
    assert (settings.initialLevel <= settings.numOfLevels, 
        "Inital level must be smaller than the total number of levels")

    val minMax = MinsMaxs(data.map { p => p.coordinates })
    val rootCell:Cell = new Cell(
            minMax.mins.zip(minMax.maxs)
              .map(t => new Boundary(t._1,t._2)))
    
    //generate the tree
    val tree:HierarchicalTree=HierarchicalTree.generateTree(rootCell, 
        settings.initialLevel, settings.numOfSplits)
    
    for (lev <- settings.initialLevel to settings.numOfLevels){

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
          .collect()
      LOG.info("At level " + lev + " there are " + relevantCells.length + " relevant cells.")
      if(LOG.isDebugEnabled()){
        LOG.debug("Relevant cells are: ")
        for ( cell <- relevantCells ){
          LOG.debug(cell.toString())
        }
      }
      tree.filterChildren(relevantCells.toSet)
      if(lev < settings.numOfLevels)
        tree.expandTree(settings.numOfSplits)
      
    }    
    
    //TODO: merge cells
    
    new STINGModel
  }
 
}