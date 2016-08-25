
public class PositionNode {
	private int lineNum;
	private int position;
	
	public PositionNode(int newLineNum, int newPosition){
		lineNum = newLineNum;
		position = newPosition;
	}
	
	public int getLineNum() {
		return lineNum;
	}
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	@Override
	public String toString() {
		return "(" + lineNum + ", " + position + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineNum;
		result = prime * result + position;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PositionNode other = (PositionNode) obj;
		if (lineNum != other.lineNum)
			return false;
		if (position != other.position)
			return false;
		return true;
	}

	
}
