package custom;

import java.beans.*;

public class ComboBoxBeanInfo extends SimpleBeanInfo {
    private final PropertyDescriptor[] propertyDescriptors;

    public ComboBoxBeanInfo() {
        try {
            // Properti: cornerRadius
            PropertyDescriptor cornerRadius = new PropertyDescriptor(
                "cornerRadius",
                ComboBox.class,
                "getCornerRadius",
                "setCornerRadius"
            );
            cornerRadius.setShortDescription("Mengatur radius lengkungan sudut ComboBox");

            // Properti: borderColor
            PropertyDescriptor borderColor = new PropertyDescriptor(
                "borderColor",
                ComboBox.class,
                "getBorderColor",
                "setBorderColor"
            );
            borderColor.setShortDescription("Mengatur warna border ComboBox");

            // Properti: arrowButtonColor
            PropertyDescriptor arrowButtonColor = new PropertyDescriptor(
                "arrowButtonColor",
                ComboBox.class,
                "getArrowButtonColor",
                "setArrowButtonColor"
            );
            arrowButtonColor.setShortDescription("Mengatur warna tombol panah pada ComboBox");

            // Properti: placeholder
            PropertyDescriptor placeholder = new PropertyDescriptor(
                "placeholder",
                ComboBox.class,
                "getPlaceholder",
                "setPlaceholder"
            );
            placeholder.setShortDescription("Placeholder atau teks petunjuk awal sebelum memilih item");

            propertyDescriptors = new PropertyDescriptor[] {
                cornerRadius,
                borderColor,
                arrowButtonColor,
                placeholder
            };
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal membuat BeanInfo untuk RoundedComboBox", e);
        }
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors;
    }
}
