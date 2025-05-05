case class Node(x: Short, y: Short) {
  def isWithinBounds(width: Int,height: Int) : Boolean =
    x >= 0 && x < width && y >= 0 && y < height
  def drawingCoords(max: Int) :(Int,Int) = (max-1-y,x)
}

case class Wire(start: Node, end: Node, id: Byte) {
  def nodes : Seq[Node] =
    if (start.x == end.x)
      (math.min(start.y , end.y) to math.max(start.y,end.y)).map(y => Node(start.x, y.toShort))
    else
      (math.min(start.x, end.x) to math.max(start.x, end.x)).map(x => Node(x.toShort, start.y))
  def isHorizontal : Boolean =
    start.y == end.y
}

class WireBuilder(var x1: Int, var y1: Int, var x2: Int, var y2: Int, var id: Int) {
  def toWire: Wire = Wire(Node(x1.toShort,y1.toShort),Node(x2.toShort,y2.toShort),id.toByte)
}

class SearchTargets(var startX: Int, var startY: Int, var goalX: Int, var goalY: Int){
  def toNodes :(Node,Node) = (Node(startX.toShort,startY.toShort),Node(goalX.toShort,goalY.toShort))
}