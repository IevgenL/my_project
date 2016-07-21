package main;

import model.*;
import view.HtmlView;


public class Main {

    public static void main(String[] args) {
        Provider providerHH = new Provider(new HHStrategy());
        Provider providerMoikrug = new Provider(new MoikrugStrategy());
        Provider providerRobota = new Provider(new RobotaStrategy());

        HtmlView view = new HtmlView();
        Model model = new Model(view, new Provider[]{ providerHH, providerRobota, providerMoikrug});
        Controller controller = new Controller(model);
        view.setController(controller);
        view.userCitySelectEmulationMethod();
    }
}
