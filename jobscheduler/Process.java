/*
 * 
 * 
 * @Ziyuan Guan  94722121
 * 
 * 
 * */
public class Process {
	public int processId;
	public int insertID;
	public long execTime;
	public long arrivalTime;
	public long totalTime;
	public RBTree rbt;
	public MinHeap mheap;
	
	public Process(RBTree rbtree,int newId,int newInsertID){
		
		processId=newId;
		insertID=newInsertID;
	}
	public Process(MinHeap hp,int newId,long newExecTime,long newTotalTime) {
		totalTime=newTotalTime;
		processId=newId;
		execTime=newExecTime;
		
	}

		
	
}
