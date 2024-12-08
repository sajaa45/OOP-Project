package service;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

import model.Car;


public class DataAnalysisController {
    private CategoricalFeatureAnalyzer categoricalAnalyzer = new CategoricalFeatureAnalyzer();
    private NumericalFeatureAnalyzer numericalAnalyzer = new NumericalFeatureAnalyzer();

    public void analyzeAndVisualizeCategoricalFeature(List<Car> cars, String featureName) {
        Map<String, Long> analysis = categoricalAnalyzer.analyze(cars, featureName);
        System.out.println("Analysis Results: " + analysis);
        categoricalAnalyzer.visualize(analysis, featureName);
    }

    public void analyzeAndVisualizeNumericalFeature(List<Car> cars, String featureName) {
        DoubleSummaryStatistics analysis = numericalAnalyzer.analyze(cars, featureName);
        System.out.println("Summary Statistics: " + analysis);
        numericalAnalyzer.visualize(cars, featureName);
    }
}
