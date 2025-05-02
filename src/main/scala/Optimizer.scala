object Optimizer {
  def optimize(size: Int, wireData: Seq[WireBuilder], searchTargets : SearchTargets): (Int,Int,Seq[Wire],Node,Node) = {
    val wires = wireData.map(wire => wire.toWire)
    val start = Node(searchTargets.StartX.toShort,searchTargets.StartY.toShort)
    val goal = Node(searchTargets.GoalX.toShort,searchTargets.GoalY.toShort)
    (size,size,wires,start,goal)
  }

}
