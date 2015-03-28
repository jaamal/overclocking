package compressionservice.compression.algorithms.lz77.windows;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.ITree;
import compressionservice.compression.algorithms.lz77.suffixTree.Tree;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.FactoriesImpl;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IFactories;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IPlace;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.IPlaceFactory;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.factories.PlaceFactory;

public class StringWindow implements IStringWindow
{
    private int limitLength;
    private String content;
    private ITree tree;
    private IFactories factories;
    private IPlaceFactory placeFactory;
    private long globalContentPosition;

    public StringWindow(int limitLength)
    {
        this.limitLength = limitLength;
        this.content = "";
        this.globalContentPosition = 0;

        this.factories = new FactoriesImpl();
        this.placeFactory = new PlaceFactory();
    }

    @Override
    public void append(String string)
    {
        if (this.limitLength < this.content.length() + string.length())
        {
            int length = (this.content.length() + 1) / 2;
            if (string.length() > length + 1)
            {
                this.globalContentPosition += this.content.length();
                if (string.length() > this.limitLength)
                    this.content = string.substring(0, this.limitLength);
                else
                    this.content = string;
            }
            else
            {
                this.globalContentPosition += length;
                this.content = this.content.substring(length) + string;
            }
            this.tree = new Tree(this.content, this.factories);
        }
        else if (string != null && string.length() > 0)
        {
            this.content += string;
            if (this.tree != null)
                this.tree.append(string);
            else
                this.tree = new Tree(this.content, this.factories);
        }
    }

    @Override
    public IPlace search(IReadableCharArray string)
    {
        if (this.content == null || this.content.length() == 0)
            return this.placeFactory.create(0, 0);
        IPlace localPlace = tree.stringInformation(string);
        return placeFactory.create(localPlace.getPosition() + globalContentPosition, localPlace.getLength());
    }
}
