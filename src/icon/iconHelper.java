/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package icon;

/**
 *
 * @author user
 */
import com.kitfox.svg.app.beans.SVGIcon;
import javax.swing.*;
import java.net.URISyntaxException;
import java.net.URL;

public class iconHelper {

    public static void setSVGIcon(JLabel label, String path, int size) {
        try {
            URL url = label.getClass().getResource(path);
            if (url == null) {
                System.err.println("File tidak ditemukan: " + path);
                return;
            }

            SVGIcon svgIcon = new SVGIcon();
            svgIcon.setSvgURI(url.toURI());
            svgIcon.setPreferredSize(new java.awt.Dimension(size, size));
            label.setIcon(svgIcon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSVGIcon(JButton button, String path, int width, int height) {
        try {
            SVGIcon svgIcon = new SVGIcon();
            svgIcon.setSvgURI(button.getClass().getResource(path).toURI());
            svgIcon.setPreferredSize(new java.awt.Dimension(width, height));
            button.setIcon(svgIcon);
        } catch (URISyntaxException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
