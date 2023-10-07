import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.Timer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;


public class FunctionArt
{

    public static void main(String args[])
    {
        JFrame frame = new JFrame("Function Art");
        FunctionArtPanel panel = new FunctionArtPanel();
        frame.getContentPane().add(panel);
        frame.setSize(1366, 700);
        frame.setLocation(0, 0);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class FunctionArtPanel extends JPanel
{
    private Font font; // used for normal sized text needed in JButtons
    private Font smallFont;// used for small sized text needed for JTextFields/Areas
    private Font titleFont;// Used for JLabels like titles
    private CardLayout cards;// The cardLayout used to show the frames
    private JPanel cardPanel;// panel holding the frames in the cardLayout
    private JPanel instructionsPanel;// a generic panel used for each of the panels in the
    								// required CardLayout Panel
    private Timer timer; // timer used to switch to the "Question Page" when time runs out
    private JTextField question; // used to show the question in the question panel
    private JTextArea answer; // used to show the answer in the question panel
    private JRadioButton[] answers;// used to show the multiple choices in the question panel
    private int chosenCorrectAnswerSet;
    private ButtonGroup bg;
    private String[][] multipleChoiceAnswers;
    private int currentLevel;

    public FunctionArtPanel()
    {
    	setLayout(new BorderLayout());
    	setBackground(Color.WHITE);
        font = new Font("Monospaced", Font.PLAIN, 30);
        smallFont = new Font("Monospaced", Font.PLAIN, 15);
        titleFont = new Font("Monospaced", Font.PLAIN, 50);
        cards = new CardLayout();
        cardPanel = new JPanel(cards);
        
        timer = null;
        question = new JTextField();
   		answer = new JTextArea(); 
   		answers = new JRadioButton[4];
   		bg = new ButtonGroup();
   		multipleChoiceAnswers = new String[3][4];
   		chosenCorrectAnswerSet = 0;
   		currentLevel = 1;

        		
        StartPagePanel spp = new StartPagePanel();
        // a for loop making instances of InfoPanel to add to the JPanel InfoPanel
        InfoPanel fip = new InfoPanel("F U N C T I O N S","FunctionInformation.txt", false, "functionImage.jpeg");
        instructionsPanel = new JPanel(cards);
        for(int i = 0; i < 3; i++)
        {
        	instructionsPanel.add(new InfoPanel("I N S T R U C T I O N S", "Instructions"+(i+1)+".txt",true,"image"+(i+1)+".jpeg.png" ),
        	 ("Page " + i));
        }
        GraphPanel gp = new GraphPanel();
        QuestionPanel qp = new QuestionPanel();
        
        cardPanel.add(spp, "Start Page");
        cardPanel.add(fip, "Function Info Page");
        cardPanel.add(instructionsPanel, "Instructions Page");
        cardPanel.add(gp, "Game Page");
        cardPanel.add(qp, "Question Page");
        add(cardPanel);
    }
    
    
    class StartPagePanel extends JPanel // makes the starting page
    {
    	private Image startingImage;
    	
        public StartPagePanel()
        {
            setLayout(null);
            setBackground(Color.WHITE);
            
            NavigationHandler nh = new NavigationHandler();
            
            JLabel startTitle = new JLabel("F U N C T I O N  A R T");
            JButton startButton = new JButton("START");
            JButton instructionButton = new JButton("INSTRUCTIONS");
            JButton functionButton = new JButton("FUNCTIONS");
            

            startTitle.setBounds(333, 60, 700, 40);
            startButton.setBounds(618,360, 130, 40);
            instructionButton.setBounds(100,520, 250, 40);
            functionButton.setBounds(1066,520, 200, 40);

            startTitle.setFont(titleFont);
            startButton.setFont(font);
            instructionButton.setFont(font);
            functionButton.setFont(font);
            
            startButton.addActionListener(nh);
            instructionButton.addActionListener(nh);
            functionButton.addActionListener(nh);
            
            startingImage = makeImage("StartingImage.jpeg.png");


            add(startTitle);
            add(startButton);
            add(instructionButton);
            add(functionButton);
        }
        
        public void paintComponent(Graphics g)
        {
        	g.drawImage(startingImage, 0, 0, 1366, 700,this);
        }
    }
    // reads and image, and sets an object to one
    public Image makeImage(String pictName)
    {
    	File pictFile = new File(pictName);
        Image image = null;
        try
        {
            image = ImageIO.read(pictFile);
    	}
            	
        catch (IOException e)
        {
            System.err.println("Cant get the file picture");
            e.printStackTrace();
        }
        return image;
    }

    class InfoPanel extends JPanel// makes a generic panel used both in the instructionsPanel and the functionPanel    
    {
    	private Image image;
    	
        public InfoPanel(String specificTitle, String fileName, boolean button, String imageName)
        {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);

            JPanel titlePanel = new JPanel();
            JPanel infoPanel = new JPanel(new GridLayout(1,2));

            titlePanel.setBackground(Color.WHITE);
            infoPanel.setOpaque(false);
            infoPanel.setBackground(Color.WHITE);


            // titlePanel Components
            JLabel title = new JLabel(specificTitle);
            title.setFont(titleFont);
            titlePanel.add(title);

            //infoPanel Components
            JPanel textPanel = new JPanel(new BorderLayout(0,0));
            textPanel.setBackground(Color.WHITE);
           	JPanel imagePanel = new JPanel();
            imagePanel.setOpaque(false);

                // textPanel Components
                JTextArea info = new JTextArea("", 10, 10);
                info.setLineWrap(true);
                info.setFont(smallFont);
                info.setEditable(false);
                JScrollPane scroller = new JScrollPane(info);
                
                //making image
            	image = makeImage(imageName);
            	
            	// reading a file
            	File inFile = new File(fileName);
            	Scanner reader = null;
            
            	try
            	{
                	reader = new Scanner(inFile);
            	}
            	catch (FileNotFoundException e)
            	{
                	System.err.println(fileName);
            	    System.exit(1);
            	}
            	
            	String text = new String("");
            	
            	while(reader.hasNext())
            	{
            		text += reader.nextLine()+"\n";
            	}

				info.setText(text);
            JPanel quitButtonPanel = new JPanel();
            quitButtonPanel.setBackground(Color.WHITE);

            // quitButtonPanel Components
            NavigationHandler nh = new NavigationHandler();
            JButton quitButton = new JButton("QUIT");
            quitButton.setFont(font);
            quitButton.addActionListener(nh);
            quitButtonPanel.add(quitButton);
            if(button)
            {
            	JButton next = new JButton("NEXT");
            	next.addActionListener(nh);
            	next.setFont(font);
            	quitButtonPanel.add(next);

            }

            textPanel.add(scroller, BorderLayout.CENTER);
            textPanel.add(quitButtonPanel, BorderLayout.SOUTH);
        	infoPanel.add(textPanel);
        	infoPanel.add(imagePanel);
        
        	add(titlePanel, BorderLayout.NORTH);
        	add(infoPanel, BorderLayout.CENTER);
            
        }
        
        public void paintComponent(Graphics g)
        {
        	g.drawImage(image, 683, 69, 683, 548,this);
        }

    }
    
    class QuestionPanel extends JPanel// makes the QuestionPanel
    {
    	private JTextArea answer;// The JTextArea where the answer is stored
    	private JPanel multipleChoicePanel;
    	private JPanel answerPanel;
    	private JButton back;
    	public QuestionPanel()
    	{
    		setLayout(new BorderLayout());
    		setBackground(Color.WHITE);
    		question = new JTextField("question?",50);
    		question.setEditable(false);
    		question.setFont(smallFont);
    		ButtonGroup bg = new ButtonGroup();
    		multipleChoicePanel = new JPanel(new GridLayout(1,2));
    		multipleChoicePanel.setBackground(Color.WHITE);
    		//multipleChoicePanel components
    		answerPanel = new JPanel(new GridLayout(5,1));
    		answerPanel.setBackground(Color.WHITE);
    		answer = new JTextArea("Answer");
    		answer.setLineWrap(true);
    		answer.setFont(font);
    		answer.setVisible(false);
    		answer.setEditable(false);
    		//questionsPanel components
            	JRadHandler jrh = new JRadHandler();
            	BackButtonHandler bbh = new BackButtonHandler();
            	for(int i = 0; i < 4; i++)
            	{
            		answers[i] = new JRadioButton("Answer " + (i+1));
            		answers[i].setFont(smallFont);
            		bg.add(answers[i]);
            		answers[i].addActionListener(jrh);
            		answerPanel.add(answers[i]);
            	}
            	
            	back = new JButton("BACK");
            	back.setFont(font);
            	back.addActionListener(bbh);
            	back.setEnabled(false);
            	answerPanel.add(back);

            multipleChoicePanel.add(answerPanel);
            multipleChoicePanel.add(answer);
    		add(question, BorderLayout.NORTH);
    		add(multipleChoicePanel,BorderLayout.CENTER);
    	}    	
    	class JRadHandler implements ActionListener // actionListener that makes answer visible
    	{
   			public void actionPerformed(ActionEvent evt)
        	{
        		String command = evt.getActionCommand();
        		answer.setVisible(true);
        		
        		if(command.equals(multipleChoiceAnswers[chosenCorrectAnswerSet][0]))
        		{
        			answer.setBackground(Color.GREEN);
        			for(int i = 0; i < 4; i++)
            		{
            			answers[i].setBackground(Color.GREEN);
            			answers[i].setEnabled(false);
            		}
            		setBackground(Color.GREEN);
            		question.setBackground(Color.GREEN);
            		answerPanel.setBackground(Color.GREEN);
            		multipleChoicePanel.setBackground(Color.GREEN);
            		answer.setText("Correct. Good job, that was a pretty hard question!");
        		}
        		
        		else
        		{
        			answer.setBackground(Color.RED);
        			for(int i = 0; i < 4; i++)
            		{
            			answers[i].setBackground(Color.RED);
            			answers[i].setEnabled(false);
            		}
            		setBackground(Color.RED);
            		question.setBackground(Color.RED);
            		answerPanel.setBackground(Color.RED);
            		multipleChoicePanel.setBackground(Color.RED);
            		answer.setText("Incorrect better luck next time.");
        		}
        		back.setEnabled(true);
        	}	
    	}
    	
    	class BackButtonHandler implements ActionListener // actionListener that makes answer visible
    	{
   			public void actionPerformed(ActionEvent evt)
        	{
        		String command = evt.getActionCommand();
        		cards.show(cardPanel, "Game Page");
                timer.start();
                answer.setVisible(false);
                answer.setBackground(Color.WHITE);
        			for(int i = 0; i < 4; i++)
            		{
            			answers[i].setBackground(Color.WHITE);
            			answers[i].setEnabled(true);
            		}
            		setBackground(Color.WHITE);
            		question.setBackground(Color.WHITE);
            		answerPanel.setBackground(Color.WHITE);
            		multipleChoicePanel.setBackground(Color.WHITE);
            		back.setEnabled(false);
                
        	}	
    	}
    }
      
    class GraphPanel extends JPanel
    {
     	private JMenuBar xShiftBar; // a JMenuBar that is used to shift the function sideways
        private JMenuBar xStretchBar;// a JMenuBar that is used to stretch the function sideways
        private JMenuBar functionBar;// a JMenuBar that is used to choose the function
        private JMenuBar yStretchBar;// a JMenuBar that is used to stretch the function up
        private JMenuBar yShiftBar;// a JMenuBar that is used to shift the function up
        private JMenuBar startDomain;// JMenuBar used to pick ? < x < 
        private JMenuBar endDomain;// JMenuBar used to pick < x < ?
        private String [] equationComp;// list used to store the equation changed by the user
        								// using the JMenuBars above
        private String [] currentEquationComp;// list used to store the equation changed by the user
        								// using the JMenuBars above before pressing submit
        private JTextField equation;// where the list equationComp is contained and read
        private JSlider zoom;// JSlider allowing you to zoom into the graph
        private JLabel timerLabel;// JLabel showing the timer
        private int specificTime;// storing the time from the the time
        private JLabel greaterDomain;// JLabel where ? < x <  is shown
        private JLabel lesserDomain;// JLabel where  < x <  ? is shown
        private int gridSpaces;// what the JSlider changes, used to draw the number of
        						// squares on the graph
        private int squareLength;// pixel measurment of a square on the graph
        private int pixelAtZeroX;// pixel where x = 0 is
        private int pixelAtZeroY;// pixel where y = 0 is
        private int domain1; //the number value of ? < x < used to interact with the paintComponent
        private int domain2;//the number value of < x <  ? used to interact with the paintComponent
        private int functionLoopCounter;// the number functions per function drawing
        private int savedDomain1;
        private int savedDomain2;
        private int currentSavedDomain1;
        private int currentSavedDomain2;
        private String[] savedCurrentEquationComp;
        private int chosenFunction;
        private String[][] sketchFunctions;
        private double[][] sketchFunctionsCoefficients;
        private int[][] sketchFunctionsConstraints;
        private double[] submitedFunctionsCoefficients;
        private JLabel encouragment;
        private int threeSeconds;
        private JLabel graphTitle;
        private boolean correctAnswer;
        private double trueCoefficent;
        private double answerCoefficent;
        private double trueHorizontalShift;
    	private double answerHorizontalShift;
    	private int randomLevel;
                       
    	public GraphPanel() 
    	{
            setLayout(new BorderLayout(0, 0));
            setBackground(Color.WHITE);
            
            gridSpaces = 15;
            pixelAtZeroX = 0;
            pixelAtZeroY = 0;
            domain1 = 0;
            domain2 = 0;
            savedDomain1 = -1*gridSpaces*2-30;
    		savedDomain2 = gridSpaces*2+30;
            currentSavedDomain1 = -1*gridSpaces*2-30;
        	currentSavedDomain2 = gridSpaces*2+30;
            functionLoopCounter = -1;
            chosenFunction = 0;
            sketchFunctions = null;
            sketchFunctionsCoefficients = null;
            sketchFunctionsConstraints = null;
            submitedFunctionsCoefficients = null;
            threeSeconds = 0;
            correctAnswer = true;
        	trueCoefficent = 0.0;
        	answerCoefficent = 0.0;
        	trueHorizontalShift = 0.0;
        	answerHorizontalShift = 0.0;
        	randomLevel = (int)(Math.random()*3);
            
            equationComp = new String[]{"", "", "", "", "x", "", "", "", ""};
            currentEquationComp = new String[]{"", "", "", "", "x", "", "", "", ""};
            savedCurrentEquationComp = new String[]{"", "", "", "", "x", "", "", "", ""};

            
            JPanel graphTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
            graphTitlePanel.setOpaque(false);

            //graphtitlePanel Components
            
            	graphTitle = new JLabel("G R A P H E D");
            	graphTitle.setFont(titleFont);
            	graphTitlePanel.add(graphTitle);
            	
            JPanel equationPanel = new JPanel(new BorderLayout(20, 20));
            equationPanel.setOpaque(false);
            
            //equationPanel Components
            
            	JPanel menuBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50,0));
            	menuBarPanel.setOpaque(false);
            	JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30,0));
            	textPanel.setOpaque(false);
            	JPanel domainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,0));
            	domainPanel.setOpaque(false);
            	
            	//part of domainPanel
            		startDomain = new JMenuBar();
            		endDomain = new JMenuBar();
            	
            	//menuBarPanel Components
            		xShiftBar = new JMenuBar();
            		xStretchBar = new JMenuBar();
            		functionBar = new JMenuBar();
            		yStretchBar = new JMenuBar();
            		yShiftBar = new JMenuBar();
            	
            		menuBarPanelSetter();
            	
            		menuBarPanel.add(xShiftBar);
            		menuBarPanel.add(xStretchBar);
            		menuBarPanel.add(functionBar);
            		menuBarPanel.add(yStretchBar);
            		menuBarPanel.add(yShiftBar);
            	// textPanel Components
            		SubmitHandler sh = new SubmitHandler();
            		NavigationHandler nh = new NavigationHandler();
					JButton submit = new JButton("SUBMIT");
            		JButton quit = new JButton("QUIT");
            		submit.setFont(smallFont);
            		quit.setFont(smallFont);
            		quit.addActionListener(nh);
            		submit.addActionListener(sh);
            	
            		equation = new JTextField("y = x",120);
                	equation.setEditable(false);
            		equation.setFont(smallFont);
            		textPanel.add(equation);
            		textPanel.add(submit);
            		textPanel.add(quit);
            	
            	// domainPanel Components
            		greaterDomain = new JLabel("");
            		greaterDomain.setFont(smallFont);
            		lesserDomain = new JLabel("");
            		lesserDomain.setFont(smallFont);
            		JLabel anX = new JLabel("x");
            		
            		anX.setFont(smallFont);
            		domainPanel.add(greaterDomain);
					domainPanel.add(startDomain);
					domainPanel.add(anX);
					domainPanel.add(endDomain);
            		domainPanel.add(lesserDomain);
            		
            	equationPanel.add(menuBarPanel, BorderLayout.CENTER);
            	equationPanel.add(textPanel, BorderLayout.SOUTH);
            	equationPanel.add(domainPanel, BorderLayout.EAST);


            // graphPanel
            JPanel graphPanel = new JPanel(new BorderLayout(0,0));
            graphPanel.setOpaque(false);
            
            TimerHandler th = new TimerHandler();
            timer = new Timer(1000, th);
            timerLabel = new JLabel("  00:30");
            timerLabel.setFont(font);
            specificTime = 30;
            
            zoom = new JSlider(5, gridSpaces*2, gridSpaces);
            zoom.setOrientation(JSlider.VERTICAL);
      		zoom.setMajorTickSpacing(5);
      		zoom.setPaintTicks(true);
      		zoom.setPaintLabels(true);
      		JSliderHandler jsh = new JSliderHandler();
      		zoom.addChangeListener(jsh);
            
            encouragment = new JLabel("");
            encouragment.setFont(font);
            encouragment.setVisible(false);
            sketchFunctions = getFunctions();
            
            graphPanel.add(zoom, BorderLayout.EAST);
        	graphPanel.add(timerLabel, BorderLayout.NORTH);
        	graphPanel.add(encouragment, BorderLayout.WEST);


            //Adding Panels
            add(graphTitlePanel, BorderLayout.NORTH);
            add(equationPanel, BorderLayout.SOUTH);
            add(graphPanel, BorderLayout.CENTER);

        }
        
        public void paintComponent(Graphics g)
        {
        	super.paintComponent(g);
        	sketchFunctionsCoefficients = new double[sketchFunctions.length][4];
        	sketchFunctionsConstraints = new int[sketchFunctions.length][4];
        	submitedFunctionsCoefficients = new double[4];
        	drawGraph(g);
        	for(int i = 0; i < sketchFunctions.length; i++)
        	{
        		g.setColor(Color.GRAY);
        		for(int index = 0; index < 9; index ++)
        		{
					currentEquationComp[index] = sketchFunctions[i][index];	
        		}
        		domain1 = Integer.parseInt(sketchFunctions[i][9]);
        		domain2 = Integer.parseInt(sketchFunctions[i][10]);
        		sketchFunctionsConstraints[i][0] = domain1;
        		sketchFunctionsConstraints[i][1] = domain2;
        	
        		if(i == chosenFunction)
        			g.setColor(Color.YELLOW);
        			sketchFunctionsCoefficients[i] = makeList(currentEquationComp);
        			drawFunction(g);
        	}
            
            domain1 = savedDomain1;
    		domain2 = savedDomain2;
            for(int i = 0; i < 9; i++)
            {
            	currentEquationComp[i] = savedCurrentEquationComp[i];
            }
            g.setColor(Color.BLACK);
            drawFunction(g);
        }

        public void drawFunction(Graphics g)
        {
        	double yCoor = 0.0;
        	double[] shifters = makeList(currentEquationComp);
        	for(double i = domain1; i < domain2; i+= 0.0005)
        	{
        		yCoor = makeDot(i, shifters);
        		drawDots(g,i,yCoor);
        	}
        }
        
        // used to get the functions for a picture and same them in a 2d array
        public String[][] getFunctions()
        {
        	String line = new String("");
        	String[][] levels = new String[3][3];
        	levels[0][0] = "Frown";
        	levels[0][1] = "Smile";
        	levels[0][2] = "Angry";
        	levels[1][0] = "Rainy Cloud";
        	levels[1][1] = "Stars in the Night";
        	levels[1][2] = "Bee Hive";
        	levels[2][0] = "Pirate Treasure";
        	levels[2][1] = "Pet Fish";
        	levels[2][2] = "Face";
        	String chosenLevel = new String(levels[currentLevel-1][randomLevel]);
        	graphTitle.setText(chosenLevel);
            File inFile = new File("FunctionDrawings.txt");
            Scanner reader = null;
            
            try
            {
                reader = new Scanner(inFile);
            }
            catch (FileNotFoundException e)
            {
                System.err.println("Cant read from file");
                System.exit(1);
            }
            
            while(!line.equals(chosenLevel+":"))
            {
                line = reader.nextLine();
            }
            
            line = reader.nextLine();
            
            while(line.indexOf("%") == -1 && reader.hasNext())
            {
            	line += " " + reader.nextLine();
            	functionLoopCounter++;
            }
            reader.close();
            int pos1 = 0;
            int pos2 = 0;
            String[][] functionsHolder = new String[functionLoopCounter][11];
            
        	for(int i = 0; i < functionLoopCounter; i++)
        	{
        		for(int index = 0; index < 11; index++)
        		{
        			pos2 = line.indexOf(" ", pos1);
        			functionsHolder[i][index] = line.substring(pos1, pos2);
        			if(functionsHolder[i][index].equals("null"))
        				functionsHolder[i][index] = "";
        			pos1 = pos2+1;
        		}
        		
        	}
        	return functionsHolder;
        }
        
        
        // used to drawGraph
        public void drawGraph(Graphics g) 
        {
			squareLength = 1366/(gridSpaces*2);
			Font reallySmallFont = new Font("Monospaced", Font.PLAIN, 8);
			
			if(gridSpaces > 10)
				g.setFont(reallySmallFont);
			else
				g.setFont(smallFont);
				
			g.setColor(Color.GRAY);
			
			pixelAtZeroX = gridSpaces*squareLength;
			pixelAtZeroY = gridSpaces/2 * squareLength;
			
			for(int i = 0; i < 1366; i+= squareLength)
			{
				if(i == pixelAtZeroX)
				{
					g.setColor(Color.RED);
					for(int i2 = -1; i2 < 1; i2++)
					{
						g.drawLine(i+i2, 0, i+i2, 700);
					}
					
				}
				g.drawLine(i, 0, i, 700);
				if((i/squareLength - gridSpaces)%2 == 0)
				{
					g.setColor(Color.BLACK);
					if(i/squareLength - gridSpaces < 1)
						g.drawString("" + (i/squareLength - gridSpaces), i-10, 340);
					else
						g.drawString("" + (i/squareLength - gridSpaces), i-5, 340);
				}
				g.setColor(Color.GRAY);
			}
			
			for(int i = 0; i < 700; i+= squareLength)
			{
				if(i == pixelAtZeroY)
				{
					g.setColor(Color.RED);
					for(int i2 = -1; i2 < 1; i2++)
					{
						g.drawLine(0, i+i2, 1366, i+i2);
					}
				}
				g.drawLine(0, i, 1366, i);
				if((i/squareLength - 700/(squareLength*2))%2 == 0)
				{
					g.setColor(Color.BLACK);
					g.drawString("" + -1*(i/squareLength - 700/(squareLength*2)), 
					gridSpaces*squareLength + 5, i+3);
				}
				g.setColor(Color.GRAY);
			}			
        }
        
        // used to make y-coordinate for the dot drawn
        public double makeDot(double xValue, double[] functionVariables) 
        {
            double yValue = 0;
            if (currentEquationComp[1].equals("sin"))
            {
                    yValue = (functionVariables[0] * (Math.sin(functionVariables[1] 
                    * xValue + functionVariables[2])) + functionVariables[3]);
            }
            else if (currentEquationComp[6].equals("|"))
            {
                    yValue = (functionVariables[0] * (Math.abs(functionVariables[1] 
                    * xValue + functionVariables[2])) + functionVariables[3]);
            }
            else if (currentEquationComp[7].equals("²"))
            {
                    yValue = (functionVariables[0] * (Math.pow(functionVariables[1] 
                    * xValue + functionVariables[2], 2)) + functionVariables[3]);
            }
            else if (currentEquationComp[7].equals("³"))
            {
                    yValue = (functionVariables[0] * (Math.pow(functionVariables[1] 
                    * xValue + functionVariables[2], 3)) + functionVariables[3]);
            }
            else if (currentEquationComp[1].equals("√"))
            {
                    yValue = (functionVariables[0] * (Math.sqrt(functionVariables[1] 
                    * xValue + functionVariables[2]) + functionVariables[3]));
            }
            else if (currentEquationComp[1].equals("/"))
            {
                    yValue = (functionVariables[0] * (1.0 / (functionVariables[1] 
                    * xValue + functionVariables[2])) + functionVariables[3]);
            }
            else
            {
                    yValue = (functionVariables[0] * (functionVariables[1] * xValue 
                    + functionVariables[2]) + functionVariables[3]);
            }
            return(yValue);
        }
        // condensed version of equationComp with doubles as elements instead of strings        
        public double[] makeList(String[] list)
        {
        	String[] getString = new String[4];
            double[] functionChangers = new double[4];
            for (double i = 0.9; i < 10.9; i+= 2.5) 
            {
                getString[(int)((i-0.9)/2.5)] = list[(int)i];
            }
            for (int i = 0; i < 4; i ++)
            {
                if (getString[i].equals(""))
                    functionChangers[i] = 1.0-(i/2);                
                else if(getString[i].indexOf('+') != -1) 
                	functionChangers[i] = Double.parseDouble(getString[i].substring(1)) ;
                else if(getString[i].equals("-"))
                	functionChangers[i] = -1.0;
                else if(getString[i].indexOf('/') != -1)
                {
                	int location = getString[i].indexOf('/');
 					functionChangers[i] = Double.parseDouble(getString[i].substring(0,location))
 					/ Double.parseDouble(getString[i].substring(location+1));
                }
    			else
    				functionChangers[i] = Double.parseDouble(getString[i]);
    			
            }
           
            return functionChangers;
        }
        
        public void drawDots(Graphics g, double xPoint, double yPoint)// used to draw a dot
        {
            g.fillOval((int) (squareLength * xPoint + pixelAtZeroX -1), 
            (int) (-1*squareLength * yPoint + pixelAtZeroY -1), 3, 3);
        }
	
        public void menuBarPanelSetter()// used to make the JMenuItems operational
        {
            JMenu xShift = new JMenu("f(x + ?)");
            JMenu yCoeff = new JMenu("f(?x)");
            JMenu yShift = new JMenu("f(x) + ?");
            JMenu xCoeff = new JMenu("?f(x)");
            JMenu function = new JMenu("Function");
            JMenu lessThanDomain = new JMenu("<");
            JMenu greaterThanDomain = new JMenu("<");


            xShift.setFont(smallFont);
            yCoeff.setFont(smallFont);
            yShift.setFont(smallFont);
            xCoeff.setFont(smallFont);
            function.setFont(smallFont);
            lessThanDomain.setFont(smallFont);
            greaterThanDomain.setFont(smallFont);


            JMenuItem[] xCoeffItems = new JMenuItem[10];
            JMenuItem[] yCoeffItems = new JMenuItem[10];
            JMenuItem[] xShiftItems = new JMenuItem[60];
            JMenuItem[] yShiftItems = new JMenuItem[60];
            JMenuItem[] lessThanDomainItems = new JMenuItem[60];
            JMenuItem[] greaterThanDomainItems = new JMenuItem[60];


            
            for (int i = 8; i > 0; i--) 
            {
                yCoeffItems[i - 1] = new JMenuItem("-1/" + (i) / 2);
                xCoeffItems[i - 1] = new JMenuItem("-" + (i) / 2);

                yCoeffItems[i-1].setFont(smallFont);
                xCoeffItems[i-1].setFont(smallFont);

                i--;
                yCoeffItems[i - 1] = new JMenuItem("1/" + (i + 1) / 2);
                xCoeffItems[i - 1] = new JMenuItem("" + (i + 1) / 2);
                
                yCoeffItems[i-1].setFont(smallFont);
                xCoeffItems[i-1].setFont(smallFont);
            }
            yCoeffItems[0] = new JMenuItem("-1");
            yCoeffItems[1] = new JMenuItem("1");
            yCoeffItems[0].setFont(smallFont);
            yCoeffItems[1].setFont(smallFont);
            
            
            for(int i = -30; i < 30; i++)
            {
				lessThanDomainItems[i+30] = new JMenuItem(""+i);
				greaterThanDomainItems[i+30] = new JMenuItem(""+i);  
				lessThanDomainItems[i+30].setFont(smallFont);
				greaterThanDomainItems[i+30].setFont(smallFont);
				
				if(i <= 0)
				{
					xShiftItems[i+30] = new JMenuItem("" + i);
					yShiftItems[i+30] = new JMenuItem("" + i);
				}
				else
				{
					xShiftItems[i+30] = new JMenuItem("+" + i);
					yShiftItems[i+30] = new JMenuItem("+" + i);
				}
					
				xShiftItems[i+30].setFont(smallFont);
				yShiftItems[i+30].setFont(smallFont);
				        	
            }

            JMenuItem[] functionItems = {new JMenuItem("linear"), new JMenuItem("reciprocal"),
                    new JMenuItem("absolute"), new JMenuItem("square root"), new JMenuItem("sin"),
                    new JMenuItem("quadratic"), new JMenuItem("cubic")};

            xShiftBarHandler xsbh = new xShiftBarHandler();
            yShiftBarHandler ysbh = new yShiftBarHandler();
            xCoeffBarHandler xcbh = new xCoeffBarHandler();
            yCoeffBarHandler ycbh = new yCoeffBarHandler();
            functionBarHandler fbh = new functionBarHandler();
            StartingDomainHandler sdh = new StartingDomainHandler();
            EndingDomainHandler edh = new EndingDomainHandler();

            for (int i = 0; i < 8; i++) 
            
            {
                yCoeffItems[i].addActionListener(ycbh);
                xCoeffItems[i].addActionListener(xcbh);

            }
            
             for(int i = 0; i < 60; i++)
            {
				lessThanDomainItems[i].addActionListener(sdh);
				greaterThanDomainItems[i].addActionListener(edh);
				yShiftItems[i].addActionListener(ysbh);
                xShiftItems[i].addActionListener(xsbh);
        	
            }


            for (int i = 0; i < 7; i++)
            {
                functionItems[i].setFont(smallFont);
                functionItems[i].addActionListener(fbh);
                function.add(functionItems[i]);
            }

            for (int i = 0; i < 8; i++) 
            {
                yCoeff.add(yCoeffItems[i]);
                xCoeff.add(xCoeffItems[i]);
            }
            
            JMenuItem[][] upToSixty = new JMenuItem[6][60];
            upToSixty[0] = xShiftItems;
            upToSixty[1] = yShiftItems;
            upToSixty[2] = lessThanDomainItems;
            upToSixty[3] = greaterThanDomainItems;
            
            JMenu[][] upToSix = new JMenu[6][6] ;
           
           JMenu[] mainMenus = new JMenu[4];
           mainMenus[0] = xShift;
           mainMenus[1] = yShift;
           mainMenus[2] = lessThanDomain;
           mainMenus[3] = greaterThanDomain;
            for (int index = 0; index < 4; index++)
            {
            	for(int i = -3; i < 3; i++)
            	{
            		upToSix[index][i+3] =  new JMenu("" + i*10); 
            		for(int i2 = 0; i2 < 10; i2++)
            		{
            			upToSix[index][i+3].add(upToSixty[index][(i+3)*10+i2]);
            		}
            	}            		
            }
            
            for(int i  = 0; i < 4; i++)
            {
            	for(int i2 = 0; i2 < 6; i2++)
            		mainMenus[i].add(upToSix[i][i2]);
            }
            
            xShiftBar.add(xShift);
            yStretchBar.add(yCoeff);
            yShiftBar.add(yShift);
            xStretchBar.add(xCoeff);
            functionBar.add(function);
            startDomain.add(lessThanDomain);
            endDomain.add(greaterThanDomain);
        }
        
        // makes the textField show the changes made by the user by setting equationComp to changes
        public void setEquation() 
        {
            equation.setText("y = " + equationComp[0] + equationComp[1] + equationComp[2] + 
            equationComp[3] + equationComp[4] + equationComp[5] + equationComp[6] + 
            equationComp[7] + equationComp[8]);
        }
             
        class xShiftBarHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                String command = evt.getActionCommand();
                if (command.equals("0"))
                    equationComp[5] = "";
                else
                    equationComp[5] = "" + command;

                setEquation();
            }
        }

        class yShiftBarHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                String command = evt.getActionCommand();
                if (command.equals("0"))
                    equationComp[8] = "";

                else
                    equationComp[8] = "" + command;
                setEquation();
            }
        }

        class xCoeffBarHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                String command = evt.getActionCommand();
                if(!(equationComp[1].equals("/")))
                {
                	if (command.equals("1"))
                    	equationComp[0] = "";
                    
                	else if (command.equals("-1"))
                    	equationComp[0] = "-";
                    else
                    equationComp[0] = command;
                }
                else
                    equationComp[0] = command;

                setEquation();
            }
        }

        class yCoeffBarHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                String command = evt.getActionCommand();
                if (command.equals("1"))
                {
                    equationComp[3] = "";
                    equationComp[2] = "(";
                	equationComp[4] = "x";
                	equationComp[6] = ")";
                	
                }
                else if (command.equals("-1"))
                {
                    equationComp[3] = "-";
                    equationComp[2] = "((";
                	equationComp[4] = "x";
                	equationComp[6] = ")";
                }
                else
                {
                    equationComp[3] = command;
                	equationComp[6] = ")";
                	equationComp[2] = "((";
                	equationComp[4] = ")x";
				}
                
                setEquation();
            }
        }

        class functionBarHandler implements ActionListener
        {
            public void actionPerformed(ActionEvent evt)
            {
                String command = evt.getActionCommand();

                if (command.equals("absolute"))
                {
                    equationComp[2] = "|";
                    equationComp[6] = "|";
                    equationComp[7] = "";
                    equationComp[1] = "";
                    
                    if (equationComp[0].equals("1"))
                    	equationComp[0] = "";
                    
                	else if (equationComp[0].equals("-1"))
                    	equationComp[0] = "-1";
                }
                
                else if (command.equals("square root"))
                {
                    equationComp[1] = "√";
                    equationComp[7] = "";
                    equationComp[2] = "(";
                    equationComp[6] = ")";
                    
                    if (equationComp[0].equals("1"))
                    	equationComp[0] = "";
                     
                	else if (equationComp[0].equals("-1"))
                    	equationComp[0] = "-1";
                }
                
                else if (command.equals("quadratic"))
                {
                    equationComp[7] = "²";
                    equationComp[2] = "(";
                    equationComp[6] = ")";
                    equationComp[1] = "";
                    
                    if (equationComp[0].equals("1"))
                    	equationComp[0] = "";
                    
                	else if (equationComp[0].equals("-1"))
                    	equationComp[0] = "-1";
                }
                
                else if (command.equals("cubic"))
                {
                    equationComp[7] = "³";
                    equationComp[2] = "(";
                    equationComp[6] = ")";
                    equationComp[1] = "";
                    
                    if (equationComp[0].equals("1"))
                    	equationComp[0] = "";
                    
                	else if (equationComp[0].equals("-1"))
                    	equationComp[0] = "-1";

                }
                
                else if (command.equals("sin"))
                {
                    equationComp[1] = "sin";
                    equationComp[2] = "(";
                    equationComp[6] = ")";
                    equationComp[7] = "";
                    
                    if (equationComp[0].equals("1"))
                    	equationComp[0] = "";
                    
                	else if (equationComp[0].equals("-1"))
                    	equationComp[0] = "-1";

                }
                
                else if (command.equals("reciprocal"))
                {
                    equationComp[1] = "/";
                    equationComp[2] = "(";
                    equationComp[6] = ")";
                    equationComp[7] = "";
                    
                    if (equationComp[0].equals(""))
                    
                        equationComp[0] = "1";
                    
                    else if (equationComp[0].equals("-"))
                
                        equationComp[0] = "-1";
                
                }
                
                else
                {
                    equationComp[1] = "";
                    equationComp[2] = "(";
                    equationComp[6] = ")";
                    equationComp[7] = "";
                    if (equationComp[0].equals("1"))
                    	equationComp[0] = "";
                    
                	else if (equationComp[0].equals("-1"))
                    	equationComp[0] = "-1";
                }
                if(!(equationComp[3].equals("")))
                {
                	equationComp[2] = "((";
                	
                	if(command.equals("absolute"))
                		equationComp[2] = "|(";
                }
                setEquation();
            }
        }
        
        class StartingDomainHandler implements ActionListener
        {
        	public void actionPerformed(ActionEvent evt)
        	{
        		String command = evt.getActionCommand();
        		greaterDomain.setText(command);
        		if(command.equals(""))
        			currentSavedDomain1 = -40;
        		else
        			currentSavedDomain1 = Integer.parseInt(command);
        	}
        }
        
        class EndingDomainHandler implements ActionListener
        {
        	public void actionPerformed(ActionEvent evt)
        	{
        		String command = evt.getActionCommand();
        		lesserDomain.setText(command);
        		if(command.equals(""))
        			currentSavedDomain2 = 40;
        		else
        			currentSavedDomain2 = Integer.parseInt(command);
        	}
        }
        class JSliderHandler implements ChangeListener
	    {
    		public void stateChanged(ChangeEvent evt)
        	{
            	int command = zoom.getValue();
            	gridSpaces = command;
            	repaint();
    
        	} 
    	}
    	
    	class SubmitHandler implements ActionListener
    	{
    		public void actionPerformed(ActionEvent evt)
        	{
        		String command = evt.getActionCommand();
            	for(int i = 0; i < equationComp.length; i++)
            	{
            		savedCurrentEquationComp[i] = equationComp[i];
            	}
            	
            	submitedFunctionsCoefficients = makeList(savedCurrentEquationComp);
            	
            	savedDomain1 = currentSavedDomain1;
    			savedDomain2 =currentSavedDomain2;

            	
        		threeSeconds = specificTime-3;
        		
        		makeCoefficents();
            	choseAndMakeQuestion();
            	
            	if(correctAnswer)
            	{
            		chosenFunction++;
            		specificTime = 30;
            		threeSeconds = 27;
            		encouragment.setText("     Good Job! I can see that you're improving!");
					encouragment.setForeground(Color.GREEN);
					encouragment.setVisible(true);
					functionLoopCounter += -1;
           
            	}
            	
            	else
            	{
            		encouragment.setText("     Try again, I'm sure you will get it this time!");
					encouragment.setForeground(Color.RED);
					encouragment.setVisible(true); 
            	}
            	
            	if(functionLoopCounter == 0)
            	{
            		encouragment.setText("Congratulations, you finished level " + currentLevel + "!");
            		encouragment.setVisible(true); 
            		chosenFunction = 0;
            		currentLevel++;
            		if(currentLevel == 4)
            		{
            			encouragment.setText("Congratulations, you finished the Game! Go back to the start page");
            			encouragment.setVisible(true);
            			currentLevel = 0;
            		}
            		randomLevel = (int)(Math.random()*3);
            		functionLoopCounter = 0;
            		sketchFunctions = getFunctions();
            		repaint();
            	}
            	
            	repaint();                       	
        	}
    	}
		public void makeCoefficents()
        {
        		trueHorizontalShift = sketchFunctionsCoefficients[chosenFunction][2]/
        		sketchFunctionsCoefficients[chosenFunction][1];
        		
        		answerHorizontalShift = submitedFunctionsCoefficients[2]/ submitedFunctionsCoefficients[1];
        		
        		if(sketchFunctions[chosenFunction][7].equals("²"))
        		{
					trueCoefficent = sketchFunctionsCoefficients[chosenFunction][0]*
					Math.pow(sketchFunctionsCoefficients[chosenFunction][1], 2);
					
					answerCoefficent = submitedFunctionsCoefficients[0]* Math.pow(submitedFunctionsCoefficients[1], 2);	
        		}
        		
        		else if(sketchFunctions[chosenFunction][7].equals("³"))
        		{
        			trueCoefficent = sketchFunctionsCoefficients[chosenFunction][0]*
					Math.pow(sketchFunctionsCoefficients[chosenFunction][1], 3);
					
					answerCoefficent = submitedFunctionsCoefficients[0]* Math.pow(submitedFunctionsCoefficients[1], 3);
        		}
        		
        		else if(sketchFunctions[chosenFunction][2].equals("|"))
        		{
        			trueCoefficent = sketchFunctionsCoefficients[chosenFunction][0]*
					Math.abs(sketchFunctionsCoefficients[chosenFunction][1]);
					
					answerCoefficent = submitedFunctionsCoefficients[0] * Math.abs(submitedFunctionsCoefficients[1]);
					
        		}
        		
        		else if(sketchFunctions[chosenFunction][1].equals("√"))
        		{
        			trueCoefficent = sketchFunctionsCoefficients[chosenFunction][0]*
					Math.sqrt(sketchFunctionsCoefficients[chosenFunction][1]);
					
					answerCoefficent = submitedFunctionsCoefficients[0] * Math.sqrt(submitedFunctionsCoefficients[1]);
        		}
        		
        	
        		else if(sketchFunctions[chosenFunction][1].equals("/"))
        		{
        			trueCoefficent = sketchFunctionsCoefficients[chosenFunction][0]/
					sketchFunctionsCoefficients[chosenFunction][1];
					
					answerCoefficent = submitedFunctionsCoefficients[0]/submitedFunctionsCoefficients[1];
        		}
        		        
        		else if(sketchFunctions[chosenFunction][2].equals("(")&&sketchFunctions[chosenFunction][7].equals("")&&
        		sketchFunctions[chosenFunction][1].equals(""))
        		{
    			trueCoefficent = sketchFunctionsCoefficients[chosenFunction][0]*
				sketchFunctionsCoefficients[chosenFunction][1];
					
				answerCoefficent = submitedFunctionsCoefficients[0] * submitedFunctionsCoefficients[1];
        	}
        }	    	
        
        public void choseAndMakeQuestion()
        	{
        		String[] questionTypes = new String[3];
        		
        		int randInt1 = 0;
        		int randInt2 = 0;
        		
        		randInt1 = (int)(Math.random()*8)+2;
        		randInt2 = (int)(Math.random()*8)-10;
        		
        		correctAnswer = true;
        		
        		if(savedDomain1 != sketchFunctionsConstraints[chosenFunction][0] ||
        		savedDomain2 != sketchFunctionsConstraints[chosenFunction][1])
        		{
        			correctAnswer = false;
        			questionTypes[0] = "The domain of a function is ["+randInt2+", " + randInt1 + 
        			"], which of the points below are possible to be a part of the function?";
        			questionTypes[1] =  "The domain of a function is ["+randInt1+", " + randInt2 + 
        			"], which of the points below are possible to be a part of the function?";
        			questionTypes[2] = "Which of the parent functions below can't have a negative x-value?";
        			
        			multipleChoiceAnswers[0][0] = "none of the above";
        			multipleChoiceAnswers[1][0] =(int)(Math.random()*20)+1+ ", " + (int)(Math.random()*randInt1)+1;
        			multipleChoiceAnswers[2][0] = "square root function";
        			
        			
        			for(int i  = 1; i<4; i++ )
        			{
        				multipleChoiceAnswers[0][i] = (int)(Math.random()*9) + ", " + (int)(Math.random()*-9);
        				multipleChoiceAnswers[1][i] =(int)(Math.random()*randInt1)+1 + ", " + (int)(Math.random()*randInt2)+1;
        			}
        			
        			multipleChoiceAnswers[2][1] ="quadratic function";
        			multipleChoiceAnswers[2][2] ="reciprocal function";
        			multipleChoiceAnswers[2][3] ="absolute function";
        		}
        		
        		
        		if(submitedFunctionsCoefficients[3] != sketchFunctionsCoefficients[chosenFunction][3])
        		{
        			correctAnswer = false;
        			
        			questionTypes[0] = "The function sin(x) + " + randInt1 + " is where compared to the function sin(x)?";
        			questionTypes[1] =  "The function f(x) - " + randInt1 + " is where compared to the function f(x)?";
        			questionTypes[2] = "Which of the changes below will move the function f(x) up?";
        			
        			multipleChoiceAnswers[0][0] = "above";
        			multipleChoiceAnswers[0][1] = "below";
        			multipleChoiceAnswers[0][2] = "to the right of";
        			multipleChoiceAnswers[0][3] = "to the left of";
        			
        			multipleChoiceAnswers[1][0] = "down " + randInt1 + " units";
        			multipleChoiceAnswers[1][1] = "up " + randInt1 + " units";
        			multipleChoiceAnswers[1][2] = "right " + randInt1 + " units";
        			multipleChoiceAnswers[1][3] = "left " + randInt1 + " units";
        			
        			
        			multipleChoiceAnswers[2][0] = "f(x) + " + randInt1;
        			multipleChoiceAnswers[2][1] = "f(x) - " + randInt1;
        			multipleChoiceAnswers[2][2] = "f(x + " + randInt1 +")" ;
        			multipleChoiceAnswers[2][3] = "f(x) - " + randInt1+")";
        		}
        		
        		if(trueHorizontalShift != answerHorizontalShift)
        		{
        			 correctAnswer = false;
        			 
        			questionTypes[0] = "The function |x + " + randInt1 + "| is where compared to the function |x|?";
        			questionTypes[1] =  "The function f(x - " + randInt1 + ") is where compared to the function f(x)?";
        			questionTypes[2] = "Which of the changes below will move the function f(x) left?";
        			
        			multipleChoiceAnswers[0][0] = "to the left of";
        			multipleChoiceAnswers[0][1] = "below";
        			multipleChoiceAnswers[0][2] = "to the right of";
        			multipleChoiceAnswers[0][3] = "above";
        			
        			multipleChoiceAnswers[1][0] = "right " + randInt1 + " units";
        			multipleChoiceAnswers[1][1] = "up " + randInt1 + " units";
        			multipleChoiceAnswers[1][2] = "right " + randInt1 + " units";
        			multipleChoiceAnswers[1][3] = "left " + randInt1 + " units";
        			
        			
        			multipleChoiceAnswers[2][0] = "f(x + " + randInt1 +")" ;
        			multipleChoiceAnswers[2][1] = "f(x) - " + randInt1;
        			multipleChoiceAnswers[2][2] = "f(x) + " + randInt1;
        			multipleChoiceAnswers[2][3] = "f(x) - " + randInt1+")";
        		}
        		
        		if(sketchFunctions[chosenFunction][1].equals("sin"))
        		{
        			if(submitedFunctionsCoefficients[0] != sketchFunctionsCoefficients[chosenFunction][0]
        			|| submitedFunctionsCoefficients[1] != sketchFunctionsCoefficients[chosenFunction][1])
        			{
        				correctAnswer = false;
        				
        			questionTypes[0] = "The function " + randInt1 + "f(1/" + (-1*randInt2) + "x) is different from the function f(x) in what ways?";
        			questionTypes[1] =  "How does the function " + randInt2 + "f(x) differ from the function f(x)";
        			questionTypes[2] = "How does the function " + randInt1 + "f(x) differ from the function f(" + randInt1 + "x)";
        			
        			multipleChoiceAnswers[0][0] = "vertically stretched by " + randInt1
        			+ " and, horizontally shrunk by 1/" + (-1*randInt2);
        			multipleChoiceAnswers[0][1] = "horizontally stretched by " + randInt1
        			+ " and, vertically shrunk by 1/" + (-1*randInt2);
        			multipleChoiceAnswers[0][2] = "vertically shrunk by 1/" + (-1*randInt2)
        			+ " and, horizontally stretched by " + randInt1;
        			multipleChoiceAnswers[0][3] = "horizontally shrunk by 1/" + (-1*randInt2)
        			+ " and, horizontally stretched by " + randInt1;

        			
        			multipleChoiceAnswers[1][0] = "reflected across the x-axis, and vertically stretched by " + (-1*randInt2);
        			multipleChoiceAnswers[1][1] = "reflected across the y-axis, and horizontally stretched by " + (-1*randInt2);
        			multipleChoiceAnswers[1][2] = "reflected across the x-axis, and vertically shrunk by 1/" + (-1*randInt2);
        			multipleChoiceAnswers[1][3] = "reflected across the y-axis, and horizontally shrunk by 1/" + (-1*randInt2);
        			
        			
        			multipleChoiceAnswers[2][0] = (randInt1 + " f(x) is vertically stretched, f(" + (-1*randInt1) + 
        			"x), is horizontally shrunk");
        			multipleChoiceAnswers[2][1] = (randInt1 + " f(x) is horizontally stretched, f(" + (-1*randInt1) + 
        			"x), is vertically shrunk");
        			multipleChoiceAnswers[2][2] = (randInt1 + " f(x) is vertically stretched, f(" + (-1*randInt1) + 
        			"x), is  horizontally shrunk");
        			multipleChoiceAnswers[2][3] = (randInt1 + " f(x) is horizontally stretched, f(" + (-1*randInt1) + 
        			"x), is vertically shrunk");
        			}		
        		}
        		
        		else if((trueCoefficent != answerCoefficent))
        		{
        			correctAnswer = false;
        			
        			questionTypes[0] = "The function " + randInt1 + "f(1/" + (-1*randInt2) + "x) is different from the function f(x) in what ways?";
        			questionTypes[1] =  "How does the function " + randInt2 + "f(x) differ from the function f(x)";
        			questionTypes[2] = "How does the function " + randInt1 + "f(x) differ from the function f(" + randInt1 + "x)";
        			
        			multipleChoiceAnswers[0][0] = "vertically stretched by " + randInt1
        			+ " and, horizontally shrunk by 1/" + (-1*randInt2);
        			multipleChoiceAnswers[0][1] = "horizontally stretched by " + randInt1
        			+ " and, vertically shrunk by 1/" + (-1*randInt2);
        			multipleChoiceAnswers[0][2] = "vertically shrunk by 1/" + (-1*randInt2)
        			+ " and, horizontally stretched by " + randInt1;
        			multipleChoiceAnswers[0][3] = "horizontally shrunk by 1/" + (-1*randInt2)
        			+ " and, horizontally stretched by " + randInt1;

        			
        			multipleChoiceAnswers[1][0] = "reflected across the x-axis, and vertically stretched by " + (-1*randInt2);
        			multipleChoiceAnswers[1][1] = "reflected across the y-axis, and horizontally stretched by " + (-1*randInt2);
        			multipleChoiceAnswers[1][2] = "reflected across the x-axis, and vertically shrunk by 1/" + (-1*randInt2);
        			multipleChoiceAnswers[1][3] = "reflected across the y-axis, and horizontally shrunk by 1/" + (-1*randInt2);
        			
        			
        			multipleChoiceAnswers[2][0] = (randInt1 + " f(x) is vertically stretched, f(" + (-1*randInt1) + 
        			"x), is horizontally shrunk");
        			multipleChoiceAnswers[2][1] = (randInt1 + " f(x) is horizontally stretched, f(" + (-1*randInt1) + 
        			"x), is vertically shrunk");
        			multipleChoiceAnswers[2][2] = (randInt1 + " f(x) is vertically stretched, f(" + (-1*randInt1) + 
        			"x), is  horizontally shrunk");
        			multipleChoiceAnswers[2][3] = (randInt1 + " f(x) is horizontally stretched, f(" + (-1*randInt1) + 
        			"x), is vertically shrunk");
        			
        		}
        		
        		if(!sketchFunctions[chosenFunction][1].equals(savedCurrentEquationComp[1])
        			|| !sketchFunctions[chosenFunction][7].equals(savedCurrentEquationComp[7])
        			|| !sketchFunctions[chosenFunction][6].equals(savedCurrentEquationComp[6]))
        		{
        			correctAnswer = false;
        			
        			questionTypes[0] = "Which one of these functions is vertically symmetrical?";
        			questionTypes[1] =  "Which character most closely resembles a quadratic function?";
        			questionTypes[2] = "What function has the coordinates (1,1),(2,4)(3,9),(4,16),(5,25)?";
        			
        			multipleChoiceAnswers[0][0] = "absolute function";
        			multipleChoiceAnswers[0][1] = "cubic function";
        			multipleChoiceAnswers[0][2] = "linear function";
        			multipleChoiceAnswers[0][3] = "reciprocal function";
        			
        			multipleChoiceAnswers[1][0] = "U";
        			multipleChoiceAnswers[1][1] = "W";
        			multipleChoiceAnswers[1][2] = "/";
        			multipleChoiceAnswers[1][3] = "S";
        			
        			
        			multipleChoiceAnswers[2][0] = "quadratic function";
        			multipleChoiceAnswers[2][1] = "cubic function";
        			multipleChoiceAnswers[2][2] = "sin function";
        			multipleChoiceAnswers[2][3] = "square root function";
        			
        			
        		}        		
        		chosenCorrectAnswerSet = (int)(Math.random()*3);
        		question.setText(questionTypes[chosenCorrectAnswerSet]);
        		int randInt3 = 0;
        		randInt3 =(int)(Math.random()*4);
        		for(int i = 0; i<4; i++)
        		{
        		 	answers[(randInt3+i)%4].setText(multipleChoiceAnswers[chosenCorrectAnswerSet][i]);
        		}
        	}
        
    	class TimerHandler implements ActionListener
        {
        	public void actionPerformed(ActionEvent evt)
        	{
        		if(specificTime > 0)
        		{
        			specificTime -= 1;
        			if(specificTime > 9)
        				timerLabel.setText("  00:"+ specificTime);
        			else
        				timerLabel.setText("  00:0"+ specificTime);
        		}
        		else
        		{
        			choseAndMakeQuestion();
        			bg.clearSelection();
        			answer.setVisible(false);
        			timer.stop();
        			cards.show(cardPanel, "Question Page");
        			specificTime = 30;
        		}
            	
            	if(specificTime  == threeSeconds)
            		encouragment.setVisible(false);
        	}
        }
    }
    
    class NavigationHandler implements ActionListener
    {
    	public void actionPerformed(ActionEvent evt)
        {
        	FunctionArtPanel fap = new FunctionArtPanel();
            String command = evt.getActionCommand();
            if (command.equals("START"))
            {
                cards.show(cardPanel, "Game Page");
                timer.start();
            }
            else if (command.equals("QUIT"))
            {	
                cards.show(cardPanel, "Start Page");
                timer.stop();
            }
            else if (command.equals("FUNCTIONS"))
            {
                cards.show(cardPanel, "Function Info Page");
                timer.stop();
            }
            else if(command.equals("INSTRUCTIONS"))
            {
            	cards.show(cardPanel, "Instructions Page");
            	timer.stop();
            }
            else if(command.equals("NEXT"))
            {
            	cards.next(instructionsPanel);
            	timer.stop();
            }
        }
    }    
}