/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponen;

import javax.swing.*;
import java.awt.*;

public class CustomComboBox extends JComboBox<String> {
    public CustomComboBox() {
        super(new String[]{"Pilih Item", "Bakso", "Soto", "Nasi Campur"});
        setFont(new Font("Arial", Font.BOLD, 14)); // Font modern
        setForeground(Color.WHITE);
        setBackground(new Color(52, 152, 219)); // Warna latar
        setPreferredSize(new Dimension(200, 30)); // Ukuran
    }
}
