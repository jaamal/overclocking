package compressionservice.runner.parameters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import serialization.ISerializer;
import dataContracts.AlgorithmType;
import dataContracts.IIDFactory;
import dataContracts.statistics.RunParamKeys;

public class RunParamsFactory implements IRunParamsFactory {

    private IIDFactory idFactory;
    private ISerializer serializer;

    public RunParamsFactory(
            IIDFactory idFactory,
            ISerializer serializer){
        this.idFactory = idFactory;
        this.serializer = serializer;
    }
    
    @Override
    public IRunParams create(Map<String, String> parameters) {
        Map<RunParamKeys, String> typedMap = new HashMap<RunParamKeys, String>();
        for (Iterator<Entry<String, String>> iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
            Entry<String, String> entry = (Entry<String, String>) iterator.next();
            RunParamKeys key = RunParamKeys.valueOf(entry.getKey());
            typedMap.put(key, entry.getValue());
        }
        return new RunParams(idFactory, serializer, typedMap);
    }

    @Override
    public IRunParams create(String sourceId, AlgorithmType algorithmType) {
        IRunParams runParams =  new RunParams(idFactory, serializer, new HashMap<RunParamKeys, String>());
        runParams.put(RunParamKeys.SourceId, sourceId);
        runParams.put(RunParamKeys.AlgorithmType, algorithmType);
        return runParams;
    }

    @Override
    public IRunParams create(AlgorithmType algorithmType) {
        IRunParams runParams =  new RunParams(idFactory, serializer, new HashMap<RunParamKeys, String>());
        runParams.put(RunParamKeys.AlgorithmType, algorithmType);
        return runParams;
    }

}
