import scala.io.StdIn
import scala.collection.mutable.ArrayBuffer

@main
def main(): Unit = {
  println("ENTER!!!!!")
  var readInput = true
  val lines = scala.collection.mutable.ListBuffer[String]()

  while(readInput){
    val input = StdIn.readLine()
    input match {
      case "0 0" => readInput = false
      case _    => lines += input
    }
  }
  val problems = parse(lines.toList)
  for problem <- problems do
    val graph = new Graph(problem._1, problem._2)
    val solution = graph.findPath(problem._3, problem._4)
    println(solution.get)
}
def parse(input: List[String]): Seq[(Int, Seq[(Int, Int, Int, Int)], Node, Node)] =
  val buffer = ArrayBuffer[(Int,Seq[(Int,Int,Int,Int)],Node,Node)]()
  val problems = input.grouped(3)
  for problem <- problems do
    val List(firstLine,secondLine,thirdLine) = problem
    val Array(n_wire, size) =  firstLine.split(" ").map(_.toInt)
    val wires = secondLine.split(" ").map(_.toInt).grouped(4).map{case Array(x1,y1,x2,y2) => (x1,y1,x2,y2)}.toSeq
    val Array(startx, starty, goalx, goaly) = thirdLine.split(" ").map(_.toInt)
    val instance = (size,wires,Node(startx,starty),Node(goalx,goaly))
    buffer += instance
  buffer.toSeq

//things to consider
//output
//
//simplify graph solving algorithm
//compressing graph (large patches of essentially nothing going on, -> sparse graph)
//horizontal-vertical split of wires for faster access
//clean up parsing process to make prettier code