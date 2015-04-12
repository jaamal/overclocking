package compressionservice.compression.algorithms.lz77.suffixTree.structures;

public class Location
{
    public final long beginPosition;
    public final long length;
    
    private Location(long beginPosition, long length){
        this.beginPosition = beginPosition;
        this.length = length;
    }

    public static Location create(long beginPosition, long length) {
        if (beginPosition < 0)
            throw new RuntimeException(String.format("Fail to create location with negative begin position %s.", beginPosition));
        if (length < 0)
            throw new RuntimeException(String.format("Fail to create location with negative length %s.", length));
        return new Location(beginPosition, length);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (beginPosition ^ (beginPosition >>> 32));
        result = prime * result + (int) (length ^ (length >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        if (beginPosition != other.beginPosition)
            return false;
        if (length != other.length)
            return false;
        return true;
    }
}
