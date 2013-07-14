package com.twock.proxytest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.inject.Inject;

import org.littleshoot.proxy.DefaultHttpProxyServer;
import org.littleshoot.proxy.HttpFilter;
import org.littleshoot.proxy.HttpResponseFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Chris Pearson
 */
@Component
public class ProxyTest {
  private static final Logger log = LoggerFactory.getLogger(ProxyTest.class);
  @Inject
  private HttpFilter mapFilter;

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProxyConfig.class);
    context.registerShutdownHook();
    context.getBean(ProxyTest.class).run(args);
  }

  public void run(String[] args) {
    new DefaultHttpProxyServer(8080, new HttpResponseFilters() {
      @Override
      public HttpFilter getFilter(String hostAndPort) {
        return mapFilter;
      }
    }).start();
    try {
      log.info("Local host address is {}", InetAddress.getLocalHost().toString());
    } catch(UnknownHostException e) {
      log.debug("Failed to lookup localhost", e);
    }
  }
}
