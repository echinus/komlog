package com.twock.komlog;

import java.net.*;
import java.nio.charset.Charset;
import javax.inject.Inject;

import org.littleshoot.proxy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Chris Pearson
 */
@Component
public class KomLog {
  private static final Logger log = LoggerFactory.getLogger(KomLog.class);
  @Inject
  private HttpFilter mapFilter;
  @Inject
  private MapRequestFilter requestFilter;

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProxyConfig.class);
    context.registerShutdownHook();
    context.getBean(KomLog.class).run(args);
  }

  public void run(String[] args) {
    new DefaultHttpProxyServer(8080, requestFilter, new HttpResponseFilters() {
      @Override
      public HttpFilter getFilter(String hostAndPort) {
        return mapFilter;
      }
    }
    ).start();
    try {
      log.info("Local host address is {}", InetAddress.getLocalHost().toString());
    } catch(UnknownHostException e) {
      log.debug("Failed to lookup localhost", e);
    }
  }
}
