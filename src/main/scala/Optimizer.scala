class Optimizer(size: Int, initialWireData: Seq[WireBuilder], searchTargets: SearchTargets) {
  private val wireData = initialWireData.toArray
  private val xCoords: Array[Int] = {
    val baseCoords = wireData.flatMap(w => Array(w.x1,w.x2))
    (baseCoords ++ Array(searchTargets.startX,searchTargets.goalX)).distinct.sorted
  }
  private val yCoords: Array[Int] = {
    val baseCoords = wireData.flatMap(w => Array(w.y1,w.y2))
    (baseCoords ++ Array(searchTargets.startY,searchTargets.goalY)).distinct.sorted
  }
  private var height = size
  private var width = size

  def cropGrid: (Int,Int,Seq[Wire],Node,Node) = {
    //Apply Bounding Box to Grid
    if(xCoords(0) > 1) updateColumns(1,xCoords(0)-1)
    if (xCoords(xCoords.length-1) + 1 < width) width = xCoords(xCoords.length-1) + 2
    if (yCoords(0) > 1) updateLines(1, yCoords(0) - 1)
    if (yCoords(yCoords.length-1) + 1 < height) height = yCoords(yCoords.length-1) + 2

    //detect identical columns
    for(i <- 0 until xCoords.length - 1){
      val gap = xCoords(i + 1) - xCoords(i)
      if (gap > 2) updateColumns(xCoords(i),gap - 2)
    }
    //detect identical lines
    for(i <- 0 until yCoords.length - 1){
      val gap = yCoords(i + 1) - yCoords(i)
      if (gap > 2) updateLines(yCoords(i),gap - 2)
    }
    //convert data
    val wires = wireData.map(wire => wire.toWire)
    val(start,goal) = searchTargets.toNodes
    (width,height,wires,start,goal)
  }

  private def updateColumns(threshold: Int, remove: Int) : Unit = {
    //update Coordinates
    for(i <- xCoords.indices) {
      if(xCoords(i) > threshold) xCoords(i) -= remove
    }
    //update Wires
    for(i <- wireData.indices){
      val wire = wireData(i)
      if(wire.x1 > threshold) wire.x1 -= remove
      if(wire.x2 > threshold) wire.x2 -= remove
    }
    //update search Targets
    if(searchTargets.startX > threshold) searchTargets.startX -= remove
    if(searchTargets.goalX > threshold) searchTargets.goalX -= remove
    //update width
    width -= remove
  }

  private def updateLines(threshold: Int, remove: Int): Unit = {
    //update Coordinates
    for (i <- yCoords.indices) {
      if (yCoords(i) > threshold) yCoords(i) -= remove
    }
    //update Wires
    for (i <- wireData.indices) {
      val wire = wireData(i)
      if (wire.y1 > threshold) wire.y1 -= remove
      if (wire.y2 > threshold) wire.y2 -= remove
    }
    //update search Targets
    if (searchTargets.startY > threshold) searchTargets.startY -= remove
    if (searchTargets.goalY > threshold) searchTargets.goalY -= remove
    //update height
    height -= remove
  }
}
