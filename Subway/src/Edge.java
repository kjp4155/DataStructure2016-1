
public class Edge {
	Vertex src, dst;
	int time;
	String linecode;
	boolean isTransfer = false;
	
	public Edge(Vertex src, Vertex dst, int time, String linecode) {
		super();
		this.src= src;
		this.dst = dst;
		this.time = time;
		this.linecode = linecode;
		this.isTransfer = false;
	}
	
	public Edge(Vertex src, Vertex dst, int time, String linecode, boolean transfer) {
		super();
		this.src= src;
		this.dst = dst;
		this.time = time;
		this.linecode = linecode;
		this.isTransfer = transfer;
	}
	
}
