package compressionservice.compression.algorithms.analysator;

import java.io.ByteArrayOutputStream;
import java.util.List;

import serialization.lzFactorizations.ILzFactorizationSerializer;
import serialization.lzFactorizations.LzFactorizationSerializer;
import serialization.primitives.IntArraySerializer;
import dataContracts.FactorDef;

public class Analysator implements IAnalysator
{
    private final ILzFactorizationSerializer serializer;

    public Analysator()
    {
        serializer = new LzFactorizationSerializer(new IntArraySerializer());
    }
    
    @Override
    public <T extends FactorDef> long countByteSize(List<T> factors) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            FactorDef[] factorsArray = factors.toArray(new FactorDef[0]);
            serializer.serialize(stream, factorsArray);
            return stream.size();
        } catch (Exception e) {
            throw new RuntimeException("Throws exception while serializing lz-factorization", e);
        }
    }

}
