package me.fabrimat.uptimex.config;

import java.io.IOException;

public final class MainConfig extends ConfigManager {
    
    private int threadPoolSize;
    private int nestedJobsProtection;
    private int urlTimeout;
    
    public void loadConfiguration() {
        try {
            loadConfig("config.yml", ConfigurationProvider.getProvider(YamlConfiguration.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void loadConfig(String fileName, ConfigurationProvider configurationProvider) throws IOException {
        super.loadConfig(fileName, configurationProvider);
        this.setThreadPoolSize(getConfiguration().getInt("thread-pool-size", 10));
        this.setNestedJobsProtection(getConfiguration().getInt("nested-jobs-protection", 10));
        this.setUrlTimeout(getConfiguration().getInt("url-timeout", 5000));
    }
    
    public int getThreadPoolSize() {
        return threadPoolSize;
    }
    
    private void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
    
    public int getNestedJobsProtection() {
        return nestedJobsProtection;
    }
    
    private void setNestedJobsProtection(int nestedJobsProtection) {
        this.nestedJobsProtection = nestedJobsProtection;
    }
    
    public int getUrlTimeout() {
        return urlTimeout;
    }
    
    private void setUrlTimeout(int urlTimeout) {
        this.urlTimeout = urlTimeout;
    }
}
