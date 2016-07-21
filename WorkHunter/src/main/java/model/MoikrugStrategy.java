package model;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vo.Vacancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//moikrug.ru

public class MoikrugStrategy implements Strategy{
    private static final String URL_FORMAT ="https://moikrug.ru/vacancies?city_id=903&page=%d&q=java&utf8=✓";

    //city_id=903




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
                //Elements elements = document.select("div#jobs_list div.inner");
                Elements elements = document.getElementsByClass("job");


                if (elements == null) break;


                if (pagesNumber > 20)   break;



                for (Element element : elements){
                    Vacancy vacancy = new Vacancy();
                    vacancy.setCity(element.select("span.location").text());//
                    vacancy.setCompanyName(element.select("div.company_name a").text());   //
                    String salary  = element.select("div.salary").text();//
                    if (salary.isEmpty())   vacancy.setSalary("");
                    else vacancy.setSalary(salary);

                    vacancy.setSiteName(document.title());
                    vacancy.setUrl("https://moikrug.ru"+element.select("div.title a").attr("href"));//
                    vacancy.setTitle(element.select("div.title").text()); //
                    vacancyList.add(vacancy);
                }
                // }


            }

        } catch (IOException e) {

        }


        return vacancyList;

    }



    protected Document getDocument(String searchString, int page) throws IOException{
        String url = String.format(URL_FORMAT,  page, searchString);
        Document document = null;

        document =  Jsoup.connect(url)
                .userAgent("Chrome/50.0.2661.102")
                .referrer("http://google.com")
                .get();

        document.html();



        return document;
    }
}
