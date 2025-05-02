import scala.collection.mutable.ArrayBuffer
object Parser {
  def parse(input: List[String]): Seq[(Int, Seq[Wire], Node, Node)] =
    val buffer = ArrayBuffer[(Int,Seq[Wire],Node,Node)]()
    val problems = input.grouped(3)
    for problem <- problems do
      val List(firstLine,secondLine,thirdLine) = problem
      val Array(n_wire, size) =  firstLine.split(" ").map(_.toInt)
      val wires = {
        val coords = secondLine.split(" ").map(_.toShort).grouped(4).toSeq
        coords.zipWithIndex.map {
          case (Array(x1, y1, x2, y2), idx) => Wire(Node(x1,y1),Node(x2,y2),idx.toByte)
        }
      } 
      val Array(startx, starty, goalx, goaly) = thirdLine.split(" ").map(_.toShort)
      val instance = (size,wires,Node(startx,starty),Node(goalx,goaly))
      buffer += instance
    buffer.toSeq


}
