package com.ygg.admin.util;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.ygg.admin.config.YggAdminProperties;


public class ESClient
{
    
    private static Client client = null;
    private static ESClient ins = new ESClient();
    
    private ESClient() {}
    
    public static ESClient getInstance() {
        return ins;
    }
    
    @SuppressWarnings("resource")
    private static void init() {
        String elasticsearch_host = YggAdminProperties.getInstance().getPropertie("es_crm_host");
        String elasticsearch_port = YggAdminProperties.getInstance().getPropertie("es_crm_port");
        String elasticsearch_cluster_name = YggAdminProperties.getInstance().getPropertie("es_crm_cluster_name");
        
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", elasticsearch_cluster_name).build();
        client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(elasticsearch_host, Integer.valueOf(elasticsearch_port)));
    }
    
    public Client getClient() {
        if(client == null) {
            init();
        }
        return client;
    }
    
    public static void closeClient() {
        if(client != null)
            client.close();
    }
}
