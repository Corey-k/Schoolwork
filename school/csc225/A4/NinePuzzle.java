/* NinePuzzle.java
   CSC 225 - Spring 2017
   Assignment 4 - Template for the 9-puzzle
   
   This template includes some testing code to help verify the implementation.
   Input boards can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
	java NinePuzzle
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. boards.txt), run the program with
    java NinePuzzle boards.txt
	
   The input format for both input methods is the same. Input consists
   of a series of 9-puzzle boards, with the '0' character representing the 
   empty square. For example, a sample board with the middle square empty is
   
    1 2 3
    4 0 5
    6 7 8
   
   And a solved board is
   
    1 2 3
    4 5 6
    7 8 0
   
   An input file can contain an unlimited number of boards; each will be 
   processed separately.
  
   B. Bird    - 07/11/2014
   M. Simpson - 11/07/2015
*/


/*
	Author			: Corey Koelewyn	
	Student Number	: V00869081
*/



import java.util.Scanner;
import java.io.File;
import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;


public class NinePuzzle{

	//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
	public static final int NUM_BOARDS = 362880;
	public static final int FLAGS = 6; //one for each direction, 1 for visited, 1 for parent;
	/* 	0: visited
		1: parent
	 	2: up
	 	3: down
	 	4: left
	 	5: right
	*/
	 public static final int up    = 1;
	 public static final int down  = 2;
	 public static final int left  = 3;
	 public static final int right = 4;
	/*  SolveNinePuzzle(B)
		Given a valid 9-puzzle board (with the empty space represented by the 
		value 0),return true if the board is solvable and false otherwise. 
		If the board is solvable, a sequence of moves which solves the board
		will be printed, using the printBoard function below.
	*/


	/*
		swap
	*/
	public static int swap(int[][] g, int parent, int dir){
		int[][] A = getBoardFromIndex(parent);
		//find 0
		int row = -1;
		int col = -1;
		zeroFind:
		for(row = 0; row < 3; row++){
			for(col = 0; col < 3; col++){
				if(A[row][col] == 0) break zeroFind;
			}
		}
		// System.out.println("row =" + row);
		// System.out.println("col ="+ col);
		switch(dir){
			case up:	if(row == 0) return -1;
						A[row][col] = A[row-1][col];
						A[row-1][col] = 0;
						// printBoard(A);
						return getIndexFromBoard(A);
			case down:	if(row == 2) return -1;
						A[row][col] = A[row+1][col];
						A[row+1][col] = 0;
						// printBoard(A);
						return(getIndexFromBoard(A));
			case left:	if(col == 0) return -1;
						A[row][col] = A[row][col-1];
						A[row][col-1] = 0;
						// printBoard(A);
						return(getIndexFromBoard(A));
			case right:	if(col == 2) return -1;
						A[row][col] = A[row][col+1];
						A[row][col+1] = 0;
						// printBoard(A);
						return(getIndexFromBoard(A));

		}
		System.out.println("Something went wrong dude...");
		return -1;
	}
	public static void printHistory(int[][] g, int start){
		if(g[start][1] < 0) {
			System.out.println();
			return;
		}
		System.out.print(g[start][1] + " - ");
		printHistory(g,g[start][1]);
		return;
	}

	public static boolean SolveNinePuzzle(int[][] B){
		int[][] graph = new int[NUM_BOARDS][FLAGS];
		int start = getIndexFromBoard(B);
		Queue<Integer> q = new LinkedList<Integer>();
		Stack<Integer> s = new Stack<Integer>();

		graph[start][0] = 1;
		graph[start][1] = -2;
		// printBoard(B);
		int tmp;
		//System.out.println("s time");
		for(int dir = 1; dir < 5; dir++){
			graph[start][1+dir] = swap(graph, start, dir);
			if(graph[start][1+dir] >= 0) {
				q.add(graph[start][1+dir]);
				graph[graph[start][1+dir]][1] = start;
		//		System.out.println("P:" + start + " C:"+ graph[start][1+dir]);
			}
		}

		//System.out.println("Q time");
		int val = -1;
		while(!q.isEmpty()){
			tmp = q.remove();
			//printHistory(graph,tmp);
			if(graph[tmp][0] == 1) continue;
			graph[tmp][0] = 1;
		//	System.out.println("p: " + tmp);
			for(int dir = 1; dir < 5; dir++){

				val = swap(graph, tmp, dir);
				// System.out.println(dir+1);
				graph[tmp][1+dir] = val;
				if(val >= 0) {
					if(graph[val][0] == 1) continue;
					q.add(val);
					graph[val][1] = tmp;
				//	System.out.println("|- " + val);
				}	
				if(graph[tmp][1+dir] == 0) {

					while (val >= 0){
					 	s.push(val);
						val = graph[val][1];

					}
					while(!s.isEmpty()){
						printBoard(getBoardFromIndex(s.pop()));
					}

					return true;
				}
			}
			// System.out.println(tmp);
		}
		return false;
		
	}
	
	/*  printBoard(B)
		Print the given 9-puzzle board. The SolveNinePuzzle method above should
		use this method when printing the sequence of moves which solves the input
		board. If any other method is used (e.g. printing the board manually), the
		submission may lose marks.
	*/
	public static void printBoard(int[][] B){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				System.out.printf("%d ",B[i][j]);
			System.out.println();
		}
		System.out.println();
	}
	
	
	/* Board/Index conversion functions
	   These should be treated as black boxes (i.e. don't modify them, don't worry about
	   understanding them). The conversion scheme used here is adapted from
		 W. Myrvold and F. Ruskey, Ranking and Unranking Permutations in Linear Time,
		 Information Processing Letters, 79 (2001) 281-284. 
	*/
	public static int getIndexFromBoard(int[][] B){
		int i,j,tmp,s,n;
		int[] P = new int[9];
		int[] PI = new int[9];
		for (i = 0; i < 9; i++){
			P[i] = B[i/3][i%3];
			PI[P[i]] = i;
		}
		int id = 0;
		int multiplier = 1;
		for(n = 9; n > 1; n--){
			s = P[n-1];
			P[n-1] = P[PI[n-1]];
			P[PI[n-1]] = s;
			
			tmp = PI[s];
			PI[s] = PI[n-1];
			PI[n-1] = tmp;
			id += multiplier*s;
			multiplier *= n;
		}
		return id;
	}
		
	public static int[][] getBoardFromIndex(int id){
		int[] P = new int[9];
		int i,n,tmp;
		for (i = 0; i < 9; i++)
			P[i] = i;
		for (n = 9; n > 0; n--){
			tmp = P[n-1];
			P[n-1] = P[id%n];
			P[id%n] = tmp;
			id /= n;
		}
		int[][] B = new int[3][3];
		for(i = 0; i < 9; i++)
			B[i/3][i%3] = P[i];
		return B;
	}
	

	public static void main(String[] args){
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */

		
		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read boards until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading board %d\n",graphNum);
			int[][] B = new int[3][3];
			int valuesRead = 0;
			for (int i = 0; i < 3 && s.hasNextInt(); i++){
				for (int j = 0; j < 3 && s.hasNextInt(); j++){
					B[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < 9){
				System.out.printf("Board %d contains too few values.\n",graphNum);
				break;
			}
			System.out.printf("Attempting to solve board %d...\n",graphNum);
			long startTime = System.currentTimeMillis();
			boolean isSolvable = SolveNinePuzzle(B);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			if (isSolvable)
				System.out.printf("Board %d: Solvable.\n",graphNum);
			else
				System.out.printf("Board %d: Not solvable.\n",graphNum);
		}
		graphNum--;
		System.out.printf("Processed %d board%s.\n Average Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>1)?totalTimeSeconds/graphNum:0);

	}

}