/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponen;

import customComponen.RoundedPanel;
import java.beans.*;

public class oundedPanelBeanInfo extends SimpleBeanInfo {
    private final PropertyDescriptor[] propertyDescriptors;

    public oundedPanelBeanInfo() {
        try {
            PropertyDescriptor cornerRadius = new PropertyDescriptor("cornerRadius", RoundedPanel.class);
            cornerRadius.setBound(true);
            cornerRadius.setShortDescription("Set the corner radius of the panel");

            PropertyDescriptor panelColor = new PropertyDescriptor("panelColor", RoundedPanel.class);
            panelColor.setBound(true);
            panelColor.setShortDescription("Set the panel color");

            propertyDescriptors = new PropertyDescriptor[]{cornerRadius, panelColor};
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors;
    }
}

