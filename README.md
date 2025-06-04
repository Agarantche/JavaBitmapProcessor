# Java CLI Bitmap Image Processor

This is a command-line application developed in Java that allows users to load, process, and save 24-bit BMP (Bitmap) image files. It provides a suite of basic image manipulation operations, demonstrating understanding of file formats, pixel manipulation, and 2D array handling.

## Features

* **BMP File Loading:** Reads and parses 24-bit BMP file headers and pixel data.
* **BMP File Writing:** Writes modified pixel data back into a valid 24-bit BMP file format, including correct header information and row padding.
* **Image Manipulations:**
    * **Invert Colors:** Creates a negative of the image.
    * **Grayscale:** Converts the image to shades of gray using a standard luma formula (0.3R + 0.59G + 0.11B).
    * **Vertical Mirror:** Flips the image upside down.
    * **Blur:** Applies a simple 3x3 box blur by averaging surrounding pixel colors.
    * **Shrink:** Reduces the image to half its original dimensions by averaging 2x2 blocks of pixels.
    * **Double Size:** Enlarges the image to twice its original dimensions by duplicating each pixel into a 2x2 block.
    * **Rotate Right:** Rotates the image 90 degrees clockwise.
* **Interactive Command-Line Interface (CLI):** Prompts the user for an input image file and then allows them to enter single-character commands to apply various manipulations sequentially.
* **Internal Color Representation:** Uses a custom nested `Color` class to store RGB values for each pixel.

## Technologies Used

* **Java:** Core programming language.
* **File I/O:** `java.io.FileInputStream` and `java.io.FileOutputStream` for reading and writing binary BMP data.
* **2D Arrays:** Used to store and manipulate the image's pixel data (`Color[][] bitmapArray`).
* **Image Processing Algorithms:** Custom implementations for inversion, grayscale, blur, mirroring, resizing, and rotation.
* **Command-Line Interface (CLI):** Interaction managed via `java.util.Scanner`.
* **Object-Oriented Programming (OOP):** Encapsulation of image data and operations within a `Bitmap` class.

## How to Compile and Run

1.  **Prerequisites:**
    * Java Development Kit (JDK) installed (e.g., JDK 8 or later).

2.  **Download Files:**
    * Ensure you have all necessary `.java` files in a single directory:
        * `Bitmap.java` (Contains the image data, manipulation methods, and BMP I/O logic)
        * `Manipulator.java` (Contains the `main` method and CLI interaction)
    * You will also need a 24-bit BMP image file to test with (e.g., `input.bmp`).

3.  **Compile:**
    * Open a terminal or command prompt.
    * Navigate to the directory where you saved the `.java` files.
    * Compile the Java files:
        ```bash
        javac Bitmap.java Manipulator.java
        ```
        (Or `javac *.java` if they are the only Java files)

4.  **Run:**
    * After successful compilation, run the main class (`Manipulator.java`):
        ```bash
        java Manipulator
        ```
    * The program will prompt you for the name of the input BMP image file.
    * Then, it will prompt you for commands to perform.

## CLI Commands

When prompted "What command would you like to perform (i, g, b, v, s, d, r, q): "
* `i`: Invert image colors.
* `g`: Convert image to grayscale.
* `b`: Blur the image.
* `v`: Vertically mirror the image.
* `s`: Shrink the image to half size.
* `d`: Double the image size.
* `r`: Rotate the image 90 degrees clockwise.
* `q`: Quit the program (will prompt for an output filename to save the modified image).

## Project Structure

* `Bitmap.java`: The core class that holds the image data as a 2D array of `Color` objects. It contains all methods for loading, manipulating, and writing BMP files. Includes a private nested `Color` class.
* `Manipulator.java`: The main driver class that handles user input via the command line and calls the appropriate methods in the `Bitmap` class.

---

*Developed by Adam Garantche.*
