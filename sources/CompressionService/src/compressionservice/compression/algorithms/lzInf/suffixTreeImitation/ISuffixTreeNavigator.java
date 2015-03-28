package compressionservice.compression.algorithms.lzInf.suffixTreeImitation;

public interface ISuffixTreeNavigator
{
    /**
     * try to move in the next state using symbol ch.
     * if the move fails then we stay at the current state
     * @param ch transition symbol
     * @return true, if transition successful <br/>
     *         false, otherwise.
     */
    public boolean tryMove(char ch);

    /**
     * @return the leftmost position of the navigator state in the string
     */
    public long getLeftmostPosition();

    /**      
     * @return number of successful calls tryMove that leads to current state
     */
    public long pathLength();
}
