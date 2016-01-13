package thehambone.gtatools.gta3savefileeditor.gui.component;

import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.Variable;

/**
 * Used to indicate that a {@link javax.swing.JComponent} is used for updating
 * the value of a 
 * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable}.
 * <p>
 * Created on Mar 29, 2015.
 * 
 * @author thehambone
 */
public interface VariableValueComponent
{
    /**
     * Sets the
     * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable}
     * to be updated.
     * 
     * @param var the Variable to be updated
     */
    public void setVariable(Variable var);
    
//    /**
//     * Sets the
//     * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable}
//     * to be updated in an array of Variables.
//     * 
//     * @param var the Variable to be updated
//     * @param arrayIndex the index in the array
//     */
//    public void setVariable(Variable var, int arrayIndex);
    
    /**
     * Updates the
     * {@link thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable}.
     */
    public void update();
}
