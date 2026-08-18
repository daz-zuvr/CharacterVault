package com.nicanimaestudios.charactervault;

import java.io.File;

public class CharacterItem {
    private String name;
    private File imageFile;

    public CharacterItem(String name, File imageFile) {
        this.name = name;
        this.imageFile = imageFile;
    }

    public String getName() {
        return name;
    }

    public File getImageFile() {
        return imageFile;
    }
}