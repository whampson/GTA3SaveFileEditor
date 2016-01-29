package thehambone.gtatools.gta3savefileeditor.savefile.struct;

/**
 * Created on Sep 16, 2015.
 * 
 * @author thehambone
 */
public abstract class Block extends Record
{
    protected final String name;
    
    protected Block(String name, int size)
    {
        super(size);
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public abstract void setSize(int size);
}
