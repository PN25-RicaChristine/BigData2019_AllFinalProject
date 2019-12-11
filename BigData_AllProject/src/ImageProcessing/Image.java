/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

/**
 *
 * @author debuayanri_sd2082
 */
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Image {

    BufferedImage image;
    int width;
    int height;

    public Image() {

        try {
            File input = new File("C:\\Users\\debuayanri_sd2082\\Desktop\\images.jpg");
            image = ImageIO.read(input);
            width = image.getWidth();
            height = image.getHeight();

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color c = new Color(image.getRGB(j, i));
                    Color e = new Color(255, 255, 255);
                    Color d = new Color(0, 0, 0);
                    if (c.getRGB() == d.getRGB() || (c.getBlue() < 50 && c.getGreen() < 50 && c.getRed() < 50)) {
                    } else {
                        image.setRGB(j, i, e.getRGB());
                    }
                }
            }

            File ouptut = new File("C:\\Users\\debuayanri_sd2082\\Desktop\\newimages.jpg");
            ImageIO.write(image, "jpg", ouptut);

        } catch (Exception e) {
        }
    }

    public static void main(String args[]) {
        Image obj = new Image();
    }

}
