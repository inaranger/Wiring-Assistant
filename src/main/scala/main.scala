import scala.io.StdIn
import scala.collection.mutable.ListBuffer

@main
def main(): Unit = {
  //input reading
  println("enter your Input")
  var readInput = true
  val lines = ListBuffer[String]()
  while(readInput){
    print("> ")
    val input = StdIn.readLine()
    input match {
      case "0 0" => readInput = false
      case _    => lines += input
    }
  }
  val problems = Parser.parse(lines.toList)
  for (size,wireData,searchTargets) <- problems do {
    val (width,height,wires,start,goal) = Optimizer.optimize(size,wireData,searchTargets)
    val grid = new PCBGraph(width,height,wires)
    val (cost, path) = grid.dijkstra(start,goal).get
    println(cost)
  }
}