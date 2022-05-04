import java.util.Random;

public class AI {
	
	static int nextMoveX;
	static int nextMoveY;
	static Random generator = new Random();
	static int DX[] = {-1, 1, 0, 0, -1, -1, 1, 1};
	static int DY[] = {0, 0, -1, 1, -1, 1, -1, 1};
	
	public static int RandomInt(int n){
		return generator.nextInt(n);
	}
	
	public static int getNextMoveX(){
		return nextMoveX;
	}
	
	public static int getNextMoveY(){
		return nextMoveY;
	}
	
	public static void createTable(int table[][], int N, int nSteps, int x[], int y[], int whoFirst){
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) table[i][j] = 0;// bang trong , chua co gì
		
		for (int i = 0; i < nSteps; i++)
			if (whoFirst == -1){
				if (i % 2 == 0) table[x[i]][y[i]] = -1; //x
				else
					table[x[i]][y[i]] = 1;// o
			}else
				if (i % 2 == 0) table[x[i]][y[i]] = 1; 
				else
					table[x[i]][y[i]] = -1;
	}
	
	public static int CheckWinner(int N, int nSteps, int x[], int y[], int whoFirst, int LengthWin){
		int table[][] = new int [N][N];
		createTable(table, N, nSteps, x, y, whoFirst);
		
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (table[i][j] != 0)
					for (int t = 0; t < 8; t++){
						int sum = 0;
						for (int k = 0; k < LengthWin; k++){
							int u = i + k * DX[t];
							int v = j + k * DY[t];
							if ((u >= 0) && (u < N) && (v >= 0) && (v < N)){
								if (table[u][v] != table[i][j]) break;
								sum += table[u][v];
							}else break;
						}
						if (sum == LengthWin) return 1;
						if (sum == - LengthWin) return -1;
					}
		
		return 0;
	}
	
	public static boolean SearchSum(int table[][], int N, int length, int sum){
		int FoundX = 0;
		int FoundY = 0;
		// tim trong table vi tri cua O
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (table[i][j] * sum >= 0) // hang i cot j la O
					for (int t = 0; t < 8; t++){ // tim trong 8 trang thai xung quanh
						boolean check = true;
						int s = 0;
						for (int k = 0; k < length; k++){ // kiem tra tra trang thai do co the la duong di tiep theo hay ko
							int u = i + k * DX[t]; // vi tri hang 
							int v = j + k * DY[t]; // vi tri cot 
							
							if ((u >= 0) && (u < N) && (v >= 0) && (v < N)){
								if (table[u][v] * sum >= 0){ // neu vi tri do trong hoac O da duoc danh
									s += table[u][v]; // do dai cua 1 duong O
									
									if (table[u][v] == 0){ // neu trong thi day la vi tri tiep theo
										FoundX = u;
										FoundY = v;
									}
								}else{
									check = false;
									break;
								}
							}else{
								check = false;
								break;
							}
						}
						
						if (check)
							if (s == sum){ // neu do dai dung bang sum thi danh o tiep theo 
								nextMoveX = FoundX;
								nextMoveY = FoundY;
								return true;
							}
					}
		return false;
	}
	
	public static void findNextMove(int N, int nSteps, int x[], int y[], int whoFirst, boolean hasAI){
		int table[][] = new int [N][N];
		createTable(table, N, nSteps, x, y, whoFirst);		
		
		// AI program here
		
		if (SearchSum(table, N, 5, 4)) return; // tim nuoc tan cong de thang
		if (SearchSum(table, N, 5, -4)) return; // tim nuoc chan
		
		if (SearchSum(table, N, 5, 3)) return; // tim nuoc tan cong
		if (SearchSum(table, N, 5, -3)) return;
		
		if (SearchSum(table, N, 4, -3)) return;
		if (SearchSum(table, N, 4, 3)) return;
		
		if (SearchSum(table, N, 3, 2)) return;
		if (SearchSum(table, N, 3, -2)) return;
		if (SearchSum(table, N, 2, 1)) return;
		if (SearchSum(table, N, 2, -1)) return;
		// tim nuoc di tiep theo
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (table[i][j] == 0){
					nextMoveX = i;
					nextMoveY = j;
					return;
				}
	}
	
}