package compressionservice.compression.algorithms.lz77;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.suffixTree.ITree;
import compressionservice.compression.algorithms.lz77.suffixTree.Tree;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.FactoriesImpl;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.IFactories;
import compressionservice.compression.algorithms.lz77.suffixTree.structures.Location;

public class TextWindow implements ITextWindow
{
    private final int size;
    private String text;
    private long globalTextPosition;
    private ITree tree;
    private IFactories factories;

    private TextWindow(int size)
    {
        this.size = size;
        this.text = "";
        this.globalTextPosition = 0;

        this.factories = new FactoriesImpl();
    }

    @Override
    public void append(String text)
    {
        if (this.size < this.text.length() + text.length())
        {
            int length = (this.text.length() + 1) / 2;
            if (text.length() > length + 1)
            {
                this.globalTextPosition += this.text.length();
                if (text.length() > this.size)
                    this.text = text.substring(0, this.size);
                else
                    this.text = text;
            }
            else
            {
                this.globalTextPosition += length;
                this.text = this.text.substring(length) + text;
            }
            this.tree = new Tree(this.text, this.factories);
        }
        else if (text != null && text.length() > 0)
        {
            this.text += text;
            if (this.tree != null)
                this.tree.append(text);
            else
                this.tree = new Tree(this.text, this.factories);
        }
    }

    @Override
    public Location search(IReadableCharArray charArray)
    {
        if (this.text == null || this.text.length() == 0)
            return Location.create(0, 0);
        Location localLocation = tree.stringInformation(charArray);
        return Location.create(localLocation.beginPosition + globalTextPosition, localLocation.length);
    }
    
    @Override
    public int size() {
        return size;
    }
    
    public static TextWindow create(int size){
        return new TextWindow(size);
    }
}
