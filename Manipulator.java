/** This is the program that can read in a bitmap file and perform a sequence of modifications consisting of the following
 * seven operations, depending on the commands given by the user.
 * Course:      COMP 2100
 * Assignment:	Project 1
 * @author	    Adam Garantche
 * @version     1.0,09/20/2024
 *
 */

import java.io.IOException;
import java.util.Scanner;

/**
 * The Manipulator class lets you edit BMP images using simple commands.
 * You can change the image by inverting, making it grayscale, blurring, etc.
 */
public class Manipulator {

    /**
     * The main method runs the program. It loads the image file and waits for the user to enter commands to edit the image.
     *
     * @param args Command line arguments (not used here).
     */
    public static void main(String[] args) {

        Scanner image = new Scanner(System.in);

        // Ask the user for the image file name
        System.out.print("What image file would you like to edit: ");
        String inputFile = image.nextLine();

        Bitmap bitmap = null;
        try {
            // Try to load the BMP image
            bitmap = new Bitmap(inputFile);
        } catch (IOException e) {
            System.out.println("Wrong file input.");
            return;
        }

        boolean running = true;
        // Keep asking for commands until the user quits
        while (running) {
            System.out.print("What command would you like to perform (i, g, b, v, s, d, r, q): ");
            char command = image.nextLine().charAt(0);

            // Run the correct image change based on the command
            if (command == 'i'){
                bitmap.invert(); // Inverts colors
            } else if (command == 'g'){
                bitmap.grayscale(); // Makes it grayscale
            } else if (command == 'b'){
                bitmap.blur(); // Blurs the image
            } else if (command == 'v'){
                bitmap.verticalMirror(); // Flips the image vertically
            } else if (command == 's'){
                bitmap.shrink(); // Shrinks the image to half size
            } else if (command == 'd'){
                bitmap.doubleSize(); // Doubles the image size
            } else if (command == 'r'){
                bitmap.rotateRight(); // Rotates the image 90 degrees
            } else if (command == 'q'){
                // Save the edited image and quit
                System.out.print("What do you want to name your new image file: ");
                String outputFile = image.nextLine();
                try {
                    bitmap.write(outputFile); // Saves the image
                    System.out.println("Image saved as " + outputFile);
                } catch (IOException e) {
                    System.out.println("Error saving the file.");
                }
                running = false; // Exit the loop and end program
            } else {
                System.out.println("Invalid command."); // Handle wrong command input
            }
        }

        image.close(); // Close the scanner when done
    }
}
