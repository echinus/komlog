package com.twock.komlog.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.inject.Inject;

import com.twock.komlog.ProxyConfig;
import com.twock.komlog.map.JsonMapReader;
import com.twock.komlog.map.MapTile;
import com.twock.komlog.map.ParsedJsonMapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.util.FileCopyUtils;
import org.testng.Assert;
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

  private ParsedJsonMapResponse load(String filename) throws IOException {
    File testInputFile = new DefaultResourceLoader().getResource("classpath:" + filename).getFile();
    String testInput = FileCopyUtils.copyToString(new FileReader(testInputFile));
    return jsonMapReader.parseJson(testInput);
  }

  @DataProvider(name = "testjson")
  public Object[][] testjsonData() {
    return new Object[][]{
      {"fetchMapTiles.php.json"},
      {"fetchMapTiles2.php.json"}
    };
  }

  @Test(dataProvider = "testjson")
  public void testLoadsWithoutError(String filename) throws Exception {
    ParsedJsonMapResponse response = load(filename);
    log.debug("Parsed {}", response);
  }

  @Test(dependsOnMethods = "testLoadsWithoutError")
  public void testAllianceRead() throws IOException {
    ParsedJsonMapResponse tiles = load("fetchMapTiles.php.json");
    MapTile tile = tiles.getData().get("l_316_t_190");
    Assert.assertNotNull(tile.getTileAlliance());
    Assert.assertEquals(tile.getTileAlliance().getName(), "Elvish Realm");
    Assert.assertEquals(tile.getTileAlliance().getMight(), 9042L);
  }

  @Test(dependsOnMethods = "testLoadsWithoutError")
  public void testUserRead() throws IOException {
    ParsedJsonMapResponse tiles = load("fetchMapTiles.php.json");
    MapTile tile = tiles.getData().get("l_317_t_178");
    Assert.assertNotNull(tile.getTileUser());
    Assert.assertEquals(tile.getTileUser().getId(), 8708881L);
    Assert.assertEquals(tile.getTileUser().getName(), "SGT_PA");
  }
}
