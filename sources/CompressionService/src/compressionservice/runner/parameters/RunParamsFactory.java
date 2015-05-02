package compressionservice.runner.parameters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import dataContracts.statistics.RunParamKeys;

public class RunParamsFactory implements IRunParamsFactory {

    @Override
    public IRunParams create(Map<String, String> parameters) {
        Map<RunParamKeys, String> typedMap = new HashMap<RunParamKeys, String>();
        for (Iterator<Entry<String, String>> iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
            Entry<String, String> entry = (Entry<String, String>) iterator.next();
            RunParamKeys key = RunParamKeys.valueOf(entry.getKey());
            typedMap.put(key, entry.getValue());
        }
        return new RunParams(typedMap);
    }

}
