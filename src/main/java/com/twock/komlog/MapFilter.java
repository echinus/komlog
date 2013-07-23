package com.twock.komlog;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.twock.komlog.map.JsonMapReader;
import com.twock.komlog.map.MapTile;
import com.twock.komlog.map.ParsedJsonMapResponse;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.littleshoot.proxy.HttpFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static java.awt.Color.*;

/**
 * @author Chris Pearson
 */
@Lazy
@Repository
@Transactional
public class MapFilter implements HttpFilter {
  public static final Charset UTF_8 = Charset.forName("UTF8");
  private static final Logger log = LoggerFactory.getLogger(MapFilter.class);
  @PersistenceContext
  private EntityManager entityManager;
  @Inject
  private JsonMapReader mapReader;

  @Override
  public HttpResponse filterResponse(HttpRequest request, HttpResponse response) {
    try {
      String responseString = response.getContent().toString(UTF_8);
      log.debug("Filtering {} {} {} {}", request.getMethod(), request.getUri(), request.getHeaders(), responseString);
      ParsedJsonMapResponse mapTiles = mapReader.parseJson(responseString);
      for(MapTile mapTile : mapTiles.getData().values()) {
        entityManager.merge(mapTile);
      }
      entityManager.flush();

      try {
        BufferedImage image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        List<MapTile> allTiles = entityManager.createQuery("select object(m) from MapTile m").getResultList();
        for(MapTile allTile : allTiles) {
          Color rgb;
          if(allTile.getTileUser() != null && "ploppy".equals(allTile.getTileUser().getName())) {
            rgb = BLUE;
          } else if(allTile.getTileUser() != null
            && ("the Doctor".equals(allTile.getTileUser().getName()) || "DaperDan".equals(allTile.getTileUser().getName()))) {
            rgb = RED;
          } else if(allTile.getTileAlliance() != null && "Breaking_Bad".equals(allTile.getTileAlliance().getName())) {
            rgb = GREEN;
          } else if(allTile.getCityName() != null) {
            rgb = WHITE;
          } else if(mapTiles.getData().values().contains(allTile)) {
            rgb = LIGHT_GRAY;
          } else {
            rgb = GRAY;
          }
          image.setRGB(allTile.getxCoord() - 1, allTile.getyCoord() - 1, rgb.getRGB());
        }
        ImageIO.write(image, "png", new File("image.png"));
      } catch(Exception e) {
        log.error("Failed to write image", e);
      }

      log.trace("Parsed response: {}", mapTiles);
    } catch(Exception e) {
      log.error("Failed to process request", e);
    }
    return response;
  }

  @Override
  public int getMaxResponseSize() {
    return 1048576 /*1 MB*/;
  }

  @Override
  public boolean filterResponses(HttpRequest httpRequest) {
    boolean filter = HttpMethod.POST.equals(httpRequest.getMethod()) && httpRequest.getUri().endsWith("/fetchMapTiles.php");
    log.debug("{} {} to {}", filter ? "Filtering" : "Not filtering", httpRequest.getMethod().toString(), httpRequest.getUri());
    return filter;
  }
}
