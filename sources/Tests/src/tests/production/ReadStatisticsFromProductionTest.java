package tests.production;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.LZ77FactorsRepository;
import storage.factorsRepository.LZFactorsRepository;
import storage.filesRepository.IFilesRepository;
import storage.slpProductsRepository.SlpProductsRepository;
import storage.statistics.IStatisticsRepository;
import compressionservice.profile.Analysator;
import dataContracts.AlgorithmType;
import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.files.FileMetadata;
import dataContracts.statistics.RunParamKeys;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class ReadStatisticsFromProductionTest extends ProductionTestBase {
    private IStatisticsRepository statisticsRepository;
    private IFilesRepository fileRepository;
    private IFactorsRepository factorsRepository;
    private SlpProductsRepository slpProductsRepository;
    private LZ77FactorsRepository lz77FactorsRepository;

    @Override
    public void setUp() {
        super.setUp();
        statisticsRepository = container.get(IStatisticsRepository.class);
        fileRepository = container.get(IFilesRepository.class);
        factorsRepository = container.get(LZFactorsRepository.class);
        lz77FactorsRepository = container.get(LZ77FactorsRepository.class);
        slpProductsRepository = container.get(SlpProductsRepository.class);
    }

    private enum FileType {
        Dna, Random, Bad, Obsolete
    }

    @Test
    public void testDeleteObsoleteStatistics() {
        String[] fileIds = fileRepository.getFileIds().toArray(new String[0]);
        System.out.println(String.format("Total file count %d", fileIds.length));
        HashMap<String, StatisticsObject[]> statsBySourceId = statisticsRepository.readAll(fileIds);
        for (String factorizationId : factorsRepository.getDoneStatisticIds()) {
            statsBySourceId.put(factorizationId, statisticsRepository.readAll(factorizationId));
        }
        HashMap<String, FileType> fileTypeByFileId = getFileTypes(fileIds);
        HashMap<String, FileType> sourceFileTypeByStatsId = findSourceFileTypes(fileTypeByFileId, statsBySourceId);
        deleteObsoleteFilesAndStatistics(fileTypeByFileId, sourceFileTypeByStatsId, statsBySourceId);
    }

    @Test
    public void testCalculateFactorizationByteSize() {
        String[] fileIds = fileRepository.getFileIds().toArray(new String[0]);;
        System.out.println(String.format("Total file count %d", fileIds.length));
        HashMap<String, StatisticsObject[]> statsBySourceId = statisticsRepository.readAll(fileIds);
        Analysator analysator = new Analysator();
        for (Map.Entry<String, StatisticsObject[]> entry : statsBySourceId.entrySet()) {
            String fileId = entry.getKey();
            for (StatisticsObject obj : entry.getValue()) {
                String algorithmKey = getAlgorithmKey(obj.runningParameters);
                System.out.println(String.format("fileId %s, algorithmKey '%s', statisticsId %s", fileId, algorithmKey, obj.getId()));

                if (obj.statistics.containsKey(StatisticKeys.FactorizationByteSize)) {
                    System.out.println("Skipped");
                    continue;
                }
                if (algorithmKey.equals(Algorithms.LZ77InMemory.key)) {
                    List<FactorDef> factors = lz77FactorsRepository.readAll(obj.getId());
                    long byteSize = analysator.countByteSize(factors);
                    obj.statistics.put(StatisticKeys.FactorizationByteSize, String.valueOf(byteSize));
                    statisticsRepository.write(fileId, obj);
                } else if (algorithmKey.equals(Algorithms.LZInfInMemory.key)) {
                    List<FactorDef> factors = factorsRepository.readAll(obj.getId());
                    long byteSize = analysator.countByteSize(factors);
                    obj.statistics.put(StatisticKeys.FactorizationByteSize, String.valueOf(byteSize));
                    statisticsRepository.write(fileId, obj);
                } else
                    System.out.println("Skipped");
            }
        }
    }


    @Test
    public void test() {

        String[] fileIds = fileRepository.getFileIds().toArray(new String[0]);;
        System.out.println(String.format("Total file count %d", fileIds.length));
        HashMap<String, StatisticsObject[]> statsBySourceId = statisticsRepository.readAll(fileIds);
        for (String factorizationId : factorsRepository.getDoneStatisticIds()) {
            statsBySourceId.put(factorizationId, statisticsRepository.readAll(factorizationId));
        }
        HashMap<String, FileType> fileTypeByFileId = getFileTypes(fileIds);
        HashMap<String, FileType> sourceFileTypeByStatsId = findSourceFileTypes(fileTypeByFileId, statsBySourceId);
        HashMap<String, ArrayList<StatisticsObject>> statsByAlgorithmKey = new HashMap<>();
        for (Map.Entry<String, StatisticsObject[]> entry : statsBySourceId.entrySet()) {
            for (StatisticsObject obj : entry.getValue()) {
                String algorithmType = getAlgorithmKey(obj.runningParameters);
                if (!statsByAlgorithmKey.containsKey(algorithmType))
                    statsByAlgorithmKey.put(algorithmType, new ArrayList<StatisticsObject>());
                statsByAlgorithmKey.get(algorithmType).add(obj);
            }
        }
        for (Map.Entry<String, ArrayList<StatisticsObject>> entry : statsByAlgorithmKey.entrySet()) {
            System.out.println(String.format("AlgorithmType '%s', count %d", entry.getKey(), entry.getValue().size()));
        }

        for (FileType fileType : new FileType[]{FileType.Dna, FileType.Random, FileType.Bad}) {
            Chart buildingSlpTimeChart = Chart.build(Algorithms.BuildingSlpAlgorithms, fileType, statsByAlgorithmKey, sourceFileTypeByStatsId, Extractors.BuildingSlpTime);
            Chart buildingSlpAvlRotationsChart = Chart.build(Algorithms.BuildingSlpAvlAlgorithms, fileType, statsByAlgorithmKey, sourceFileTypeByStatsId, Extractors.BuildingSlpAvlRotations);
            Chart compressionItemSizeChart = Chart.build(Algorithms.CompressionAlgorithms, fileType, statsByAlgorithmKey, sourceFileTypeByStatsId, Extractors.CompressionItemSize);
            Chart compressionByteSizeChart = Chart.build(Algorithms.CompressionAlgorithmsWithByteSize, fileType, statsByAlgorithmKey, sourceFileTypeByStatsId, Extractors.CompressionByteSize);
            Chart slpHeightChart = Chart.build(Algorithms.BuildingSlpAlgorithms, fileType, statsByAlgorithmKey, sourceFileTypeByStatsId, Extractors.SlpHeightExtractor);
            Chart concurrentIterationCountChart = Chart.build(new Algorithms.Algorithm[]{Algorithms.AvlConcurrentFourInMemory}, fileType, statsByAlgorithmKey, sourceFileTypeByStatsId, Extractors.ConcurrentIterationCountExtractor);

            final int chartWidth = 11;
            final int chartHeight = 17;
            buildingSlpTimeChart.printInFile(String.format("buildingSlpTimeFor%s.sty", fileType), String.format("buildingSlpTimesInMemoryFor%s", fileType), chartWidth, chartHeight, AxeMarkers.textSizeAxeMarker, AxeMarkers.buildingSlpTimeAxeMarker);
            buildingSlpAvlRotationsChart.printInFile(String.format("buildingSlpAvlRotationsFor%s.sty", fileType), String.format("buildingSlpAvlRotationsFor%s", fileType), chartWidth, chartHeight, AxeMarkers.textSizeAxeMarker, AxeMarkers.buildingSlpAvlRotationsAxeMarker);
            compressionItemSizeChart.printInFile(String.format("compressionItemSizeFor%s.sty", fileType), String.format("compressionItemSizeFor%s", fileType), chartWidth, chartHeight, AxeMarkers.textSizeAxeMarker, AxeMarkers.compressingItemSizeAxeMarker);
            compressionByteSizeChart.printInFile(String.format("compressionByteSizeFor%s.sty", fileType), String.format("compressionByteSizeFor%s", fileType), chartWidth, chartHeight, AxeMarkers.textSizeAxeMarker, AxeMarkers.compressingByteSizeAxeMarker);
            slpHeightChart.printInFile(String.format("slpHeightFor%s.sty", fileType), String.format("slpHeightFor%s", fileType), chartWidth, chartHeight, AxeMarkers.textSizeAxeMarker, AxeMarkers.slpHeightAxeMarker);
            concurrentIterationCountChart.printInFile(String.format("concurrentIterationCountFor%s.sty", fileType), String.format("concurrentIterationCountFor%s", fileType), chartWidth, chartHeight, AxeMarkers.textSizeAxeMarker, AxeMarkers.concurrentIterationCountAxeMarker);
        }
    }

    private void deleteObsoleteFilesAndStatistics(HashMap<String, FileType> fileTypeByFileId,
                                                  HashMap<String, FileType> sourceFileTypeByStatsId,
                                                  HashMap<String, StatisticsObject[]> statsBySourceId) {
        HashMap<String, StatisticsObject> statsById = new HashMap<>();
        for (StatisticsObject[] array : statsBySourceId.values()) {
            for (StatisticsObject obj : array) {
                statsById.put(obj.getId(), obj);
            }
        }
        System.out.println(String.format("Statistics total count: %d", statsById.size()));
        for (Map.Entry<String, FileType> entry : sourceFileTypeByStatsId.entrySet()) {
            String statisticsId = entry.getKey();
            StatisticsObject statisticsObject = statsById.get(statisticsId);
            String algorithmType = statisticsObject.runningParameters.get(RunParamKeys.AlgorithmType);
            if (algorithmType.contains("lz")) continue;

            long textSize = (long) Extractors.BuildingSlpTime.getX(statisticsObject);
            if (//entry.getValue() == FileType.Obsolete ||
                    (getAlgorithmKey(statisticsObject.runningParameters).equals(Algorithms.AvlConcurrentFourInMemory.key)
                            && (textSize >= 4 * 1024 * 1024)
                            && sourceFileTypeByStatsId.get(statisticsId) == FileType.Bad
                    )) {
                System.out.println(String.format("Deleting slp-statistics %s...", statisticsId));
                String sourceId = statisticsObject.runningParameters.get(RunParamKeys.SourceId);
                slpProductsRepository.remove(statisticsId);
                statisticsRepository.remove(sourceId, statisticsId);
            }
        }
        for (Map.Entry<String, FileType> entry : sourceFileTypeByStatsId.entrySet()) {
            String statisticsId = entry.getKey();
            StatisticsObject statisticsObject = statsById.get(statisticsId);
            String algorithmType = statisticsObject.runningParameters.get(RunParamKeys.AlgorithmType);
            if (!algorithmType.contains("lz")) continue;

            if (//entry.getValue() == FileType.Obsolete
                    false) {
                System.out.println(String.format("Deleting lz-statistics %s...", statisticsId));
                String sourceId = statisticsObject.runningParameters.get(RunParamKeys.SourceId);
                lz77FactorsRepository.remove(statisticsId);
                factorsRepository.remove(statisticsId);
                statisticsRepository.remove(sourceId, statisticsId);
            }
        }
        for (Map.Entry<String, FileType> entry : fileTypeByFileId.entrySet()) {
            String fileId = entry.getKey();
            if (//entry.getValue() == FileType.Obsolete
                    false) {
                System.out.println(String.format("Deleting file %s...", fileId));
                fileRepository.remove(fileId);
            }
        }
    }

    private HashMap<String, FileType> getFileTypes(String[] fileIds) {
        HashMap<String, FileType> fileTypeById = new HashMap<>();
        for (String fileId : fileIds) {
            FileMetadata metadata = fileRepository.getMeta(fileId);
            FileType fileType;
            if (metadata.getName().contains("DNA"))
                fileType = FileType.Dna;
            else if (metadata.getName().contains("Random"))
                fileType = FileType.Random;
//            else if (metadata.getFileName().contains("Bad_cut"))
//                fileType = FileType.Bad;
            else if (metadata.getName().contains("Bad"))
                fileType = FileType.Bad;
            else
                throw new RuntimeException(String.format("Can not recognize fileType [id %s, name %s]", fileId, metadata.getName()));
            fileTypeById.put(fileId, fileType);
        }
        return fileTypeById;
    }

    private HashMap<String, FileType> findSourceFileTypes(HashMap<String, FileType> fileTypeByFileId, HashMap<String, StatisticsObject[]> statsBySourceId) {
        HashMap<String, StatisticsObject> statsById = new HashMap<>();
        for (StatisticsObject[] array : statsBySourceId.values()) {
            for (StatisticsObject obj : array) {
                statsById.put(obj.getId(), obj);
            }
        }

        HashMap<String, FileType> fileTypeByStatsId = new HashMap<>();
        for (Map.Entry<String, StatisticsObject[]> entry : statsBySourceId.entrySet()) {
            String sourceId = entry.getKey();
            FileType fileType;
            String textId = sourceId;
            while (!fileTypeByFileId.containsKey(textId))
                textId = statsById.get(textId).runningParameters.get(RunParamKeys.SourceId);
            fileType = fileTypeByFileId.get(textId);
            for (StatisticsObject obj : entry.getValue()) {
                fileTypeByStatsId.put(obj.getId(), fileType);
            }
        }
        return fileTypeByStatsId;
    }

    private static String getAlgorithmKey(Map<RunParamKeys, String> runningParameters) {
        String algorithmType = runningParameters.get(RunParamKeys.AlgorithmType);
        if (algorithmType.equals(AlgorithmType.avlSlp.toString())) {
            String mergePattern = runningParameters.get(RunParamKeys.AvlMergePattern);
            String splitPattern = runningParameters.get(RunParamKeys.AvlSplitPattern);
            String dataFactory = runningParameters.get(RunParamKeys.DataFactoryType);
            return String.format("%s, mergePattern %s, splitPattern %s, dataFactory %s", algorithmType, mergePattern, splitPattern, dataFactory);
        } else {
            String dataFactory = runningParameters.get(RunParamKeys.DataFactoryType);
            return String.format("%s, dataFactory %s", algorithmType, dataFactory);
        }
    }

    private static class Algorithms {
        public static final Algorithm RytterInMemory = new Algorithm(getRytterParams(AvlMergePattern.sequential, AvlSplitPattern.fromMerged), "Алгоритм Риттера", "\\drawRytter{%x}{%y};");
        public static final Algorithm Modern1RytterInMemory = new Algorithm(getRytterParams(AvlMergePattern.block, AvlSplitPattern.fromFirst), "Алгоритм Риттера с эвристикой 1", "\\drawModernFirstRytter{%x}{%y};");
        public static final Algorithm Modern2RytterInMemory = new Algorithm(getRytterParams(AvlMergePattern.block2, AvlSplitPattern.fromFirst), "Алгоритм Риттера с эвристикой 2", "\\drawModernSecondRytter{%x}{%y};");
        public static final Algorithm CartesianInMemory = new Algorithm(getCartesianParams(), "Алгоритм с рандомизированными деревьями", "\\drawCartesian{%x}{%y};");
        public static final Algorithm LcaOnlineInMemory = new Algorithm(getLcaOnlineParams(), "Алгоритм LCA-online", "\\drawLcaOnline{%x}{%y};");
        public static final Algorithm AvlConcurrentFourInMemory = new Algorithm(getAvlConcurrentFourParams(), "Многопоточный алгоритм", "\\drawAvlConcurrentFour{%x}{%y};");

        public static final Algorithm LZ77InMemory = new Algorithm(getLzFactorizationParams(AlgorithmType.lz77), "LZ77", "\\drawLz{%x}{%y};");
        public static final Algorithm LZWInMemory = new Algorithm(getLzFactorizationParams(AlgorithmType.lzw), "LZW", "\\drawLzw{%x}{%y};");
        public static final Algorithm LZInfInMemory = new Algorithm(getLzFactorizationParams(AlgorithmType.lzInf), "LZInf", "\\drawLzInf{%x}{%y};");

        public static final Algorithms.Algorithm[] CompressionAlgorithms = new Algorithm[]{
                Algorithms.RytterInMemory,
                Algorithms.Modern1RytterInMemory,
                Algorithms.Modern2RytterInMemory,
                Algorithms.CartesianInMemory,
                Algorithms.LcaOnlineInMemory,
                Algorithms.LZ77InMemory,
                Algorithms.LZWInMemory,
                Algorithms.LZInfInMemory,
                Algorithms.AvlConcurrentFourInMemory
        };

        public static final Algorithms.Algorithm[] CompressionAlgorithmsWithByteSize = new Algorithm[]{
                Algorithms.RytterInMemory,
                Algorithms.Modern1RytterInMemory,
                Algorithms.Modern2RytterInMemory,
                Algorithms.CartesianInMemory,
                Algorithms.LcaOnlineInMemory,
                Algorithms.LZ77InMemory,
                Algorithms.LZInfInMemory,
                Algorithms.AvlConcurrentFourInMemory
        };

        public static final Algorithms.Algorithm[] BuildingSlpAlgorithms = new Algorithm[]{
                Algorithms.RytterInMemory,
                Algorithms.Modern1RytterInMemory,
                Algorithms.Modern2RytterInMemory,
                Algorithms.CartesianInMemory,
                Algorithms.LcaOnlineInMemory,
                Algorithms.AvlConcurrentFourInMemory
        };

        public static final Algorithms.Algorithm[] BuildingSlpAvlAlgorithms = new Algorithm[]{
                Algorithms.RytterInMemory,
                Algorithms.Modern1RytterInMemory,
                Algorithms.Modern2RytterInMemory};


        private static Map<RunParamKeys, String> getRytterParams(AvlMergePattern avlMergePattern, AvlSplitPattern avlSplitPattern) {
            HashMap<RunParamKeys, String> params = new HashMap<>();
            params.put(RunParamKeys.AlgorithmType, AlgorithmType.avlSlp.toString());
            params.put(RunParamKeys.AvlMergePattern, avlMergePattern.toString());
            params.put(RunParamKeys.AvlSplitPattern, avlSplitPattern.toString());
            params.put(RunParamKeys.DataFactoryType, DataFactoryType.memory.toString());
            return params;
        }

        private static Map<RunParamKeys, String> getLcaOnlineParams() {
            HashMap<RunParamKeys, String> params = new HashMap<>();
            params.put(RunParamKeys.AlgorithmType, AlgorithmType.lcaOnlineSlp.toString());
            params.put(RunParamKeys.DataFactoryType, DataFactoryType.memory.toString());
            return params;
        }

        private static Map<RunParamKeys, String> getAvlConcurrentFourParams() {
            HashMap<RunParamKeys, String> params = new HashMap<>();
            params.put(RunParamKeys.AlgorithmType, AlgorithmType.avlSlpConcurrent.toString());
            params.put(RunParamKeys.DataFactoryType, DataFactoryType.memory.toString());
            params.put(RunParamKeys.ThreadCount, "4");
            params.put(RunParamKeys.AvlMergePattern, AvlMergePattern.sequential.toString());
            return params;
        }

        private static Map<RunParamKeys, String> getLzFactorizationParams(AlgorithmType algorithmType) {
            HashMap<RunParamKeys, String> params = new HashMap<>();
            params.put(RunParamKeys.AlgorithmType, algorithmType.toString());
            params.put(RunParamKeys.DataFactoryType, DataFactoryType.memory.toString());
            return params;
        }

        private static Map<RunParamKeys, String> getCartesianParams() {
            HashMap<RunParamKeys, String> params = new HashMap<>();
            params.put(RunParamKeys.AlgorithmType, AlgorithmType.cartesianSlp.toString());
            params.put(RunParamKeys.DataFactoryType, DataFactoryType.memory.toString());
            return params;
        }

        public static class Algorithm {
            public final String key;
            public final String value;
            private final Map<RunParamKeys, String> params;
            private final String texPointTemplate;

            public Algorithm(Map<RunParamKeys, String> params, String value, String texPointTemplate) {
                this.params = params;
                this.texPointTemplate = texPointTemplate;
                this.key = getAlgorithmKey(params);
                this.value = value;
            }

            public String drawPoint(double x, double y) {
                return texPointTemplate
                        .replaceAll("%x", String.format(Locale.US, "%.3f", x))
                        .replaceAll("%y", String.format(Locale.US, "%.3f", y));
            }

            public int compareTo(Algorithm algorithm) {
                return key.compareTo(algorithm.key);
            }
        }
    }

    private static class AxeMarkers {
        public static final IAxeMarker textSizeAxeMarker = new TextSizeAxeMarker();
        public static final IAxeMarker buildingSlpTimeAxeMarker = new BuildingSlpTimeAxeMarker();
        public static final IAxeMarker buildingSlpAvlRotationsAxeMarker = new BuildingSlpAvlRotationsAxeMarker();
        public static final IAxeMarker compressingItemSizeAxeMarker = new CompressingItemSizeAxeMarker();
        public static final IAxeMarker compressingByteSizeAxeMarker = new CompressingByteSizeAxeMarker();
        public static final IAxeMarker slpHeightAxeMarker = new SlpHeightAxeMarker();
        public static final IAxeMarker concurrentIterationCountAxeMarker = new ConcurrentIterationCountAxeMarker();

        private static class ConcurrentIterationCountAxeMarker implements IAxeMarker {

            @Override
            public double getStep() {
                return 3;
            }

            @Override
            public String getMarkText(int index) {
                return String.format("%d", 3 * index);
            }
        }

        private static class SlpHeightAxeMarker implements IAxeMarker {

            @Override
            public double getStep() {
                return 5;
            }

            @Override
            public String getMarkText(int index) {
                return String.format("%d", 5 * index);
            }
        }

        private static class CompressingItemSizeAxeMarker implements IAxeMarker {

            @Override
            public double getStep() {
                return 0.05;
            }

            @Override
            public String getMarkText(int index) {
                return String.format("%d%%", index * 5);
            }
        }

        private static class CompressingByteSizeAxeMarker implements IAxeMarker {

            @Override
            public double getStep() {
                return 0.05;
            }

            @Override
            public String getMarkText(int index) {
                return String.format("%d%%", index * 5);
            }
        }

        private static class BuildingSlpAvlRotationsAxeMarker implements IAxeMarker {

            @Override
            public double getStep() {
                return 4e5;
            }

            @Override
            public String getMarkText(int index) {
                return String.format("%.1fМ", index * 0.4);
            }
        }

        private static class BuildingSlpTimeAxeMarker implements IAxeMarker {

            @Override
            public double getStep() {
                return 10 * 1000;
            }

            @Override
            public String getMarkText(int index) {
                return String.format("%dс", index * 10);
            }
        }

        private static class TextSizeAxeMarker implements IAxeMarker {

            @Override
            public double getStep() {
                return 4 * 1024 * 1024;
            }

            @Override
            public String getMarkText(int index) {
                return String.format("%dМБ", index * 4);
            }
        }

        public interface IAxeMarker {
            public double getStep();

            public String getMarkText(int index);
        }
    }

    private static class Extractors {
        public static final IFeatureExtractor BuildingSlpTime = new BuildSlpTimeExtractor();
        public static final IFeatureExtractor BuildingSlpAvlRotations = new BuildSlpAvlRotationsExtractor();
        public static final IFeatureExtractor CompressionItemSize = new CompressionItemSizeExtractor();
        public static final IFeatureExtractor CompressionByteSize = new CompressionByteSizeExtractor();
        public static final IFeatureExtractor SlpHeightExtractor = new SlpHeightExtractor();
        public static final IFeatureExtractor ConcurrentIterationCountExtractor = new ConcurrentIterationCountExtractor();

        private static class ConcurrentIterationCountExtractor extends ExtractorBase {
            @Override
            public double getY(StatisticsObject statisticsObject) {
                return Long.parseLong(statisticsObject.statistics.get(StatisticKeys.CountOfLayers));
            }
        }

        private static class SlpHeightExtractor extends ExtractorBase {
            @Override
            public double getY(StatisticsObject statisticsObject) {
                return Long.parseLong(statisticsObject.statistics.get(StatisticKeys.SlpHeight));
            }
        }

        private static class CompressionByteSizeExtractor extends ExtractorBase {
            @Override
            public double getY(StatisticsObject statisticsObject) {
                long count;
                if (statisticsObject.statistics.containsKey(StatisticKeys.SlpByteSize))
                    count = Long.parseLong(statisticsObject.statistics.get(StatisticKeys.SlpByteSize));
                else if (statisticsObject.statistics.containsKey(StatisticKeys.FactorizationByteSize))
                    count = Long.parseLong(statisticsObject.statistics.get(StatisticKeys.FactorizationByteSize));
                else {
                    count = 0;
                    //throw new RuntimeException();
                }
                return count / getX(statisticsObject);
            }
        }

        private static class CompressionItemSizeExtractor extends ExtractorBase {
            @Override
            public double getY(StatisticsObject statisticsObject) {
                long count;
                if (statisticsObject.statistics.containsKey(StatisticKeys.SlpCountRules))
                    count = Long.parseLong(statisticsObject.statistics.get(StatisticKeys.SlpCountRules));
                else if (statisticsObject.statistics.containsKey(StatisticKeys.FactorizationLength))
                    count = Long.parseLong(statisticsObject.statistics.get(StatisticKeys.FactorizationLength));
                else
                    throw new RuntimeException();
                return count / getX(statisticsObject);
            }
        }

        private static class BuildSlpTimeExtractor extends ExtractorBase {
            @Override
            public double getY(StatisticsObject statisticsObject) {
                return Long.parseLong(statisticsObject.statistics.get(StatisticKeys.RunningTime));
            }
        }

        private static class BuildSlpAvlRotationsExtractor extends ExtractorBase {
            @Override
            public double getY(StatisticsObject statisticsObject) {
                return Long.parseLong(statisticsObject.statistics.get(StatisticKeys.RebalanceCount));
            }
        }

        private static abstract class ExtractorBase implements IFeatureExtractor {
            @Override
            public double getX(StatisticsObject statisticsObject) {
                if (statisticsObject.statistics.containsKey(StatisticKeys.SourceLength))
                    return Long.parseLong(statisticsObject.statistics.get(StatisticKeys.SourceLength));
                return Long.parseLong(statisticsObject.statistics.get(StatisticKeys.SlpWidth));
            }

            public abstract double getY(StatisticsObject statisticsObject);
        }

        public interface IFeatureExtractor {
            double getX(StatisticsObject statisticsObject);

            double getY(StatisticsObject statisticsObject);
        }
    }

    private static class Chart {
        private final ArrayList<Point> points = new ArrayList<>();

        private Chart() {
        }

        private void addPoint(Algorithms.Algorithm algorithm, double x, double y) {
            points.add(new Point(algorithm, x, y));
        }

        public static Chart build(Algorithms.Algorithm[] algorithms,
                                  FileType targetFileType,
                                  HashMap<String, ArrayList<StatisticsObject>> statsByAlgorithmKey,
                                  HashMap<String, FileType> fileTypeByStatsId,
                                  Extractors.IFeatureExtractor featureExtractor) {
            Chart result = new Chart();
            for (Algorithms.Algorithm algorithm : algorithms) {
                ArrayList<StatisticsObject> stats = statsByAlgorithmKey.get(algorithm.key);
                if (stats == null) continue;
                for (StatisticsObject obj : stats) {
                    FileType currentFileType = fileTypeByStatsId.get(obj.getId());
                    if (currentFileType != targetFileType) continue;
                    result.addPoint(algorithm, featureExtractor.getX(obj), featureExtractor.getY(obj));
                }
            }
            return result;
        }

        public void printInFile(String filePath, String methodName, final double sizeX, final double sizeY, AxeMarkers.IAxeMarker xMarker, AxeMarkers.IAxeMarker yMarker) {
            try (Writer writer = Files.newBufferedWriter(Paths.get("texFiles", filePath), Charset.forName("UTF-8"))) {
                print(writer, methodName, sizeX, sizeY, xMarker, yMarker);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void print(Writer writer, String methodName, final double sizeX, final double sizeY, AxeMarkers.IAxeMarker xMarker, AxeMarkers.IAxeMarker yMarker) throws IOException {
            Point[] array = points.toArray(new Point[0]);

            final double INF = 1e20;
            double minX = INF, maxX = -INF, minY = INF, maxY = -INF;

            Arrays.sort(array, new Comparator<Point>() {
                @Override
                public int compare(Point o1, Point o2) {
                    int algorithmComp = o1.algorithm.compareTo(o2.algorithm);
                    if (algorithmComp != 0)
                        return algorithmComp;
                    return new Double(o1.x).compareTo(new Double(o2.x));
                }
            });
            for (Point point : array) {
                minX = Math.min(minX, point.x);
                maxX = Math.max(maxX, point.x);
                minY = Math.min(minY, point.y);
                maxY = Math.max(maxY, point.y);
            }
            double dX = Math.max(1, maxX - minX);
            double dY = Math.max(1, maxY - minY);
            minX = Math.max(0.0, minX - 0.05 * dX);
            minY = Math.max(0.0, minY - 0.05 * dY);
            maxX += 0.05 * dX;
            maxY += 0.05 * dY;
            dX = maxX - minX;
            dY = maxY - minY;

            writer.write(String.format(Locale.US, "%% minX = %f, maxX = %f\n", minX, maxX));
            writer.write(String.format(Locale.US, "%% minY = %f, maxY = %f\n", minY, maxY));
            writer.write(String.format(Locale.US, "%% sizeX = %f, sizeY = %f\n", sizeX, sizeY));
            writer.write(String.format("\\newcommand{\\%s}{\\begin{tikzpicture}\n", methodName));
            writer.write(String.format(Locale.US, "\\draw [<->] (0,%f) -- (0,0) -- (%f,0);\n", sizeY, sizeX));
            if (array.length != 0) {
                for (int index = 1; ; ++index) {
                    double x = index * xMarker.getStep();
                    if (x < minX + 0.05 * dX) continue;
                    if (x > maxX - 0.01 * dX) break;
                    double normX = (x - minX) / dX * sizeX;
                    writer.write(String.format(Locale.US, "\\draw [help lines] (%f,-0.05) -- (%f,%f);\n", normX, normX, sizeY));
                    writer.write(String.format(Locale.US, "\\node [below] at (%f,0) {%s};\n", normX, formatText(xMarker.getMarkText(index))));
                }
                for (int index = 1; ; ++index) {
                    double y = index * yMarker.getStep();
                    if (y < minY + 0.05 * dY) continue;
                    if (y > maxY - 0.01 * dY) break;
                    double normY = (y - minY) / dY * sizeY;
                    writer.write(String.format(Locale.US, "\\draw [help lines] (-0.05,%f) -- (%f,%f);\n", normY, sizeX, normY));
                    writer.write(String.format(Locale.US, "\\node [left] at (0,%f) {%s};\n", normY, formatText(yMarker.getMarkText(index))));
                }
            }

            writer.write("\n");
            for (Point point : array) {
                double normX = (point.x - minX) / dX * sizeX;
                double normY = (point.y - minY) / dY * sizeY;
                writer.write('\t');
                writer.write(point.algorithm.drawPoint(normX, normY));
                writer.write('\n');
            }
            writer.write("\\end{tikzpicture}}");
        }

        private static String formatText(String text) {
            return String.format("\\scriptsize {\\sf %s}", escapeText(text));
        }

        private static String escapeText(String text) {
            return text
                    .replace("\\", "\\\\")
                    .replace("{", "\\{")
                    .replace("}", "\\}")
                    .replace("%", "\\%");
        }

        private class Point {
            private final Algorithms.Algorithm algorithm;
            private final double x;
            private final double y;

            private Point(Algorithms.Algorithm algorithm, double x, double y) {
                this.algorithm = algorithm;
                this.x = x;
                this.y = y;
            }
        }
    }
}
