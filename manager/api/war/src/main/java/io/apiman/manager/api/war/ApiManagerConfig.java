/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apiman.manager.api.war;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.configuration.Configuration;
import org.overlord.commons.config.ConfigurationFactory;

/**
 * Configuration object for the API Manager.
 *
 * @author eric.wittmann@redhat.com
 */
@ApplicationScoped
public class ApiManagerConfig {

    public static final String APIMAN_MANAGER_CONFIG_FILE_NAME = "apiman-manager.config.file.name"; //$NON-NLS-1$
    public static final String APIMAN_MANAGER_CONFIG_FILE_REFRESH = "apiman-manager.config.file.refresh"; //$NON-NLS-1$

    public static final String APIMAN_PLUGIN_REPOSITORIES = "apiman.plugins.repositories"; //$NON-NLS-1$

    private static Configuration config;
    static {
        String configFile = System.getProperty(APIMAN_MANAGER_CONFIG_FILE_NAME);
        String refreshDelayStr = System.getProperty(APIMAN_MANAGER_CONFIG_FILE_REFRESH);
        Long refreshDelay = 5000l;
        if (refreshDelayStr != null) {
            refreshDelay = new Long(refreshDelayStr);
        }

        config = ConfigurationFactory.createConfig(configFile, "apiman.properties", //$NON-NLS-1$
                refreshDelay, null, ApiManagerConfig.class);
    }

    /**
     * Constructor.
     */
    public ApiManagerConfig() {
    }
    
    /**
     * @return the configured plugin repositories
     */
    public Set<URL> getPluginRepositories() {
        Set<URL> rval = new HashSet<URL>();
        String repositories = config.getString(APIMAN_PLUGIN_REPOSITORIES);
        if (repositories != null) {
            String[] split = repositories.split(","); //$NON-NLS-1$
            for (String repository : split) {
                try {
                    rval.add(new URL(repository.trim()));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return rval;
    }

    /**
     * @return the configuration
     */
    public Configuration getConfig() {
        return config;
    }

}
