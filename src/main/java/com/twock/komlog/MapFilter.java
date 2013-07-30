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
  private static final Logger log = LoggerFactory.getLogger(MapFilter.class);
  @PersistenceContext
  private EntityManager entityManager;
  @Inject
  private JsonMapReader mapReader;
  @Inject
  private Charset utf8;

  @Override
  public HttpResponse filterResponse(HttpRequest request, HttpResponse response) {
    try {
      String responseString = response.getContent().toString(utf8);
      log.debug("Filtering {} {} {} {}", request.getMethod(), request.getUri(), request.getHeaders(), request.getContent().toString(utf8));
      log.debug("Response {}", responseString);
      /*
    YmxvY2tzPWJsXzM5NV9idF8yNzAlMmNibF8zOTVfYnRfMjY1JTJjYmxfMzk1X2J0XzI2MCZjaGFuZ2VkPWJsXzM5MF9idF8yNzAlMmNibF8zOTBfYnRfMjY1JTJjYmxfMzkwX2J0XzI2MCZsYW5nPWVuJmthYmFtX2lkPTExNDM4MDEyMCZuYWlkPTExMjc4NTI1MiZhY2Nlc3NfdG9rZW49YzA3YzJmYjgtYmRiOS00YzI0LTk4MmUtMWFmYzI5MWQ3ZjEzJm1vYmlsZWlkPTc3NTU4NGE3NjRkZTMzOTIzNzFkZTI3ZmFkZDEyNzJiJnBsYXRmb3JtaWQ9MjAzJmJlY29tZV91c2VyX2lkPSZiZWNvbWVfcGFzc3dvcmQ9JmRlYnVnPTAmZ3Zlcj02LjAuMSZnYW1lU2xvdD0yNiZyYWNlPTEmZ2FtZU51bWJlcj0xMzc0OTU4MDU1JmdhbWVLZXk9MjE3MjRiNjNiN2E3NGE0MTFjOTUyZTA4YTA5ZGRjYzI=
    vcs=6.0.1
    blocks=bl_395_bt_270,bl_395_bt_265,bl_395_bt_260
changed=bl_390_bt_270,bl_390_bt_265,bl_390_bt_260
lang=en
kabam_id=114380120
naid=112785252
access_token=c07c2fb8-bdb9-4c24-982e-1afc291d7f13
mobileid=775584a764de3392371de27fadd1272b
platformid=203
become_user_id=
become_password=
debug=0
gver=6.0.1
gameSlot=26
race=1
gameNumber=1374958055
gameKey=21724b63b7a74a411c952e08a09ddcc2


    YmxvY2tzPWJsXzQwMF9idF8yNzAlMmNibF80MDBfYnRfMjY1JTJjYmxfNDAwX2J0XzI2MCZjaGFuZ2VkPWJsXzM5NV9idF8yNzAlMmNibF8zOTVfYnRfMjY1JTJjYmxfMzk1X2J0XzI2MCZsYW5nPWVuJmthYmFtX2lkPTExNDM4MDEyMCZuYWlkPTExMjc4NTI1MiZhY2Nlc3NfdG9rZW49YzA3YzJmYjgtYmRiOS00YzI0LTk4MmUtMWFmYzI5MWQ3ZjEzJm1vYmlsZWlkPTc3NTU4NGE3NjRkZTMzOTIzNzFkZTI3ZmFkZDEyNzJiJnBsYXRmb3JtaWQ9MjAzJmJlY29tZV91c2VyX2lkPSZiZWNvbWVfcGFzc3dvcmQ9JmRlYnVnPTAmZ3Zlcj02LjAuMSZnYW1lU2xvdD0zNCZyYWNlPTEmZ2FtZU51bWJlcj0xMzc0OTU4MDYzJmdhbWVLZXk9MDE0ZGJjMTFkYTU5YTk1MzJlMzliOTJmZWI1MzcwMGU=
    
    blocks=bl_400_bt_270,bl_400_bt_265,bl_400_bt_260
changed=bl_395_bt_270,bl_395_bt_265,bl_395_bt_260
lang=en
kabam_id=114380120
naid=112785252
access_token=c07c2fb8-bdb9-4c24-982e-1afc291d7f13
mobileid=775584a764de3392371de27fadd1272b
platformid=203
become_user_id=
become_password=
debug=0
gver=6.0.1
gameSlot=34
race=1
gameNumber=1374958063
gameKey=014dbc11da59a9532e39b92feb53700e

YmxvY2tzPWJsXzQxMF9idF8yNjUlMmNibF80MTBfYnRfMjYwJTJjYmxfNDEwX2J0XzI1NSUyY2JsXzQwNV9idF8yNjUlMmNibF80MDVfYnRfMjYwJTJjYmxfNDA1X2J0XzI1NSZjaGFuZ2VkPSZsYW5nPWVuJmthYmFtX2lkPTExNDM4MDEyMCZuYWlkPTExMjc4NTI1MiZhY2Nlc3NfdG9rZW49YzA3YzJmYjgtYmRiOS00YzI0LTk4MmUtMWFmYzI5MWQ3ZjEzJm1vYmlsZWlkPTc3NTU4NGE3NjRkZTMzOTIzNzFkZTI3ZmFkZDEyNzJiJnBsYXRmb3JtaWQ9MjAzJmJlY29tZV91c2VyX2lkPSZiZWNvbWVfcGFzc3dvcmQ9JmRlYnVnPTAmZ3Zlcj02LjAuMSZnYW1lU2xvdD00MiZyYWNlPTEmZ2FtZU51bWJlcj0xMzc0OTU4MDcxJmdhbWVLZXk9N2FmNWI3YWEyM2EzNThkNzYzYjE5YTRmODQzMjg4NGQ=
blocks=bl_410_bt_265,bl_410_bt_260,bl_410_bt_255,bl_405_bt_265,bl_405_bt_260,bl_405_bt_255
changed=
lang=en
kabam_id=114380120
naid=112785252
access_token=c07c2fb8-bdb9-4c24-982e-1afc291d7f13
mobileid=775584a764de3392371de27fadd1272b
platformid=203
become_user_id=
become_password=
debug=0
gver=6.0.1
gameSlot=42
race=1
gameNumber=1374958071
gameKey=7af5b7aa23a358d763b19a4f8432884d

22:01:53.677 [New I/O worker #4] DEBUG com.twock.komlog.MapFilter - Response 
{"ok":true,"data":
{"l_406_t_266":
{"tileId":"241326","xCoord":"406","yCoord":"266","tileType":"40","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_406_t_267":
{"tileId":"241327","xCoord":"406","yCoord":"267","tileType":"51","orgTileLevel":"5","tileLevel":"9","tileCityId":"5709","tileUserId":"8555249","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","cityName":"NewCity53D9T","race":"2","misted":false},"l_406_t_268":
{"tileId":"241328","xCoord":"406","yCoord":"268","tileType":"30","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_406_t_269":
{"tileId":"241329","xCoord":"406","yCoord":"269","tileType":"30","orgTileLevel":"5","tileLevel":"5","tileCityId":"5709","tileUserId":"8555249","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false,"cityName":"???"},"l_406_t_270":
{"tileId":"241330","xCoord":"406","yCoord":"270","tileType":"30","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_407_t_266":
{"tileId":"241331","xCoord":"407","yCoord":"266","tileType":"51","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_407_t_267":
{"tileId":"241332","xCoord":"407","yCoord":"267","tileType":"20","orgTileLevel":"8","tileLevel":"8","tileCityId":"5709","tileUserId":"8555249","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false,"cityName":"???"},"l_407_t_268":
{"tileId":"241333","xCoord":"407","yCoord":"268","tileType":"20","orgTileLevel":"1","tileLevel":"1","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_407_t_269":
{"tileId":"241334","xCoord":"407","yCoord":"269","tileType":"40","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_407_t_270":
{"tileId":"241335","xCoord":"407","yCoord":"270","tileType":"40","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_408_t_266":
{"tileId":"241336","xCoord":"408","yCoord":"266","tileType":"10","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_408_t_267":
{"tileId":"241337","xCoord":"408","yCoord":"267","tileType":"10","orgTileLevel":"10","tileLevel":"10","tileCityId":"5709","tileUserId":"8555249","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false,"cityName":"???"},"l_408_t_268":
{"tileId":"241338","xCoord":"408","yCoord":"268","tileType":"51","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_408_t_269":
{"tileId":"241339","xCoord":"408","yCoord":"269","tileType":"51","orgTileLevel":"10","tileLevel":"2","tileCityId":"20370","tileUserId":"7733457","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","cityName":"NewCity4LR69","race":"1","misted":false},"l_408_t_270":
{"tileId":"241340","xCoord":"408","yCoord":"270","tileType":"20","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_409_t_266":
{"tileId":"241341","xCoord":"409","yCoord":"266","tileType":"10","orgTileLevel":"6","tileLevel":"6","tileCityId":"14282","tileUserId":"8099545","tileAllianceId":"1147","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false,"cityName":"???"},"l_409_t_267":
{"tileId":"241342","xCoord":"409","yCoord":"267","tileType":"40","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_409_t_268":
{"tileId":"241343","xCoord":"409","yCoord":"268","tileType":"20","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_409_t_269":
{"tileId":"241344","xCoord":"409","yCoord":"269","tileType":"40","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_409_t_270":
{"tileId":"241345","xCoord":"409","yCoord":"270","tileType":"30","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_410_t_266":
{"tileId":"241346","xCoord":"410","yCoord":"266","tileType":"51","orgTileLevel":"4","tileLevel":"1","tileCityId":"20204","tileUserId":"8574609","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","cityName":"NewCity53S7L","race":"2","misted":false},"l_410_t_267":
{"tileId":"241347","xCoord":"410","yCoord":"267","tileType":"51","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_410_t_268":
{"tileId":"241348","xCoord":"410","yCoord":"268","tileType":"30","orgTileLevel":"1","tileLevel":"1","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_410_t_269":
{"tileId":"241349","xCoord":"410","yCoord":"269","tileType":"40","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false},"l_410_t_270":
{"tileId":"241350","xCoord":"410","yCoord":"270","tileType":"10","orgTileLevel":"9","tileLevel":"9","tileCityId":"14282","tileUserId":"8099545","tileAllianceId":"1147","tileProvinceId":"7","tileBlockId":"81053","tileImgName":"","misted":false,"cityName":"???"},"l_406_t_256":
{"tileId":"240976","xCoord":"406","yCoord":"256","tileType":"51","orgTileLevel":"8","tileLevel":"8","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_406_t_257":
{"tileId":"240977","xCoord":"406","yCoord":"257","tileType":"10","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_406_t_258":
{"tileId":"240978","xCoord":"406","yCoord":"258","tileType":"10","orgTileLevel":"7","tileLevel":"7","tileCityId":"19870","tileUserId":"8056881","tileAllianceId":"6772","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false,"cityName":"???"},"l_406_t_259":
{"tileId":"240979","xCoord":"406","yCoord":"259","tileType":"30","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_406_t_260":
{"tileId":"240980","xCoord":"406","yCoord":"260","tileType":"51","orgTileLevel":"2","tileLevel":"1","tileCityId":"4914","tileUserId":"6791833","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","cityName":"NewCity41KM1","race":"1","misted":false},"l_407_t_256":
{"tileId":"240981","xCoord":"407","yCoord":"256","tileType":"0","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_407_t_257":
{"tileId":"240982","xCoord":"407","yCoord":"257","tileType":"40","orgTileLevel":"1","tileLevel":"1","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_407_t_258":
{"tileId":"240983","xCoord":"407","yCoord":"258","tileType":"30","orgTileLevel":"8","tileLevel":"8","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_407_t_259":
{"tileId":"240984","xCoord":"407","yCoord":"259","tileType":"20","orgTileLevel":"8","tileLevel":"8","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_407_t_260":
{"tileId":"240985","xCoord":"407","yCoord":"260","tileType":"20","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_408_t_256":
{"tileId":"240986","xCoord":"408","yCoord":"256","tileType":"10","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_408_t_257":
{"tileId":"240987","xCoord":"408","yCoord":"257","tileType":"20","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_408_t_258":
{"tileId":"240988","xCoord":"408","yCoord":"258","tileType":"40","orgTileLevel":"6","tileLevel":"6","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_408_t_259":
{"tileId":"240989","xCoord":"408","yCoord":"259","tileType":"30","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_408_t_260":
{"tileId":"240990","xCoord":"408","yCoord":"260","tileType":"20","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_409_t_256":
{"tileId":"240991","xCoord":"409","yCoord":"256","tileType":"20","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_409_t_257":
{"tileId":"240992","xCoord":"409","yCoord":"257","tileType":"51","orgTileLevel":"6","tileLevel":"9","tileCityId":"19870","tileUserId":"8056881","tileAllianceId":"6772","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","cityName":"Doth Golgor","race":"1","misted":false},"l_409_t_258":
{"tileId":"240993","xCoord":"409","yCoord":"258","tileType":"51","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_409_t_259":
{"tileId":"240994","xCoord":"409","yCoord":"259","tileType":"40","orgTileLevel":"8","tileLevel":"8","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_409_t_260":
{"tileId":"240995","xCoord":"409","yCoord":"260","tileType":"30","orgTileLevel":"1","tileLevel":"1","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_410_t_256":
{"tileId":"240996","xCoord":"410","yCoord":"256","tileType":"30","orgTileLevel":"6","tileLevel":"6","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_410_t_257":
{"tileId":"240997","xCoord":"410","yCoord":"257","tileType":"40","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_410_t_258":
{"tileId":"240998","xCoord":"410","yCoord":"258","tileType":"30","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_410_t_259":
{"tileId":"240999","xCoord":"410","yCoord":"259","tileType":"51","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_410_t_260":
{"tileId":"241000","xCoord":"410","yCoord":"260","tileType":"10","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81051","tileImgName":"","misted":false},"l_406_t_261":
{"tileId":"241301","xCoord":"406","yCoord":"261","tileType":"10","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_406_t_262":
{"tileId":"241302","xCoord":"406","yCoord":"262","tileType":"10","orgTileLevel":"1","tileLevel":"1","tileCityId":"19922","tileUserId":"8500905","tileAllianceId":"608","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false,"cityName":"???"},"l_406_t_263":
{"tileId":"241303","xCoord":"406","yCoord":"263","tileType":"30","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_406_t_264":
{"tileId":"241304","xCoord":"406","yCoord":"264","tileType":"51","orgTileLevel":"8","tileLevel":"2","tileCityId":"19922","tileUserId":"8500905","tileAllianceId":"608","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","cityName":"thazrandril","race":"2","misted":false},"l_406_t_265":
{"tileId":"241305","xCoord":"406","yCoord":"265","tileType":"40","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_407_t_261":
{"tileId":"241306","xCoord":"407","yCoord":"261","tileType":"30","orgTileLevel":"8","tileLevel":"8","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_407_t_262":
{"tileId":"241307","xCoord":"407","yCoord":"262","tileType":"20","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_407_t_263":
{"tileId":"241308","xCoord":"407","yCoord":"263","tileType":"30","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_407_t_264":
{"tileId":"241309","xCoord":"407","yCoord":"264","tileType":"30","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_407_t_265":
{"tileId":"241310","xCoord":"407","yCoord":"265","tileType":"30","orgTileLevel":"8","tileLevel":"8","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_408_t_261":
{"tileId":"241311","xCoord":"408","yCoord":"261","tileType":"30","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_408_t_262":
{"tileId":"241312","xCoord":"408","yCoord":"262","tileType":"30","orgTileLevel":"6","tileLevel":"6","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_408_t_263":
{"tileId":"241313","xCoord":"408","yCoord":"263","tileType":"30","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_408_t_264":
{"tileId":"241314","xCoord":"408","yCoord":"264","tileType":"10","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_408_t_265":
{"tileId":"241315","xCoord":"408","yCoord":"265","tileType":"40","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_409_t_261":
{"tileId":"241316","xCoord":"409","yCoord":"261","tileType":"51","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_409_t_262":
{"tileId":"241317","xCoord":"409","yCoord":"262","tileType":"40","orgTileLevel":"8","tileLevel":"8","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_409_t_263":
{"tileId":"241318","xCoord":"409","yCoord":"263","tileType":"30","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_409_t_264":
{"tileId":"241319","xCoord":"409","yCoord":"264","tileType":"20","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_409_t_265":
{"tileId":"241320","xCoord":"409","yCoord":"265","tileType":"51","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_410_t_261":
{"tileId":"241321","xCoord":"410","yCoord":"261","tileType":"30","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_410_t_262":
{"tileId":"241322","xCoord":"410","yCoord":"262","tileType":"10","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_410_t_263":
{"tileId":"241323","xCoord":"410","yCoord":"263","tileType":"0","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_410_t_264":
{"tileId":"241324","xCoord":"410","yCoord":"264","tileType":"40","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","misted":false},"l_410_t_265":
{"tileId":"241325","xCoord":"410","yCoord":"265","tileType":"51","orgTileLevel":"9","tileLevel":"10","tileCityId":"14282","tileUserId":"8099545","tileAllianceId":"1147","tileProvinceId":"7","tileBlockId":"81052","tileImgName":"","cityName":"Arcadia","race":"1","misted":false},"l_411_t_256":
{"tileId":"241076","xCoord":"411","yCoord":"256","tileType":"51","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_411_t_257":
{"tileId":"241077","xCoord":"411","yCoord":"257","tileType":"30","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_411_t_258":
{"tileId":"241078","xCoord":"411","yCoord":"258","tileType":"10","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_411_t_259":
{"tileId":"241079","xCoord":"411","yCoord":"259","tileType":"20","orgTileLevel":"1","tileLevel":"1","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_411_t_260":
{"tileId":"241080","xCoord":"411","yCoord":"260","tileType":"30","orgTileLevel":"6","tileLevel":"6","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_412_t_256":
{"tileId":"241081","xCoord":"412","yCoord":"256","tileType":"10","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_412_t_257":
{"tileId":"241082","xCoord":"412","yCoord":"257","tileType":"20","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_412_t_258":
{"tileId":"241083","xCoord":"412","yCoord":"258","tileType":"20","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_412_t_259":
{"tileId":"241084","xCoord":"412","yCoord":"259","tileType":"10","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_412_t_260":
{"tileId":"241085","xCoord":"412","yCoord":"260","tileType":"30","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_413_t_256":
{"tileId":"241086","xCoord":"413","yCoord":"256","tileType":"51","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_413_t_257":
{"tileId":"241087","xCoord":"413","yCoord":"257","tileType":"10","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_413_t_258":
{"tileId":"241088","xCoord":"413","yCoord":"258","tileType":"10","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_413_t_259":
{"tileId":"241089","xCoord":"413","yCoord":"259","tileType":"20","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_413_t_260":
{"tileId":"241090","xCoord":"413","yCoord":"260","tileType":"30","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_414_t_256":
{"tileId":"241091","xCoord":"414","yCoord":"256","tileType":"30","orgTileLevel":"1","tileLevel":"1","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_414_t_257":
{"tileId":"241092","xCoord":"414","yCoord":"257","tileType":"51","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_414_t_258":
{"tileId":"241093","xCoord":"414","yCoord":"258","tileType":"0","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_414_t_259":
{"tileId":"241094","xCoord":"414","yCoord":"259","tileType":"30","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_414_t_260":
{"tileId":"241095","xCoord":"414","yCoord":"260","tileType":"20","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_415_t_256":
{"tileId":"241096","xCoord":"415","yCoord":"256","tileType":"30","orgTileLevel":"8","tileLevel":"8","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_415_t_257":
{"tileId":"241097","xCoord":"415","yCoord":"257","tileType":"30","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_415_t_258":
{"tileId":"241098","xCoord":"415","yCoord":"258","tileType":"20","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_415_t_259":
{"tileId":"241099","xCoord":"415","yCoord":"259","tileType":"10","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_415_t_260":
{"tileId":"241100","xCoord":"415","yCoord":"260","tileType":"40","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82051","tileImgName":"","misted":false},"l_411_t_261":
{"tileId":"241401","xCoord":"411","yCoord":"261","tileType":"30","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_411_t_262":
{"tileId":"241402","xCoord":"411","yCoord":"262","tileType":"40","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_411_t_263":
{"tileId":"241403","xCoord":"411","yCoord":"263","tileType":"20","orgTileLevel":"1","tileLevel":"1","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_411_t_264":
{"tileId":"241404","xCoord":"411","yCoord":"264","tileType":"40","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_411_t_265":
{"tileId":"241405","xCoord":"411","yCoord":"265","tileType":"40","orgTileLevel":"8","tileLevel":"8","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_412_t_261":
{"tileId":"241406","xCoord":"412","yCoord":"261","tileType":"20","orgTileLevel":"6","tileLevel":"6","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_412_t_262":
{"tileId":"241407","xCoord":"412","yCoord":"262","tileType":"51","orgTileLevel":"1","tileLevel":"1","tileCityId":"6370","tileUserId":"8555993","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","cityName":"NewCity53DUH","race":"2","misted":false},"l_412_t_263":
{"tileId":"241408","xCoord":"412","yCoord":"263","tileType":"30","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_412_t_264":
{"tileId":"241409","xCoord":"412","yCoord":"264","tileType":"30","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_412_t_265":
{"tileId":"241410","xCoord":"412","yCoord":"265","tileType":"51","orgTileLevel":"3","tileLevel":"1","tileCityId":"11935","tileUserId":"8563329","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","cityName":"NewCity53JI9","race":"2","misted":false},"l_413_t_261":
{"tileId":"241411","xCoord":"413","yCoord":"261","tileType":"40","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_413_t_262":
{"tileId":"241412","xCoord":"413","yCoord":"262","tileType":"10","orgTileLevel":"9","tileLevel":"9","tileCityId":"14282","tileUserId":"8099545","tileAllianceId":"1147","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false,"cityName":"???"},"l_413_t_263":
{"tileId":"241413","xCoord":"413","yCoord":"263","tileType":"30","orgTileLevel":"6","tileLevel":"6","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_413_t_264":
{"tileId":"241414","xCoord":"413","yCoord":"264","tileType":"40","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_413_t_265":
{"tileId":"241415","xCoord":"413","yCoord":"265","tileType":"10","orgTileLevel":"8","tileLevel":"8","tileCityId":"14282","tileUserId":"8099545","tileAllianceId":"1147","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false,"cityName":"???"},"l_414_t_261":
{"tileId":"241416","xCoord":"414","yCoord":"261","tileType":"20","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_414_t_262":
{"tileId":"241417","xCoord":"414","yCoord":"262","tileType":"40","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_414_t_263":
{"tileId":"241418","xCoord":"414","yCoord":"263","tileType":"20","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_414_t_264":
{"tileId":"241419","xCoord":"414","yCoord":"264","tileType":"20","orgTileLevel":"8","tileLevel":"8","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_414_t_265":
{"tileId":"241420","xCoord":"414","yCoord":"265","tileType":"51","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_415_t_261":
{"tileId":"241421","xCoord":"415","yCoord":"261","tileType":"0","orgTileLevel":"6","tileLevel":"6","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_415_t_262":
{"tileId":"241422","xCoord":"415","yCoord":"262","tileType":"20","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_415_t_263":
{"tileId":"241423","xCoord":"415","yCoord":"263","tileType":"10","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_415_t_264":
{"tileId":"241424","xCoord":"415","yCoord":"264","tileType":"40","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_415_t_265":
{"tileId":"241425","xCoord":"415","yCoord":"265","tileType":"30","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82052","tileImgName":"","misted":false},"l_411_t_266":
{"tileId":"241426","xCoord":"411","yCoord":"266","tileType":"10","orgTileLevel":"1","tileLevel":"1","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_411_t_267":
{"tileId":"241427","xCoord":"411","yCoord":"267","tileType":"30","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_411_t_268":
{"tileId":"241428","xCoord":"411","yCoord":"268","tileType":"20","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_411_t_269":
{"tileId":"241429","xCoord":"411","yCoord":"269","tileType":"20","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_411_t_270":
{"tileId":"241430","xCoord":"411","yCoord":"270","tileType":"30","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_412_t_266":
{"tileId":"241431","xCoord":"412","yCoord":"266","tileType":"20","orgTileLevel":"4","tileLevel":"4","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_412_t_267":
{"tileId":"241432","xCoord":"412","yCoord":"267","tileType":"20","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_412_t_268":
{"tileId":"241433","xCoord":"412","yCoord":"268","tileType":"40","orgTileLevel":"1","tileLevel":"1","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_412_t_269":
{"tileId":"241434","xCoord":"412","yCoord":"269","tileType":"10","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_412_t_270":
{"tileId":"241435","xCoord":"412","yCoord":"270","tileType":"51","orgTileLevel":"6","tileLevel":"1","tileCityId":"13479","tileUserId":"8565433","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","cityName":"NewCity53L4P","race":"2","misted":false},"l_413_t_266":
{"tileId":"241436","xCoord":"413","yCoord":"266","tileType":"51","orgTileLevel":"6","tileLevel":"6","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_413_t_267":
{"tileId":"241437","xCoord":"413","yCoord":"267","tileType":"10","orgTileLevel":"7","tileLevel":"7","tileCityId":"14282","tileUserId":"8099545","tileAllianceId":"1147","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false,"cityName":"???"},"l_413_t_268":
{"tileId":"241438","xCoord":"413","yCoord":"268","tileType":"30","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_413_t_269":
{"tileId":"241439","xCoord":"413","yCoord":"269","tileType":"20","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_413_t_270":
{"tileId":"241440","xCoord":"413","yCoord":"270","tileType":"20","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_414_t_266":
{"tileId":"241441","xCoord":"414","yCoord":"266","tileType":"30","orgTileLevel":"2","tileLevel":"2","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_414_t_267":
{"tileId":"241442","xCoord":"414","yCoord":"267","tileType":"10","orgTileLevel":"6","tileLevel":"6","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_414_t_268":
{"tileId":"241443","xCoord":"414","yCoord":"268","tileType":"51","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_414_t_269":
{"tileId":"241444","xCoord":"414","yCoord":"269","tileType":"40","orgTileLevel":"5","tileLevel":"5","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_414_t_270":
{"tileId":"241445","xCoord":"414","yCoord":"270","tileType":"40","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_415_t_266":
{"tileId":"241446","xCoord":"415","yCoord":"266","tileType":"51","orgTileLevel":"3","tileLevel":"3","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_415_t_267":
{"tileId":"241447","xCoord":"415","yCoord":"267","tileType":"10","orgTileLevel":"9","tileLevel":"9","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_415_t_268":
{"tileId":"241448","xCoord":"415","yCoord":"268","tileType":"40","orgTileLevel":"1","tileLevel":"1","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_415_t_269":
{"tileId":"241449","xCoord":"415","yCoord":"269","tileType":"30","orgTileLevel":"10","tileLevel":"10","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false},"l_415_t_270":
{"tileId":"241450","xCoord":"415","yCoord":"270","tileType":"20","orgTileLevel":"7","tileLevel":"7","tileCityId":"0","tileUserId":"0","tileAllianceId":"0","tileProvinceId":"7","tileBlockId":"82053","tileImgName":"","misted":false}},"userInfo":
{"u8555249":
{"n":"DerekTheDefiler","t":"28","m":"199712","s":"M","w":"1","a":"0","i":"0","r":"2"},"u8099545":
{"n":"Tigerlily","t":"32","m":"1158806","s":"M","w":"1","a":"1147","i":"0","r":"1"},"u6791833":
{"n":"flint_forgefire","t":"2","m":"0","s":"M","w":"1","a":"0","i":"0","r":"1"},"u7733457":
{"n":"julsky","t":"5","m":"0","s":"M","w":"1","a":"0","i":"0","r":"1"},"u8056881":
{"n":"Oakenshield","t":"29","m":"878192","s":"M","w":"1","a":"6772","i":"0","r":"1"},"u8500905":
{"n":"wisest oswald","t":"6","m":"2291","s":"M","w":"1","a":"608","i":"0","r":"2"},"u8555993":
{"n":"vania44","t":"2","m":"0","s":"M","w":"1","a":"0","i":"0","r":"2"},"u8563329":
{"n":"mhusdafendy","t":"4","m":"25","s":"M","w":"1","a":"0","i":"0","r":"2"},"u8565433":
{"n":"NewUser-53L4P","t":"3","m":"0","s":"M","w":"1","a":"0","i":"0","r":"2"},"u8574609":
{"n":"NewUser-53S7L","t":"2","m":"0","s":"M","w":"1","a":"0","i":"0","r":"2"}},"allianceNames":
{"a1147":"Timelords","a6772":"Elven Force One","a608":"Angels_Of_Fury"},"allianceMights":
{"a1147":"2375876","a6772":"136964993","a608":"60245"}}


   */
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
