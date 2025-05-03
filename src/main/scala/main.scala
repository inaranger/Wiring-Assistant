import scala.io.StdIn
import scala.collection.mutable.ListBuffer

@main
def main(): Unit = {
  //read the input
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
  
  //parse the input
  val problems = Parser.parse(lines.toList)
  for (size,wireData,searchTargets) <- problems do {
    //optimize the grid size
    val gridU = new Optimizer(size,wireData,searchTargets)
    val (width,height,wires,start,goal) = gridU.cropGrid
    //create graph
    val grid = new PCBGraph(width,height,wires)
    //solve the problem
    val (cost, path) = grid.dijkstra(start,goal).get
    println(cost)
  }
}