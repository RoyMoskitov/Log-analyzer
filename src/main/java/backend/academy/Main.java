package backend.academy;

import backend.academy.Controller.AnalyzerController;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        AnalyzerController.processAnalysis(args, System.out);
    }
}
