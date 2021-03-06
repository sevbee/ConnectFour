/*
 * Board class contains all of the information about the game board
 */

public class Board {

	final static int PLAYER_ONE = 1;
	final static int PLAYER_TWO = -1;
	final static int EMPTY = 0;

	Point[][] grid;
	int[] heights;

	int cols;
	int rows;

	int[] moves;
	int lm;

	int cp;
	Point[][] cl; 

	
//	public static void main(String args[]) throws Exception {
//		Board test = new Board(5,5);
//		System.out.println(test);
//		test.makeMove(3);
//		System.out.println(test);
//		test.makeMove(4);
//		System.out.println(test);
//		test.makeMove(3);
//		System.out.println(test);
//		test.makeMove(4);
//		System.out.println(test);
//		test.makeMove(3);
//		System.out.println(test);
//		test.makeMove(4);
//		System.out.println(test);
//		test.makeMove(3);
//		System.out.println(test);
//	}

	Board(int columns, int inrows)
	{
		cols=columns;
		rows=inrows;
		grid=new Point[cols][rows];
		heights= new int[cols];
		moves=new int[cols*rows];
		lm=-1;
		for(int x =0; x<cols;x++)
		{
			heights[x]=0;
			for(int y=0;y<rows;y++)
				grid[x][y]=new Point(x,y,EMPTY); 

		}
		generateCL();
		cp=PLAYER_ONE;
	}

	void generateCL()
	{
		cl=new Point[69][4];
		int count=0;
		
		// Finds and stores all possible horizontal 'fours'
		for(int y=0;y<rows;y++)
		{
			for(int x=0;x<cols-3;x++)
			{
				Point[] temp = new Point[4];
				for(int i=x;i<x+4;i++)
					temp[i-x]=grid[i][y];
				cl[count]=temp;
				count++;
			}

		}

		// Finds and stores all possible vertical 'fours'.
		for(int x=0;x<cols;x++)
		{
			for(int y=0;y<rows-3;y++)
			{
				Point[] temp = new Point[4];
				for(int i=y;i<y+4;i++)
					temp[i-y]=grid[x][i];
				cl[count]=temp;
				count++;
			}

		}

		// Finds and stores all possible diagonal 'fours'
		for(int x=0;x<cols-3;x++)
		{
			for(int y=0;y<rows-3;y++)
			{
				Point[] temp = new Point[4];
				for(int t=x,i=y;t<x+4 && i<y+4;t++,i++)
					temp[i-y]=grid[t][i];
				cl[count]=temp;
				count++;
			}

		}
		//And the diagonal fours in the other direction
		for(int x=0;x<cols-3;x++)
		{
			for(int y=rows-1;y>rows-4;y--)
			{
				Point[] temp = new Point[4];
				for(int t=x,i=y;t<x+4 && i>-1;t++,i--)
					temp[t-x]=grid[t][i];
				cl[count]=temp;
				count++;
			}
		}


	}
	boolean validMove(int column)
	{
		return heights[column]<rows;
	}

	void makeMove(int column)
	{
		//Set point on the grid to relevant colour
		grid[column][heights[column]].setState(cp);
		//Increase height of relevant column
		heights[column]++;
		//Increment number of moves made
		lm++;
		//Record move made
		moves[lm]=column;
		//Change whose go it is
		cp=-cp;
	}

	void undoMove()
	{

		grid[moves[lm]][heights[moves[lm]]-1].setState(EMPTY);
		heights[moves[lm]]--;
		lm--;
		cp=-cp;
	}


	boolean validMovesLeft()
	{
		return lm<moves.length-1;
	}


	int winnerIs()
	{
		for(int i=0;i<cl.length;i++)
			if(getScore(cl[i])==4)
			{
				return PLAYER_ONE;
			}
			else if(getScore(cl[i])==-4)
				return PLAYER_TWO;
		return 0;

	}

	private int getScore(Point[] points) {
		int playerone=0;
		int playertwo=0;
		for(int i=0;i<points.length;i++)
			if(points[i].getState()==Board.PLAYER_ONE)
				playerone++;
			else if(points[i].getState()==Board.PLAYER_TWO)
				playertwo++;
		if((playerone+playertwo>0) && (!(playerone>0 && playertwo>0)))
		{
			return (playerone!=0)?playerone:playertwo;
		}
		else
			return 0;
	}


	int getStrength()
	{
		int sum=0;
		int[] weights = {0,1,10,50,600};
		for(int i=0;i<cl.length;i++)
		{
			sum+=(getScore(cl[i])>0)?weights[Math.abs(getScore(cl[i]))]:-weights[Math.abs(getScore(cl[i]))];
		}
		return sum+(cp==PLAYER_ONE?16:-16);
	}

	public String toString()
	{
		String temp = "";
		for(int y=rows-1;y>-1;y--){
			for(int x=0;x<cols;x++)
				if(grid[x][y].getState()==EMPTY)
					temp = temp + "-";
				else if(grid[x][y].getState()==PLAYER_ONE)
					temp = temp + "O";
				else
					temp = temp + "X";
			temp += "\n";
		}
		return temp;
	}


}