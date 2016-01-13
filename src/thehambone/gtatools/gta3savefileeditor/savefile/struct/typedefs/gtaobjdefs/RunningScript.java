package thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs;

import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAObject;
import java.io.IOException;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;

/**
 * A RunningScript object represents a running mission script (thread) whose
 * content is defined in main.scm. RunningScripts are stored in an array of
 * variable size at the end of the "Scripts" sub-block of the "SimpleVars" block
 * (block 0).
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 14, 2015
 * @deprecated 
 */
public class RunningScript extends GTAObject
{
    public static final int SIZE = 136;
    
    private int pNext, pPrevious;
    private String name;
    private int currentIp;
    private int[] returnStack;
    private int i24, i28;
    private short stackCounter;
    private int[] localVars;
    private int timerA, timerB;
    private byte ifResult;
    private byte b79, b7A;
    private int wakeTime;
    private short ifNumber;
    private byte b82, b83, b84, b85, b86, b87;
    
    public RunningScript()
    {
        this.pNext = 0;
        this.pPrevious = 0;
        this.name = "noname";
        this.currentIp = 0;
        this.returnStack = new int[4];
        this.i24 = 0;
        this.i28 = 0;
        this.stackCounter = 0;
        this.localVars = new int[16];
        this.timerA = 0;
        this.timerB = 0;
        this.ifResult = 0;
        this.b79 = 0;
        this.b7A = 0;
        this.wakeTime = 0;
        this.ifNumber = 0;
        this.b82 = 0;
        this.b83 = 0;
        this.b84 = 0;
        this.b85 = 0;
        this.b86 = 0;
        this.b87 = 0;
    }
    
    @Override
    public int getSize()
    {
        return SIZE;
    }

    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        int startOffset = in.getPointer();
        pNext = in.readInt();
        pPrevious = in.readInt();
        name =  in.readStringUTF8(8);
        currentIp = in.readInt();
        in.readInt(returnStack);
        i24 = in.readInt();
        i28 = in.readInt();
        stackCounter = in.readShort();
        in.skip(2);
        in.readInt(localVars);
        timerA = in.readInt();
        timerB = in.readInt();
        ifResult = in.read();
        b79 = in.read();
        b7A = in.read();
        in.skip(1);
        wakeTime = in.readInt();
        ifNumber = in.readShort();
        b82 = in.read();
        b83 = in.read();
        b84 = in.read();
        b85 = in.read();
        b86 = in.read();
        b87 = in.read();
        return in.getPointer() - startOffset;
    }

    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int startOffset = out.getPointer();
        out.writeInt(pNext);
        out.writeInt(pPrevious);
        out.writeStringUTF8(name, 8);
        out.writeInt(currentIp);
        out.writeInt(returnStack);
        out.writeInt(i24);
        out.writeInt(i28);
        out.writeShort(stackCounter);
        out.skip(2);
        out.writeInt(localVars);
        out.writeInt(timerA);
        out.writeInt(timerB);
        out.write(ifResult);
        out.write(b79);
        out.write(b7A);
        out.skip(1);
        out.writeInt(wakeTime);
        out.writeShort(ifNumber);
        out.write(b82);
        out.write(b83);
        out.write(b84);
        out.write(b85);
        out.write(b86);
        out.write(b87);
        return out.getPointer() - startOffset;
    }
}