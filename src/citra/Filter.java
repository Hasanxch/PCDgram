/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citra;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javax.swing.ImageIcon;




/**
 *
 * @author Hasan Mangrove
 */
public class Filter {
    // filter biasa
    public WritableImage tigaXtigaRata2(Image sumber){
        Image image = new ImageIcon(sumber).getImage();
        Dimension size = new Dimension();
        size.width = image.getWidth(null);
        size.height = image.getHeight(null);
        
        BufferedImage prosesImage3 = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        BufferedImage prosesImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        
        Graphics g = prosesImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        for (int x = 0; x < size.width; x++) {
            for (int y = 0; y < size.height; y++ ) {
                int RGB = prosesImage.getRGB(x, y);
                int alpha = (RGB << 24) & 0xFF;
                int red = (RGB >> 16) & 0xFF;
                int green = (RGB >> 8) & 0xFF;
                int blue = (RGB >> 0) & 0xFF;
                int avg = (red + green + blue)/3;
                int gray = alpha | avg << 16 | avg << 8 | avg;
                prosesImage.setRGB(x, y, gray);
            }
        }
        for (int v = 1; v <= size.height - 2; v++) {
            for (int u = 1; u <= size.width - 2; u++) {
                int sum = 0;
                int temp = 0;
                for (int j = -1; j <= 1; j++){
                    for (int i = -1; i <= 1; i++) {
                        int RGB = prosesImage.getRGB(u + i, v + j);
                        int alpha = (RGB << 24) & 0xFF;
                        int red = (RGB >> 16) & 0xFF;
                        temp = alpha;
                        sum = sum + red;
                    }
                }
                int q = (int) Math.round(sum / 9.0);
                int gray2 = temp | q << 16 | q << 8 | q;
                prosesImage3.setRGB(u, v, gray2);
            }
        }
        
        return SwingFXUtils.toFXImage(prosesImage, null);
    }
    
    public WritableImage medianFilter(Image source){
        BufferedImage img = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_RGB);
        BufferedImage img2 = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_RGB);
        long width = source.getWidth(null);
        long height = source.getHeight(null);
        
        int kernelwidth = 3;
        int kernelheight = 3;
        

        int[] rMedian = new int [kernelwidth*kernelheight];
        int[] gMedian = new int [kernelwidth*kernelheight];
        int[] bMedian = new int [kernelwidth*kernelheight];

        int kerneliter = 0;

        // Walk the entire image but stop before you go out of bounds at the kernel boundraries.
        for (int i = 0; i < width-kernelwidth; i++){
            for (int j=0; j < height-kernelheight; j++){
                // Walk the kernel itself.
                for (int ki = i; ki<kernelwidth; ki++){
                    for(int kj = j; kj<kernelheight; kj++){
                        Color col = new Color(img.getRGB(ki, kj));
                        rMedian[kerneliter] = col.getRed();
                        gMedian[kerneliter] = col.getGreen();
                        bMedian[kerneliter] = col.getBlue();
                        kerneliter++;
                    }
                }
                kerneliter = 0;
                Arrays.sort(rMedian);
                Arrays.sort(gMedian);
                Arrays.sort(bMedian);
                Color colfinal = new Color(rMedian[4], gMedian[4], bMedian[4]);
                img2.setRGB(i+1, j+1, colfinal.getRGB());
            }
        }
        return SwingFXUtils.toFXImage(img2, null);
    }
}
