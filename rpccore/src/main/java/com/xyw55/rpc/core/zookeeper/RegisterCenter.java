package com.xyw55.rpc.core.zookeeper;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xyw55.rpc.core.helper.IPHelper;
import com.xyw55.rpc.core.helper.PropertyConfigeHelper;
import com.xyw55.rpc.core.model.InvokerService;
import com.xyw55.rpc.core.model.ProviderService;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.Provider;
import java.util.List;
import java.util.Map;

/**
 * @author xyw55
 * @date 2018/3/17
 */
public class RegisterCenter implements IRegisterCenter4Provider, IRegisterCenter4Invoker {

    private static RegisterCenter registerCenter = new RegisterCenter();

    /**
     * 服务提供者列表，Key：服务提供者接口， value：服务提供者方法列表
     */
    private static final Map<String, List<ProviderService>> providerMap = Maps.newConcurrentMap();

    /**
     * 服务端ZK服务元数据，选择服务（第一次从ZK拉取，后续由ZK的监听机制主动更新）
     */
    private static final Map<String, List<ProviderService>> serviceMetaDataMap4Consume = Maps.newConcurrentMap();

    /**
     * 从配置文件中获取ZK服务器地址列表
     */
    private static String ZK_SERVICE = PropertyConfigeHelper.getZkService();

    /**
     * 从配置文件中获取ZK会话超时配置
     */
    private static int ZK_SESSION_TIMEOUT = PropertyConfigeHelper.getZkSessionTimeout();

    /**
     * 从配置文件中获取ZK连接超时配置
     */
    private static int ZK_CONNECTION_TIMEOUT = PropertyConfigeHelper.getZkConnectionTimeout();

    /**
     * 组装ZK根路径 /APPKEY 路径
     */
    private String ROOT_PATH = "/config_register/";
    private static String PROVIDER_TYPE = "/provider";
    private static String INVOKER_TYPE = "/consumer";

    private static volatile ZkClient zkClient = null;


    private RegisterCenter() {

    }

    public static RegisterCenter singleton() {
        return registerCenter;
    }

    @Override
    public void registerProvider(final List<ProviderService> serviceMetaData) {
        if (CollectionUtils.isEmpty(serviceMetaData)) {
            return;
        }
        //连接ZK，注册服务
        synchronized (RegisterCenter.class) {
            for (ProviderService provider : serviceMetaData) {
                String serviceItfKey = provider.getServiceItf().getName();
                List<ProviderService> providers = providerMap.get(serviceItfKey);
                if (providers == null) {
                    providers = Lists.newArrayList();
                }
                providers.add(provider);
                providerMap.put(serviceItfKey, providers);
            }
            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT, new SerializableSerializer());
            }
            String APPKEY = serviceMetaData.get(0).getAppKey();
            String ZK_PATH = ROOT_PATH + "/" + APPKEY;
            //创建ZK命名空间/当前部署应用APP命名空间
            boolean exist = zkClient.exists(ZK_PATH);
            if (!exist) {
                zkClient.createPersistent(ZK_PATH, true);
            }

            for (Map.Entry<String, List<ProviderService>> entry : providerMap.entrySet()) {
                //创建服务提供者节点
                String seriveNode = entry.getKey();
                String servicePath = ZK_PATH + "/" + seriveNode + PROVIDER_TYPE;
                exist = zkClient.exists(servicePath);
                if (!exist) {
                    zkClient.createPersistent(servicePath, true);
                }
                //创建当前服务器节点
                int servicePort = entry.getValue().get(0).getServerPort();
                String localIp = IPHelper.localIp();
                String currentServiceIpNode = servicePath + "/" + localIp + "|" + servicePort;
                exist = zkClient.exists(currentServiceIpNode);
                if (!exist) {
                    zkClient.createEphemeral(currentServiceIpNode);
                }
                //监听注册服务的变化，同时更新数据到本地缓存
                zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                    @Override
                    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                        if (currentChilds == null) {
                            currentChilds = Lists.newArrayList();
                        }
                        //存活服务IP列表
                        List<String> activityServiceIpList = Lists.newArrayList(Lists.transform(currentChilds, new Function<String, String>() {
                            @Override
                            public String apply(String s) {
                                return StringUtils.split(s,"|")[0];
                            }
                        }));
                        refreshActivityService(activityServiceIpList);
                    }
                });
            }

        }
    }


    @Override
    public Map<String, List<ProviderService>> getProviderServiceMap() {
        return providerMap;
    }


    @Override
    public void initProviderMap(String remoteAppKey, String groupName) {
        if (MapUtils.isEmpty(serviceMetaDataMap4Consume)) {
            serviceMetaDataMap4Consume.putAll(fetchOrUpdateServiceMetaData(remoteAppKey, groupName));
        }
    }


    @Override
    public Map<String, List<ProviderService>> getServiceMetaMapData4Consume() {
        return serviceMetaDataMap4Consume;
    }

    @Override
    public void registerInvoker(InvokerService invoker) {
        if (invoker == null) {
            return;
        }

        synchronized (RegisterCenter.class) {
            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT, new SerializableSerializer());
            }
            String APPKEY = invoker.getRemoteAppKey();
            String ZK_PATH = ROOT_PATH + "/" + APPKEY;
            //创建ZK命名空间/当前部署应用APP命名空间
            boolean exist = zkClient.exists(ZK_PATH);
            if (!exist) {
                zkClient.createPersistent(ZK_PATH, true);
            }

            //创建服务消费者节点
            String seriveNode = invoker.getServiceItf().getName();
            String invokerPath = ZK_PATH + "/" + seriveNode + INVOKER_TYPE;
            exist = zkClient.exists(invokerPath);
            if (!exist) {
                zkClient.createPersistent(invokerPath, true);
            }
            //创建当前消费者节点
            String localIp = IPHelper.localIp();
            String currentInvokerIpNode = invokerPath + "/" + localIp;
            exist = zkClient.exists(currentInvokerIpNode);
            if (!exist) {
                zkClient.createEphemeral(currentInvokerIpNode);
            }
        }
    }

    /**
     * 利用ZK自动刷新当前存活的服务提供者列表数据
     * @param serviceIpList
     */
    private void refreshActivityService(List<String> serviceIpList) {
        if (serviceIpList == null) {
            serviceIpList = Lists.newArrayList();
        }

        Map<String, List<ProviderService>> currentServiceMetaDataMap = Maps.newHashMap();
        for (Map.Entry<String, List<ProviderService>> entry : providerMap.entrySet()) {
            String key = entry.getKey();
            List<ProviderService> providerServices = entry.getValue();
            List<ProviderService> serviceMetaDataModelList = currentServiceMetaDataMap.get(key);
            if (serviceMetaDataModelList == null) {
                serviceMetaDataModelList = Lists.newArrayList();
            }

            for (ProviderService serviceMetaData : providerServices) {
                if (serviceIpList.contains(serviceMetaData.getServerIp())) {
                    serviceMetaDataModelList.add(serviceMetaData);
                }
            }
            currentServiceMetaDataMap.put(key, serviceMetaDataModelList);
        }
        providerMap.putAll(currentServiceMetaDataMap);
    }


    private Map<String, List<ProviderService>> fetchOrUpdateServiceMetaData(String remoteAppKey, String groupName) {
        final Map<String, List<ProviderService>> providerSerivceMap = Maps.newConcurrentMap();
        //连接ZK
        synchronized (RegisterCenter.class) {
            if (zkClient == null) {
                zkClient = new ZkClient(ZK_SERVICE, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT, new SerializableSerializer());
            }

        }
        //从ZK获取服务提供者列表
        String providerPath = ROOT_PATH + "/" + remoteAppKey;
        List<String> providerServices = zkClient.getChildren(providerPath);
        for (String serviceName : providerServices) {
            String servicePath = providerPath + "/" + serviceName + PROVIDER_TYPE;
            List<String> ipPathList = zkClient.getChildren(servicePath);
            for (String ipPath : ipPathList) {
                String serverIp = StringUtils.split(ipPath, "|")[0];
                String serverPort = StringUtils.split(ipPath, "|")[1];
                List<ProviderService> providerServiceList = providerSerivceMap.get(serviceName);
                if (providerServiceList == null) {
                    providerServiceList = Lists.newArrayList();
                }
                ProviderService providerService = new ProviderService();
                try {
                    providerService.setServiceItf(ClassUtils.getClass(serviceName));

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                providerService.setServerIp(serverIp);
                providerService.setServerPort(Integer.parseInt(serverPort));
                providerServiceList.add(providerService);
                providerSerivceMap.put(serviceName, providerServiceList);

            }
            //监听注册服务变化，同时更新数据到本地缓存
            zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                @Override
                public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                    if (currentChilds == null) {
                        currentChilds = Lists.newArrayList();
                    }
                    currentChilds = Lists.newArrayList(Lists.transform(currentChilds, new Function<String, String>() {
                        @Override
                        public String apply(String input) {
                            return StringUtils.split(input, "|")[0];
                        }
                    }));
                    refreshServiceMetaDataMap(currentChilds);
                }
            });
        }
        return providerSerivceMap;
    }

    private void refreshServiceMetaDataMap(List<String> serviceIpList) {
        if (serviceIpList == null) {
            serviceIpList = Lists.newArrayList();
        }
        Map<String, List<ProviderService>> currentServiceMetaDataMap = Maps.newConcurrentMap();
        for (Map.Entry<String, List<ProviderService>> entry : serviceMetaDataMap4Consume.entrySet()) {
            String serviceItfKey = entry.getKey();
            List<ProviderService> serviceList = entry.getValue();
            List<ProviderService> providerServiceList = currentServiceMetaDataMap.get(serviceItfKey);
            if (providerServiceList == null) {
                providerServiceList = Lists.newArrayList();
            }
            for (ProviderService serviceMetaData : serviceList) {
                if (serviceIpList.contains(serviceMetaData.getServerIp())) {
                    providerServiceList.add(serviceMetaData);
                }
            }
            currentServiceMetaDataMap.put(serviceItfKey, providerServiceList);
        }
        serviceMetaDataMap4Consume.putAll(currentServiceMetaDataMap);
        System.out.println("serviceMetaDataMap4Consume: " + JSON.toJSONString(serviceMetaDataMap4Consume));
    }
}
