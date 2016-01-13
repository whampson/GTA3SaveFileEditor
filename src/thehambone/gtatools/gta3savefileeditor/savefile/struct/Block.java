package thehambone.gtatools.gta3savefileeditor.savefile.struct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import thehambone.gtatools.gta3savefileeditor.io.IO;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileInputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.io.SaveFileOutputStream;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.VariableDefinitions;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * A Block represents a structured chunk of data in a GTA III gamesave. The data
 * contained in a Block can be of multiple types. Each save file is broken up
 * into 20 main blocks, followed by up to 4 padding blocks. Almost
 * every block contains nested sub-blocks.
 * 
 * Blocks are always preceded by a DWORD value representing the block's size in
 * bytes.
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, February 12, 2015
 * @deprecated 
 */
public abstract class Block implements DataStructure
{
    protected final Identifier identifier;
    protected final DataStructure[] structs;
    protected final VariableDefinitions vars;
    
    protected int expectedSize = 0;
    
    private Align[] alignData = new Align[0];
    
    /**
     * Creates a new Block.
     * 
     * @param identifier the block's identifier
     * @param vars the VariableDefinitions instance to use for referencing and
     *        setting variables
     * @param structs an array of DataStructures, each representing a piece of
     *        data stored in the block
     */
    public Block(Identifier identifier, VariableDefinitions vars, DataStructure[] structs)
    {
        this.identifier = identifier;
        this.vars = vars;
        this.structs = structs;
    }
    
    public Identifier getIdentifier()
    {
        return identifier;
    }
    
    /**
     * Gets the block's expected size. The expected size is the size stored in
     * the four bytes preceding the block. When loading the block, this value is
     * compared with the number of bytes read to check if all data has been
     * loaded.
     * 
     * @return the block's expected size
     */
    public int getExpectedSize()
    {
        return expectedSize;
    }
    
    /**
     * Sets the block's expected size. The expected size is the size stored in
     * the four bytes preceding the block.
     * 
     * @param expectedSize the block's expected size
     */
    public void setExpectedSize(int expectedSize)
    {
        this.expectedSize = expectedSize;
    }
    
    /**
     * Gets the block's hierarchal path in the file buffer in relation to other
     * blocks. The block path is analogous to a file path.
     * 
     * @return a String representation of the block's path
     */
    public String getBlockPath()
    {
        StringBuilder pathBuilder = new StringBuilder();
        Identifier id = identifier;
        List<Identifier> blockIdentifiers = new ArrayList<>();
        while (id != null) {
            blockIdentifiers.add(id);
            id = id.getParentIdeitifier();
        }
        for (int i = blockIdentifiers.size() - 1; i > -1; i--) {
            Identifier blockIdentifier = blockIdentifiers.get(i);
            pathBuilder.append(blockIdentifier.getFriendlyName());
            if (blockIdentifier == Identifier.GAMESAVE) {
                pathBuilder.append("://");
            } else {
                pathBuilder.append("/");
            }
        }
        return pathBuilder.toString();
    }
    
    public Align[] getAlignData()
    {
        List<Align> data = new ArrayList<>();
        for (DataStructure struct : structs) {
            if (struct instanceof Align) {
                data.add((Align)struct);
            } else if (struct instanceof Block) {
                data.addAll(Arrays.asList(((Block)struct).getAlignData()));
            }
        }
        return data.toArray(new Align[0]);
    }
    
    public void setAlignData(Align[] alignData)
    {
        this.alignData = alignData;
    }
    
    private void setAlignData(Align[] alignData, int dataOffset)
    {
        List<Align> alignDataList = new ArrayList<>();
        for (int i = 0; i < alignData.length - dataOffset; i++) {
            alignDataList.add(alignData[i + dataOffset]);
        }
        this.alignData = alignDataList.toArray(new Align[0]);
    }
    
    @Override
    public int getSize()
    {
        int size = 0;
        for (DataStructure struct : structs) {
            size += struct.getSize();
            if (struct instanceof Block) {
                size += 4;      // Add 4 bytes for size of block
            }
        }
        return size;
    }
    
    @Override
    public int load(SaveFileInputStream in) throws IOException
    {
        List<Align> alignDataList = new ArrayList<>();
        int bytesRead = 0;
        int offset = 0;
        
        for (DataStructure struct : structs) {
            offset = in.getPointer();
            if (struct instanceof Block) {
                ((Block)struct).setExpectedSize(in.readInt());
                bytesRead += 4;
            }
            bytesRead += struct.load(in);
            if (struct instanceof Align) {
                alignDataList.add((Align)struct);
            } else if (struct instanceof GTADataType
                    && ((GTADataType)struct).isMappedToVariable()) {
                Logger.debug("0x%08x: Loaded variable \"%s\"\n", offset, ((GTADataType)struct).getVariableMappedToName());
            } else if (struct instanceof Array
                    && ((Array)struct).isMappedToVariable()) {
                Logger.debug("0x%08x: Loaded variable \"%s\"\n", offset, ((Array)struct).getVariableMappedToName());
            }
        }
        alignData = alignDataList.toArray(new Align[0]);
        if (bytesRead != expectedSize) {
            Logger.warn("0x%08x: %s: The number of bytes read does not match the expected size of the block.\n"
                    + "    Expected: 0x%04x\n"
                    + "    Actual  : 0x%04x\n", in.getPointer(), getBlockPath(), expectedSize, bytesRead);
            int bytesToSkip = expectedSize - bytesRead;
            Logger.info("Skipping %s 0x%04x bytes...\n", (bytesToSkip > -1) ? "forward" : "back", Math.abs(bytesToSkip));
            bytesRead += in.skip(bytesToSkip);
        }
        return bytesRead;
    }
    
    @Override
    public int save(SaveFileOutputStream out) throws IOException
    {
        int alignDataOffset = 0;
        int bytesWritten = 0;
        int offset = 0;
        
        for (DataStructure struct : structs) {
            offset = out.getPointer();
            if (struct instanceof Block) {
                if (alignData.length != 0) {
                    ((Block)struct).setAlignData(alignData, alignDataOffset);
                }
                bytesWritten += out.writeInt(struct.getSize());
            } else if (struct instanceof Align && alignData.length != 0) {
                struct = (Align)alignData[alignDataOffset++];
            } else if (struct instanceof GTADataType 
                    && ((GTADataType)struct).isMappedToVariable()) {
                Logger.debug("0x%08x: Saved variable \"%s\"\n", offset, ((GTADataType)struct).getVariableMappedToName());
            } else if (struct instanceof Array
                    && ((Array)struct).isMappedToVariable()) {
                Logger.debug("0x%08x: Saved variable \"%s\"\n", offset, ((Array)struct).getVariableMappedToName());
            }
            bytesWritten += struct.save(out);
        }
        return bytesWritten;
    }
    
    /**
     * Block identifier definitions.
     */
    public static enum Identifier
    {
        GAMESAVE(-1, null, "GAMESAVE"),
            BLOCK00(0, GAMESAVE, "SimpleVars"),
                SCR(0, BLOCK00, "scr"),
                    SCRIPT_DATA(0, SCR, "TheScripts"),
                        SCM(0, SCRIPT_DATA, "scm"),
            BLOCK01(1, GAMESAVE, "PlayerPeds"),
                PLAYER_PED_DATA(1, BLOCK01, "playerPedData"),
                    PLAYER_PED(1, PLAYER_PED_DATA, "playerPed"),
                        CPED(1, PLAYER_PED, "cPed"),
            BLOCK02(2, GAMESAVE, "Garages"),
                GARAGE_DATA(2, BLOCK02, "garageData"),
            BLOCK12(12, GAMESAVE, "Gangs"),
                GNG(12, BLOCK12, "gng"),
                    GANG_DATA(12, GNG, "gangData"),
            BLOCK16(16, GAMESAVE, "PlayerInfo"),
                PLAYER_INFO_DATA(16, BLOCK16, "playerInfoData"),
            BLOCK19(19, GAMESAVE, "PedTypes"),
                PTP(19, BLOCK19, "ptp"),
                    PED_TYPE_DATA(19, PTP, "pedTypeData");
        
        private final int blockIndex;
        private final Identifier parentBlock;
        private final String friendlyName;
        
        private Identifier(int blockIndex, Identifier parentBlockIdentifier, String friendlyName)
        {
            this.blockIndex = blockIndex;
            this.parentBlock = parentBlockIdentifier;
            this.friendlyName = friendlyName;
        }
        
        public int getIndex()
        {
            return blockIndex;
        }
        
        public Identifier getParentIdeitifier()
        {
            return parentBlock;
        }
        
        public String getFriendlyName()
        {
            return friendlyName;
        }
    }
}