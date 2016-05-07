package it.mgaido.spark.ml.clustering.gridbased.sting
import it.mgaido.spark.ml.clustering._


/**
 * @author m.gaido
 */
private[sting] class Cell(val boundaries:Array[Boundary]) {
  
  private var numberOfPoints:Long=0L
  
  // unused variables
  /*
  var mean:Double
  var stdDev:Double
  var min:Double
  var max:Double
  var distribution:Distribution
  */
  
  /**
   * @return This method returns the number of points
   *          in the cell, which is referred as n in
   *          STING paper
   */
  def getN:Long = numberOfPoints
  
  /**
   * @return It returns the surface or volume of the cell,
   *        which is referred as S in STING paper
   */
  def getS:Double = {
    var volume:Double = 1.0
    for ( side <- boundaries) volume*=side.getLength
    volume
  }
  
  def getDensity:Double = (getN/getS)
  
  /**
   * @return The side length of the maximum cube 
   *          which can be contained in the cell
   */
  def getL:Double = boundaries.map { b => b.getLength }.reduceLeft(_ min _)
  
  def splitIntoSubcells(numOfCells:Int):Array[Cell] = {
    assert(numOfCells%2 == 0 && numOfCells>0)
    
    var result = Array[Cell](this)
    while(result.length != numOfCells) {
      result = result.flatMap { x => Cell.splitAlongLongestDimension(x) } 
    }
    
    result
  }
  
  def contains(p:Point):Boolean = {
    assert(p.coordinates.length == this.boundaries.length)
    
    ! this.boundaries.zip(p.coordinates)
        .map( x => x._1.lowerBound <= x._2 && x._1.upperBound >= x._2 )
        .contains(false)
  }
  
  def incrementNumberOfPoints() { numberOfPoints+=1L }
  
  def setNumberOfPoints(value:Long) { numberOfPoints=value }
  
  override def equals(other:Any):Boolean = {
    if( !other.isInstanceOf[Cell] ) return false 
    
    ! other.asInstanceOf[Cell].boundaries.zip(this.boundaries)
        .map( t => t._1.equals(t._2))
        .contains(false)
    
  }
  
  
}

object Cell {
  
  private[Cell] def splitAlongLongestDimension(cell:Cell):Array[Cell] = {
    var indexLongestDim:Int = 0
    var maxVal:Double = 0.0
    for ((b,index) <- cell.boundaries.view.zipWithIndex ) {
      if(b.getLength>maxVal){
        maxVal=b.getLength
        indexLongestDim = index
      }
    }
    val splitPoint = cell.boundaries(indexLongestDim).lowerBound+cell.boundaries(indexLongestDim).getLength/2
    val newLowerCell=cell.boundaries.clone()
    newLowerCell(indexLongestDim)=Boundary(newLowerCell(indexLongestDim).lowerBound,
        splitPoint)
    val newUpperCell=cell.boundaries.clone()
    newUpperCell(indexLongestDim)=Boundary(splitPoint,
        newUpperCell(indexLongestDim).upperBound)  
    Array[Cell](new Cell(newLowerCell), new Cell(newUpperCell))
  }
  
}