package dev.barfuzzle99.no99chunks;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Config {
        private final File configFile;
        private YamlConfiguration yamlConfig;

        private final JavaPlugin pluginInstance;

        public Config(JavaPlugin instance, String relativePath) {
            this.pluginInstance = instance;
            this.configFile = new File(
                    this.pluginInstance.getDataFolder().getAbsolutePath() + File.separator + relativePath);
        }

        @Nullable
        public YamlConfiguration yamlConfig() {
            if (this.yamlConfig == null) {
                loadYamlFromFile();
            }
            return this.yamlConfig;
        }

        public void createFile() {
            try {
                if (this.configFile.getParentFile() != null && !this.configFile.getParentFile().exists()) {
                    this.configFile.getParentFile().mkdirs();
                }
                this.configFile.createNewFile();
            } catch (IOException ex) {
                pluginInstance.getLogger().log(Level.SEVERE, "Could not create file " + configFile.getName());
                ex.printStackTrace();
            }
        }

        public void saveChanges() {
            try {
                this.yamlConfig.save(this.configFile);
            } catch (IOException ex) {
                pluginInstance.getLogger().log(Level.SEVERE, "I/O error saving to " + this.configFile.getName());
                ex.printStackTrace();
            }
        }

        public void loadYamlFromFile() {
            this.yamlConfig = new YamlConfiguration();
            try {
                this.yamlConfig.load(this.configFile);
            } catch (IOException ex) {
                pluginInstance.getLogger().log(Level.SEVERE, "I/O error while loading " + this.configFile.getName());
                ex.printStackTrace();
            } catch (InvalidConfigurationException ex) {
                pluginInstance.getLogger().log(Level.SEVERE, "Invalid configuration in " + this.configFile.getName());
                ex.printStackTrace();
            }

        }

        public File getFile() {
            return this.configFile;
        }
}
