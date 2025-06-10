/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package custom;

import custom.RoundedPanel01;
import java.beans.*;

public class RoundedPanelBeanInfo01 extends SimpleBeanInfo {
    private final PropertyDescriptor[] propertyDescriptors;

    public RoundedPanelBeanInfo01() {
        try {
            PropertyDescriptor cornerRadius = new PropertyDescriptor("cornerRadius", RoundedPanel01.class);
            cornerRadius.setBound(true);
            cornerRadius.setShortDescription("Set the corner radius of the panel");

            PropertyDescriptor panelColor = new PropertyDescriptor("panelColor", RoundedPanel01.class);
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

