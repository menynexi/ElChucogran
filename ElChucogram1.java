/* CS1101 – Intro to Computer Science 
Instructor: Aguirre OR Akbar OR Logan 
Comprehensive Lab 2
By including my name below, I confirm that:
-	I am submitting my original work.
-	If I include code obtained from another source or I received help I am giving attribution to those sources as comments.
-	This submission does not incur in any academic dishonesty practice as described in the course syllabus.
Modified and submitted by: [MANUEL GUTIERREZ JR] 
*/ 
  
//classes many imported
import java.util.Scanner;//imported Scanner
import java.io.FileWriter;//import FileWriter to be able to Write on a file
import java.io.PrintWriter;//import PrintWriter to be able to print on a file

//classes that where already here
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

//look in to swing library for GUI
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import jdk.jfr.Description;

public class ElChucogram1{

	// *-*-*-*-*-*-*-*-*-*-*-*-
	// Ignore this part
	// *-*-*-*-*-*-*-*-*-*-*-*-
	private static JLabel imageLabel = null;
	private static JFrame frame = null;
	private static Color[][] workingImage = null;
	private static ArrayList<Integer> mouseClicks = new ArrayList<Integer>();
	private static int normFactor = 50;
	private static JButton recordButton = null;
	private static boolean recording = false;
	private static String recordingFileName = null;
	
	private enum Effect { LIQUIFY, TWIST_LEFT, TWIST_RIGHT, PINCH, BULGE, MANY;} 
	private static Effect currentEffect = Effect.LIQUIFY;
	 
	// *-*-*-*-*-*-*-*-*-*-*-*-
	// Ignore this part
	// *-*-*-*-*-*-*-*-*-*-*-*-	
	
	
	public static void main(String[] args) throws Exception
	{
		displayApp();
	}
	
	/** 
	 * TODO: Add this code in by disecting diegos code
	 */
	//due to the deadline I will not implement this code but will work as a future implementation
	public static void manyMethod(Color[][] imagePixels, int rowP0, int colP0, int rowP1, int colP1, double normFactor) 
	{
		Color[][] destination = new Color[imagePixels.length][imagePixels[0].length];
		for(int destRow = 0; destRow < imagePixels.length; destRow++){
			for(int destCol = 0; destCol < imagePixels[0].length; destCol++){
				int deltaRow = rowP1 - rowP0;
				int deltaCol = colP1 - colP0;
		 		// int distance = (int) Math.sqrt(Math.pow((colP1 - colP0), 2) + Math.pow((rowP1 - rowP0), 2));//
                // double weight = Math.pow(2,(-distance/normFactor));
                double weight = 1;
		 		int scrRow = (int) (destRow - deltaRow * weight);
				int scrCol = (int) (destCol - deltaCol * weight);
				if(scrRow < 0){
					scrRow = 0;
				}

				if(scrCol < 0){
					scrCol = 0;
				}

				if( scrCol > imagePixels[0].length -1){
					scrCol = imagePixels[0].length -1;
				}

				if(scrRow > imagePixels.length -1){
					scrRow = imagePixels.length -1;
				}

				destination[destRow][destCol]= imagePixels[scrRow][scrCol];
			}
		}

		updateImage(destination);
	}
	

	/**
	 * This method is done liquifyEffect
	 * 
	 */
	//This method should grab two points and change the distance from one point to another
	public static void liquifyEffect(Color[][] imagePixels, int rowP0, int colP0, int rowP1, int colP1, double normFactor) 
	{
		Color[][] destination = new Color[imagePixels.length][imagePixels[0].length];
		//a 2D array that traverses the whole image 
		for(int destRow = 0; destRow < imagePixels.length; destRow++){
			for(int destCol = 0; destCol < imagePixels[0].length; destCol++){

				int deltaRow = rowP1 - rowP0;
				int deltaCol = colP1 - colP0;

		 		double distance = Math.sqrt(Math.pow((rowP1 - destRow), 2) + Math.pow((colP1 - destCol), 2));
		 		double weight = Math.pow(2,(-distance/normFactor));
		 		int scrRow = (int) (destRow - deltaRow * weight);
				int scrCol = (int) (destCol - deltaCol * weight);
				//this ensores the image stays within a specific domain
				if(scrRow < 0){
					scrRow = 0;
				}

				if(scrCol < 0){
					scrCol = 0;
				}

				if( scrCol > imagePixels[0].length -1){
					scrCol = imagePixels[0].length -1;
				}

				if(scrRow > imagePixels.length -1){
					scrRow = imagePixels.length -1;
				}
	
				destination[destRow][destCol] = imagePixels[scrRow][scrCol];
			}
		}
		//the computer is updateing the image as every iteration of the method
		updateImage(destination);
	}

	
	/**
	 * TODO: Math.PI
	 */
	//this method should twist the image to the left when clicked
	public static void twistLeftEffect(Color[][] imagePixels, int rowP0, int colP0, double normFactor) 
	{
		Color[][] destination = new Color[imagePixels.length][imagePixels[0].length];
		//The programmer can mess around with the value of maxAngleDelta to create a bigger twist or a more descrite one
		double maxAngleDelta = 3.14 / 2.0; //This angle is for twistLeftEffect


		for(int destRow = 0; destRow < imagePixels.length; destRow++){
			for(int destCol = 0; destCol < imagePixels[0].length; destCol++){
				//NOTE when messing with the distance formula if you write it as "double distance = Math.sqrt(Math.pow((destRow - destCol), 2) + Math.pow((rowP0 - colP0), 2));"
				//doing this will create a "SUPERZOOM" because it pulls the im age towards that point THINGS TO TRY OUT
				//this is also applicable to TWIST_RIGHT
				double distance = Math.sqrt(Math.pow((rowP0 - destRow), 2) + Math.pow((colP0 - destCol), 2));
				double weight = Math.pow(2,(-distance/normFactor));
				double angleDelta = maxAngleDelta * weight;
				double newAngle = Math.atan2(destRow - rowP0, destCol - colP0) + angleDelta;

				int scrRow = (int) (rowP0 + Math.sin(newAngle) * distance);
				int scrCol = (int) (colP0 + Math.cos(newAngle) * distance);

				if(scrRow < 0){
					scrRow = 0;
				}

				if(scrCol < 0){
					scrCol = 0;
				}

				if( scrCol > imagePixels[0].length -1){
					scrCol = imagePixels[0].length -1;
				}

				if(scrRow > imagePixels.length -1){
					scrRow = imagePixels.length -1;
				}
				
				destination[destRow][destCol] = imagePixels[scrRow][scrCol];
			}
		}
		
		updateImage(destination);
	}
	
	/**
	 * This method is complete (may need to ask about the Math.PI)
	 */
	//this method should create a TWIST_RIGHT when clicked 
	public static void twistRightEffect(Color[][] imagePixels, int rowP0, int colP0, double normFactor) 
	{
		Color[][] destination = new Color[imagePixels.length][imagePixels[0].length];

		double maxAngleDelta = -3.14 / 2.0; //This angle is for twistLeftEffect


		for(int destRow = 0; destRow < imagePixels.length; destRow++){
			for(int destCol = 0; destCol < imagePixels[0].length; destCol++){

				double distance = Math.sqrt(Math.pow((rowP0 - destRow), 2) + Math.pow((colP0 - destCol), 2));
				double weight = Math.pow(2,(-distance/normFactor));
				double angleDelta = maxAngleDelta * weight;
				double newAngle = Math.atan2(destRow - rowP0, destCol - colP0) + angleDelta;

				int scrRow = (int) (rowP0 + Math.sin(newAngle) * distance);
				int scrCol = (int) (colP0 + Math.cos(newAngle) * distance);

				if(scrRow < 0){
					scrRow = 0;
				}

				if(scrCol < 0){
					scrCol = 0;
				}

				if( scrCol > imagePixels[0].length -1){
					scrCol = imagePixels[0].length -1;
				}

				if(scrRow > imagePixels.length -1){
					scrRow = imagePixels.length -1;
				}

				destination[destRow][destCol] = imagePixels[scrRow][scrCol];
			}
		}
		
		updateImage(destination);
	}
	
	/**
	 * THIS METHOD IS DONE DO NOT TOUCH
	 */
	public static void pinchEffect(Color[][] imagePixels, int rowP0, int colP0, double normFactor) 
	{
		Color[][] destination = new Color[imagePixels.length][imagePixels[0].length];
		double maxDistDelta = -0.5;

		for(int destRow = 0; destRow < imagePixels.length; destRow++){
			for(int destCol = 0; destCol < imagePixels[0].length; destCol++){

				double distance = Math.sqrt(Math.pow((rowP0 - destRow), 2) + Math.pow((colP0 - destCol), 2));
				double weight = Math.pow(2,(-distance/normFactor));
				double angle = Math.atan2(destRow - rowP0, destCol - colP0);
				double deltaDistance = maxDistDelta * distance;
				double weightedDistance = distance - (weight * deltaDistance);

				int scrRow = (int) (rowP0 + Math.sin(angle) * weightedDistance);
				int scrCol = (int) (colP0 + Math.cos(angle) * weightedDistance);

				if(scrRow < 0){
					scrRow = 0;
				}

				if(scrCol < 0){
					scrCol = 0;
				}

				if( scrCol > imagePixels[0].length -1){
					scrCol = imagePixels[0].length -1;
				}

				if(scrRow > imagePixels.length -1){
					scrRow = imagePixels.length -1;
				}

				destination[destRow][destCol] = imagePixels[scrRow][scrCol];
			}
		}

		updateImage(destination);
	}
	
	/**
	 * THIS METHOD IS DONE //DO NOT TOUCH
	 */
	//this method should increase the size of the cliked area
	public static void bulgeEffect(Color[][] imagePixels, int rowP0, int colP0, double normFactor) 
	{
		Color[][] destination = new Color[imagePixels.length][imagePixels[0].length];

		double maxDistDelta = 0.5;

		//a 2D array is used to travers all the image
		for(int destRow = 0; destRow < imagePixels.length; destRow++){
			for(int destCol = 0; destCol < imagePixels[0].length; destCol++){
				
				double distance = Math.sqrt(Math.pow((rowP0 - destRow), 2) + Math.pow((colP0 - destCol), 2));
				double weight = Math.pow(2,(-distance/normFactor));
				double angle = Math.atan2(destRow - rowP0, destCol - colP0);
				double deltaDistance = maxDistDelta * distance;
				double weightedDistance = distance - (weight * deltaDistance);

				int scrRow = (int) (rowP0 + Math.sin(angle) * weightedDistance);
				int scrCol = (int) (colP0 + Math.cos(angle) * weightedDistance);

				if(scrRow < 0){
					scrRow = 0;
				}

				if(scrCol < 0){
					scrCol = 0;
				}

				if( scrCol > imagePixels[0].length -1){
					scrCol = imagePixels[0].length -1;
				}

				if(scrRow > imagePixels.length -1){
					scrRow = imagePixels.length -1;
				}

				destination[destRow][destCol] = imagePixels[scrRow][scrCol];
			}
		}
		
		updateImage(destination);
	}
	
	/**
	 * Executes the operations stored in the given text file 
	 * @param filePath Path to the file that contains the instructions
	 * 
	 */
	//thi smethod should execute instructions for an exported file 
	//Ex: LIQUIFY 50 40 60 70 the computer should execute each one as a paramater
	private static void executeFileInstructions(String filePath){
		//String readTheFile;
		File fileName = new File(filePath);
		try{

			Scanner reading = new Scanner(fileName);
				while(reading.hasNextLine()){//this gives the computer the ability to see if the file has no more comands to execute
					String readTheFile = reading.next();//reads the first element in the file which we are assuking is the effect 
														//NOTE: This method wont work if the file is not in the correct format

					//the switch statement lets the computer travers the file based on its elements then call the method passing the parameters given by the file
					switch(readTheFile){
					
						case "LIQUIFY":
							liquifyEffect(workingImage, reading.nextInt(), reading.nextInt(), reading.nextInt(), reading.nextInt(), normFactor);
						break;

						case "TWIST_LEFT":
							twistLeftEffect(workingImage, reading.nextInt(), reading.nextInt(), normFactor);
						break;

						case "BULGE":
							bulgeEffect(workingImage, reading.nextInt(), reading.nextInt(), normFactor);
						break;

						case "PINCH":
							pinchEffect(workingImage, reading.nextInt(), reading.nextInt(), normFactor);
						break;

						case "TWIST_RIGHT":
							twistRightEffect(workingImage, reading.nextInt(), reading.nextInt(), normFactor);
						break;
					}
				}
			}
		catch(FileNotFoundException e){
			System.out.println("File Not Found");
		}
	} 
	
	/**
	 * Appends the given effect to the given text file
	 * 
	 * @param filePath		Path to the text file
	 * @param effectName	One of the following: LIQUIFY, TWIST_LEFT, TWIST_RIGHT, PINCH, BULGE
	 * @param effectParams	Effect parameters
	 */
	private static void appendOperationToFile(String filePath, String effectName, int[] effectParams) {
		//for almost all file interaction you need a try & catch to allert if the file could not be open 
		try{
			File recordFile = new File(filePath);

			//if(recordFile.createNewFile()){
				String additionToString = " ";
				System.out.println("File has been updated");//you dont need thi sbut its good to have it allerst the user the file has been updated

				//this for loop traverses an effect by adding its effect to the file
				//ex: LIQUIFY 50 100 300 40
 				for(int i = 0; i < effectParams.length; i++){
					  additionToString = additionToString + effectParams[i] + " ";
				}
				String writeToFile = effectName + additionToString;
				FileWriter apprendInsFile = new FileWriter(filePath, true);
				PrintWriter writeToInstrFile = new PrintWriter(apprendInsFile, true);

				//this end the ability to write on the file
				writeToInstrFile.println(writeToFile);
				apprendInsFile.close();
				writeToInstrFile.close();
			//}
			//else{
				//System.out.println("File already exist");
			//}
		}
		catch(Exception e){
			System.out.println("exeption");
		}
	}
	
	/**
	 * Extra Credit: Save the image that is being displayed.
	 * Hint: the global variable workingImage holds the pixel values of the image that needs to be saved
	 */
	protected static void saveImageClicked() {
	    JOptionPane.showMessageDialog(frame, "Your IMage has been saved as NewImage");
	    saveImage(workingImage, "NewImage");
	}
	
	
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	// 
	// You can ignore the rest of this file. You don't have to understand what these methods do.
	// However, if you are curious, feel free to read them! (:
	//
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	
	/**
	 * Updates the image being displayed
	 * @param imagePixels New image to be displayed
	 */
	public static void updateImage(Color[][] imagePixels) {
		workingImage = imagePixels;
		mouseClicks.clear();
		frame.remove(imageLabel);
		BufferedImage image = createImageFromPixelArray(imagePixels);
		ImageIcon icon = new ImageIcon();
		icon.setImage(image);
		
		imageLabel = new JLabel();
		imageLabel.setIcon(icon);

		imageLabel.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent me) { }
			public void mouseReleased(MouseEvent me) { }
			public void mouseEntered(MouseEvent me) { }
			public void mouseExited(MouseEvent me) { }
			public void mouseClicked(MouseEvent me) { 
				if(me.getButton() == MouseEvent.BUTTON1) {
					imageClicked(me.getY(), me.getX());
				}
			}
		});

		frame.add(imageLabel);
		frame.invalidate();
		frame.revalidate();
		frame.repaint();

	}

	/**
	 * Computes Euclidean distance of the two given points 
	 * @param rowP0
	 * @param colP0
	 * @param rowP1
	 * @param colP1
	 * @return
	 */
	public static double computeDistance(int rowP0, int colP0, int rowP1, int colP1) 
	{
		return Math.sqrt(Math.pow(rowP1 - rowP0, 2) + Math.pow(colP1 - colP0, 2));
	}

	/**
	 * Executes when the Record/Stop button is clicked.
	 */
	public static void recordClicked() {
		
		if (! recording) 
			recordingFileName = JOptionPane.showInputDialog(frame, "Type the name of the file");
		
		recording = ! recording;
		
		recordButton.setText(recording ? "Stop" : "Record");
	}
	
	/**
	 * This method saves a given image to disk
	 * 
	 * @param imagePixels Image to be saved
	 * @param fileName	  Name of the file
	 */
	public static void saveImage(Color[][] imagePixels, String fileName) 
	{
		try {
			// Convert to BufferedImage
			BufferedImage bi = createImageFromPixelArray(imagePixels);

			//Save file
			File outputfile = new File(fileName);
			ImageIO.write(bi, "jpg", outputfile);
		} catch (IOException e) {
			System.out.println("Error: " + e.toString());
		}
	}
	
	/**
	 * Executes a given effect when the image is clicked
	 * 
	 * @param row row where the image was clicked
	 * @param col col where the image was clicked
	 */
	private static void imageClicked(int row, int col) {
		
		int[] params = {row, col};
		switch(currentEffect) {
		case LIQUIFY:
			if (mouseClicks.size() < 2) {
				mouseClicks.add(row);
				mouseClicks.add(col);
			}
			else {

				int[] liquifyParams = {mouseClicks.get(0), mouseClicks.get(1), row, col};
				liquifyEffect(workingImage, mouseClicks.get(0), mouseClicks.get(1), row, col, normFactor);
				
				if (recording)
					appendOperationToFile(recordingFileName, "LIQUIFY", liquifyParams);
				
				mouseClicks.clear();
				
			}
			break;
		case TWIST_LEFT:
			twistLeftEffect(workingImage, row, col, normFactor);
			
			if (recording)
				appendOperationToFile(recordingFileName, "TWIST_LEFT", params);
			
			break;
		case TWIST_RIGHT:
			twistRightEffect(workingImage, row, col, normFactor);
			
			if (recording)
				appendOperationToFile(recordingFileName, "TWIST_RIGHT", params);
			
			break;
		case PINCH:
			pinchEffect(workingImage, row, col, normFactor);
			
			if (recording)
				appendOperationToFile(recordingFileName, "PINCH", params);

			break;
		case BULGE:
			bulgeEffect(workingImage, row, col, normFactor);
			
			if (recording)
				appendOperationToFile(recordingFileName, "BULGE", params);
			
            break;
            
            case MANY:
			if (mouseClicks.size() < 2) {
				mouseClicks.add(row);
				mouseClicks.add(col);
			}
			else {

				int[] manyParams = {mouseClicks.get(0), mouseClicks.get(1), row, col};
				manyMethod(workingImage, mouseClicks.get(0), mouseClicks.get(1), row, col, normFactor);
				
				if (recording)
					appendOperationToFile(recordingFileName, "MANY", manyParams);
				
				mouseClicks.clear();
				
			}
		}
		
	}
	
	/**
	 * Display a GUI component to select a text file where
	 * the set of effect instructions is stored.
	 */
	private static void executeFromFileButtonClicked() {
		FileFilter textFilter = new FileNameExtensionFilter(
			    "Text Files", "txt");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(textFilter);
		int returnVal = fc.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            
            executeFileInstructions(file.getAbsolutePath());
        } 
	}

	/**
	 * Displays the GUI
	 * 
	 */
	public static void displayApp() 
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		Color[][] whiteImage = {{Color.WHITE}};

		ImageIcon icon = new ImageIcon(resizeImage(createImageFromPixelArray(whiteImage)));
		imageLabel = new JLabel();
		imageLabel.setIcon(icon);

		frame = new JFrame();
		frame.setLayout(new FlowLayout());
        frame.setSize(screenSize.width,screenSize.height);
        
		//JButton menyButton = new JButton("MENY")
		JButton selectImageButton = new JButton("Select Image");
		JButton readFileButton = new JButton("Execute Instructions (from file)");
		recordButton = new JButton("Record");
        JButton saveImageButton = new JButton("Save Image");
		
		
	    ButtonGroup group = new ButtonGroup();
	    JRadioButton liquifyButton = new JRadioButton("Liquify");
	    JRadioButton twistLeftButton = new JRadioButton("Twist Left");
	    JRadioButton twistRightButton = new JRadioButton("Twist Right");
	    JRadioButton pinchButton = new JRadioButton("Pinch");
        JRadioButton bulgeButton = new JRadioButton("Bulge");
        JRadioButton manyButton = new JRadioButton("Many");//what this does is it creates a new button with the name Many
	    group.add(liquifyButton);
	    group.add(twistLeftButton);
	    group.add(twistRightButton);
	    group.add(pinchButton);
        group.add(bulgeButton);
        group.add(manyButton);//what this does is adds to the group of buttons within it 
        
		
	    liquifyButton.setSelected(true);
		frame.add(selectImageButton);
		frame.add(readFileButton);
		frame.add(recordButton);
		frame.add(saveImageButton);
		frame.add(liquifyButton);
		frame.add(twistLeftButton);
		frame.add(twistRightButton);
		frame.add(pinchButton);
		frame.add(bulgeButton);
        frame.add(imageLabel);
        frame.add(manyButton);//added button
		
		
		selectImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readImageClicked();
			}
		});
		
		readFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				executeFromFileButtonClicked();
			}
		});

		recordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				recordClicked();
			}
		});
		saveImageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveImageClicked();
				
			}
		});
		
		liquifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				effectChanged(Effect.LIQUIFY);
			}
		});
		
		twistLeftButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				effectChanged(Effect.TWIST_LEFT);
			}
		});
		
		twistRightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				effectChanged(Effect.TWIST_RIGHT);
			}
		});
		
		pinchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				effectChanged(Effect.PINCH);
			}
		});
		
		bulgeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				effectChanged(Effect.BULGE);
			}
        });
        
        //Things many did
        manyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				effectChanged(Effect.MANY);
			}
		});
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}




	/**
	 * Changes the current effect when a radio button is clicked
	 * @param effect Selected effect
	 */
	protected static void effectChanged(Effect effect) {
		
		currentEffect = effect;
		mouseClicks.clear();
	}



	/**
	 * Opens a file chooser to select an image
	 */
	protected static void readImageClicked() {
		
		FileFilter imageFilter = new FileNameExtensionFilter("Image Files", ImageIO.getReaderFileSuffixes());
		
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(imageFilter);
		int returnVal = fc.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            updateImage(readImage(file.getAbsolutePath()));
        } 
		
	}


	/**
	 * This method reads an image given its path
	 * 
	 * @param filePath	The path of the image to be read
	 * @return			A 2D array of pixels representing the image
	 * @throws Exception Exception is thrown when the file does not exist
	 */
	public static Color[][] readImage(String filePath){
		File imageFile = new File(filePath);
		BufferedImage image;
		try {
			image = ImageIO.read(imageFile);
			image = resizeImage(image);
			
			Color[][] colors = new Color[image.getHeight()][image.getWidth()];

			for (int row = 0; row < image.getHeight(); row++) {
				for (int col = 0; col < image.getWidth(); col++) {
					colors[row][col] = new Color(image.getRGB(col, row));
				}
			}
			
			return colors;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * Method that resized the input image to 854x480
	 * @param Image Image to be resized
	 * @return Resized image
	 */
	public static BufferedImage resizeImage(BufferedImage image) {
		int width = 854;
		int height = 480;
		Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		image = new BufferedImage(width, height, Image.SCALE_REPLICATE);
		image.getGraphics().drawImage(tmp, 0, 0 , null);
		
		return image;
	}

	/**
	 * This method receives a 2D array of pixel colors and returns
	 * its equivalent BufferedImage representation
	 * 
	 * @param imagePixels Pixel values of the image to be converted
	 * 
	 * @return BufferedImage representation of the input
	 */
	public static BufferedImage createImageFromPixelArray(Color[][] imagePixels) {
		BufferedImage image = new BufferedImage(imagePixels[0].length, imagePixels.length, BufferedImage.TYPE_INT_RGB);

		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				image.setRGB(col, row, imagePixels[row][col].getRGB());
			}
		}

		return image;
	}

}