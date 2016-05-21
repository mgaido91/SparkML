package it.mgaido.spark.ml.clustering.gridbased.sting

import it.mgaido.spark.ml.clustering.Point
import scala.collection.mutable.HashSet

/**
 * @author m.gaido
 */
class HierarchicalTree(val root:HierarchicalTreeNode) {
  
  def getLeafCellForPoint(p:Point):Option[Cell]={
    getRecursiveCellForPoint(root,p)
  }
  
  private[this] def getRecursiveCellForPoint(node:HierarchicalTreeNode, p:Point):Option[Cell] = {
    if(!node.item.contains(p)) return None
    if(node.isLeaf() && node.item.contains(p)) return Option(node.item)
    for(child<-node.getChildren()) {
      val result = getRecursiveCellForPoint(child,p)
      if(result.isDefined) return result
    }
    None
  }
  
  def expandTree(numOfSplits:Int) = {

    applyToChildren { child => 
      child.item.splitIntoSubcells(numOfSplits)
      .map { x => new HierarchicalTreeNode(x) }
      .foreach { x => 
        x.setParent(Some(child)) 
      }
    }
  }
  
  def filterChildren(toBeMantained:Set[Cell]){
    
     applyToChildren { child => 
       if(! toBeMantained.contains(child.item) ){
         child.setParent(None)
       }
     }
     
    
  }
  
  private [this] def applyToChildren(f:(HierarchicalTreeNode)=>Unit ):Unit = {
    var currentNodes = this.root.getChildren()
    while ( !currentNodes.isEmpty ){ 
      val newNodes = new HashSet[HierarchicalTreeNode]
      for ( node <- currentNodes ){
        if(node.isLeaf()){
          f(node)
        }else{
          newNodes ++= node.getChildren()
        }
        currentNodes = newNodes
      } 
    }
  }
  
  //TODO
  def mergeAdjacentCells():Iterable[Cell] = ???//mergeRecursive(this.root.getChildren()).toSeq
  //it's wrong, to be debugged
  /*
  private[this] def mergeRecursive(nodes: scala.collection.mutable.HashSet[HierarchicalTreeNode])
      :scala.collection.mutable.ArrayBuffer[Cell] = {
    val cells = new scala.collection.mutable.ArrayBuffer[Cell]
    if(nodes.isEmpty) return cells
    
    nodes.foreach { node => 
      if(node.isLeaf()){
        cells.append(node.item)
      }else{
        val tmpCells = mergeRecursive(node.getChildren())
        val tmpCells2 = new scala.collection.mutable.HashSet[Cell]
        tmpCells2 ++ tmpCells
        for( tmpCell <- tmpCells){
          var adjCells:Iterable[Cell] = None
          tmpCells2 - tmpCell
          do{
            adjCells = tmpCells2.filter { c => c.isAdjacentTo(tmpCell) }
            adjCells.foreach { x => tmpCell.mergeWith(x) }
            tmpCells2 -- adjCells
          }while(!adjCells.isEmpty)
          cells.append(tmpCell)
        }
        
      }
      
    }
    
    cells
  }*/
  
}

object HierarchicalTree {
  def generateTree(rootCell: Cell, numOfLevels: Int, numOfSplits: Int) = {
    new HierarchicalTree(
        recursiveGenerateChildren(new HierarchicalTreeNode(rootCell), numOfLevels-1, numOfSplits)
        )
  }
  
  
  
  private def recursiveGenerateChildren(node:HierarchicalTreeNode, levelsToDo:Int, numOfSplits: Int):HierarchicalTreeNode = {
    if(levelsToDo>0){
      node.item.splitIntoSubcells(numOfSplits)
          .map { x => new HierarchicalTreeNode(x) }
          .foreach { x => 
            x.setParent(Some(node)) 
            recursiveGenerateChildren(x, levelsToDo-1, numOfSplits)
          }
    }
    node
  }
}