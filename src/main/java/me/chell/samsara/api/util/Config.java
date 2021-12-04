package me.chell.samsara.api.util;

import com.google.common.base.Splitter;
import me.chell.samsara.Samsara;
import me.chell.samsara.api.gui.GuiTheme;
import me.chell.samsara.api.module.Module;
import me.chell.samsara.api.value.Bind;
import me.chell.samsara.api.value.Value;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class Config {
    private final File configFile = new File(Samsara.FOLDER, "Config.txt");
    private final Splitter splitter = Splitter.on(':');

    public void save() {
        try {
            if (!Files.exists(Paths.get(Samsara.MODID))) {
                Files.createDirectories(Paths.get(Samsara.MODID));
            }

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
            String s = ":";

            writer.println("Section" + s + "Client");
            for(Value<?> v : ClientValues.values) {
                writer.println(v.getName() + s + v.getValue());
            }

            writer.println("Section" + s + "GUI Theme");
            for(Value<?> v : GuiTheme.values) {
                writer.println(v.getName() + s + v.getValue());
            }

            writer.println("Section" + s + "Categories");
            for(Module.Category c : Module.Category.values()) {
                writer.println(c.name() + s + c.getName());
            }

            writer.println("Section" + s + "Modules");
            for(Module m : Samsara.INSTANCE.moduleManager.getModules()) {
                writer.println("Module" + s + m.getName());
                for(Value<?> v : m.getValues()) {
                    writer.println(v.getName() + s + v.getValue());
                }
            }

            Samsara.LOGGER.info("Saved config.");
            writer.close();
        } catch (Exception e) {
            Samsara.LOGGER.error("Failed to save config", e);
        }
    }

    public void load() {
        FileInputStream fileInputStream = null;
        try {
            if (!configFile.exists()) return;

            List<String> list = IOUtils.readLines(fileInputStream = new FileInputStream(configFile), StandardCharsets.UTF_8);

            int parsing = 0;
            Module parseModule = null;
            for (String s : list) {
                try {
                    Iterator<String> iterator = splitter.limit(2).split(s).iterator();
                    String s1 = iterator.next();
                    String s2 = iterator.next();
                    switch (s1) {
                        case "Section":
                            switch (s2) {
                                case "Modules":
                                    parsing = 0;
                                    break;
                                case "Client":
                                    parsing = 1;
                                    break;
                                case "GUI Theme":
                                    parsing = 2;
                                    break;
                                case "Categories":
                                    parsing = 3;
                                    break;
                            }
                            break;
                        case "Module":
                            parseModule = Samsara.INSTANCE.moduleManager.getModule(s2);
                            break;
                        default:
                            switch (parsing) {
                                case 0:
                                    parseValue(parseModule.getValue(s1), s2);
                                    break;
                                case 1:
                                    parseValue(ClientValues.getValue(s1), s2);
                                    break;
                                case 2:
                                    parseValue(GuiTheme.getValue(s1), s2);
                                    break;
                                case 3:
                                    Module.Category.valueOf(s1).setName(s2);
                                    break;
                            }
                    }
                } catch (Exception e) {
                    Samsara.LOGGER.warn("Skipping bad option: {}", s);
                }
            }
            Samsara.LOGGER.info("Loaded config.");
        } catch (Exception e) {
            Samsara.LOGGER.error("Failed to load config", e);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseValue(Value<?> value, String s2) {
        if(value.getValue() instanceof Boolean) {
            ((Value<Boolean>)value).setValue(s2.equalsIgnoreCase("true"));
        }
        else if(value.getValue() instanceof Integer) {
            ((Value<Integer>)value).setValue(Integer.parseInt(s2));
        }
        else if(value.getValue() instanceof Float) {
            ((Value<Float>)value).setValue(Float.parseFloat(s2));
        }
        else if(value.getValue() instanceof Double) {
            ((Value<Double>)value).setValue(Double.parseDouble(s2));
        }
        else if(value.getValue() instanceof Bind) {
            ((Value<Bind>)value).getValue().setKey(Keyboard.getKeyIndex(s2));
        }
        else if(value.getValue() instanceof Color) {
            ((Value<Color>)value).getValue().setARGB(Integer.decode(s2));
        }
        else if(value.getValue() instanceof Enum) {
            Value<Enum> v = (Value<Enum>) value;
            Class<Enum> clazz = v.getValue().getDeclaringClass();
            v.setValue(Enum.valueOf(clazz, s2));
        }
        else if(value.getValue() instanceof Character) {
            ((Value<Character>)value).setValue(s2.charAt(0));
        }
        else if(value.getValue() instanceof String) {
            ((Value<String>)value).setValue(s2);
        }
    }
}
