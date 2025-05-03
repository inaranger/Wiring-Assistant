import scala.collection.mutable.ArrayBuffer
object Parser {
  def parse(input: List[String]): Seq[(Int, Seq[WireBuilder], SearchTargets)] =
    val buffer = ArrayBuffer[(Int,Seq[WireBuilder],SearchTargets)]()
    //seperate problem instances
    val problems = input.grouped(3)
    
    for problem <- problems do
      val List(firstLine,secondLine,thirdLine) = problem
      //first line -> size
      val Array(n_wire, size) =  firstLine.split(" ").map(_.toInt)
      //second line -> wire coordinates
      val wireData = {
        val coords = secondLine.split(" ").map(_.toInt).grouped(4).toSeq
        coords.zipWithIndex.map {
          case (Array(x1, y1, x2, y2), idx) => new WireBuilder(x1,y1,x2,y2,idx)
        }
      } 
      //third line -> start and goal coordinates
      val Array(startX, startY, goalX, goalY) = thirdLine.split(" ").map(_.toInt)
      
      val instance = (size,wireData,new SearchTargets(startX,startY,goalX,goalY))
      buffer += instance
    buffer.toSeq
}
