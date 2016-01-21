package it.mgaido.spark.ml.clustering.gridbased.sting

import it.mgaido.spark.ml.clustering.Point

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
            x.setParent(node) 
            recursiveGenerateChildren(x, levelsToDo-1, numOfSplits)
          }
    }
    node
  }
}