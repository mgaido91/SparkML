package it.mgaido.spark.ml.clustering.gridbased.sting

import scala.sys.SystemProperties

/**
 * @author m.gaido
 */
private[sting] class STINGSettings private (val numOfLevels:Int, val numOfSplits:Int, val minDensity:Double) {
  
}

private[sting] object STINGSettings {
  
  val defaultLevels="7"
  val defaultSplits="4"
  
  def apply(density:Double):STINGSettings = {
    
    val levels=System.getProperty("STING.levels", defaultLevels).toInt
    val splits=System.getProperty("STING.splits", defaultSplits).toInt
    
    new STINGSettings(levels, splits, density)
  }
  
}