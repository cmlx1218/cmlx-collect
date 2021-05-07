package com.cmlx.commons.jsoup;

import com.cmlx.commons.constant.ErrorCode;
import com.cmlx.commons.exception.EXPF;
import com.cmlx.commons.support.StringUtility;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:58
 * @Desc ->
 **/
public class JsoupTest {

    /**
     * 获取缓存key
     *
     * @param
     * @return
     */
    protected String getCacheKey(String nameSpace, Object key) {
        StringBuilder builder = new StringBuilder(nameSpace);
        builder.append(":").append(key);
        return builder.toString();
    }


    public UrlDto resolveUrlOrdinary(String url) throws Exception {
        //设置官网地址
        String basePath = null;
        String[] split = url.split("/");
        if (split.length == 0 || split.length == 1) {
            basePath = split[0];
        }
        if (url.contains("//")) {
            basePath = split[2];
        }
        Document doc = null;
        String title = null;
        String image = null;
        if (!url.contains("http") && !url.contains("https")) {
            url = "http://" + url;
        }
        try {


            /*for (int i = 0; i < 5; i++) {
                String s = HttpUtil.get(url);
                doc = Jsoup.parse(s);
                if (s != null) {
                    break;
                }
            }*/


            WebClient browser = new WebClient(BrowserVersion.CHROME);
            browser.getOptions().setCssEnabled(false);
            browser.getOptions().setJavaScriptEnabled(true);
            browser.getOptions().setThrowExceptionOnScriptError(false);
            browser.getOptions().setUseInsecureSSL(true);
            HtmlPage htmlPage = browser.getPage(url);
            browser.waitForBackgroundJavaScript(1000);

            doc = Jsoup.parse(htmlPage.asXml());
            title = doc.title();
/*            String charset = null;
            Elements meta = doc.select("meta");
            for (Element element : meta) {
                if (element.attr("charset") != null && !"".equals(element.attr("charset"))) {
                    charset = element.attr("charset");
                    break;
                }
            }
            if (charset != null && Charset.defaultCharset().toString().equals(charset)) {
                Document parse = Jsoup.parse(new URL(url).openStream(), charset, url);
                title = parse.title();
            }*/

            Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif|webp)]");
            if (images.size() > 0) {
                String src1 = images.get(0).attr("src");
                if (src1 != null && (!src1.contains("//") || src1.contains("data:")) && !src1.contains("data:image/")) {
                    src1 = basePath + src1;
                }
                System.out.println("src : " + src1);
                image = src1;
                if (!image.contains("http") && !image.contains("https") && !src1.contains("data:image/")) {
                    image = "http://" + image;
                }
                if (image.contains("////")) {
                    image = image.replace("////", "//");
                }
            }
        } catch (Exception e) {
            //接收到错误链接（404页面）
            throw EXPF.exception(ErrorCode.ErrorResolveUrl.getCode(), "解析失败", true);
        }

        UrlDto urlDto = new UrlDto().setUrlTitle(title).setUrlImage(image).setUrl(url);
        if (urlDto.getUrlTitle() == null && urlDto.getUrlImage() == null) {
            throw EXPF.exception(ErrorCode.ErrorResolveUrl.getCode(), "解析失败", true);
        }
        return urlDto;
    }

    public UrlDto resolveUrl(String url) throws Exception {
        // 获取BaseUrl
        String basePath = null;
        String[] split = url.split("/");
        if (split.length == 0 || split.length == 1) {
            basePath = split[0];
        } else {
            if (url.contains("//")) {
                basePath = split[2];
            } else {
                basePath = split[0];
            }
        }
        Document doc = null;
        String title = null;
        String image = null;
        if (!url.contains("http") && !url.contains("https")) {
            url = "http://" + url;
        }
        // 查看缓存中是否有这个链接的图片
        //String imageKey = getCacheKey(CacheConstant.NS_APP_FEED, "Url_Image");
        //BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(imageKey);
        //image = (String) boundHashOperations.get(basePath);
        ////TODO 快手图片处理
        //if (basePath.contains("m.chenzhongtech.com")) {
        //    image = (String) boundHashOperations.get("m.chenzhongtech.com");
        //}

        try {
            //TODO 对不好解析的链接进行单独处理
            // 网易链接
            if ("music.163".contains(basePath) || "m.damai.cn".equals(basePath) || "m.toutiaoimg.cn".contains(basePath)) {
                String s = HttpUtil.get(url);
                doc = Jsoup.parse(s);
            }
            // QQ音乐
            else if ("c.y.qq.com".equals(basePath) || "c.migu.cn".equals(basePath) || "m.damai.cn".equals(basePath) || "h5.m.jd.com".equals(basePath)) {
                WebClient webClient = new WebClient();
                webClient.getOptions().setJavaScriptEnabled(true);
                webClient.getOptions().setCssEnabled(false);
                webClient.setAjaxController(new NicelyResynchronizingAjaxController());
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                HtmlPage page = webClient.getPage(url);
                doc = Jsoup.parse(page.asXml());
            } else {
                WebClient browser = new WebClient();
                browser.getOptions().setCssEnabled(false);
                browser.getOptions().setJavaScriptEnabled(false);
                browser.setAjaxController(new NicelyResynchronizingAjaxController());
                browser.getOptions().setThrowExceptionOnScriptError(false);
                browser.getOptions().setUseInsecureSSL(true);
                HtmlPage htmlPage = browser.getPage(url);
                browser.waitForBackgroundJavaScript(1000);
                doc = Jsoup.parse(htmlPage.asXml());
            }
            //TODO 针对淘宝口令做特殊处理(淘宝分享口令不能用于解析，需要拿到js里面的真正地址)
            if ("m.tb.cn".equals(basePath) || "item.taobao.com".equals(basePath)) {
                Elements script = doc.getElementsByTag("script");
                for (Element element : script) {
                    String data = element.data();
                    if (data.contains("extraData")) {
                        title = data.substring(data.indexOf("{\"title\":\"") + 10, data.indexOf("\","));
                        break;
                    }
//                    boolean flag = false;
                    /*取得JS变量数组*/
//                    String[] data = element.data().toString().split("var");
                    /*取得单个JS变量*/
//                    for (String s : data) {
                    /*获取满足条件的JS变量*/
//                        if (s.contains("url")) {
//                            url = s.split("=", 1)[0];
//                            if (url.contains("taobao.com")) {
//                                url = url.substring(url.indexOf("\'") + 1, url.lastIndexOf("\'"));
//                                flag = true;
//                                break;
//                            }
//                        }
//                    }
//                    if (flag) {
//                        break;
//                    }
                }
                // 过滤title里面的所有html标签
                if (title != null) {
                    title = StringUtility.delHTMLTag(title);
                }
                UrlDto urlDto = new UrlDto().setUrlTitle(title).setUrlImage(image).setUrl(url);
                if (urlDto.getUrlTitle() == null && urlDto.getUrlImage() == null) {
                    throw EXPF.exception(ErrorCode.ErrorResolveUrl.getCode(), "解析失败", true);
                }
                return urlDto;
            }
            //TODO 针对微博做特殊（微博使用自定义图片，且抓取的内容是content）
            if ("m.weibo.cn".contains(basePath) || "weibo.com".contains(basePath)) {
                Elements script = doc.getElementsByTag("script");
                for (Element element : script) {
                    String data = element.data();
                    if (data.contains("$render_data")) {
                        if (data.contains("textLength")) {
                            int i = data.indexOf("\"text\": \"");
                            int i1 = data.lastIndexOf("\"text\": \"");
                            if (i == i1) {
                                title = data.substring(data.lastIndexOf("\"text\": \"") + 9, data.indexOf("\",\n" +
                                        "        \"textLength\""));
                            }
                            if (i != i1) {
                                title = data.substring(data.lastIndexOf("\"text\": \"") + 9, data.indexOf("\",\n" +
                                        "            \"textLength\""));
                            }
                            break;
                        }
                    }
                }
                //TODO 微博的转发动态拿不到content，就拿微博的title
                if (title == null || "".equals(title)) {
                    Elements titles = doc.select("meta[content]");
                    for (int i = 0; i < titles.size(); i++) {
                        String name = titles.get(i).attr("name");
                        if (name != null && name.equals("description")) {
                            title = titles.get(i).attr("content");
                            break;
                        }
                    }
                }
                if (title == null || "".equals(title)) {
                    title = doc.title();
                }
                // 过滤title里面的所有html标签
                if (title != null) {
                    title = StringUtility.delHTMLTag(title);
                }
                UrlDto urlDto = new UrlDto().setUrlTitle(title).setUrlImage(image).setUrl(url);
                if (urlDto.getUrlTitle() == null && urlDto.getUrlImage() == null) {
                    throw EXPF.exception(ErrorCode.ErrorResolveUrl.getCode(), "解析失败", true);
                }
                return urlDto;
            }
            //TODO 抖音
            if ("v.douyin.com".equals(basePath)) {
                Elements elements = doc.getElementsByClass("desc");
                if (elements.size() > 0) {
                    title = elements.get(0).text();
                }
            }
            //TODO 大众点评
            if ("m.dianping.com".equals(basePath)) {
                Elements elements = doc.getElementsByClass("seed-feed-content-txt");
                if (elements.size() > 0) {
                    title = elements.get(0).text();
                }
            }
            //TODO 绿洲
            if ("oasis.weibo.cn".equals(basePath)) {
                Elements elements = doc.getElementsByClass("title");
                if (elements.size() > 0) {
                    title = elements.get(0).text();
                }
            }
            //TODO 腾讯视频
            if ("m.v.qq.com".equals(basePath)) {
                Elements elements = doc.getElementsByClass("text");
                if (elements.size() > 0) {
                    title = elements.get(0).text();
                }
            }
            //TODO 微信
            if ("mp.weixin.qq.com".equals(basePath)) {
                Elements elements = doc.getElementsByClass("rich_media_title");
                if (elements.size() > 0) {
                    title = elements.get(0).text();
                }
            }
            //TODO 拼多多
            if ("mobile.yangkeduo.com".equals(basePath)) {
                Elements elements = doc.getElementsByClass("enable-select");
                if (elements.size() > 0) {
                    title = elements.get(0).text();
                }
            }
            //TODO 腾讯看点
            if ("post.mp.qq.com".equals(basePath)) {
                Elements elements = doc.getElementsByClass("rich_media_title");
                if (elements.size() > 0) {
                    title = elements.get(0).text();
                }
            }
            //TODO 咪咕音乐
            if ("c.migu".equals(basePath)) {
                title = "咪咕音乐_放肆听·趣玩乐";
            }
            //TODO 快手
            if (basePath.contains("m.chenzhongtech.com")) {
                title = "我给你分享了一个快手短视频给你,快来看看吧";
            }
            //TODO 美团
            if ("w.meituan.com".equals(basePath)) {
                title = "美团网-美食_团购_外卖_酒店_旅游_电影票_吃喝玩乐全都有";
            }
            //TODO 唯品会
            if ("m.vip.com".equals(basePath)) {
                title = "唯品会 -品牌特卖！都是好牌子，天天有3折！";
            }
            //TODO 人人视频
            if ("mobile.rr.tv".equals(basePath)) {
                title = "人人影视 字幕组 为您翻译最新最快的海外影视字幕";
            }
            //TODO 微视
            if ("h5.weishi.qq.com".equals(basePath)) {
                title = "腾讯微视·发现更有趣";
            }
            //TODO 西瓜视频
            if ("m.ixigua.com".equals(basePath)) {
                title = "《西瓜视频》给你新鲜好看!";
            }
            //TODO 闲鱼
            if ("market.m.taobao.com".equals(basePath)) {
                title = "闲鱼.淘宝二手 - 轻松卖闲置,放心淘二手";
            }
            //TODO
            if ("www.xiaohongshu.com".equals(basePath)) {
                title = "小红书- 标记我的生活";
            }
            if (title == null || "".equals(title)) {
                title = doc.title();
            }
            // 过滤title里面的所有html标签
            if (title != null) {
                title = StringUtility.delHTMLTag(title);
            }
            if (image == null) {
                Elements images = doc.select("img[src]");
                if (images.size() > 0) {
                    String src1 = images.get(0).attr("src");
                    if (src1 != null && (!src1.contains("//") || src1.contains("data:")) && !src1.contains("data:image/")) {
                        src1 = basePath + src1;
                    }
                    System.out.println("src : " + src1);
                    image = src1;
                    if (!image.contains("http") && !image.contains("https") && !src1.contains("data:image/")) {
                        image = "http://" + image;
                    }
                    if (image.contains("////")) {
                        image = image.replace("////", "//");
                    }
                }
            }
        } catch (Exception e) {
            //接收到错误链接（404页面）
            throw EXPF.exception(ErrorCode.ErrorResolveUrl.getCode(), "解析失败", true);
        }

        UrlDto urlDto = new UrlDto().setUrlTitle(title).setUrlImage(image).setUrl(url);
        if (urlDto.getUrlTitle() == null && urlDto.getUrlImage() == null) {
            throw EXPF.exception(ErrorCode.ErrorResolveUrl.getCode(), "解析失败", true);
        }
        return urlDto;
    }

}
