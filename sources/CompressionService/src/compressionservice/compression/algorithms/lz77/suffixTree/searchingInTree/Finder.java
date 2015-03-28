package compressionservice.compression.algorithms.lz77.suffixTree.searchingInTree;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IEdge;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.INode;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IPlaceFactory;

public class Finder implements IFinder
{
    private String text;

    private IReadableCharArray string;
    private final IFindingSearcher findingSearcher;
    private IPlaceFactory placeFactory;

    public Finder(String text, IReadableCharArray string, IFindingSearcher findingSearcher, IPlaceFactory placeFactory)
    {
        this.text = text;
        this.string = string;
        this.findingSearcher = findingSearcher;
        this.placeFactory = placeFactory;
    }


    @Override
    public IPlace search(INode root)
    {
        if (this.text.length() == 0 || this.string.length() == 0)
            return this.placeFactory.create(0, 0);

        IEdge edge = this.findingSearcher.search(this.text, this.string, root, 0);
        if (edge == null)
            return this.placeFactory.create(0, 0);

        int stringIndex = 0;
        int textIndex = 0;
        int position = 0;

        while (stringIndex < this.string.length() && textIndex < this.text.length())
        {
            if (textIndex + edge.beginPosition() > edge.endPosition())
            {
                position = edge.getNumber();
                edge = this.findingSearcher.search(this.text, this.string, edge.endNode(), stringIndex);
                textIndex = 0;
            }
            if (edge == null)
                return this.placeFactory.create(position, stringIndex);

            position = edge.getNumber();
            while (
                    textIndex + edge.beginPosition() < this.text.length() &&
                            textIndex + edge.beginPosition() <= edge.endPosition() &&
                            stringIndex < this.string.length() &&
                            this.text.charAt(textIndex + edge.beginPosition()) == this.string.get(stringIndex))
            {
                ++textIndex;
                ++stringIndex;
            }

            if (textIndex + edge.beginPosition() == edge.endPosition() + 1 && edge.endNode() == null)
                return this.placeFactory.create(position, stringIndex);

            if (
                    textIndex + edge.beginPosition() < this.text.length() &&
                            textIndex + edge.beginPosition() < edge.endPosition() + 1 &&
                            stringIndex < this.string.length() &&
                            this.text.charAt(textIndex + edge.beginPosition()) != this.string.get(stringIndex))
                return this.placeFactory.create(position, stringIndex);
        }
        return this.placeFactory.create(position, stringIndex);
    }
}
