package sudokusolver;
import java.util.*;

public class sudokusolver {
	
	public static ArrayList<ArrayList<ArrayList<Integer>>> Eliminate (ArrayList<ArrayList<ArrayList<Integer>>> grid, int min){
		int max = 0; //to count number of spots found
		int size = 9;	//many things in the sudoku problem have size 9	
		
		for (int i = 0; i < size; i++){//for every spot
			for(int j = 0; j < size; j++){	//for every spot
				if(grid.get(i).get(j).size() > 1){	//if no solution has been found yet for this spot
					int [] square = DetermineSquare(i,j);	//determine the square in the sudoku
					
					for(int k = 0; k < size; k++){	//for every column/row				
						if(k != i && grid.get(k).get(j).size() == 1){//column search by scanning different rows in same column
							int x = grid.get(k).get(j).get(0);//value of the known spot							
							if(grid.get(i).get(j).size() > 1 && grid.get(i).get(j).contains(x)){//remove the possibility if it hasn't been removed yet
								grid.get(i).get(j).remove(grid.get(i).get(j).indexOf(x));								
							}							
						}						
						if(k != j && grid.get(i).get(k).size() == 1){//row search by scanning different columns in same row
							int x = grid.get(i).get(k).get(0); //value of the known spot
							if(grid.get(i).get(j).size() > 1 && grid.get(i).get(j).contains(x)){//remove the possibility if it hasn't been removed already
								grid.get(i).get(j).remove(grid.get(i).get(j).indexOf(x));
							}
						}
						
					}				
					for(int k = square[0]; k < square[1]; k++){ //the corresponding square
						for(int l = square[2]; l < square[3]; l++){
							if(k != i && l != j && grid.get(k).get(l).size() == 1){//search the corresponding sudoku 3x3 square
								int x = grid.get(k).get(l).get(0);//value of the known spot
								if(grid.get(i).get(j).size() > 1 && grid.get(i).get(j).contains(x)){//remove the possibility if it hasn't been removed already
									grid.get(i).get(j).remove(grid.get(i).get(j).indexOf(x));
								}									
							}
						}
					}
				
				}
				else{
					//size == 1
				}
			}			
		}
		for(int i = 0; i < size; i++){//for every spot
			for(int j = 0; j < size; j++){
				
				int s = grid.get(i).get(j).size();	//the amount of possibilities at the current spot			
				if(s > 1){//if no solution has been found yet
					int[] square = DetermineSquare(i,j); //determine the square in the sudoku:
					
					for(int k = 0; k < s; k++){	//for every possibility at the current spot					
						int x = grid.get(i).get(j).get(k); //get the value of the possibility
						int q = 0; //counter
						boolean z = false; //for when a solution has been found, to go the next spot
						
						for(int l = 0; l < size; l++){ //for every row
							if(l != i && !grid.get(l).get(j).contains(x)){	//if not the current spot && a spot in the same column does not contain this possibility
								q++; 
								if(q == 8){ //for every other spot in the column x is not a possibility, so current spot == x
									ArrayList<Integer> y = new ArrayList<Integer>(1); //set value of current spot to x
									y.add(x);
									grid.get(i).remove(j);
									grid.get(i).add(j,y);
									z = true;	//a solution has been found, move to the next spot								
								}
							}
							else if(l != i && grid.get(l).get(j).contains(x)){								
								break; //if x is a possibility somewhere else in the column, no need to continue scanning this column for this possibility
							}
						}
						if(z){	//= true iff a solution has been found for this spot						
							break; //move to next spot
						}
						q = 0; //reset counter
						for(int l = 0; l < size; l++){ //for every column
							if(l != j && !grid.get(i).get(l).contains(x)){ //if not the current spot && a spot in the same row does not contain x as possibility
								q++;
								if(q == 8){ //for every other spot in the row x is not a possibility, so current spot == x									
									ArrayList<Integer> y = new ArrayList<Integer>(1); //set value of current spot to x
									y.add(x);
									grid.get(i).remove(j);
									grid.get(i).add(j,y);
									z = true;	//a solution has been found for this spot, move on to the next spot								
								}
							}
							else if(l != j && grid.get(i).get(l).contains(x)){
								break; //if x is a possibility somewhere else in the row, no need to continue scanning this row for this possibility
							}
						}
						if(z){// = true iff a solution has been found for this spot
							break; //move to the next spot
						}
						q = 0;//reset counter
						boolean zz = false;						
						for(int l = square[0]; l < square[1]; l++){//for every square
							for(int m = square[2]; m < square[3]; m++){
								if(!(l == i && m == j) && !grid.get(l).get(m).contains(x)){		//if not the same square && x not a possibility in a spot							
									q++;
									if(q == 8){ //for every other spot in the square x is not a possibility so current spot == x								
										ArrayList<Integer> y = new ArrayList<Integer>(1); //set value of current spot to x
										y.add(x);
										grid.get(i).remove(j);
										grid.get(i).add(j,y);
										z = true;	//a solution has been found for this spot, move on to the next spot									
									}
								}
								else if(!(l == i && m == j) && grid.get(l).get(m).contains(x)){//if x is a possibility somewhere else in the square, stop
									zz = true; //move to next possibility
									break;
								}
							}
							if(zz){
								break; //move to next possibility 
							}
						}
						if(z){
							break; //if a solution has been found, move to the next spot
						}											
					}					
				}				
			}
		}
		for(int i = 0; i < size; i++){//count number of solutions, need 81 for solved sudoku
			for(int j = 0; j < size; j++){
				if(grid.get(i).get(j).size() == 1){
					max++;
				}			
			}		
		}			
		if(max != min && max < 81){		//if there has been progress and not a full solution				
			grid = Eliminate(grid,max); //eliminate possibilities
			//returns a false solution iff a wrong possiblity has been chosen, they get filtered out in line 192
			return grid;
		}
		else if(max == min && max < 81){ //if no progress has been made and not a full solution
		
			int few = 10;
			int a = 10;
			int b = 10;
			
			for(int i = 0; i < size; i++){//find spot with fewest possibilities
				for(int j = 0; j < size; j++){
					if(grid.get(i).get(j).size() > 1 && grid.get(i).get(j).size() < few){
						few = grid.get(i).get(j).size();
						a = i;
						b = j;
					}
				}
			}			
			for(int k = 0; k < few; k++){ //as long as no solution can be found, try the next possibility				
					
				ArrayList<Integer> y = new ArrayList<Integer>(1); //set value of current spot to the value of possibility k
				ArrayList<ArrayList<ArrayList<Integer>>> guess = Copy(grid); //create a copy of grid, so we can return to this point if needed				
				int x = guess.get(a).get(b).get(k); //get value of possibility k				
				y.add(x);				
				guess.get(a).remove(b);
				guess.get(a).add(b,y);						
				
				guess = Eliminate(guess, max+1); //solve the sudoku with the chosen possibility				
				if(CheckSolution(guess)){//if a solution has been found return it					
					return guess;					
				}					
			}	
		}		
		
		//max == 81, 1 digit at every spot			
		return grid; //returns a wrong 'solution' iff a wrong possibility has been 'guessed' (and gets filtered out in line 192)
	}
	public static int[] DetermineSquare (int i, int j){	
		/* to determine the location of the spot in the sudoku:
		 * 1 2 3
		 * 4 5 6	each a 3x3 square
		 * 7 8 9
		 */			
		int[] square = new int[4];						
		if(i < 3){//square 1,2,3
			square[0] = 0;
			square[1] = 3;
			if(j < 3){//square 1
				square[2] = 0;
				square[3] = 3;
			}
			else if(j < 6){//square 2
				square[2] = 3;
				square[3] = 6;
			}
			else{//square 3
				square[2] = 6;
				square[3] = 9;
			}					
		}
		else if(i < 6){//square 4,5,6
			square[0] = 3;
			square[1] = 6;
			if(j < 3){//square 4
				square[2] = 0;
				square[3] = 3;
			}
			else if(j < 6){//square 5
				square[2] = 3;
				square[3] = 6;
			}
			else{//square 6
				square[2] = 6;
				square[3] = 9;						
			}		
		}
		else{//square 7,8,9
			square[0] = 6;
			square[1] = 9;
			if(j < 3){//square 7
				square[2] = 0;
				square[3] = 3;						
			}
			else if(j < 6){//square 8
				square[2] = 3;
				square[3] = 6;						
			}
			else{//square 9
				square[2] = 6;
				square[3] = 9;
			}		
		}		
		return square;
	}
	
	public static ArrayList<ArrayList<ArrayList<Integer>>> Copy(ArrayList<ArrayList<ArrayList<Integer>>> grid){//create a deep copy of grid
		int size = 9;
		ArrayList<ArrayList<ArrayList<Integer>>> copy = new ArrayList<ArrayList<ArrayList<Integer>>>(size);		
		for(int i = 0; i < size; i++){			
			ArrayList<ArrayList<Integer>> row = new ArrayList<ArrayList<Integer>>(size);
			
			for (int j = 0; j < size; j++){
				row.add(new ArrayList<Integer>(size));				
			}
			copy.add(row);
		}
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				for(int k = 0; k < grid.get(i).get(j).size(); k++){
					int x = grid.get(i).get(j).get(k); //copy every possibility at every spot
					copy.get(i).get(j).add(x);				
				}
			}
	
		}
		return copy;
	}
	public static boolean CheckSolution(ArrayList<ArrayList<ArrayList<Integer>>> grid){//checks if the current grid is a valid solution
		int size = 9;		
		for(int i = 0; i < size; i++){//for every spot
			for(int j = 0; j < size; j++){
				if(grid.get(i).get(j).size() > 1){//if there is more than 1 possibility at a spot, no solution has been found yet
					return false;
				}
				else{
					int x = grid.get(i).get(j).get(0);
					int p = 0;
					int q = 0;
					for(int k = 0; k < size; k++){						
						if(k != i && !grid.get(k).get(j).contains(x)){//check if the same number is not somewhere else in the same column
							p++;
						}
						if(k != j && !grid.get(i).get(k).contains(x)){//check if the same number is not somewhere else in the same row
							q++;								
						}						
					}
					if(p < 8 || q < 8){//if the same number is somewhere else in the same column or row, no solution has been found
						return false;						
					}
					int[] square = DetermineSquare(i,j);
					int r = 0;
					for(int l = square[0]; l < square[1]; l++){
						for(int m = square[2]; m < square[3]; m++){
							if(!(l == i && m == j) && !grid.get(l).get(m).contains(x)){//check if the same number is not somewhere else in the same square
								r++;
							}
						}
					}
					if( r < 8){//if the same number is somewhere else in the same square, no solution has been found
						return false;
					}
				}
			}
		}
		
		return true;
	}
	public static int[][] Solve (int[][] input){
		int size = 9;		
		ArrayList<ArrayList<ArrayList<Integer>>> grid = new ArrayList<ArrayList<ArrayList<Integer>>>(size);		//creates a 9x9 grid of ArrayLists (ArrayList x ArrayList)
		for(int i = 0; i < size; i++){	//create a 9x9 grid of ArrayList(Integer)																		//with  an ArrayList of possibilities (integers) at the 81 spots
			ArrayList<ArrayList<Integer>> row = new ArrayList<ArrayList<Integer>>(size);
			
			for (int j = 0; j < size; j++){
				row.add(new ArrayList<Integer>(size));				
			}
			grid.add(row);
		}
		for(int i = 0; i < size; i++){//fill the grid with the given input
			for(int j = 0; j < size; j++){
				if(input[i][j] == 0){//if no value for a spot has been given, every integer from 1 to 9 is a possibility
					grid.get(i).remove(j);
					ArrayList<Integer> x = new ArrayList<Integer>(9);
					for(int k = 0; k < size; k++){
						x.add(k+1);
					}
					grid.get(i).add(j, x);						
				}
				else{//the value for the spot has been given, so only 1 possibility remains
					ArrayList<Integer> x = new ArrayList<Integer>(1);
					x.add(input[i][j]);	
					grid.get(i).remove(j);
					grid.get(i).add(j, x);	
				}
			}
	
		}		
		grid = Eliminate(grid,0); //solve the sudoku puzzle
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				input[i][j] = grid.get(i).get(j).get(0); //transform the solution back to int[][]
			}
		}
		return input;		//return solution	
	}
	public static void main(String[] args) {
		int[][] input = new int[9][9];	
		int[][] input2 = new int[9][9];
		
		/*easy sudoku
		input[0][0] = 5;
		input[0][1] = 3;
		input[0][4] = 7;
		
		input[1][0] = 6;
		input[1][3] = 1;
		input[1][4] = 9;		
		input[1][5] = 5;
		
		input[2][1] = 9;
		input[2][2] = 8;
		input[2][7] = 6;
		
		input[3][0] = 8;
		input[3][4] = 6;
		input[3][8] = 3;
		
		input[4][0] = 4;
		input[4][3] = 8;
		input[4][5] = 3;
		input[4][8] = 1;
		
		input[5][0] = 7;
		input[5][4] = 2;
		input[5][8] = 6;
		
		input[6][1] = 6;
		input[6][6] = 2;
		input[6][7] = 8;
		
		input[7][3] = 4;
		input[7][4] = 1;
		input[7][5] = 9;
		input[7][8] = 5;
		
		input[8][4] = 8;
		input[8][7] = 7;
		input[8][8] = 9;*/
		
		/*hard sudoku
		
		input[1][5] = 3;
		input[1][7] = 8;
		input[1][8] = 5;
		
		input[2][2] = 1;
		input[2][4] = 2;
		
		input[3][3] = 5;
		input[3][5] = 7;
		
		input[4][2] = 4;
		input[4][6] = 1;
		
		input[5][1] = 9;
		
		input[6][0] = 5;
		input[6][7] = 7;
		input[6][8] = 3;
		
		input[7][2] = 2;
		input[7][4] = 1;
		
		input[8][4] = 4;
		input[8][8] = 9;*/
		
		/* "guess" sudoku
		
		input[0][1] = 7;
		input[0][5] = 3;
		input[0][6] = 2;
		
		input[1][2] = 5;
		input[1][3] = 6;
		input[1][4] = 9;
		
		input[3][4] = 8;
		input[3][6] = 7;
		input[3][8] = 3;
		
		input[4][2] = 4;
		input[4][6] = 8;
		
		input[5][0] = 9;
		input[5][2] = 1;
		input[5][4] = 2;
		
		input[7][4] = 5;
		input[7][5] = 8;
		input[7][6] = 4;
		
		input[8][2] = 6;
		input[8][3] = 4;
		input[8][7] = 1;*/
		
		/* another guess sudoku
		
		input[0][1] = 3;
		input[0][2] = 4;
		input[0][5] = 6;
		
		input[1][3] = 5;
		input[1][6] = 2;
		
		input[3][1] = 1;
		input[3][4] = 4;
		input[3][7] = 3;
		input[3][8] = 6;
		
		input[4][0] = 5;
		input[4][3] = 2;
		
		input[5][3] = 7;
		
		input[6][0] = 2;
		input[6][6] = 8;
		
		input[7][4] = 1;
		input[7][5] = 3;
		
		input[8][0] = 7;*/
		
		
		/*and another 'guess' sudoku*/
		input[0][0] = 3;
		input[0][1] = 1;
		input[0][3] = 6;
		
		input[1][2] = 2;
		
		input[2][1] = 8;
		input[2][4] = 9;
		input[2][6] = 7;
		input[2][7] = 6;
		
		input[3][5] = 5;
		
		input[4][1] = 6;
		input[4][4] = 1;
		input[4][7] = 9;
		
		input[5][3] = 4;
		
		input[6][1] = 9;
		input[6][2] = 8;
		input[6][4] = 6;
		input[6][7] = 3;
		
		input[7][6] = 5;
		
		input[8][5] = 7;
		input[8][7] = 4;
		input[8][8] = 2;
	
	
		
		System.out.println("Input:");
		for(int i = 0; i < 9; i++){//print input sudoku
			for(int j = 0; j < 9; j++){
				System.out.print(input[i][j] + "\t");
			}
			System.out.print("\n");
		}
		System.out.println("\n\n\nSolution:");
		input = Solve(input); //solve the sudoku
		
		
		for(int i = 0; i < 9; i++){//print solution
			for(int j = 0; j < 9; j++){
				System.out.print(input[i][j] + "\t");
			}
			System.out.print("\n");
		}
		
	}

}
