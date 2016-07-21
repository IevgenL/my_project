package view;


import main.Controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import vo.Vacancy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;


public class HtmlView implements View {

    private final String filePath =  "web\\vacancies.jsp";

   // private final String filePath = "./src/"+ this.getClass().getPackage().getName().replaceAll("\\.","\\/") + "/vacancies.jsp";

    Controller controller;

    public HtmlView() {
    }


    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            updateFile(getUpdatedFileContent(vacancies));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;

    }

    public void userCitySelectEmulationMethod(){
        controller.onCitySelect("Dnepropetrovsk");
    }

    private void updateFile(String s){
        try {
            PrintWriter fileWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
//            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
//             fileOutputStream.write(Integer.parseInt(s));
//             fileOutputStream.close();
            fileWriter.write(s);
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String getUpdatedFileContent(List<Vacancy> list){
        Document document = null;
        try {

            document = getDocument();

            //String h = document.body().getElementsByClass("template").text();
            Element element = document.getElementsByClass("template").first();   //элемент, у которого есть класс template.
            Element elementClon = element.clone();   // копия этого объекта
            elementClon.removeClass("template").removeAttr("style");  //уд. атрибут "style" и класс "template".

            document.getElementsByAttributeValue("class","vacancy").remove(); //Уд. все добавленные ранее вакансии.



            for (Vacancy vacancy: list){

                Element vac = elementClon.clone();  //шаблон тега

                vac.getElementsByAttributeValue("class", "city").get(0).text(vacancy.getCity());  // элемент, у которого есть класс "city". запись
                vac.getElementsByAttributeValue("class", "companyName").get(0).text(vacancy.getCompanyName());  // элемент, у которого есть класс "companyName". Запись
                vac.getElementsByAttributeValue("class", "salary").get(0).text(vacancy.getSalary());  //элемент, у которого есть класс "salary". Запись
                Element link = vac.getElementsByTag("a").get(0);  //элемент-ссылка с тегом a
                link.text(vacancy.getTitle());  //Запись вакансии(title)
                link.attr("href", vacancy.getUrl());  // реальная ссылка на вакансию вместо href="url"
                element.before(vac.outerHtml());  // outerHtml элемента

            }

        } catch (IOException e) {
            System.out.println("Some exception occurred");
        }

        return document.html();
    }


    protected Document getDocument() throws IOException{
        Document documentHTML =  Jsoup.parse(new File(filePath), "windows-1251");


        return documentHTML;

    }
}
