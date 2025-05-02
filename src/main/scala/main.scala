import scala.io.StdIn
import scala.collection.mutable.ListBuffer

@main
def main(): Unit = {
  //input reading
  println("enter your Input")
  var readInput = true
  val lines = ListBuffer[String]()
  while(readInput){
    val input = StdIn.readLine()
    input match {
      case "0 0" => readInput = false
      case _    => lines += input
    }
  }
  val problems = Parser.parse(lines.toList)

  val wires: Seq[Wire] = Seq(
    Wire(Node(0,0),Node(1,0),0),Wire(Node(4,0),Node(4,6),1),
    Wire(Node(0,5),Node(5,5),2),Wire(Node(8,2),Node(8,6),3),
    Wire(Node(6,4),Node(6,7),4),Wire(Node(7,6),Node(7,8),5),
    Wire(Node(2,6),Node(5,6),6),Wire(Node(7,8),Node(9,8),7),
  )

  val graph = new PCBGraph(10, 10, wires)
  val result = graph.dijkstra(Node(6,8),Node(5,1))
  print(result)
  val nfg = graph.getNeighbours(Node(5,5))
  //main loop + parsing
}