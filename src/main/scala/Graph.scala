import scala.collection.mutable

case class Node(x: Int,y: Int)
case class Wire(from: Node, to: Node, id: Int)

class Graph(val size: Int, wireData: Seq[(Int,Int,Int,Int)]) {

  private val wires = initGraph(size, wireData)

  private def isNodeOnWire(node: Node,wire: Wire) : Boolean =
    if(wire.from.x == wire.to.x)
      node.x == wire.from.x && node.y >= math.min(wire.from.y,wire.to.y) && node.y <= math.max(wire.from.y,wire.to.y)
    else
      node.y == wire.from.y && node.x >= math.min(wire.from.x, wire.to.x) && node.x <= math.max(wire.from.x, wire.to.x)

  private def getWiresOnNode(node: Node) : Set[Int] =
    val setBuilder = Set.newBuilder[Int]
    wires.foreach(w => if(isNodeOnWire(node,w)) setBuilder += w.id)
    setBuilder.result()
  
  private def movementCost(from: Node, to: Node): Option[Int] =
    if(getWiresOnNode(from).intersect(getWiresOnNode(to)).nonEmpty) None
    else if (getWiresOnNode(to).nonEmpty) Some(1)
    else Some(0)

  private def getNeighbours(from: Node) : Seq[(Node, Int)] =
    var neighbours = Seq[(Node,Int)]()
    val potNeighbours = Seq((from.x+1,from.y),(from.x-1,from.y),(from.x,from.y+1),(from.x,from.y-1))
    potNeighbours.foreach((x, y) =>
      if(x >= 0 && x < size && y >= 0 && y < size)
        val neighbour = Node(x,y)
        movementCost(from, neighbour) match {
          case Some(cost) => neighbours = (neighbour,cost) +: neighbours
          case None => //discard
        }
    )
    neighbours

  def findPath(start: Node, goal: Node): Option[Int] = {
    implicit val ord: Ordering[(Node, Int)] = Ordering.by(-_._2)
    val pq = mutable.PriorityQueue[(Node, Int)]((start, 0))
    val costs = mutable.Map(start -> 0)
    val previous = mutable.Map[Node, Node]()

    while(pq.nonEmpty){

      val (current, currentCost) = pq.dequeue()

      if(current == goal){
        val path = mutable.ListBuffer[Node]()
        var currentPathNode = goal
        while(currentPathNode != start ){
          path.prepend(currentPathNode)
          currentPathNode = previous(currentPathNode)
        }
        return Some(currentCost)
      }
      for((neighbour,cost) <- getNeighbours(current)){
        val alternativeCost = currentCost + cost
        if(alternativeCost < costs.getOrElse(neighbour,Int.MaxValue)){
          costs(neighbour) = alternativeCost
          previous(neighbour) = current
          pq.enqueue((neighbour,alternativeCost))
        }
      }
    }
    None
  }

  private def initGraph(size: Int, wireData: Seq[(Int,Int,Int,Int)]): Seq[Wire] =
    wireData.zipWithIndex.map{ case ((x1,y1,x2,y2),id) => Wire(Node(x1,y1),Node(x2,y2),id) }
}
