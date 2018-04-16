import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.OutputStreamWriter;   
/*
 * 
 * @Ziyuan Guan 
 * 
 * 
 * */

public class jobscheduler {
	public static RBTree rbt;
	public static MinHeap hp;
	public static int node_count=0;
	public static int n;
	public static long global_time=0;
	public static void main(String[] args)throws Exception {
		File f1=new File(args[0]);
		Scanner input=new Scanner(f1);
		rbt=new RBTree(); // create the RBT
		ArrayList<Process> processes=new ArrayList<Process>(); // used to remember the execution time
		ArrayList<Process> jobs=new ArrayList<Process>(); // used to remember the JobID
		hp=new MinHeap();// create the heap
		int flag=0;
		int insert_time=0;
		//System.out.println("Start!!");
		////System.out.println("The global time and insert time is :   "+ global_time+ "  ,  "+insert_time);
		while(input.hasNextLine()) {
			flag=0;
			//System.out.println("--------------print the arraylist --------");
			
			for(int k=0;k<processes.size();k++) {
				//System.out.println("global time :"+global_time+"  ID: "+processes.get(k).processId+": "+processes.get(k).execTime);
			}
			//System.out.println("the number of nodes is "+node_count);
			String line = input.nextLine();
			//System.out.println(line);
			int leng=line.length();
			char []word=new char[leng];
			word=line.toCharArray();
			int a=0;
			int b=0;
			for(int i=0;i<line.length();i++) { // we need to identify whether we have comma or not
				if(word[i]==':'){
					a=i;
					////System.out.println("a is"+a);
				}
				if(word[i]=='(') {	
					b=i;
				}
			}
			char []instime_c=new char[a];
			for(int i=0;i<a;i++) {
				instime_c[i]=word[i];
			}
			char []inscomman_c=new char[b-a-2];// there is a space between : and (
			for(int i=0;i<b-a-2;i++) {
				inscomman_c[i]=word[a+2+i];
			}
			////System.out.println(new String(instime_c));
			insert_time=Integer.parseInt(new String(instime_c)); // we need to notice that change the type
			String command= new String(inscomman_c);
			switcher(command,leng,word,insert_time,processes,jobs,b,flag);
		}
		flag=1;
		scheduler(processes,jobs,flag);
		input.close();
	}
	public static void switcher(String command,int leng,char[]word ,int insert_time,ArrayList<Process>processes,ArrayList<Process>jobs,int b,int flag) {
		//System.out.println("COMMAND IS :"+command);
		String JobID="";
		String JobID2="";
		int JobID_int=0;
		long total_time=0;
		int c=0;
		int d=0;
		switch(command) {
		case "Insert":	
			for(int i=0;i<leng;i++) { // we need to identify whether we have comma or not
				if(word[i]==','){
					c=i;
				}
				if(word[i]==')') {	
					d=i;
				}
			}
			char []insvar1_c=new char[c-b-1];
			for(int i=0;i<c-b-1;i++) {
				insvar1_c[i]=word[b+1+i];
			}
			char []insvar2_c=new char[d-c-1];
			for(int k=0;k<d-c-1;k++) {
				insvar2_c[k]=word[c+1+k];
			}
			JobID_int=Integer.parseInt(new String(insvar1_c));
			//System.out.println("Job_ID IS: "+JobID_int);
			total_time=Integer.parseInt(new String(insvar2_c)); // read the input files;
			//long execution_time=0;
			while(global_time<insert_time) {
				scheduler(processes,jobs,flag);
			}
			//System.out.println("-----------Enter the insert----------");
			//System.out.println("Global time is : "+global_time);
			processes.add(new Process(hp,JobID_int,0,total_time)); // insert the node into the MinHeap
			jobs.add(new Process(rbt,JobID_int,node_count));
			node_count++;
			break;
		case "PrintJob":
			
			while(global_time<insert_time) {
				scheduler(processes,jobs,flag);
			}
			//System.out.println("--------Enter the print--------- ");
			for(int i=0;i<leng;i++) { // we need to identify whether we have comma or not
				if(word[i]==','){
					c=i;
				}
				if(word[i]==')') {	
					d=i;
				}
			}
			if(c==0) {
				// it means there is only one variable
				char []var1=new char[d-b-1];
				for(int i=0;i<d-b-1;i++) {
					var1[i]=word[b+1+i];
				}
				JobID=new String(var1);
				try {
					printJob(JobID,"No",processes,jobs);
				}
				catch(IOException e) {
					e.printStackTrace();
				}
				
			}
			else {
				char []var1=new char[c-b-1];
				char []var2=new char[d-c-1];
				for(int i=0;i<c-b-1;i++) {
					var1[i]=word[b+1+i];
				}
				for(int k=0;k<d-c-1;k++) {
					var2[k]=word[c+1+k];
				}
				JobID=new String(var1);
				JobID2=new String(var2);
				try {
					printJob(JobID,JobID2,processes,jobs);
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			break;
		case "NextJob":
			while(global_time<insert_time) {
				scheduler(processes,jobs,flag);
			}
			//System.out.println("------------Enter the nextjob-----------");
			for(int i=0;i<leng;i++) { // we need to identify whether we have comma or not
				if(word[i]==','){
					c=i;
				}
				if(word[i]==')') {	
					d=i;
				}
			}
			char []var1=new char[d-b-1];
			for(int i=0;i<d-b-1;i++) {
				var1[i]=word[b+1+i];
			}
			JobID=new String(var1);
			try {
				nextJob(JobID,processes,jobs);
			}
			catch(IOException e){
				e.printStackTrace();
			}
			break;
		case "PreviousJob":
			//System.out.println("---------Enter the previousjob-----------");
			while(global_time<insert_time) {
				scheduler(processes,jobs,flag);
			}
			for(int i=0;i<leng;i++) { // we need to identify whether we have comma or not
				if(word[i]==','){
					c=i;
				}
				if(word[i]==')') {	
					d=i;
				}
			}
			char []var2=new char[d-b-1];
			for(int i=0;i<d-b-1;i++) {
				var2[i]=word[b+1+i];
			}
			JobID=new String(var2);
			try {
				previousJob(JobID,processes,jobs);
			}
			catch(IOException e){
				e.printStackTrace();
			}
			break;
		}
	}
	public static void printJob(String JobID1,String JobID2,ArrayList<Process> processes,ArrayList<Process>jobs) throws IOException{  // rewrite to get the order of IDs
		FileWriter output = new FileWriter("output_file.txt",true);
		int count1=2000;
		int count2=2000;
		long execution_time1=0;
		long execution_time2=0;
		long total_time1=0;
		long total_time2=0;
		int JobID_int1=Integer.parseInt(JobID1);
		RBTree <Integer> rbtree=new RBTree<>();
		for(int i=0;i<jobs.size();i++) {
			rbtree.insert(jobs.get(i).processId); // insert the JobID into the RBTree.
		}
		for(int i=0;i<jobs.size();i++) {
			if(jobs.get(i).processId==JobID_int1) {  // get the id from RBT tree
				count1=i;
				break;
			}
		}
		if(JobID2=="No") { // it means there is only one index
			//System.out.println("This print operation has only one var");
			if(count1==2000) { // it means the JobID not exist
				//System.out.println("global_time : "+global_time+" (0,0,0)"); // cannot find the record
				output.write("(0,0,0)");
				output.write(System.getProperty("line.separator")); // to make sure the "/n" is working
			}else {
				execution_time1=processes.get(count1).execTime;
				total_time1=processes.get(count1).totalTime;
				//System.out.println("global_time : "+global_time+" ("+processes.get(count1).processId+","+execution_time1+","+total_time1+")");
				output.write("("+processes.get(count1).processId+","+execution_time1+","+total_time1+")");
				output.write(System.getProperty("line.separator"));
			}
		}
		else { // there is a range
			int emptytree=0;
			int JobID_int2=Integer.parseInt(JobID2);
			for(int j=0;j<jobs.size();j++) {
				if(jobs.get(j).processId==JobID_int2) {
					count2=j;
					break;
				}
			}
			if(count2==2000) { //the id is not in the heap
				if(rbtree.maximum()==null) { // there is empty tree
					emptytree=1;
				}else {
					int B=rbtree.maximum();
					int C=0;
					while(B>=JobID_int2) {
						if(rbtree.previous(B)==null) {
							// JobID2< minimum
							break;
						}
						C=rbtree.previous(B);
						B=C;
					}
					count2=B;
				}
			}
			if(count1==2000) {
				if(rbtree.minimum()==null) {
					emptytree=1;
				}else {
					int B=rbtree.minimum(); // get the key 
					int C=0;
					while(B<=JobID_int1) {
						if(rbtree.next(B)==null) {
							//JobID1>=max
							break;
						}
						C=rbtree.next(B);
						B=C;
					}
					count1=B;
				}
			}
			else {
				count1=jobs.get(count1).processId;
			}
			// now we get the key of the job .
			if(emptytree==1) {
				//System.out.println("empty tree!");
				//System.out.println("global_time : "+global_time+" (0,0,0)"); // cannot find the record
				output.write("(0,0,0)");
				output.write(System.getProperty("line.separator"));
			}
			else { // we have got the range correctly
				int temp=0;
				/*
				if(rbtree.previous(count1)!=null) {
					count1=rbtree.previous(count1);
				}
				*/
				if(count2==rbtree.maximum()) {
					// there is only one variable satisfied in the tree
					temp=count2;
				}else {
					temp=rbtree.next(count2);
				}
				while(count1<count2) { // to avoid the null situation
					for(int i=0;i<jobs.size();i++) {
						if(jobs.get(i).processId==count1) {
							execution_time2=processes.get(i).execTime;
							total_time2=processes.get(i).totalTime;
							//System.out.print("global_time : "+global_time+" ("+processes.get(i).processId+","+execution_time2+","+total_time2+")");
							output.write("("+processes.get(i).processId+","+execution_time2+","+total_time2+")");
						}
					}
					if(count1<temp) {  // to avoid the null exception
						if(rbtree.next(count1)==null) {
							count1=count2;
							System.out.print(",");
							output.write(",");							
							break;
						}
						count1=rbtree.next(count1);
						//System.out.print(",");
						output.write(",");
					}else {
						count1=count2;
						break;
					}
				}
				for(int i=0;i<jobs.size();i++) {
					if(jobs.get(i).processId==count1) {
						execution_time2=processes.get(i).execTime;
						total_time2=processes.get(i).totalTime;
						//System.out.print("global_time : "+global_time+" ("+processes.get(i).processId+","+execution_time2+","+total_time2+")");
						output.write("("+processes.get(i).processId+","+execution_time2+","+total_time2+")");
					}
				}
				//System.out.println("");
				output.write(System.getProperty("line.separator"));
				
			}
			
		}	
			// should print in the file
		output.close();
	}
	
	public static void nextJob(String JobID,ArrayList<Process> processes,ArrayList<Process>jobs) throws IOException{
		FileWriter output = new FileWriter("output_file.txt",true);
		RBTree <Integer> rbtree=new RBTree<>();
		for(int i=0;i<jobs.size();i++) {
			rbtree.insert(jobs.get(i).processId); // insert the JobID into the RBTree.
		}
		int Job_ID=0;
		Job_ID=Integer.parseInt(JobID);
		int count=2000;
		for(int i=0;i<node_count;i++) {
			if(jobs.get(i).processId==Job_ID) {
				count=i;
				break;
			}
		}
		if(count==2000) {
			if(rbtree.maximum()==null) {
				// tree is empty
				//System.out.println("(0,0,0)");
				output.write("(0,0,0)");
				output.write(System.getProperty("line.separator"));
			}else {
				int B =rbtree.maximum();
				if(B<=Job_ID) {
					// couldnot find
					//System.out.println("(0,0,0)");
					output.write("(0,0,0)");
					output.write(System.getProperty("line.separator"));
				}else {
					int C=0;
					while(B>Job_ID) {
						if(rbtree.previous(B)==null) {
							// B is the biggest one
							break;
						}
						C=rbtree.previous(B);
						B=C;
					}
					count=B;
					for(int i=0;i<jobs.size();i++) {
						if(jobs.get(i).processId==count) {
							count=i;
							break;// get the index
						}
					}
					int E=0;
					if(rbtree.next(jobs.get(count).processId)==null) {
						E= rbtree.maximum();
					}else {
						E= rbtree.next(jobs.get(count).processId);
					}

					for(int i=0;i<jobs.size();i++) {
						if(jobs.get(i).processId==E) {
							count=i;
							break;
						}
					}
					//System.out.println("global_time : "+global_time+" ("+processes.get(count).processId+","+processes.get(count).execTime+","+processes.get(count).totalTime+")");
					output.write("("+processes.get(count).processId+","+processes.get(count).execTime+","+processes.get(count).totalTime+")");
					output.write(System.getProperty("line.separator"));
				}
			}
			
		}else {
			if(jobs.get(count).processId>=rbtree.maximum()) {
				//System.out.println("(0,0,0)");
				output.write("(0,0,0)");
				output.write(System.getProperty("line.separator"));
			}
			else {
				int D=0;//changed here!
				if(rbtree.next(jobs.get(count).processId)==null) {
					D= rbtree.maximum();
				}else {
					D= rbtree.next(jobs.get(count).processId);
				}
				
				for(int i=0;i<jobs.size();i++) {
					if(jobs.get(i).processId==D) {
						count=i;
						break;
					}
				}
			//System.out.println("global_time : "+global_time+" ("+processes.get(count).processId+","+processes.get(count).execTime+","+processes.get(count).totalTime+")");
			output.write("("+processes.get(count).processId+","+processes.get(count).execTime+","+processes.get(count).totalTime+")");
			output.write(System.getProperty("line.separator"));
			}
		}
		
		output.close();
	}
	public static void previousJob(String JobID,ArrayList<Process>processes,ArrayList<Process>jobs) throws IOException{
		FileWriter output = new FileWriter("output_file.txt",true);
		RBTree <Integer> rbtree=new RBTree<>();
		for(int i=0;i<jobs.size();i++) {
			rbtree.insert(jobs.get(i).processId); // insert the JobID into the RBTree.
		}
		int Job_ID=0;
		Job_ID=Integer.parseInt(JobID);
		int count=2000;
		for(int i=0;i<node_count;i++) {
			if(jobs.get(i).processId==Job_ID) {
				count=i;
				break;
			}
		}
		if(count==2000) {
			if(rbtree.minimum()==null) {
				// tree is empty
				//System.out.println("(0,0,0)");
				output.write("(0,0,0)");
				output.write(System.getProperty("line.separator"));
			}else {
				int B =rbtree.minimum();
				if(B>=Job_ID) {
					// couldnot find
					//System.out.println("(0,0,0)");
					output.write("(0,0,0)");
					output.write(System.getProperty("line.separator"));
				}else {
					int C=0;
					while(B<Job_ID) {
						if(rbtree.next(B)==null) {
							// B is the biggest one
							break;
						}
						C=rbtree.next(B);
						B=C;
					}
					count=B;
					for(int i=0;i<jobs.size();i++) {
						if(jobs.get(i).processId==count) {
							count=i;
							break;// get the index
						}
					}
					int E=0;
					if(rbtree.previous(jobs.get(count).processId)==null) {
						E=rbtree.minimum();
					}else {
						E= rbtree.previous(jobs.get(count).processId);
					}
					for(int i=0;i<jobs.size();i++) {
						if(jobs.get(i).processId==E) {
							count=i;
							break;
						}
					}
					//System.out.println("global_time : "+global_time+" ("+processes.get(count).processId+","+processes.get(count).execTime+","+processes.get(count).totalTime+")");
					output.write("("+processes.get(count).processId+","+processes.get(count).execTime+","+processes.get(count).totalTime+")");
					output.write(System.getProperty("line.separator"));
				}
			}
			
		}else {
			if(jobs.get(count).processId<=rbtree.minimum()) {
				//System.out.println("(0,0,0)");
				output.write("(0,0,0)");
				output.write(System.getProperty("line.separator"));
			}
			else{
				int D=0;// changed here!!
				if(rbtree.previous(jobs.get(count).processId)==null) {
					D=rbtree.minimum();
				}else {
					D= rbtree.previous(jobs.get(count).processId);
				}
				for(int i=0;i<jobs.size();i++) {
					if(jobs.get(i).processId==D) {
						count=i;
						break;
					}
				}
			//System.out.println("global_time : "+global_time+" ("+processes.get(count).processId+","+processes.get(count).execTime+","+processes.get(count).totalTime+")");
			output.write("("+processes.get(count).processId+","+processes.get(count).execTime+","+processes.get(count).totalTime+")");
			output.write(System.getProperty("line.separator"));
			}
		}
		output.close();
	}
	public static void scheduler(ArrayList<Process>processes,ArrayList<Process>jobs,int flag) {
		////System.out.println("----------------Enter the scheduler--------------");
		if(node_count!=0 && flag==0) {// the tree is already built and the input file is not done
			MinHeap<Long>heap=new MinHeap<>();
			for(int i=0;i<node_count;i++) {
				try {
					heap.push(processes.get(i).execTime);// push the execution time into the tree
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			////System.out.println("The size of the heap 1 is "+heap.size());
			////System.out.println(heap.toString());// print the heap
			long e=heap.top();// find the top
			int job=0;
			for(int k=0;k<node_count;k++) {
				if(e==processes.get(k).execTime) {
					job=k;
				}
			}
			long left_time=processes.get(job).totalTime-e;
			if(left_time<=5) {
				global_time=global_time+left_time;// run the job and depatch it
				heap.pop();// delete the job in the heap
				//processes.set(job, new Process(hp,processes.get(job).processId,0,0));// change the total time to 0;
				processes.remove(job);// delete the record
				jobs.remove(job);
				node_count--;
			}else if(left_time>5) {
				global_time=global_time+5;
				heap.pop(); // delete the top 
				////System.out.println("the size of the heap is "+heap.size());
				processes.set(job,new Process(hp,processes.get(job).processId,processes.get(job).execTime+5,processes.get(job).totalTime));
				try {
					heap.push(processes.get(job).execTime);// reenter into the tree 
				}
				catch(Exception d) {
					d.printStackTrace();
				}
			}
		}
		if(flag==1) {  // there are no new jobs any more
			//System.out.println("----------------------------------------");
			//System.out.println("Job Finished!");			
		}
		if(flag==0 && node_count==0) {
			global_time++;
		}
		
	}
}
