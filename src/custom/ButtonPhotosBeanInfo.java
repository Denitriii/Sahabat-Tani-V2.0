package custom;

import java.beans.*;

public class ButtonPhotosBeanInfo extends SimpleBeanInfo {
    private final PropertyDescriptor[] propertyDescriptors;

    public ButtonPhotosBeanInfo() {
        try {
            PropertyDescriptor cornerRadius = new PropertyDescriptor("cornerRadius", ButtonPhotos.class);
            cornerRadius.setBound(true);
            cornerRadius.setShortDescription("Set the corner radius of the panel");

            PropertyDescriptor panelColor = new PropertyDescriptor("panelColor", ButtonPhotos.class);
            panelColor.setBound(true);
            panelColor.setShortDescription("Set the panel color");

            PropertyDescriptor icon = new PropertyDescriptor("icon", ButtonPhotos.class);
            icon.setBound(true);
            icon.setShortDescription("The icon displayed on the button");

            PropertyDescriptor text = new PropertyDescriptor("text", ButtonPhotos.class);
            text.setBound(true);
            text.setShortDescription("The text displayed on the button");

            PropertyDescriptor textColor = new PropertyDescriptor("textColor", ButtonPhotos.class);
            textColor.setBound(false);  // biasanya warna tidak di-bound
            textColor.setShortDescription("Set the text color");

            PropertyDescriptor horizontalTextPosition = new PropertyDescriptor("horizontalTextPosition", ButtonPhotos.class);
            horizontalTextPosition.setBound(false);
            horizontalTextPosition.setShortDescription("Set the horizontal position of the text relative to the icon");

            PropertyDescriptor verticalTextPosition = new PropertyDescriptor("verticalTextPosition", ButtonPhotos.class);
            verticalTextPosition.setBound(false);
            verticalTextPosition.setShortDescription("Set the vertical position of the text relative to the icon");

            propertyDescriptors = new PropertyDescriptor[]{
                cornerRadius,
                panelColor,
                icon,
                text,
                textColor,
                horizontalTextPosition,
                verticalTextPosition
            };
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors;
    }
}
