package compressionservice.algorithms.lz77.suffixTree.searchingInTree;

import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import data.charArray.IReadableCharArray;

public class Finder implements IFinder
{
    @Override
    public Location search(INode root, String text, IReadableCharArray pattern)
    {
        if (text.length() == 0 || pattern.length() == 0)
            return Location.create(0, 0);

        if (root == null)
            return null;
        
        IEdge edge = root.findEdge(pattern.get(0));
        if (edge == null)
            return Location.create(0, 0);

        int stringIndex = 0;
        int textIndex = 0;
        int position = 0;

        while (stringIndex < pattern.length() && textIndex < text.length())
        {
            if (textIndex + edge.fromPosition() > edge.toPosition())
            {
                position = edge.getNumber();
                edge = edge.toNode() == null 
                        ? null 
                        : edge.toNode().findEdge(pattern.get(stringIndex));
                textIndex = 0;
            }
            if (edge == null)
                return Location.create(position, stringIndex);

            position = edge.getNumber();
            while (
                    textIndex + edge.fromPosition() < text.length() &&
                            textIndex + edge.fromPosition() <= edge.toPosition() &&
                            stringIndex < pattern.length() &&
                            text.charAt(textIndex + edge.fromPosition()) == pattern.get(stringIndex))
            {
                ++textIndex;
                ++stringIndex;
            }

            if (textIndex + edge.fromPosition() == edge.toPosition() + 1 && edge.toNode() == null)
                return Location.create(position, stringIndex);

            if (
                    textIndex + edge.fromPosition() < text.length() &&
                            textIndex + edge.fromPosition() < edge.toPosition() + 1 &&
                            stringIndex < pattern.length() &&
                            text.charAt(textIndex + edge.fromPosition()) != pattern.get(stringIndex))
                return Location.create(position, stringIndex);
        }
        return Location.create(position, stringIndex);
    }
}
