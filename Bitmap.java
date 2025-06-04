/** This Program is where all the methods for modifying the bmp files are located.
 * @author	    Adam Garantche
 * @version     1.0,09/20/2024
 *
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class represents a Bitmap image, specifically a 24-bit BMP file.
 * It provides methods to manipulate the image such as inverting, making grayscale, mirroring, blurring, shrinking,
 * doubling size, and rotating the image.
 */
public class Bitmap {
    private int width, height;
    private Color[][] bitmapArray;  // A 2D array to store pixel colors

    /**
     * Loads a BMP image file and reads its pixel data into the bitmapArray.
     * @param file The path to the BMP image file.
     * @throws IOException If there is a problem reading the file or the file is not a 24-bit BMP.
     */
    public Bitmap(String file) throws IOException{
        FileInputStream bmpImageIn = new FileInputStream(file);
        byte[] header = new byte[54];
        bmpImageIn.read(header);  // Reading BMP file header (54 bytes)

        // Extract image width, height, and bits per pixel from the header
        width = readIntLittleEndian(header, 18);  // Image width
        height = readIntLittleEndian(header, 22); // Image height
        int bitsPerPixel = readShortLittleEndian(header, 28);  // Bits per pixel (should be 24)

        if (bitsPerPixel != 24) {
            throw new IOException("Only 24-bit BMP files are supported.");  // Error if not a 24-bit BMP
        }

        // Calculate row size and padding to align pixel data
        int rowSize = (width * 3 + 3) & ~3;  // Row size in bytes (including padding)
        int padding = rowSize - width * 3;   // Padding bytes for each row
        int offset = readIntLittleEndian(header, 10);  // Offset where pixel data starts

        bmpImageIn.skip(offset - 54);  // Skip to the start of pixel data (skip the header)

        bitmapArray = new Color[height][width];  // Initialize the array to store pixel colors

        // Read pixel data row by row (bottom to top for BMP format)
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                int blue = bmpImageIn.read();  // Read blue component
                int green = bmpImageIn.read();  // Read green component
                int red = bmpImageIn.read();  // Read red component
                bitmapArray[i][j] = new Color(red, green, blue);  // Store the pixel color
            }
            bmpImageIn.skip(padding);  // Skip padding bytes at the end of the row
        }
        bmpImageIn.close();  // Close the file
    }

    /**
     * Inverts the colors of the image (makes a negative).
     */
    public void invert() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                Color color = bitmapArray[i][j];  // Get current pixel color
                // Invert each color component
                color.setRed(255 - color.getRed());
                color.setGreen(255 - color.getGreen());
                color.setBlue(255 - color.getBlue());
            }
        }
    }

    /**
     * Converts the image to grayscale by averaging the red, green, and blue components.
     */
    public void grayscale() {
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                Color color = bitmapArray[i][j];  // Get current pixel color
                // Calculate grayscale value (using standard RGB to grayscale conversion)
                int gray = (int) (0.3 * color.getRed() + 0.59 * color.getGreen() + 0.11 * color.getBlue());
                // Set all components (R, G, B) to the grayscale value
                color.setColor(gray, gray, gray);
            }
        }
    }

    /**
     * Mirrors the image vertically (flips the image upside down).
     */
    public void verticalMirror() {
        for (int i = 0; i < height / 2; i++){
            Color[] temp = bitmapArray[i];  // Temporary row to hold a row of pixels
            bitmapArray[i] = bitmapArray[height - i - 1];  // Swap rows
            bitmapArray[height - i - 1] = temp;
        }
    }

    /**
     * Blurs the image by averaging the surrounding pixel colors for each pixel.
     */
    public void blur() {
        Color[][] newBitmapArray = new Color[height][width];  // New array to store blurred pixels
        int[] dr = {-1, 0, 1};  // Row direction offsets for 3x3 blur kernel
        int[] dc = {-1, 0, 1};  // Column direction offsets for 3x3 blur kernel

        // Loop through every pixel
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                int redSum = 0, greenSum = 0, blueSum = 0;
                int count = 0;

                // Loop through the surrounding pixels
                for (int di : dr) {
                    for (int dj : dc) {
                        int ni = i + di;  // New row index
                        int nj = j + dj;  // New column index

                        if (ni >= 0 && ni < height && nj >= 0 && nj < width) {  // Check if within bounds
                            Color color = bitmapArray[ni][nj];  // Get surrounding pixel color
                            redSum += color.getRed();  // Sum red component
                            greenSum += color.getGreen();  // Sum green component
                            blueSum += color.getBlue();  // Sum blue component
                            count++;  // Keep track of number of surrounding pixels
                        }
                    }
                }

                // Calculate average color for blur effect
                newBitmapArray[i][j] = new Color(redSum / count, greenSum / count, blueSum / count);
            }
        }

        bitmapArray = newBitmapArray;  // Replace original pixel array with blurred one
    }

    /**
     * Shrinks the image to half its original size by averaging 2x2 blocks of pixels.
     */
    public void shrink() {
        int newWidth = width / 2;  // New width (half the original)
        int newHeight = height / 2;  // New height (half the original)
        Color[][] newBitmapArray = new Color[newHeight][newWidth];  // New array for smaller image

        // Loop through 2x2 blocks of pixels
        for (int i = 0; i < newHeight; i++){
            for (int j = 0; j < newWidth; j++) {
                int redSum = 0, greenSum = 0, blueSum = 0;
                for (int di = 0; di < 2; di++) {
                    for (int dj = 0; dj < 2; dj++) {
                        Color color = bitmapArray[i * 2 + di][j * 2 + dj];  // Get the color from the 2x2 block
                        redSum += color.getRed();
                        greenSum += color.getGreen();
                        blueSum += color.getBlue();
                    }
                }
                newBitmapArray[i][j] = new Color(redSum / 4, greenSum / 4, blueSum / 4);  // Average the colors
            }
        }

        width = newWidth;  // Update the width
        height = newHeight;  // Update the height
        bitmapArray = newBitmapArray;  // Update the image with the smaller version
    }

    /**
     * Doubles the size of the image by duplicating each pixel.
     */
    public void doubleSize() {
        int newWidth = width * 2;  // New width (double the original)
        int newHeight = height * 2;  // New height (double the original)
        Color[][] newBitmapArray = new Color[newHeight][newWidth];  // New array for larger image

        // Loop through every pixel
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                Color color = bitmapArray[i][j];  // Get the original pixel color
                // Duplicate the pixel to four positions in the new image
                newBitmapArray[i * 2][j * 2] = color;
                newBitmapArray[i * 2 + 1][j * 2] = color;
                newBitmapArray[i * 2][j * 2 + 1] = color;
                newBitmapArray[i * 2 + 1][j * 2 + 1] = color;
            }
        }

        width = newWidth;  // Update the width
        height = newHeight;  // Update the height
        bitmapArray = newBitmapArray;  // Update the image with the larger version
    }

    /**
     * Rotates the image 90 degrees clockwise by rearranging pixel coordinates.
     */
    public void rotateRight(){
        int newWidth = height;  // New width is the original height
        int newHeight = width;  // New height is the original width
        Color[][] newBitmapArray = new Color[newHeight][newWidth];  // New array for the rotated image

        // Loop through every pixel
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newBitmapArray[j][height - 1 - i] = bitmapArray[i][j];  // Set new coordinates
            }
        }

        width = newWidth;  // Update the width
        height = newHeight;  // Update the height
        bitmapArray = newBitmapArray;  // Update the image with the rotated version
    }

    /**
     * Writes the bitmap image to a file.
     * @param file The path to the output BMP file.
     * @throws IOException If there is an error writing the file.
     */
    public void write(String file) throws IOException{
        FileOutputStream bmpImageOut = new FileOutputStream(file);
        int rowSize = (width * 3 + 3) & ~3;  // Row size in bytes (including padding)
        int padding = rowSize - width * 3;  // Padding bytes for each row
        int fileSize = 54 + rowSize * height;  // Total file size (header + pixel data)
        int dataSize = rowSize * height;  // Pixel data size

        // Write BMP header
        bmpImageOut.write(new byte[]{0x42, 0x4D});  // BM signature
        bmpImageOut.write(intToByteArray(fileSize));  // File size
        bmpImageOut.write(new byte[4]);  // Reserved bytes (not used)
        bmpImageOut.write(intToByteArray(54));  // Offset to pixel data (54 bytes)
        bmpImageOut.write(intToByteArray(40));  // DIB header size (40 bytes)
        bmpImageOut.write(intToByteArray(width));  // Image width
        bmpImageOut.write(intToByteArray(height));  // Image height
        bmpImageOut.write(shortToByteArray((short) 1));  // Planes (always 1)
        bmpImageOut.write(shortToByteArray((short) 24));  // Bits per pixel (24-bit)
        bmpImageOut.write(new byte[24]);  // Rest of the DIB header (ignored)
        bmpImageOut.write(intToByteArray(dataSize));  // Image data size

        // Write pixel data
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++){
                Color color = bitmapArray[i][j];
                bmpImageOut.write(color.getBlue());
                bmpImageOut.write(color.getGreen());
                bmpImageOut.write(color.getRed());
            }
            bmpImageOut.write(new byte[padding]);  // Add padding
        }
        bmpImageOut.close();  // Close the file
    }

    // Utility methods to handle little-endian data
    private static int readIntLittleEndian(byte[] data, int offset) {
        return (data[offset] & 0xFF) | ((data[offset + 1] & 0xFF) << 8)
                | ((data[offset + 2] & 0xFF) << 16) | ((data[offset + 3] & 0xFF) << 24);
    }

    private static int readShortLittleEndian(byte[] data, int offset) {
        return (data[offset] & 0xFF) | ((data[offset + 1] & 0xFF) << 8);
    }

    private static byte[] intToByteArray(int value){
        return new byte[]{
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 24) & 0xFF)
        };
    }

    private static byte[] shortToByteArray(short value){
        return new byte[]{
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF)
        };
    }

    // Internal class to represent a pixel color
    private static class Color {
        int red, green, blue;

        public Color() {
            this(0, 0, 0);
        }

        public Color(int red, int green, int blue){
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }

        public void setColor(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public int[] getRGB() {
            return new int[]{red, green, blue};
        }
    }
}
