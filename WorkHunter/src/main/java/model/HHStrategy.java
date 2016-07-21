package model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vo.Vacancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class HHStrategy implements Strategy {
    private static final String URL_FORMAT ="http://hh.ua/search/vacancy?text=java+%s&page=%d";



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
                Elements elements = document.select("[data-qa=\"vacancy-serp__vacancy\"]");
                if (elements == null) break;


                if (pagesNumber > 20)   break;

                for (Element element : elements){
                    Vacancy vacancy = new Vacancy();
                    vacancy.setCity(element.select("[data-qa=\"vacancy-serp__vacancy-address\"]").text());
                    vacancy.setCompanyName(element.select("[data-qa=\"vacancy-serp__vacancy-employer\"").text());
                    String salary  = element.select("[data-qa=\"vacancy-serp__vacancy-compensation\"]").text();
                    if (salary.isEmpty())   vacancy.setSalary("");
                    else vacancy.setSalary(salary);

                    vacancy.setSiteName(document.title());
                    vacancy.setUrl(element.select("[data-qa=\"vacancy-serp__vacancy-title\"]").attr("href"));
                    vacancy.setTitle(element.select("[data-qa=\"vacancy-serp__vacancy-title\"]").text());
                    vacancyList.add(vacancy);
                }



            }

        } catch (IOException e) {

        }


        return vacancyList;
    }


    protected Document getDocument(String searchString, int page) throws IOException{
        String url = String.format(URL_FORMAT, searchString, page);
        Document document = null;

        document =  Jsoup.connect(url)
                .userAgent("Chrome/50.0.2661.102")
                .referrer("http://google.com")
                .get();

        document.html();



        return document;
    }
}
