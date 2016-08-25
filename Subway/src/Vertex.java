import java.util.LinkedList;

public class Vertex {
	String code;
	String name;
	String before;
	LinkedList<Edge> adjacencyList = new LinkedList<Edge>();
	String linecode;
	int time;
	int transferCnt;
	
	public void initialize(){
		time = Integer.MAX_VALUE;
		transferCnt = Integer.MAX_VALUE;
		before = null;
	}
	
	public Vertex(String code, String name, String linecode) {
		super();
		this.code = code;
		this.name = name;
		this.transferCnt = Integer.MAX_VALUE;
		this.linecode = linecode;
	}
	@Override
	public String toString() {  // Simple toString for debugging
		return "Vertex [code=" + code + ", name=" + name + ", before=" + before 
				+ ", linecode=" + linecode + ", time=" + time + ", changeNum=" + transferCnt + "]";
	}
	public void addEdge(Vertex dst, int time){
		adjacencyList.add( new Edge(this, dst, time, linecode) );
	}
	
	public void addTransferEdge(Vertex dst, int time){
		adjacencyList.add( new Edge(this, dst, time, linecode, true) );
	}
	
	public void addNormalEdge(Vertex dst, int time){
		adjacencyList.add( new Edge(this, dst, time, linecode, false) );
	}

}
