package compressionservice.compression.algorithms.lz77.windows;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.ITree;
import compressionservice.compression.algorithms.lz77.suffixTree.Tree;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.FactoriesImpl;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IFactories;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Location;

public class StringWindow implements IStringWindow
{
    private int limitLength;
    private String content;
    private ITree tree;
    private IFactories factories;
    private long globalContentPosition;

    public StringWindow(int limitLength)
    {
        this.limitLength = limitLength;
        this.content = "";
        this.globalContentPosition = 0;

        this.factories = new FactoriesImpl();
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
    public Location search(IReadableCharArray string)
    {
        if (this.content == null || this.content.length() == 0)
            return Location.create(0, 0);
        Location localLocation = tree.stringInformation(string);
        return Location.create(localLocation.beginPosition + globalContentPosition, localLocation.length);
    }
}
