package compressionservice.algorithms.lz77.suffixTree.searchingInTree;

import compressionservice.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import data.charArray.IReadableCharArray;

public class Finder implements IFinder
{
    private String text;

    private IReadableCharArray string;
    private final IFindingSearcher findingSearcher;

    public Finder(String text, IReadableCharArray string, IFindingSearcher findingSearcher)
    {
        this.text = text;
        this.string = string;
        this.findingSearcher = findingSearcher;
    }

    @Override
    public Location search(INode root)
    {
        if (this.text.length() == 0 || this.string.length() == 0)
            return Location.create(0, 0);

        IEdge edge = this.findingSearcher.search(this.text, this.string, root, 0);
        if (edge == null)
            return Location.create(0, 0);

        int stringIndex = 0;
        int textIndex = 0;
        int position = 0;

        while (stringIndex < this.string.length() && textIndex < this.text.length())
        {
            if (textIndex + edge.fromPosition() > edge.toPosition())
            {
                position = edge.getNumber();
                edge = this.findingSearcher.search(this.text, this.string, edge.toNode(), stringIndex);
                textIndex = 0;
            }
            if (edge == null)
                return Location.create(position, stringIndex);

            position = edge.getNumber();
            while (
                    textIndex + edge.fromPosition() < this.text.length() &&
                            textIndex + edge.fromPosition() <= edge.toPosition() &&
                            stringIndex < this.string.length() &&
                            this.text.charAt(textIndex + edge.fromPosition()) == this.string.get(stringIndex))
            {
                ++textIndex;
                ++stringIndex;
            }

            if (textIndex + edge.fromPosition() == edge.toPosition() + 1 && edge.toNode() == null)
                return Location.create(position, stringIndex);

            if (
                    textIndex + edge.fromPosition() < this.text.length() &&
                            textIndex + edge.fromPosition() < edge.toPosition() + 1 &&
                            stringIndex < this.string.length() &&
                            this.text.charAt(textIndex + edge.fromPosition()) != this.string.get(stringIndex))
                return Location.create(position, stringIndex);
        }
        return Location.create(position, stringIndex);
    }
}
