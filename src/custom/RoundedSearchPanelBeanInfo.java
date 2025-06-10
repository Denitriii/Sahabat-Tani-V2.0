package custom;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * BeanInfo for RoundedSearchPanel to expose properties in GUI Builder.
 */
public class RoundedSearchPanelBeanInfo extends SimpleBeanInfo {
    private final PropertyDescriptor[] propertyDescriptors;

    public RoundedSearchPanelBeanInfo() {
        try {
            // Property: cornerRadius
            PropertyDescriptor cornerRadius = new PropertyDescriptor(
                "cornerRadius",
                RoundedSearchPanel.class,
                "getCornerRadius",
                "setCornerRadius"
            );
            cornerRadius.setBound(true);
            cornerRadius.setShortDescription("Radius sudut panel");

            // Property: panelColor (mapped to background)
            PropertyDescriptor panelColor = new PropertyDescriptor(
                "panelColor",
                RoundedSearchPanel.class,
                null,
                "setBackground"
            );
            panelColor.setBound(true);
            panelColor.setShortDescription("Warna background panel");

            // Property: text
            PropertyDescriptor text = new PropertyDescriptor(
                "text",
                RoundedSearchPanel.class,
                "getText",
                "setText"
            );
            text.setPreferred(true);
            text.setShortDescription("Teks di dalam search field");

            // Property: font
            PropertyDescriptor font = new PropertyDescriptor(
                "font",
                RoundedSearchPanel.class,
                "getFont",
                "setFont"
            );
            font.setShortDescription("Font teks di dalam search field");

            propertyDescriptors = new PropertyDescriptor[] {
                cornerRadius,
                panelColor,
                text,
                font
            };
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create BeanInfo for RoundedSearchPanel", e);
        }
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors;
    }
}
