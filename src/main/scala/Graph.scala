import scala.collection.mutable

class Graph(width: Int, height: Int, wires: Seq[Wire]) {

  //Map of all Nodes with a Wire on them
  private val nodeWireMap: Map[Node, Set[Byte]] = {
    val builder = mutable.Map.empty[Node, mutable.Set[Byte]].withDefault(_ => mutable.Set.empty)
    wires.foreach { wire =>
      wire.nodes.foreach { node =>
        builder(node) = builder(node) + wire.id
      }
    }
    builder.view.mapValues(_.toSet).toMap
  }
  
  //Graph is saved as Adjacency List (Map of Nodes)
  private val adjacencyMap: Map[Node, Array[(Node, Byte)]] = build(width, height, wires)

  //builds a PCBGraph from width and height of grid and Data of Wires on Grid
  private def build(width: Int, height: Int, wires: Seq[Wire]): Map[Node, Array[(Node, Byte)]] = {
    val adjacencyMapBuilder = mutable.Map.empty[Node, mutable.ArrayBuffer[(Node, Byte)]]

    //compute all nodes and edges
    for {
      x <- 0 until width
      y <- 0 until height

      node = Node(x.toShort, y.toShort) //all nodes

      (dx, dy) <- Seq((1, 0), (-1, 0), (0, 1), (0, -1))
      neighbour = Node((x + dx).toShort, (y + dy).toShort) //calculate all neighbours to node

      if neighbour.isWithinBounds(width, height) //check bounds of neighbours

      cost = computeCost(node, neighbour, nodeWireMap) //calculate cost of edge to neighbour

      if cost.isDefined //invalid edges get removed
    } {
      adjacencyMapBuilder.getOrElseUpdate(node, mutable.ArrayBuffer.empty) += (neighbour -> cost.get)
      //adjacencyMapBuilder.put(node,adjacencyMapBuilder(node).addOne((neighbour,cost.get)))
    }
    adjacencyMapBuilder.view.mapValues(_.toArray).toMap
  }

  //computes the cost of an edge between a node and one of its neighbours
  private def computeCost(node: Node, neighbour: Node, nodeWireMap: Map[Node, Set[Byte]]): Option[Byte] = {
    val nodeWires = nodeWireMap.getOrElse(node, Set.empty)
    val neighbourWires = nodeWireMap.getOrElse(neighbour, Set.empty)

    if ((nodeWires & neighbourWires).nonEmpty) None //nodes share wires -> no edge
    else Some(neighbourWires.size.toByte) // neighbour has wires -> costs equal to amount of wires on node
  }

  //returns neighbours
  private def getNeighbours(node: Node): Array[(Node, Byte)] = adjacencyMap.getOrElse(node, Array.empty)

  private def manhattanDistance(node: Node,goal: Node): Int =
    math.abs(node.x -goal.x) + math.abs(node.y-goal.y)

  //Simple dijkstra for Pathfinding
  def dijkstra(start: Node, goal: Node): Option[(Int,Seq[Node])] = {

    val queue = mutable.PriorityQueue.empty[(Int,Int,Node)](
      Ordering.by[(Int,Int, Node),(Int,Int)]({case (cost,distance, _) => (-cost,-distance)})
    )
    val costs: mutable.Map[Node,Int] = mutable.Map(start -> 0)
    val predecessor = mutable.Map.empty[Node,Node]

    queue.enqueue((0,manhattanDistance(start,goal),start))

    while(queue.nonEmpty){
      val (_,_,current) = queue.dequeue()

      if (current == goal) {
        //get final cost -> min amount of crossing. Incorporate Wires affecting start Node
        val cost = costs(goal) + nodeWireMap.getOrElse(start,Set.empty).size
        // Reconstruct path
        val path = mutable.ListBuffer.empty[Node]
        var node = goal
        while (node != start) {
          path.prepend(node)
          node = predecessor(node)
        }
        path.prepend(start)
        return Some((cost,path.toSeq))
      }

      getNeighbours(current).foreach { case (neighbour, cost) =>
        val tentative_cost = costs(current) + cost
        if (tentative_cost < costs.getOrElse(neighbour, Int.MaxValue)){
          predecessor(neighbour) = current
          costs(neighbour) = tentative_cost
          queue.enqueue((tentative_cost,manhattanDistance(neighbour,goal),neighbour))
        }
      }
    }
    None
  }
}
