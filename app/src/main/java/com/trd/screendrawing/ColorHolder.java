package com.trd.screendrawing;

import java.util.ArrayList;
import java.util.List;

public class ColorHolder {
    private ColorItem defaultColor = basicColors().get(0);
    private List<ColorItem> basicColors = basicColors();

    public ColorItem getDefaultColor() {
        return this.defaultColor;
    }

    public int colorPosition(ColorItem colorObject) {
        for (int position = 0; position < basicColors.size(); ++position) {
            if (colorObject == basicColors.get(position)) {
                return position;
            }
        }
        return 0;
    }

    public List<ColorItem> basicColors() {
        ArrayList<ColorItem> colorObjects = new ArrayList<>();
        String whiteHex = "FFFFFF";
        String blackHex = "000000";
        colorObjects.add(new ColorItem("White transparent", "BFFBFAFA", whiteHex));
        colorObjects.add(new ColorItem("Gray", "84848C", whiteHex));
        colorObjects.add(new ColorItem("Black", "000000", whiteHex));
        colorObjects.add(new ColorItem("Silver", "F0C0C6", blackHex));
        colorObjects.add(new ColorItem("Maroon", "800000", whiteHex));
        colorObjects.add(new ColorItem("Red", "FF0000", whiteHex));
        colorObjects.add(new ColorItem("Fuchsia", "FF00FF", whiteHex));
        colorObjects.add(new ColorItem("Green", "008000", whiteHex));
        colorObjects.add(new ColorItem("Lime", "00FF00", blackHex));
        colorObjects.add(new ColorItem("Olive", "808000", whiteHex));
        colorObjects.add(new ColorItem("Yellow", "FFFF00", blackHex));
        colorObjects.add(new ColorItem("Navy", "000080", whiteHex));
        colorObjects.add(new ColorItem("Blue", "0000FF", whiteHex));
        colorObjects.add(new ColorItem("Teal", "008080", whiteHex));
        colorObjects.add(new ColorItem("Aqua", "00FFFF", blackHex));

        return colorObjects;
    }
}
