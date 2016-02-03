
package thehambone.gtatools.gta3savefileeditor.savefile.var.component;

/**
 * Created on Feb 3, 2016.
 *
 * @author thehambone
 */
public class VariableComboBoxItem implements Comparable<VariableComboBoxItem>
{
    private final int id;
    private final String displayName;
    
    public VariableComboBoxItem(int id, String displayName)
    {
        this.id = id;
        this.displayName = displayName;
    }
    
    public int getID()
    {
        return id;
    }
    
    public String getDisplayName()
    {
        return displayName;
    }
    
    @Override
    public int compareTo(VariableComboBoxItem o)
    {
        return displayName.compareTo(o.getDisplayName());
    }
    
    @Override
    public String toString()
    {
        return displayName;
    }
}
