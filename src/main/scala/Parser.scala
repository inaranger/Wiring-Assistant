import scala.collection.mutable.ArrayBuffer
object Parser {
  def parse(input: List[String]): Seq[(Int, Seq[WireBuilder], SearchTargets)] =
    val buffer = ArrayBuffer[(Int,Seq[WireBuilder],SearchTargets)]()
    val problems = input.grouped(3)
    for problem <- problems do
      val List(firstLine,secondLine,thirdLine) = problem
      val Array(n_wire, size) =  firstLine.split(" ").map(_.toInt)
      val wireData = {
        val coords = secondLine.split(" ").map(_.toShort).grouped(4).toSeq
        coords.zipWithIndex.map {
          case (Array(x1, y1, x2, y2), idx) => new WireBuilder(x1,y1,x2,y2,idx)
        }
      } 
      val Array(startx, starty, goalx, goaly) = thirdLine.split(" ").map(_.toInt)
      val instance = (size,wireData,new SearchTargets(startx,starty,goalx,goaly))
      buffer += instance
    buffer.toSeq


}
