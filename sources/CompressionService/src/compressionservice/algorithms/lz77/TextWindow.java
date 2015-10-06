package compressionservice.algorithms.lz77;

import compressionservice.algorithms.lz77.suffixTree.ISuffixTree;
import compressionservice.algorithms.lz77.suffixTree.SuffixTree;
import compressionservice.algorithms.lz77.suffixTree.structures.FactoriesImpl;
import compressionservice.algorithms.lz77.suffixTree.structures.IFactories;
import compressionservice.algorithms.lz77.suffixTree.structures.Location;
import data.charArray.IReadableCharArray;

public class TextWindow implements ITextWindow
{
    private final int size;
    private String text;
    private long globalTextPosition;
    private ISuffixTree tree;
    private IFactories factories;

    private TextWindow(int size)
    {
        this.size = size;
        text = "";
        globalTextPosition = 0;

        factories = new FactoriesImpl();
    }

    @Override
    public void append(String text)
    {
        if (text == null || text.length() == 0)
            return;
        
        if (size < this.text.length() + text.length())
        {
            int length = (this.text.length() + 1) / 2;
            if (text.length() > length + 1)
            {
                globalTextPosition += this.text.length();
                if (text.length() > size)
                    this.text = text.substring(0, size);
                else
                    this.text = text;
            }
            else
            {
                globalTextPosition += length;
                this.text = this.text.substring(length) + text;
            }
            tree = new SuffixTree(this.text, factories);
        }
        else
        {
            this.text += text;
            if (tree != null)
                tree.append(text);
            else
                tree = new SuffixTree(this.text, factories);
        }
    }

    @Override
    public Location search(IReadableCharArray charArray)
    {
        if (text == null || text.length() == 0)
            return Location.EMPTY;
        
        return tree.search(charArray).shiftRight(globalTextPosition);
    }
    
    @Override
    public int size() {
        return size;
    }
    
    public static TextWindow create(int size){
        return new TextWindow(size);
    }
}
