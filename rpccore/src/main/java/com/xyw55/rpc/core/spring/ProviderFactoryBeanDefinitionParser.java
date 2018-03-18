package com.xyw55.rpc.core.spring;

import com.xyw55.rpc.core.provider.ProviderFactoryBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @author xyw55
 * @date 2018/3/18
 */
public class ProviderFactoryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser{
    private static final Logger logger = LoggerFactory.getLogger(ProviderFactoryBeanDefinitionParser.class);

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ProviderFactoryBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        try {
            String serviceItf = element.getAttribute("interface");
            String timeOut = element.getAttribute("timeout");
            String serverPort = element.getAttribute("serverPort");
            String ref = element.getAttribute("ref");
            String weight = element.getAttribute("weight");
            String workerThreads = element.getAttribute("workerThreads");
            String appKey = element.getAttribute("appKey");
            String groupName = element.getAttribute("groupName");


            builder.addPropertyValue("timeout", Integer.parseInt(timeOut));
            builder.addPropertyValue("serverPort", Integer.parseInt(serverPort));
            builder.addPropertyValue("appKey", appKey);
            builder.addPropertyValue("serviceItf", Class.forName(serviceItf));
            builder.addPropertyValue("serviceObject", ref);
            if (NumberUtils.isNumber(weight)) {
                builder.addPropertyValue("weight", weight);
            }

            if (NumberUtils.isNumber(workerThreads)) {
                builder.addPropertyValue("workerThreads", workerThreads);
            }
            if (StringUtils.isNotBlank(groupName)) {
                builder.addPropertyValue("groupName", groupName);
            }
        } catch (Exception e) {
            logger.error("ProviderFactoryBeanDefinitionParser error.", e);
            throw new RuntimeException(e);
        }
    }
}
