package com.twock.komlog.test;

import javax.inject.Inject;

import com.thoughtworks.xstream.XStream;
import com.twock.komlog.MapRequestFilter;
import com.twock.komlog.ProxyConfig;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author Chris Pearson
 */
@ContextConfiguration(classes = ProxyConfig.class)
public class TestRequestFilter extends AbstractTransactionalTestNGSpringContextTests {
  @Inject
  private MapRequestFilter filter;

  @Test
  public void parseRequest() {
    XStream xStream = new XStream();
    HttpRequest request = (HttpRequest)xStream.fromXML(getClass().getResourceAsStream("/fetchRequest.xml"));
    filter.filter(request);
  }
}
