object GridVisualizer {
  def printProblem(width: Int, height: Int, wires: Seq[Wire],start: Node, goal: Node) : Unit = {
    println("Grid was reduced, new Specification:")
    println()
    println("Grid Size: width: " + width + " height: " + height)
    println()
    println("Wires: ")
    wires.foreach{wire =>
      println("from: " + wire.start.x + "," + wire.start.y + " to: " + wire.end.x + "," + wire.end.y)
    }
    println()
    println("Start: " + start.x + "," + start.y + " Goal: " + goal.x + "," + goal.y)
    println()
  }

  def animateGrid(width: Int, height: Int, wires: Seq[Wire], path: Seq[Node], start: Node, goal: Node): Unit = {
    //initialize grid
    val grid = Array.fill(height,width)('.')

    //draw the wires
    wires.foreach { wire =>
      wire.nodes.foreach{ node =>
        val (x,y) = node.drawingCoords(height)
        grid(x)(y) = grid(x)(y) match {
          case '.' =>  if wire.isHorizontal then '-' else '|'
          case _ => '+'
        }
      }
    }

    //draw start and end
    val (startX,startY) = start.drawingCoords(height)
    val (endX,endY) = goal.drawingCoords(height)
    grid(startX)(startY) = '*'
    grid(endX)(endY) = '*'

    //draw path, print the grid a new for every step
    path.foreach{ node =>
      val (x,y) = node.drawingCoords(height)
      grid(x)(y) = grid(x)(y) match {
        case '.' => 'o'
        case '*' => '*'
        case _ => '@'
      }
      printFrame(grid)
    }


  }

  private def printFrame(frame: Array[Array[Char]]): Unit = {
    for (row <- frame) {
      println(row.mkString(" "))
    }
    println()
    Thread.sleep(500)
  }
}

//val horizWire = '-'
//val vertWire = '|'
//val crossWire = '+'
//val node = '.'
//val pathNode = 'o'
//val pathCrossing = '@'
//val targetNode = "*"