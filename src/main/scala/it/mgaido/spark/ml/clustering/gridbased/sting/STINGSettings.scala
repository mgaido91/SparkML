package it.mgaido.spark.ml.clustering.gridbased.sting

import scala.sys.SystemProperties

/**
 * @author m.gaido
 */
private[sting] class STINGSettings private (val numOfLevels:Int, val numOfSplits:Int, val initialLevel:Int, val minDensity:Double) {
  
}

private[sting] object STINGSettings {
  
  val defaultLevels="7"
  val defaultSplits="4"
  val defaultInitialLevel="3"
  
  def apply(density:Double):STINGSettings = {
    
    val levels=System.getProperty("STING.levels", defaultLevels).toInt
    val initLevel=System.getProperty("STING.initialLevel", defaultInitialLevel).toInt
    val splits=System.getProperty("STING.splits", defaultSplits).toInt
    
    new STINGSettings(levels, splits, initLevel, density)
  }
  
}