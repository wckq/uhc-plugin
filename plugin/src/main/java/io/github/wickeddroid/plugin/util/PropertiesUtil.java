package io.github.wickeddroid.plugin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private Properties properties;
    private File f;
    public PropertiesUtil(File file) {
        properties = new Properties();
        this.f = file;

        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public boolean getBoolean(String property) {
        return Boolean.parseBoolean(getPropertyWithCreator(property));
    }

    public boolean getBoolean(String property, String defaultProp) {
        return Boolean.parseBoolean(getPropertyWithCreatorDefault(property, defaultProp));
    }

    public int getInt(String property) {
        return Integer.parseInt(getPropertyWithCreator(property));
    }

    public int getInt(String property, String defaultProp) {
        return Integer.parseInt(getPropertyWithCreatorDefault(property, defaultProp));
    }

    public String getProperty(String property) {
        return String.valueOf(getPropertyWithCreator(property));
    }


    public String getProperty(String property, String defaultProp) {
        return getPropertyWithCreatorDefault(property, defaultProp);
    }

    public String getPropertyWithCreatorDefault(String property, String defaultProp) {
        String prop = getProperties().getProperty(property);

        if(prop == null || prop.equals("")) {
            getProperties().setProperty(property, defaultProp);
            try {
                save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return defaultProp;
        }

        return prop;
    }

    public void setProperty(String s, String s1) {
        getProperties().setProperty(s, s1);

        try {
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPropertyWithCreator(String property) {
        String prop = getProperties().getProperty(property);

        if(prop == null) {
            getProperties().setProperty(property, "");
            try {
                save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        return prop;
    }

    public void save() throws IOException {
        FileOutputStream file = new FileOutputStream(f);
        properties.store(file, "");
        file.close();
        load();
    }

    public void load() throws IOException {
        FileInputStream fr = new FileInputStream(f);
        properties.load(fr);
        fr.close();
    }
}
