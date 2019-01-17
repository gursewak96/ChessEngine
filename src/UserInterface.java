import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.Image;

import java.awt.Graphics;


public class UserInterface extends JPanel implements MouseListener, MouseMotionListener{

	static int x = 0, y = 0;
	static  int tileSize = 50;
	static int row, col, newRow, newCol;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent( g);
		this.setBackground(Color.yellow);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		for(int i = 0;i<64;i+=2) {
			g.setColor(new Color(255,200,100));
			g.fillRect((i%8+(i/8)%2)*tileSize, (i/8)*tileSize, tileSize,tileSize);
			g.setColor(new Color(150,50,30));
			g.fillRect(((i+1)%8-((i+1)/8)%2)*tileSize, ((i+1)/8)*tileSize, tileSize,tileSize);
		}
		
		Image chessPieces;
		chessPieces = new ImageIcon("../img/chess.png").getImage();
		int imgWidth = chessPieces.getWidth(this)/6;
		int imgHeight = chessPieces.getHeight(this)/2;
		for(int i = 0;i<64;i++) {
			int j = -1, k = -1;
			switch(Chess.board[i/8][i%8]) {
			case "P": j = 5; k = 1;
				break;
			case "R": j = 4; k = 1;
				break;
			case "B": j = 2; k = 1;
				break;
			case "K": j = 3; k = 1;
				break;
			case "Q": j = 1; k = 1;
				break;
			case "A": j = 0; k = 1;
				break;
			case "p": j = 5; k = 0;
				break;
			case "r": j = 4; k = 0;
				break;
			case "b": j = 2; k = 0;
				break;
			case "k": j = 3; k = 0;
				break;
			case "q": j = 1; k = 0;
				break;
			case "a": j = 0; k = 0;
				break;
			}
			
			if(j!=-1&&k!=-1) {
				g.drawImage(chessPieces, i%8*tileSize, (i/8)*tileSize, ((i%8)+1)*tileSize, ((i/8)+1)*tileSize, j*imgWidth, k*imgHeight, (j+1)*imgWidth, (k+1)*imgHeight, this);
			}
		}
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getX()<8*tileSize && e.getY()<8*tileSize) {
			row = e.getY()/tileSize;
			col = e.getX()/tileSize;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		String move = "";
		if(e.getX()<8*tileSize && e.getY()<8*tileSize) {
			newRow = e.getY()/tileSize;
			newCol = e.getX()/tileSize;
			
			if(e.getButton() == MouseEvent.BUTTON1) {
				if(newRow == 0 && row == 1 && "P".equals(Chess.board[row][col])) {
					move = ""+col+newCol+Chess.board[newRow][newCol]+"QP";
				}else {
					move = ""+row+col+newRow+newCol+Chess.board[newRow][newCol];
				}
			}
			String possibleMoves = Chess.possibleMoves();
			if(possibleMoves.replaceAll(move,"").length()<possibleMoves.length()) {
				Chess.makeMove(move);
				Chess.flipboard();
				Chess.makeMove(Chess.alphaBeta(Chess.globalDepth, 10000, -1000, "", 0));
				Chess.flipboard();
			}
		}
		repaint();
	}

}
