package com.twock.proxytest.test;

import java.io.File;
import java.io.FileReader;
import javax.inject.Inject;

import com.twock.proxytest.ProxyConfig;
import com.twock.proxytest.map.JsonMapReader;
import com.twock.proxytest.map.ParsedJsonMapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.util.FileCopyUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Chris Pearson
 */
@ContextConfiguration(classes = ProxyConfig.class)
public class TestParser extends AbstractTransactionalTestNGSpringContextTests {
  private static final Logger log = LoggerFactory.getLogger(TestParser.class);
  @Inject
  private JsonMapReader jsonMapReader;

  @DataProvider(name = "testjson")
  public Object[][] testjsonData() {
    return new Object[][]{
      {"fetchMapTiles.php.json"},
      {"fetchMapTiles2.php.json"}
    };
  }

  @Test(dataProvider = "testjson")
  public void testMe(String filename) throws Exception {
    File testInputFile = new DefaultResourceLoader().getResource("classpath:" + filename).getFile();
    String testInput = FileCopyUtils.copyToString(new FileReader(testInputFile));
    ParsedJsonMapResponse response = jsonMapReader.parseJson(testInput);
    log.debug("Parsed {}", response);
  }
}
