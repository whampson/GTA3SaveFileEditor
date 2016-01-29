
package thehambone.gtatools.gta3savefileeditor.savefile.var;

/**
 * Created on Jan 11, 2016.
 *
 * @author thehambone
 */
public abstract class IntegerVariable<T> extends NumericVariable<T>
{
    protected IntegerVariable(int size)
    {
        super(size);
    }
    
    public abstract void parseValue(String s, boolean parseUnsigned);
    
    public abstract long toUnsignedLong();
}
