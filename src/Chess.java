import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;

public class Chess {
	
	static String board[][] = {
			{"r","k","b","q","a","b","k","r"},
			{"p","p","p","p","p","p","p","p"},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{"P","P","P","P","P","P","P","P"},
			{"R","K","B","Q","A","B","K","R"}
		};
	static int globalDepth = 4;
	static int kingPosLower, kingPosUpper;
	
	public static void main(String args []) {
		JFrame f = new JFrame("Chess.IO");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UserInterface ui = new UserInterface();
		f.add(ui);
		f.setSize(500,500);
		f.setVisible(true);
		
		Object [] option = {"Computer","Human"};
		int humanAsWhite = JOptionPane.showOptionDialog(null, "Who should play the first move?", "ABC options", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
		if(humanAsWhite == 0) {
			flipboard();
			makeMove(alphaBeta(globalDepth,10000,-10000,"",0));
			flipboard();
			f.repaint();
			
		}
	}
	
	
	public static String alphaBeta(int depth, int beta, int alpha,String move, int player) {
		
		String list = possibleMoves();
		
		if(depth == 0 || list.length() == 0) {
			//System.out.println(Rating.rating(list.length(),depth)*(player*2-1));
			return move +(Rating.rating(list.length(),depth)*(player*2-1));
			
		}
		
		player = 1-player;
		
		for(int i = 0; i<list.length(); i+=5) {
			
			makeMove(list.substring(i, i+5));
			flipboard();
			String returnString = alphaBeta(depth-1,beta,alpha,list.substring(i,i+5),player);
			
			int value = Integer.valueOf(returnString.substring(5));
			flipboard();
			undoMove(list.substring(i,i+5));
			
			if(player == 0) {
				
				if(value<=beta) {
					beta = value;
					if(depth == globalDepth){
						move = returnString.substring(0,5);
					}
				}
			}else {
				if(value >alpha) {
					alpha = value;
					if(depth == globalDepth) {
						move = returnString.substring(0,5);
					}
				}
			}
			
			if(alpha >= beta) {
				if(player == 0) {
					return move+beta;
				}else {
					return move+alpha;
				}
			}
		}
		
		if(player==0) {
			return move+beta;
		}else {
			return move+alpha;
		}
		
	}
	
	
	public static int rating() {
		return 0;
	}
	
	
	public static void flipboard() {

		String temp;
		for(int i = 0; i<32;i++) {
			int r = i/8, c = i%8;
			
			if(Character.isUpperCase(board[r][c].charAt(0))) {
				temp = board[r][c].toLowerCase();
			}else {
				temp = board[r][c].toUpperCase();
			}
			if(Character.isUpperCase(board[7-r][7-c].charAt(0))) {
				board[r][c] = board[7-r][7-c].toLowerCase();
			}else {
				board[r][c] = board[7-r][7-c].toUpperCase();
			}
			board[7-r][7-c] = temp;
		}
		
		int kingTemp = kingPosLower;
		kingPosLower = 63- kingPosUpper;
		kingPosUpper = 63-kingTemp;
	}
	
	
	public static String possibleMoves(){
		String list = "";

		for(int i = 0; i<64; i++){
			switch(board[i/8][i%8]){
				case "P": list += possibleP(i);
					break;
				case "R": list += possibleR(i);
					break;
				case "K": list += possibleK(i);
					break;
				case "B": list += possibleB(i);
					break;
				case "Q": list += possibleQ(i);
					break;
				case "A": list += possibleA(i);
					break;
			}
		}
		return list;//x1,y1,x2,y2,piece captured
	}

	
	public static void makeMove(String move){
		
		if(move.charAt(4)!='P' ){
			// row_old, col_old, row_new, col_new, old_piece
			board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
			 board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
			 
			 if("A".equals(board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
				 kingPosUpper = 8*Character.getNumericValue(move.charAt(2)) + Character.getNumericValue(move.charAt(3));
			 }
		}else{
			//if pawn promotion
			//col_old, col_new, piece_captured, new_piece, promotion
			board[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3));
			board[1][Character.getNumericValue(move.charAt(0))] = " ";

		}
	}

	
	public static void undoMove(String move){

		if(move.charAt(4)!='P'){
			// row_old, col_old, row_new, col_new, old_piece
			board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
			 board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = String.valueOf(move.charAt(4));
			 if("A".equals(board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {
				 kingPosUpper = 8*Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
			 }

		}else{
			//if pawn promotion
			//col_old, col_new, piece_captured, new_piece, promotion
			board[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(2));
			board[1][Character.getNumericValue(move.charAt(0))] = "P";

		}
	}

	
	//Pawn's logic
	public static String possibleP(int i){
		String list = "", oldPiece;
		int r = i/8, c= i%8;

		for(int j = -1;j<=1;j+=2){

			try{ //Capture
				if(Character.isLowerCase(board[r-1][c+j].charAt(0)) && i>=16){
					oldPiece = board[r-1][c+j];
					board[r][c] = " ";
					board[r-1][c+j] = "P";

					if(isKingSafe()){
						// row_old, col_old, row_new, col_new, old_piece
						list = list + r+ c+ (r-1)+ (c+j)+oldPiece;
					}

					board[r-1][c+j] = oldPiece;
					board[r][c] = "P";
				}
			}catch (Exception e){}

			try{ //Capture and promotion 
				if(Character.isLowerCase(board[r-1][c+j].charAt(0)) && i<16){
					String [] temp = {"R","B","K","Q"};

					for(int k=0;k<4;k++){
						oldPiece = board[r-1][c+j];
						board[r][c] = " ";
						board[r-1][c+j] = temp[k];

						if(isKingSafe()){
							//col_old, col_new, piece_captured, new_piece, promotion
							list = list+c+(c+j)+oldPiece+temp[k]+"P";
						}
						board[r][c] = "P";
						board[r-1][c+j] = oldPiece;
					}
				}
			}catch(Exception e){}
		}

		try{ //move pawn one up
			if(" ".equals(board[r-1][c]) && i>=16){
				oldPiece = board[r-1][c];
				board[r][c] = " ";
				board[r-1][c] = "P";

				if(isKingSafe()){
					list = list+r+c+(r-1)+(c)+oldPiece;
				}

				board[r][c] = "P";
				board[r-1][c] = oldPiece;
			}
		}catch (Exception e){}

		try{ //no Capture and promotion
			if(" ".equals(board[r-1][c]) && i <16){
				String [] temp = {"Q","B","R","K"};

				for(int k = 0;k <4;k++){
					oldPiece = board[r-1][c];
					board[r-1][c] = temp[k];
					board[r][c] = " ";

					if(isKingSafe()){
						list = list + c+ c+oldPiece+temp[k]+"P";
					}

					board[r-1][c]= oldPiece;
					board[r][c] = "P";
				}
			}

		}catch (Exception e){}

		try{ //move pawn two up
			if(" ".equals(board[r-2][c])&& " ".equals(board[r-1][c]) && i>=48){
				oldPiece = board[r-2][c];
				board[r][c] = " ";
				board[r-2][c] = "P";

				if(isKingSafe()){
					list = list+r+c+(r-2)+(c)+oldPiece;
				}

				board[r][c] = "P";
				board[r-2][c] = oldPiece;
			}
		}catch (Exception e){}
		return list;
	}

	
	//Rookie's logic
	public static String possibleR(int i){
		String list = "",oldPiece;
		int r= i/8, c= i%8;
		
		for(int  j = -1;j<=1;j+=2){
			int temp = 1;
			try{
				while(" ".equals(board[r][c+temp*j])){
					oldPiece = board[r][c+temp*j];
					board[r][c+temp*j] = "R";
					board[r][c] = " ";
					if(isKingSafe()){
						list = list + r+c+r+(c+temp*j)+oldPiece;
					}
					board[r][c] = "R";
					board[r][c+temp*j] = oldPiece;
					temp++;
				}
				if(Character.isLowerCase(board[r][c+temp*j].charAt(0))){
					oldPiece = board[r][c+temp*j];
					board[r][c+temp*j] = "R";
					board[r][c] = " ";
					if(isKingSafe()){
						list = list + r+c+r+(c+temp*j)+oldPiece;
					}
					board[r][c] = "R";
					board[r][c+temp*j] = oldPiece;
				}
			}catch (Exception e){}
			temp = 1;

			try{
				while(" ".equals(board[r+temp*j][c])){
					oldPiece = board[r+temp*j][c];
					board[r+temp*j][c] = "R";
					board[r][c] = " ";
					if(isKingSafe()){
						list = list + r+c+(r+temp*j)+(c)+oldPiece;
					}
					board[r][c] = "R";
					board[r+temp*j][c] = oldPiece;
					temp++;
				}
				if(Character.isLowerCase(board[r+temp*j][c].charAt(0))){
					oldPiece = board[r+temp*j][c];
					board[r+temp*j][c] = "R";
					board[r][c] = " ";
					if(isKingSafe()){
						list = list + r+c+(r+temp*j)+(c)+oldPiece;
					}
					board[r][c] = "R";
					board[r+temp*j][c] = oldPiece;
				}
			}catch (Exception e){}

		}
		return list;
	}

	
	//Knight's logic
	public static String possibleK(int i){
		String list = "",oldPiece;
		int r = i/8, c = i%8;

		for(int j = -1;j<=1;j+=2){
			for(int k = -1;k<=1;k+=2){

				try{
					if(Character.isLowerCase(board[r+j][c+2*k].charAt(0)) || " ".equals(board[r+j][c+2*k])){
						oldPiece = board[r+j][c+2*k];
						board[r+j][c+2*k] = "K";
						board[r][c] = " ";

						if(isKingSafe()){
							list = list+r+c+(r+j)+(c+2*k)+oldPiece;
						}
						board[r+j][c+2*k] = oldPiece;
						board[r][c] = "K";
					}
				}catch (Exception e){}

				try{
					if(Character.isLowerCase(board[r+2*k][c+j].charAt(0)) || " ".equals(board[r+2*k][c+j])){
						oldPiece = board[r+2*k][c+j];
						board[r+2*k][c+j] = "K";
						board[r][c] = " ";

						if(isKingSafe()){
							list = list+r+c+(r+2*k)+(c+j)+oldPiece;
						}
						board[r+2*k][c+j] = oldPiece;
						board[r][c] = "K";
					}
				}catch (Exception e){}
			}
		}
		return list;
	}

	
	//Bishop's logic
	public static String possibleB(int i){
		String list = "", oldPiece;
		int r = i/8, c = i%8;
		int temp = 1;

		for(int k = -1;k<=1;k+=2){
			for(int j = -1;j<=1;j+=2){

				try{
					//move in certain direction as long as next position
					//is empty and king is safe
					while(" ".equals(board[r+k*temp][c+j*temp])){
						oldPiece = board[r+k*temp][c+j*temp];
						board[r][c] =" ";
						board[r+k*temp][c+j*temp] = "B";

						if(isKingSafe()){
							list = list+r+c+(r+k*temp)+(c+j*temp)+oldPiece;
						}
						board[r][c] = "B";
						board[r+k*temp][c+j*temp] = oldPiece;
						temp++;
					}
					//if piece at next position is opponent, replace it
					//and restore the original state of queen 
					if(Character.isLowerCase(board[r+k*temp][c+j*temp].charAt(0))){
						oldPiece = board[r+k*temp][c+j*temp];
						board[r][c] =" ";
						board[r+k*temp][c+j*temp] = "B";

						if(isKingSafe()){
							list = list+r+c+(r+k*temp)+(c+j*temp)+oldPiece;
						}
						board[r][c] = "B";
						board[r+k*temp][c+j*temp] = oldPiece;
					}

				}catch (Exception e){ }
				temp = 1;
			}
		}
		
		return list;
	}
	

	//Queen's logic
	public static String possibleQ(int i){

		String list = "", oldPiece;
		int r = i/8, c = i%8;
		int temp = 1;

		for(int k = -1;k<=1;k++){
			for(int j = -1;j<=1;j++){

				try{
					//move in certain direction as long as next position
					//is empty and king is safe
					while(" ".equals(board[r+k*temp][c+j*temp])){
						oldPiece = board[r+k*temp][c+j*temp];
						board[r][c] =" ";
						board[r+k*temp][c+j*temp] = "Q";

						if(isKingSafe()){
							list = list+r+c+(r+k*temp)+(c+j*temp)+oldPiece;
						}
						board[r][c] = "Q";
						board[r+k*temp][c+j*temp] = oldPiece;
						temp++;
					}
					//if piece at next position is opponent, replace it
					//and restore the original state of queen 
					if(Character.isLowerCase(board[r+k*temp][c+j*temp].charAt(0))){
						oldPiece = board[r+k*temp][c+j*temp];
						board[r][c] =" ";
						board[r+k*temp][c+j*temp] = "Q";

						if(isKingSafe()){
							list = list+r+c+(r+k*temp)+(c+j*temp)+oldPiece;
						}
						board[r][c] = "Q";
						board[r+k*temp][c+j*temp] = oldPiece;
					}

				}catch (Exception e){ }
				temp = 1;
			}
		}
		return list;
	}

	
	//King's Logic 
	public static String possibleA(int i){
		String list = "",oldPiece;
		int row = i/8, col = i%8;
		int tempKingPos;
		
		for(int j = 0;j<9;j++){
			if(j!=4){
				try{
					//if opponent is captured replace it
					//iff king is safe by the move
					if(Character.isLowerCase((board[row-1+j/3][col-1+j%3]).charAt(0)) || " ".equals(board[row-1+j/3][col-1+j%3])){
						oldPiece = board[row-1+j/3][col-1+j%3];
						
						board[row-1+j/3][col-1+j%3] = "A";
						board[row][col]= " ";
						tempKingPos = i;
						kingPosUpper = i+j/3*8+j%3-9;
						
						if(isKingSafe()){
							list = list + row+col+(row-1+j/3)+(col-1+j%3)+oldPiece;
							
						}
						board[row-1+j/3][col-1+j%3] = oldPiece;
						board[row][col] = "A";
						kingPosUpper = tempKingPos;
					}
				}catch (Exception e){}
			}
		}
		
		return list;
	}
	
	
	//If the king is safe after a particular piece movement
	public static boolean isKingSafe(){
		int temp = 1;

		//Opponent bishop and queen check
		for(int i = -1;i<=1;i+=2){
			for(int j = -1;j<=1;j+=2){

				try{
					while(" ".equals(board[kingPosUpper/8+i*temp][kingPosUpper%8 +j*temp])){++temp;}

					if("b".equals(board[kingPosUpper/8+i*temp][kingPosUpper%8 +j*temp]) ||
						"q".equals(board[kingPosUpper/8+i*temp][kingPosUpper%8+j*temp])){
						return false;
					}
				}catch (Exception e){}

				temp = 1;
			
			}
		}

		//Opponent Rookie and Queen check
		for(int i = -1;i<=1;i+=2){

			try{
				while(" ".equals(board[kingPosUpper/8][kingPosUpper%8+i*temp])){temp++;}

				if("r".equals(board[kingPosUpper/8][kingPosUpper%8+i*temp]) ||
					"q".equals(board[kingPosUpper/8][kingPosUpper%8+i*temp])){
					return false;
				}
			}catch (Exception e){}

			temp = 1;

			try{
				while(" ".equals(board[kingPosUpper/8+i*temp][kingPosUpper%8])){temp++;}

				if("r".equals(board[kingPosUpper/8][kingPosUpper%8+i*temp]) ||
					"q".equals(board[kingPosUpper/8][kingPosUpper%8+i*temp])){
					return false;
				}
			}catch (Exception e){}
			temp=1;
		}

		//look for opponent's pawn
		if(kingPosUpper>=16){

			try{
				if("p".equals(board[kingPosUpper/8-1][kingPosUpper%8-1])){
					return false;
				}
			}catch (Exception e){}

			try{
				if("p".equals(board[kingPosUpper/8 -1][kingPosUpper%8+1])){
					return false;
				}
			}catch (Exception e){}
		}

		//look for opponent's king area
		for(int i = -1;i<=1;i++){
			for(int j = -1;j<=1;j++){

				if(j!=0 || i!=0){
					try{
						if("a".equals(board[kingPosUpper/8+i][kingPosUpper%8+j])){
							return false;
						}
					}catch (Exception e){}
				}
			}
		}
		return true;
	}

}
