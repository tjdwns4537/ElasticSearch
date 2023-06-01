package com.example.elasticsearch.crawler;

import com.example.elasticsearch.crawler.repository.StockJpaRepository;
import com.example.elasticsearch.crawler.repository.LikeStockJpaRepository;
import com.example.elasticsearch.stock.domain.StockLikeDto;
import com.example.elasticsearch.thema.domain.Thema;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class CrawlerServiceTest {

    @Autowired
    StockJpaRepository stockJpaRepositorys;

    @Autowired
    LikeStockJpaRepository likeStockJpaRepository;

    @Test
    @DisplayName("네이버 업종 크롤링")
    public void upjongCrawler() {
        String url = "https://finance.naver.com/sise/sise_group.naver?type=upjong";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements big1 = doc.getElementsByAttributeValue("class", "type_1");
            Elements trSelect = big1.get(0).select("tr");

            for (int i = 2; i < trSelect.size(); i++) {
                Element tdSelect = trSelect.get(i).selectFirst("td");
                System.out.println(trSelect);
                Elements href = tdSelect.getElementsByAttribute("href");
                Elements percentEl = trSelect.get(i).getElementsByAttributeValue("class","tah p11 red01");

                if(!percentEl.hasText()){
                    percentEl = trSelect.get(i).getElementsByAttributeValue("class","tah p11 nv01");
                }

                if (href.hasText() && percentEl.hasText() ) {
                    String text = href.text();
                    String percent = percentEl.text();
                    String hrefLink = href.attr("href");
//                    System.out.println(hrefLink);
//                    System.out.println("업종명: " + text + " percent : " + percent);
                }
            }
        } catch (IOException e) {

        }
    }

    @Test
    @DisplayName("구글 크롤링")
    public void googleCrawler() {
        String search = "카카오";
        try {
            for (int i = 0; i < 10; i++) {
                String url = "https://www.google.com/search?q=" + search + "&tbm=nws&sxsrf=APwXEdf_M3-MhMZ4bGGTwdUIp4Xcy2ZIeg:1685424458306&ei=Sol1ZNivEpDr-Qad-LdY&start=" + i + "0&sa=N&ved=2ahUKEwjY_Iexp5z_AhWQdd4KHR38DQsQ8tMDegQIBBAE&biw=1440&bih=734&dpr=2";
                Document doc = Jsoup.connect(url).get();
                Elements titleElements = doc.getElementsByAttributeValue("class", "n0jPhd ynAwRc MBeuO nDgy9d");
                for (Element j : titleElements) {
                    System.out.println(j.text());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("크롤링 정상 작동 테스트")
    public void crawlerImp() {

        String[] numberArr = {
                "005930", "035720", "035420", "000270", "051910", "096770"
        };
        String url = "https://finance.naver.com/item/main.naver?code=";


        try {
            for (int i = 0; i < numberArr.length; i++) {
                Document doc = Jsoup.connect(url + numberArr[i]).get();

                Elements titleElements = doc.getElementsByAttributeValue("class", "wrap_company");
                Element titleElement = titleElements.get(0);
                Elements title = titleElement.select("a");
                String text1 = title.text();

                Elements priceElements = doc.getElementsByAttributeValue("class", "no_today");
                Element priceElement = priceElements.get(0);
                Elements priceSpanElements = priceElement.select("span");
                String text2 = priceSpanElements.get(0).text();

                String percentText = "";
                Elements percentElements = doc.getElementsByAttributeValue("class", "no_exday");
                Element percnetElement = percentElements.get(0);
                System.out.println(percnetElement);
                Elements percentSpanElements = percnetElement.select(".blind");
                Elements selectDown = percnetElement.select(".no_down");
                Elements selectUp = percnetElement.select(".no_up");
                if (!selectDown.isEmpty()) percentText += "-";
                if (!selectUp.isEmpty()) percentText += "+";
                String percent = percentText + percentSpanElements.get(0).text();

                Elements tradeElements = doc.getElementsByAttributeValue("class", "no_info");
                Element tradeElement = tradeElements.get(0);
                Element trade = tradeElement.select(".blind").get(3);
                String text4 = trade.text();

                System.out.println("title : " + text1);
                System.out.println("price : " + text2);
                System.out.println("percent : " + percent);
                System.out.println("tradeCount : " + text4);

            }

        } catch (IOException e) {

        }
    }


    @DisplayName("종목번호 찾기")
    @Test
    void testFind() {
        String[] arr = {"https://kr.investing.com/search/?q=삼성전자",
                "https://kr.investing.com/search/?q=기아",
                "https://kr.investing.com/search/?q=네이버"
        };

        try {

            for (String i : arr) {
                Document doc = Jsoup.connect(i).get();

                /** 종목 이름 **/
                Elements titleElements = doc.getElementsByAttributeValue("class", "js-inner-all-results-quote-item row");
                Element titleElement = titleElements.get(0);
                Elements title = titleElement.select(".second");
                String titleResult = title.get(0).text();
                StockLikeDto stockLikeDto = StockLikeDto.of(titleResult);
                likeStockJpaRepository.save(stockLikeDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<StockLikeDto> all = likeStockJpaRepository.findAll();
        for (StockLikeDto i : all) {
            System.out.println("stock : " + i.getLikeStock());
        }
    }

    @Test
    @DisplayName("실시간 차트 종목 순위")
    void liveStock() {
        String url = "https://finance.naver.com/";

        try {
            Document doc = Jsoup.connect(url).get();

            /** 종목 이름 **/
            Elements titleElements = doc.getElementsByAttributeValue("class", "group_type is_active");
            Element titleElement = titleElements.get(0);
            Elements title = titleElement.select("#_topItems1");
            String titleResult = title.get(0).text();
            System.out.println("titleResult : ");
            System.out.println(titleResult);

            String[] splitResult = titleResult.split("% ");
            for (int i = 0; i < splitResult.length; i++) {
                splitResult[i] = splitResult[i] + "%";
                System.out.println("res : " + splitResult[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("테마주 검색에 어떤 정보가 있는지 확인")
    public void themaPageTest() {
        String paxNet = "http://www.paxnet.co.kr/stock/infoStock/thema";
        try {
            Document paxNetDoc = Jsoup.connect(paxNet).get();

            Elements divValue = paxNetDoc.getElementsByAttributeValue("class", "table-data");

            Element tbody = divValue.select("tbody").get(1);

            Elements tdValue = tbody.select("td");

//            System.out.println(tdValue);

            List<String> strings1 = tbody.getElementsByAttributeValue("class", "ellipsis").eachText(); // 테마명
            for (String i : strings1) {
//                System.out.println(i);
            }

            for (int i = 0; i < tdValue.size(); i++) {
                Elements themaNames = tdValue.get(i).getElementsByAttributeValue("class", "ellipsis");
                Elements next = tdValue.get(i).getElementsByAttributeValue("class", "ellipsis").next();


                if (next.hasText() && themaNames.hasText()) {
                    String percent = next.text();
                    String thema = themaNames.text();
                    String best1 = tdValue.get(i + 6).getElementsByAttribute("href").text();
                    String best2 = tdValue.get(i + 7).getElementsByAttribute("href").text();
                    System.out.println(percent + " " + thema + " " +best1 + " " + best2);
                }
            }

        } catch (IOException e) {
        }
    }

    @Test
    @DisplayName("테마주 검색")
    public void themaStock() {
        String naverUrl = "https://finance.naver.com/sise/theme.naver";
        String paxNet = "http://www.paxnet.co.kr/stock/infoStock/thema";
        try {
            Document naverDoc = Jsoup.connect(naverUrl).get();
            Document paxNetDoc = Jsoup.connect(paxNet).get();

            /** 네이버 종목 테마 **/
            Elements naverTitleElements = naverDoc.getElementsByAttributeValue("class", "type_1 theme");

            Element naverTitleElement = naverTitleElements.get(0);

            Elements naverTitle = naverTitleElement.select(".col_type1");

            String naverPercent = naverDoc.getElementsByAttributeValue("class", "number col_type2").text();

            String[] naverStockPercent = naverPercent.split(" "); // 테마 퍼센트

            System.out.println(naverTitle.size() + " " + naverStockPercent.length);

            for (Element i : naverTitle) {
                System.out.println("thema : " + i.text());
            }

            for (String i : naverStockPercent) {
                System.out.println("percent : " + i);
            }

            /** paxNet 종목 테마 **/
            Elements paxNetTitleElements = paxNetDoc.getElementsByAttributeValue("class", "table-data");
            Elements paxNetSelect = paxNetTitleElements.select(".ellipsis");
            String paxNetSelectText = paxNetSelect.text();
            Elements paxNetSelectPercent = paxNetTitleElements.select(".red");

            String[] paxNetSelectStockThemaName = paxNetSelectText.split(" "); // 테마명
            String[] percentText = paxNetSelectPercent.text().split(" "); // 테마 퍼센트


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("실시간 뉴스 기사 크롤링")
    public void liveArticle() {
        String url = "https://news.naver.com/";

        try {
            Document articleDoc = Jsoup.connect(url).get();

            /** 뉴스기사 **/
            Elements articleTitleElements1 = articleDoc.getElementsByAttributeValue("class", "cjs_dept_desc");
            Elements articleTitleElements2 = articleDoc.getElementsByAttributeValue("class", "cjs_d");

            List<String> list = articleTitleElements1.eachText();

            for (String i : list) System.out.println(i);

//            System.out.println(articleTitleElements1);
//            System.out.println(articleTitleElements2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
