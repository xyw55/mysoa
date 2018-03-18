package com.xyw55.rpc.core.spring;

import com.xyw55.rpc.core.revoker.RevokerFactoryBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @author xyw55
 * @date 2018/3/18
 */
public class RevokerFactoryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final Logger logger = LoggerFactory.getLogger(RevokerFactoryBeanDefinitionParser.class);
    @Override
    protected Class<?> getBeanClass(Element element) {
        return RevokerFactoryBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        try {
            String timeOut = element.getAttribute("timeout");
            String targetInterface= element.getAttribute("interface");
            String clusterStrategy = element.getAttribute("clusterStrategy");
            String remoteAppKey = element.getAttribute("remoteAppKey");
            String groupName = element.getAttribute("groupName");
            builder.addPropertyValue("timeout", Integer.parseInt(timeOut));
            builder.addPropertyValue("targerInterface", Class.forName(targetInterface));
            builder.addPropertyValue("remoteAppKey", remoteAppKey);
            if (StringUtils.isNotBlank(clusterStrategy)) {
                builder.addPropertyValue("clusterStrategy", clusterStrategy);
            }
            if (StringUtils.isNotBlank(groupName)) {
                builder.addPropertyValue("groupName", groupName);
            }

        } catch (Exception e) {
            logger.error("RevokerFactoryBeanDefinitionParser error.", e);
            throw new RuntimeException(e);
        }
    }
}
