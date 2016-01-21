package it.mgaido.spark.ml.clustering.gridbased.sting

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet

/**
 * @author m.gaido
 */
class HierarchicalTreeNode(val item:Cell) {
  
  private[HierarchicalTreeNode] var layer:Int=1
  private[HierarchicalTreeNode] var parent:Option[HierarchicalTreeNode] = None 
  private[HierarchicalTreeNode] val children:HashSet[HierarchicalTreeNode] = HashSet[HierarchicalTreeNode]()
  
  def setParent(parent:HierarchicalTreeNode){
    this.parent = Option(parent)
    this.layer = parent.layer+1
    parent.children.add(this)
  }
  
  def addChildren(nodes:TraversableOnce[HierarchicalTreeNode]) {
    children++=nodes
    nodes.foreach { x => 
      x.parent=Option(this)
      x.layer=this.layer+1
    }
  }
  
  def getParent():Option[HierarchicalTreeNode] = parent
  
  def getChildren():HashSet[HierarchicalTreeNode] = children
  
  def isRoot():Boolean = parent.isEmpty
  
  def isLeaf():Boolean = children.isEmpty
  
  def getLayer():Int = layer
}