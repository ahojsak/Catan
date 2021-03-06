/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package catanui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import gamelogic.*;

/**
 *
 * @author jfedor
 */
public class ChatBar extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ActionListener, KeyListener {
    
    int _width = 1000-176;
    int _height = 200;
    int _smallheight = 105;
    
    LinkedList<String> text = new LinkedList<String>();
    
    public JTextField _textfield;

    public ClientGameBoard gameLogic;

    private boolean firstpaint = true;

    private int _scroll;

    public ChatBar(ClientGameBoard gl,JTextField tx) {
		gameLogic = gl;
        gameLogic._chatBar = this;
	_textfield = tx;
        addMouseListener(this);
        addMouseMotionListener(this);
	addMouseWheelListener(this);
	
    }
    
    public void keyTyped(KeyEvent e) {
	
    }
    public void keyReleased(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
	if (e.getKeyModifiersText(e.getModifiers()).equals("Alt")) {
	      int arg = 0;
	      try {
		arg = Integer.parseInt(e.getKeyText(e.getKeyCode()));
		
	      } catch (NumberFormatException ex) {
		return;
	      }
	      switch (arg) {
		    case 1: //settlement
			   gameLogic._sideBar._exchangers.get(0).steal();
			   break;
		    case 2: // road
			   gameLogic._sideBar._exchangers.get(1).steal();
			   break;
		    case 3: // dev
			   gameLogic._sideBar._exchangers.get(2).steal();
			   break;
		    case 4: // city
			   gameLogic._sideBar._exchangers.get(3).steal();
			   break;
		    default:
			    break;
	      }
	 }}

    @Override
    public void paint(Graphics g) {

		if (firstpaint) {

				g.setColor(Color.GRAY);
			g.fillRect(0, 0, _width, _height);
			
			g.setColor(new Color(200,200,200));
			g.fillRect(0, 5, _width - 5, _height - 10);
			

			}
		else {

			g.setColor(Color.GRAY);
			g.fillRect(0, 0, _width, _smallheight);
			
			g.setColor(new Color(200,200,200));
			g.fillRect(0, 5, _width - 5, _smallheight - 10);

			g.fillRect(_height - 5, 5, _width - 5, 5);
			


		}

		g.setColor(new Color(10,10,10));
		g.setFont(new Font("Arial", Font.BOLD, 15));
		int i = _scroll;
		int j = 0;
		synchronized(text) {
			while (i < text.size() && j < 4) {
				g.setColor(Road.getColorFromNumber(Integer.parseInt(text.get(text.size()-1-i).substring(0,1))));
				g.drawString(text.get(text.size()-1-i).substring(1), 10, (90-j*20));
				
				i++;
				j++;
			}
			_textfield.repaint();
		}

		firstpaint = false;
    }

    public void addLine(String s) {
		synchronized(text) {
			text.add(s);
			if (text.size() > 20)
				text.removeFirst();
        }
        repaint();
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        
    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent me) {
       
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }

    @Override
    public void mouseDragged(MouseEvent me) {
       
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
	_scroll -= e.getWheelRotation();
	_scroll = Math.min(_scroll,text.size());
	_scroll = Math.max(_scroll,0);
	repaint();

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
	if (ae.getActionCommand().equals("/exit")) {
		gameLogic.exit();
		System.exit(0);
	}
	else if (ae.getActionCommand().equals("/cheat")) {
		gameLogic._sideBar.addCard(BoardObject.type.DEV);
		gameLogic._sideBar.addCard(BoardObject.type.WHEAT);
		gameLogic._sideBar.addCard(BoardObject.type.SHEEP);
		gameLogic._sideBar.addCard(BoardObject.type.ORE);
		gameLogic._sideBar.addCard(BoardObject.type.WOOD);
		gameLogic._sideBar.addCard(BoardObject.type.BRICK);
		gameLogic._sideBar.repaint();
		((JTextField)ae.getSource()).setText("");
		return;
	}
	if (ae.getActionCommand().length() > 8 && ae.getActionCommand().substring(0,6).equals("/tell ")) {
	    int i;
	    for (i=6;i<ae.getActionCommand().length()-1;i++) {
		if (ae.getActionCommand().charAt(i) == ' ')
		    break;
	    }
	    addLine(gameLogic._playerNum+"(private to "+ae.getActionCommand().substring(6,i)+") "+gameLogic._name+": "+ae.getActionCommand().substring(i+1));
	    gameLogic.sendLinePrivate("(private to "+ae.getActionCommand().substring(6,i)+") "+gameLogic._name+": "+ae.getActionCommand().substring(i+1),
							  ae.getActionCommand().substring(6,i));
	}
	else {
	    addLine(gameLogic._playerNum + gameLogic._name+": "+ae.getActionCommand());
	    gameLogic.sendLine(gameLogic._name+": "+ae.getActionCommand());
	}
        ((JTextField)ae.getSource()).setText("");
    }

}
