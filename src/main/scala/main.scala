import scala.io.StdIn
import scala.collection.mutable.ArrayBuffer

@main
def main(): Unit = {
  //read the input
  println("enter your Input")
  var readInput = true
  val lines = ArrayBuffer[String]()
  while(readInput){
    print("> ")
    val input = StdIn.readLine()
    input match {
      case "0 0" => readInput = false
      case _    => lines += input
    }
  }
  //parse the input
  val problems = Parser.parse(lines.toSeq)
  for (size,wireData,searchTargets) <- problems do {
    //optimize the grid size
    val reducedProblem = new Optimizer(size,wireData,searchTargets)
    val (width,height,wires,start,goal) = reducedProblem.cropGrid
    //print new Problem
    Visualizer.printProblem(width, height, wires, start, goal)
    println("Press Anything to find Path")
    StdIn.readLine()

    //create graph
    val grid = new Graph(width,height,wires)
    //solve the problem
    val (cost, path) = grid.dijkstra(start,goal).get

    //print Solution
    Visualizer.animateGrid(width, height, wires, path, start, goal)
    println("Minimal Crossings: " + cost)
    println()
  }

}