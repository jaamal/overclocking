package statisticsservice.export;

public class StatisticsConverterFactory implements IStatisticsConverterFactory {

    @Override
    public IStatisticsConverter create() {
        return new StatisticsConverter();
    }

}
