case class Node(x: Short, y: Short) 

case class Wire(start: Node, end: Node, id: Byte) {
  def nodes : Seq[Node] =
    if (start.x == end.x)
      (math.min(start.y , end.y) to math.max(start.y,end.y)).map(y => Node(start.x, y.toShort))
    else
      (math.min(start.x, end.x) to math.max(start.x, end.x)).map(x => Node(x.toShort, start.y))
}
