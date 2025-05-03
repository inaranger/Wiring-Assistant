import scala.collection.mutable

class PCBGraph(width: Int,height: Int, wires: Seq[Wire]) {
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
  private val edges: Map[Node, Array[(Node, Byte)]] = build(width, height, wires)

  //returns neighbours
  private def getNeighbours(node: Node): Array[(Node, Byte)] = edges.getOrElse(node, Array.empty)

  //Simple dijkstra for Pathfinding
  def dijkstra(start: Node, goal: Node): Option[(Int,Seq[Node])] = {
    val queue = mutable.PriorityQueue.empty[(Int,Node)](
      Ordering.by[(Int, Node),Int](-_._1)
    )
    val distances: mutable.Map[Node,Int] = mutable.Map(start -> 0)
    val predecessor = mutable.Map.empty[Node,Node]

    queue.enqueue((0,start))

    while(queue.nonEmpty){
      val (current_distance,current) = queue.dequeue()

      if (current == goal) {
        //get final cost -> min amount of crossing. Incorporate Wires affecting start Node
        val cost = distances(goal) + nodeWireMap.getOrElse(start,Set.empty).size
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
        val tentative_distance = distances(current) + cost
        if (tentative_distance < distances.getOrElse(neighbour, Int.MaxValue)){
          predecessor(neighbour) = current
          distances(neighbour) = tentative_distance
          queue.enqueue((tentative_distance,neighbour))
        }
      }
    }
    None
  }

  //builds a PCBGraph from width and height of grid and Data of Wires on Grid
  private def build(width: Int, height: Int, wires: Seq[Wire]): Map[Node, Array[(Node, Byte)]] = {
    val edges = mutable.Map.empty[Node, mutable.ArrayBuffer[(Node, Byte)]]

    //compute all nodes and edges
    for {
      x <- 0 until width
      y <- 0 until height
      node = Node(x.toShort, y.toShort) //all nodes
      (dx, dy) <- Seq((1, 0), (-1, 0), (0, 1), (0, -1))
      neighbour = Node((x + dx).toShort, (y + dy).toShort) //calculate all neighbours to node
      if neighbour.x >= 0 && neighbour.x < width && neighbour.y >= 0 && neighbour.y < height //check bounds of neighbours
      cost = computeCost(node, neighbour, nodeWireMap) //calculate cost of edge to neighbour
      if cost >= 0 //invalid edges get removed
    } {
      edges.getOrElseUpdate(node, mutable.ArrayBuffer.empty) += (neighbour -> cost) //add nodes with edge to map
    }
    edges.view.mapValues(_.toArray).toMap
  }

  //computes the cost of an edge between a node and one of its neighbours
  private def computeCost(node: Node, neighbour: Node, nodeWireMap: Map[Node, Set[Byte]]): Byte = {
    val nodeWires = nodeWireMap.getOrElse(node, Set.empty)
    val neighbourWires = nodeWireMap.getOrElse(neighbour, Set.empty)
    
    if ((nodeWires & neighbourWires).nonEmpty)  -1 //nodes share wires -> no edge
    else if (neighbourWires.isEmpty) 0 // neighbour is free -> no costs
    else  neighbourWires.size.toByte // neighbour has wires -> costs equal to amount of wires on node
  }
}