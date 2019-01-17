
public class Rating {
	public static int rating(int list, int depth) {
		int counter = 0;
		counter+=rateAttack();
		counter+=rateMaterial();
		counter+=rateMoveability();
		counter+=ratePositional();
		Chess.flipboard();
		counter-=rateAttack();
		counter-=rateMaterial();
		counter-=rateMoveability();
		counter-=ratePositional();
		Chess.flipboard();
		return -(counter+depth*50);
	}
	public static int rateAttack() {
		return 0;
	}
	public static int rateMaterial() {
		int counter = 0, bishopCounter = 0;
		for(int i = 0;i<64;i++) {
			
			switch(Chess.board[i/8][i%8]){
				case "P": counter+=100;
				break;
				case "R": counter+=500;
				break;
				case "K": counter+=300;
				break;
				case "B": bishopCounter++;
				break;
				case "Q": counter+=900;
				break;
			}
		}
		if(bishopCounter >=2) {
			counter+=300*bishopCounter;
		}else {
			if(bishopCounter == 1) {
				counter += 250;
			}
		}
		
		return counter;
	}
	public static int rateMoveability() {
		return 0;
	}
	public static int ratePositional() {
		return 0;
	}
}
