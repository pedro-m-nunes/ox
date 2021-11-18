import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

// adicionar sons | fazer 'x' não aparecer ao redor de 'o' | fazer mensagem de dicas só aparecer quando for a primeira vez que o aplicativo foi aberto ou somente a cada tempo
// ver quais variáveis realmente precisam ser globais | testar melhor sistema do isInOSurroundings()

// Pedro Müller Nunes, 2021

public class ox implements ActionListener {
	private Random rng = new Random();
	private Set<Integer> keysBeingPressed = new HashSet<Integer>();
	
	private Color backgroundColor = Color.decode("#2C2F33");
	private Color textColor = Color.WHITE;
	private String textColorName = "White";
	
	private char ch = 'o'; // 'o' 'ó' 'ò' 'õ' 'ô' 'ö' 'ø' | 'O' 'Ó' 'Ò' 'Õ' 'Ô' 'Ö' 'Ø'
	private boolean uppercase = false, rgbMode = false;
	private String map;
	private JLabel mapLabel = new JLabel(map, JLabel.CENTER); //tabuleiro que aparece na tela (em texto)
	
	private int posArraySize = 7; // de 3 a 9, preferencialmente 7
	private char[][] pos = new char[posArraySize][posArraySize]; // [7][7]
	private final int frameSize = (int) (((posArraySize + 1) / 2.0) * 100) + 80; // 480
	
	private int chY = pos.length / 2;
	private int chX = pos[0].length / 2;
	
	private int objY = rng.nextInt(pos.length);
	private int objX = rng.nextInt(pos[0].length);
	
	private int points = 0, moves = 0;
	private JLabel pointsLabel = new JLabel("x " + points, JLabel.CENTER);
	private JLabel movesLabel = new JLabel("> " + moves, JLabel.CENTER);
	
	private JButton bNorth = new JButton("^"); // \u02C4
	private JButton bSouth = new JButton("v"); // \u02C5
	private JButton bWest = new JButton("<"); // \u02C2
	private JButton bEast = new JButton(">"); // \u02C3
	
//	//cria tabuleiro de diferentes tamanhos | NÃO FUNCIONANDO
//	private ox(int size) {
//		if(size >= 3 && size <= 9) {
//			posArraySize = size;
//			pos = Arrays.copyOf(pos, size);
//			for(char[] p : pos)
//				p = Arrays.copyOf(p, size);
//		}
//		new ox();
//	}
	
	private ox() {
		JFrame frame = new JFrame("o x");
		
		try {
			ImageIcon logo = new ImageIcon("src/ox_logo.png");
			frame.setIconImage(logo.getImage());
		} catch(Exception e) {}
		
		//janela
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameSize, frameSize);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		
		//área/painel central
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(backgroundColor);
		frame.add(panel, BorderLayout.CENTER);
		
		//tabuleiro
		mapLabel.setForeground(textColor);
		mapLabel.setFont(new Font("Consolas", 0, 40));
		mapLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(mapLabel);
		
		//contador de pontos
		pointsLabel.setForeground(textColor);
		pointsLabel.setFont(new Font("Consolas", 0, 17));
		pointsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(pointsLabel);
		
		//contador de movimentos
		movesLabel.setForeground(textColor);
		movesLabel.setFont(new Font("Consolas", 0, 12));
		movesLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(movesLabel);
		
		//botões de direções
		directionButtonCommonConfig(bNorth);
		directionButtonCommonConfig(bSouth);
		directionButtonCommonConfig(bWest);
		directionButtonCommonConfig(bEast);
		
		frame.add(bNorth, BorderLayout.PAGE_START);
		frame.add(bSouth, BorderLayout.PAGE_END);
		frame.add(bWest, BorderLayout.LINE_START);
		frame.add(bEast, BorderLayout.LINE_END);
		
//		UIManager ui = new UIManager();
//		ui.put("OptionPane.background", backgroundColor);
//		ui.put("Panel.background", backgroundColor);
//		ui.put("OptionPane.messageFont", new Font("Consolas", Font.PLAIN, 12));
//		ui.put("OptionPane.buttonFont", new Font("Consolas", Font.PLAIN, 12));
//		ui.put("OptionPane.foreground", textColor);
		
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent ke) {}

			@Override
			public void keyPressed(KeyEvent ke) {
				if(!keysBeingPressed.contains(ke.getKeyCode())) {
					// w = 87 | s = 83 | a = 65 | d = 68
					// arrowup = 38 | arrowdown = 40 | arrowleft = 37 | arrowright = 39
					if(ke.getKeyCode() == 87 || ke.getKeyCode() == 38) { // w '^'
						bNorth.doClick();
					} else if(ke.getKeyCode() == 83 || ke.getKeyCode() == 40) { // s 'v'
						bSouth.doClick();
					} else if(ke.getKeyCode() == 65 || ke.getKeyCode() == 37) { // a '<'
						bWest.doClick();
					} else if(ke.getKeyCode() == 68 || ke.getKeyCode() == 39) { // d '>'
						bEast.doClick();
					}
					
					keysBeingPressed.add(ke.getKeyCode());
				}
			}

			@Override
			public void keyReleased(KeyEvent ke) {
				keysBeingPressed.remove(ke.getKeyCode());
				
				switch(ke.getKeyCode()) { // esc = 27 | f1 = 112 | f2 = 113 | f3 == 114
				case 112: // f1 | customizar cor do texto
					try {
						if(!rgbMode) {
							String c = (String) JOptionPane.showInputDialog(frame, "Font color", frame.getTitle(), JOptionPane.PLAIN_MESSAGE, null, 
									new String[]{"White", "Red", "Blue", "Yellow", "Nebula Green", "Kelly Green", "Carrot Orange", "Clementis Violet"}, 
									textColorName);
							String cHex;
							
							switch(c) { //https://www.computerhope.com/htmcolor.htm
								case "Red": cHex = "#FF0000"; break;
								case "Blue": cHex = "#0000FF"; break;
								case "Yellow": cHex = "#FFFF00"; break;
								case "Nebula Green": cHex = "#59E817"; break;
								case "Kelly Green": cHex = "#4CC552"; break;
								case "Carrot Orange": cHex = "#F88017"; break;
								case "Clementis Violet": cHex = "#842DCE"; break;
								
								default: cHex = "#FFFFFF"; c = "White"; break;
							}
							
							textColorName = c;
							textColor = Color.decode(cHex);
							updateComponentsTextColor(textColor);
						} else {
							JOptionPane.showMessageDialog(frame, "Cannot change color on RGB Mode.\nTo turn it off, use 'F2'.", frame.getTitle(), -1, null);
						}
					} catch(Exception e) {}
					break;
				
				case 113: // f2 | modo RGB
					try {
						String o = (String) JOptionPane.showInputDialog(frame, "RGB Mode", frame.getTitle(), -1, null, new String[] {"Off", "On"}, rgbMode ? "Off" : "On");

						switch(o) {
							case "On": rgbMode = true; updateComponentsTextColor(new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256))); break;
							default: rgbMode = false; updateComponentsTextColor(textColor); break;
						}
					} catch(Exception e) {}
					break;
				}
			}
		});
		
		while(chY == objY && chX == objX) {
			objY = rng.nextInt(pos.length);
			objX = rng.nextInt(pos[0].length);
		}
		
		draw();
		
		JOptionPane.showMessageDialog(frame, "Press 'F1' to change the font color.\nPress 'F2' to toggle RGB Mode.", frame.getTitle(), -1, null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) { //move o personagem
		if(e.getSource() == bNorth) {
			chY--;
		} else if(e.getSource() == bSouth) {
			chY++;
		} else if(e.getSource() == bWest) {
			chX--;
		} else if(e.getSource() == bEast) {
			chX++;
		}
		
		//para sair do outro lado
		if(chY < 0)
			chY = pos.length - 1;
		else if(chX < 0)
			chX = pos[0].length - 1;
		else if(chY >= pos.length)
			chY = 0;
		else if(chX >= pos[0].length)
			chX = 0;
			
		moves++;
		movesLabel.setText(((JButton) e.getSource()).getText() + " " + moves);
		
		if(chY == objY && chX == objX) { //quando o personagem chega ao objetivo
			while(chY == objY && chX == objX) { //não deixa o 'x' aparecer na mesma posição do 'o' ou nos arredores
				objY = rng.nextInt(pos.length);
				objX = rng.nextInt(pos[0].length);
			}
			
			if(rgbMode)
				updateComponentsTextColor(new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256)));
			
			points++;
			pointsLabel.setText("x " + points);
			
			switch(points) {
				case  75: ch = 'ö'; break;
				case 150: ch = 'õ'; break;
				case 225: ch = 'ô'; break;
				case 300: ch = 'ø'; break;
			}
		}
		
		if(uppercase == false && points == 0 && moves == 100)
			uppercase = true;
//		else if(points == 1111 && Character.isUpperCase(ch))
//			; // algo aqui
		
		draw();
		
	}
	
	//desenha o tabuleiro
	private void draw() {
		map = "<html>";
		
		for(int l = 0; l < pos.length; l++) {
			for(int c = 0; c < pos[0].length; c++) {
				if(l == chY && c == chX)
					pos[l][c] = ch;
				else if(l == objY && c == objX)
					pos[l][c] = 'x';
				else
					pos[l][c] = '-';
				
				if(uppercase)
					ch = Character.toUpperCase(ch);
				
				map += pos[l][c] + " ";
			}
			map += "<br/>";
		}
		
		map += "</html>";
		
//		System.out.println(map.replace("<html>", "\n").replace("<br/>", "\n").replace("</html>", "\n")); //para testes
		
		mapLabel.setText(map.replace("-", "<font color='#565051'>-</font>")); //#565051
		
	}
	
//	//mostra os arredores de 'o' (para spawn do 'x') | NÃO FUNCIONANDO
//	private boolean isInOSurroundings(int distance) {
//		if(pos.length <= 5 && pos[0].length <= 5)
//			return false;
//		
//		if(distance < 0 || distance > 3)
//			return false;
//		
//		int north = chY - distance;
//		int south = chY + distance;
//		int west = chX - distance;
//		int east = chX + distance;
//		
//		if(north < 0)
//			north = pos.length - 1;
//		if(west < 0)
//			west = pos[0].length - 1;
//		if(south >= pos.length)
//			south = 0;
//		if(east >= pos[0].length)
//			east = 0;
//		
//		boolean isNorth = objY == north && objX == chX;
//		boolean isSouth = objY == south && objX == chX;
//		boolean isWest = objY == chY && objX == west;
//		boolean isEast = objY == chY && objX == east;
//		
//		System.out.println("\nY " + chY + " | X " + chX);
//		System.out.println("N " + north + " | S " + south + " | W " + west + " | E " + east);
//		System.out.println("objYX " + objY + " | " + objX);
//		System.out.println(isNorth + "" + isSouth + isWest + isEast + "");
//		
//		return isNorth || isSouth || isWest || isEast;
//	}
	
	//configurações em comum dos botões de direções
	private void directionButtonCommonConfig(JButton button) {
		button.setForeground(textColor);
		button.setBackground(backgroundColor);
		button.setFont(new Font("Consolas", Font.BOLD, 15));
		button.setFocusable(false);
		button.addActionListener(this);
	}
	
	//muda a cor do texto dos componentes
	private void updateComponentsTextColor(Color color) {
		mapLabel.setForeground(color);
		pointsLabel.setForeground(color);
		movesLabel.setForeground(color);
		bNorth.setForeground(color);
		bSouth.setForeground(color);
		bWest.setForeground(color);
		bEast.setForeground(color);
	}
	
	public static void main(String[] args) {
		new ox();
	}
}
