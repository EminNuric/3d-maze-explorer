import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import javax.swing.*;

public class mazeGame extends Frame {   
	public static mazeGame mazeGames;
    public static double[] playerData = new double[7];

	public static String input = "nill";
	public static String pressed = "";
	public static boolean W = false;
	public static boolean A = false;
	public static boolean S = false;
	public static boolean D = false;
	public static boolean M = false;
	public static boolean N = false;
	public static boolean L = false;
    static int count = 0;
    static int yMax = 0;
    static int xMax = 0;
    static String[] array;
    static String[][] maze;
    //public static String[][] entityData = new String[4096][7];
    private final Timer timer1;
    
    private final int[][] imageData;
    public ImageDisplayCreate imageDisplay; 

	public class ImageDisplayCreate extends JPanel {

		private int[][] imageData;
	
		public ImageDisplayCreate(int[][] imageData) {
			this.imageData = imageData;
		}
        
		public void setImageData(int[][] imageData) {
			this.imageData = imageData;
		}
        
		public void render() {
			repaint();
		}
        
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			int width = imageData[0].length;
			int height = imageData.length;


			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D imageGraphics = image.createGraphics();
			imageGraphics.setColor(Color.WHITE);
			imageGraphics.fillRect(0, 0, image.getWidth(), image.getHeight());

			imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int grayValue = imageData[y][x];

                    int rgbValue = (grayValue << 16) | (grayValue << 8) | grayValue; // Grayscale to RGB

                    image.setRGB(x, y, rgbValue);
                    	
				}
			}

			AffineTransform at = new AffineTransform();
			at.translate(getWidth() / 2, getHeight() / 2);
			at.scale(1, 1);
			at.translate(-image.getWidth(this) / 2, -image.getHeight(this) / 2);
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(image, at, null);

        }

	}
    
	public void setDataFromThread(int[][] imageData) {
		SwingUtilities.invokeLater(() -> {
			imageDisplay.setImageData(imageData);
		});
	}
	public static void main(String args[]) throws URISyntaxException, IOException  {   
		mazeGames = new mazeGame();


        try{
            Scanner inputFile = new Scanner(new File ("maze.txt"));
            while (inputFile.hasNextLine()) {
            	inputFile.nextLine();
            	count++;
            }
			inputFile.close();
        
            array = new String[count];
            //reopen the file for input 
            inputFile = new Scanner(new File ("maze.txt"));
            for (int i = 0; i < count; i++){
                array[i] = inputFile.nextLine();
            }
			inputFile.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("file Not found: " + e);
        }
	    xMax = array[0].length();
	    yMax = array.length;
	    maze = new String[yMax][xMax];
    
        
		for (int i = 0+1; i < yMax+1; i++) {
		    for (int ii = 0+1; ii < xMax+1;ii++) {
			    if ((array[i-1]).charAt(ii-1) == 'X') {
				    maze[i-1][ii-1] = "█";
				
		    	} else {
		    		maze[i-1][ii-1] = ""+(array[i-1]).charAt(ii-1); 
		    	}
		    }
    	}
        playerData[0] = 46.0;
        playerData[1] = 10.0;
        playerData[2] = 0.0;

		ClientThread ct=new ClientThread(); 
		ct.start();
        
    }
    mazeGame() {  

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        
        imageData = new int[720][1280];
        
		for (int i = 0; i < imageData.length; i++) {
			for (int ii = 0; ii < imageData[i].length; ii++) {
                imageData[i][ii] = 50;
                if (i > 500) {
                    imageData[i][ii] = 100;
                }
			}
		}


		imageDisplay = new ImageDisplayCreate(imageData);
		imageDisplay.setBounds(30, 60, 1280, 720);
		add(imageDisplay);

		
		TextField t = new TextField();  
		t.setBounds(480,30,80,30);
		add(t); 
        
		timer1 = new Timer(20, (ActionEvent e) -> {
                    imageDisplay.render();
                    if (L == true) {
                        try {
                        int centerX = t.getLocationOnScreen().x + t.getWidth()/2;
                        int mouseX = MouseInfo.getPointerInfo().getLocation().x;
                        playerData[2] += ((centerX - mouseX) *-1)/10.0;
    
    
                        Robot robot = new Robot();
                        robot.mouseMove(centerX, t.getLocationOnScreen().y + t.getHeight()/2);
                        } catch (AWTException e1) {
                        }
                        
                    }
        });	
        timer1.start();

		
			
		KeyListener listener = new KeyListener() {
            @Override
			public void keyPressed(KeyEvent e) {
				
				int key = e.getKeyCode();
				String text = t.getText();
				if (key == KeyEvent.VK_ENTER) {
				input = text;
				t.setText("");
				}
				
				//left
				if (key == KeyEvent.VK_LEFT) {
					pressed = "a";
					A = true;
				}
				
				//up
				if (key == KeyEvent.VK_UP) {
					pressed = "w";
					W = true;
				}
				
				//right
				if (key == KeyEvent.VK_RIGHT) {
					pressed = "d";
					D = true;
				}
				
				//down
				if (key == KeyEvent.VK_DOWN) { 
					pressed = "s";
					S = true;
				}
				
				//M
				if (key == KeyEvent.VK_X) {
					M = true;
				}
				
				//N
				if (key == KeyEvent.VK_Z) { 
					N = true;
				}
				//L
				if(key == KeyEvent.VK_L) {
					L = true;
					
				}
				//L
				if (key == KeyEvent.VK_U) {
					L = false;
				}

				if (key == KeyEvent.VK_F10) { 
					System.exit(0);
				}
				
				if (key == KeyEvent.VK_A) {
					t.setText("");
					pressed = "a";
					A = true;

				} 
				if (key == KeyEvent.VK_W) {
					t.setText("");
					pressed = "w";
					W = true;

				} 
				if (key == KeyEvent.VK_D) {
					t.setText("");
					pressed = "d";
					D = true;

				} 
				if (key == KeyEvent.VK_S) {
					t.setText("");
					pressed = "s";
					S = true;
				} 
				
			}
            @Override
			public void keyReleased(KeyEvent e) {
				pressed = "";
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_LEFT) {
					A = false;
				}
				
				//up
				if (key == KeyEvent.VK_UP) {
					W = false;
				}
				
				//right
				if (key == KeyEvent.VK_RIGHT) {
					D = false;
				}
				
				//down
				if (key == KeyEvent.VK_DOWN) {
					S = false;
				}
				
				//M
				if (key == KeyEvent.VK_X) {
					M = false;
				}
				
				//N
				if (key == KeyEvent.VK_Z) {
					N = false;
				}

				//left
				if (key == KeyEvent.VK_A) {
					A = false;
				}

				//up
				if (key == KeyEvent.VK_W) {
					W = false;
				}
				
				//right
				if (key == KeyEvent.VK_D) {
					D = false;
				}
				
				//down
				if (key == KeyEvent.VK_S) {
					S = false;
				}
					
			}
            @Override
			public void keyTyped(KeyEvent event) {
			}
		};
		t.addKeyListener(listener);

		// frame size 300 width and 300 height    
		setSize(imageDisplay.getBounds().width + imageDisplay.getBounds().x + 50,imageDisplay.getBounds().height + imageDisplay.getBounds().y + 50);  

		// setting the title of Frame  
		setTitle("Maze game");   
		
		// no layout manager   
		setLayout(null);   

		// now frame will be visible, by default it is not visible    
		setVisible(true);


	}
}

class ClientThread extends Thread{  


    @Override
	public void run() {

		double moveSpeed = 0.1; // Speed of movement
		double turnSpeed = 5;   // Speed of turning
        while (true) { 
            outputThread ot=new outputThread(); 
            ot.start();
			try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
            if (mazeGame.A) {
                mazeGame.playerData[0] += moveSpeed * Math.cos(Math.toRadians(mazeGame.playerData[2] - 90));
                mazeGame.playerData[1] += moveSpeed * Math.sin(Math.toRadians(mazeGame.playerData[2] - 90));
            }
            if (mazeGame.D) {
                mazeGame.playerData[0] += moveSpeed * Math.cos(Math.toRadians(mazeGame.playerData[2] + 90));
                mazeGame.playerData[1] += moveSpeed * Math.sin(Math.toRadians(mazeGame.playerData[2] + 90));
            }
            if (mazeGame.W) {
                mazeGame.playerData[0] += moveSpeed * Math.cos(Math.toRadians(mazeGame.playerData[2]));
                mazeGame.playerData[1] += moveSpeed * Math.sin(Math.toRadians(mazeGame.playerData[2]));
            }
            if (mazeGame.S) {
                mazeGame.playerData[0] -= moveSpeed * Math.cos(Math.toRadians(mazeGame.playerData[2]));
                mazeGame.playerData[1] -= moveSpeed * Math.sin(Math.toRadians(mazeGame.playerData[2]));
            }
            if (mazeGame.N) {
                mazeGame.playerData[2] = mazeGame.playerData[2] - turnSpeed;
            }
            if (mazeGame.M) {
                mazeGame.playerData[2] = mazeGame.playerData[2] + turnSpeed;
            }
        }
    }
}

class outputThread extends Thread{ 

    @Override
	public void run() {
        int[][] imageOut = new int[720][1280];
        int width = imageOut[0].length;
        int height = imageOut.length;

        for (int y = 0; y < height-360; y++) {
            int test = (int) ((255.0/height)* y+0.0);
            for (int x = 0; x < width-1; x++) {
                imageOut[y][x] = (255-test)/2;
            }
        }
        for (int y = 360; y < height-1; y++) {
            int test = (int) ((255.0/height)* y+0.0);
            for (int x = 0; x < width-1; x++) {
                imageOut[y][x] = test/2;
            }
        }
        
        double playerX = mazeGame.playerData[0]; 
        double playerY = mazeGame.playerData[1]; 
        double playerR = mazeGame.playerData[2]; 

        int distance = 0;
        int range = 15;
        for (int x = 0; x < width-1; x++) {
            double angle = playerR + ((x - width / 2) * 90.0 / width);
            double angleRad = Math.toRadians(angle);
        
            double newX = range * Math.cos(angleRad);
            double newY = range * Math.sin(angleRad);

            for (int dis = 0; dis < 252; dis++) {
                if(distance == 0) {
                    double newerX = playerX + (newX/254*dis);
                    double newerY = playerY + (newY/254*dis);
                    if (mazeGame.maze[(int)Math.round(newerY-1)][(int)Math.round(newerX-1)].equals("█")) {
                        distance = dis;
                    }
                }
            }
            if (distance == 0) {
                distance = 254;
            }
            for (int y = 0; y < height-1; y++) {
                if (y > (distance) && y < height - (distance)) {
                    imageOut[y][x] = (254 - distance) ;
                    
                }
            }
            distance=0;
        }
        
        mazeGame.mazeGames.setDataFromThread(imageOut);
    }
}