package model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vo.Vacancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//http://rabota.ua





public class RobotaStrategy implements Strategy {
    private static final String URL_FORMAT ="http://rabota.ua/jobsearch/vacancy_list?regionId=4&keyWords=java+&searchdesc=true&pg=%d";
    // regionId=4 - Dnepr;


    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancyList = new ArrayList<>();

        int pagesNumber = 0;
        Document document ;

        try {
            while (true) {
                int elementsSize;
                document = getDocument(searchString, pagesNumber++);
                if (document == null) break;

                //Elements elements = document.getElementsByAttributeValue(" data-qa","[data-qa=\"vacancy-serp__vacancy\"]");
                Elements elements = document.getElementsByClass("v");
                if (elements == null) break;


                if (pagesNumber > 20)   break;



                for (Element element : elements){
                    Vacancy vacancy = new Vacancy();
                    String tmp = element.select("div.s").text();
                    vacancy.setCity(tmp.substring(tmp.indexOf("•")+1  ));
                    vacancy.setCompanyName(tmp.substring(0 , tmp.indexOf("•")));
                    String salary  = element.select("div.s b").text();
                    if (salary.isEmpty())   vacancy.setSalary("");
                    else vacancy.setSalary(salary);

                    vacancy.setSiteName(document.title());
                    vacancy.setUrl("https://rabota.ua"+ element.select("div.rua-g-clearfix a").attr("href"));
                    vacancy.setTitle(element.select("div.rua-g-clearfix").text());
                    vacancyList.add(vacancy);
                }



            }

        } catch (IOException e) {

        }


        return vacancyList;
    }


    protected Document getDocument(String searchString, int page) throws IOException{
        String url = String.format(URL_FORMAT,  page);
        Document document = null;

        document =  Jsoup.connect(url)
                .userAgent("Chrome/50.0.2661.102")
                .referrer("http://google.com")
                .get();

        document.html();



        return document;
    }
}
