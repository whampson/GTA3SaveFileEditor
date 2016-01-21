package thehambone.gtatools.gta3savefileeditor.gui.page;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;
import thehambone.gtatools.gta3savefileeditor.gui.CarColorSelectorDialog;
import thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer.StoredCarListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.newshit.SaveFileNew;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.SaveCarGarageSlot;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.StoredCar;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.newshit.struct.var.VarByte;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 31, 2015
 */
public class GaragesPage extends Page
{
    private static final Color DEFAULT_BACKGROUND
            = UIManager.getColor("Panel.background");
    
    private JComponent[] vehicleComponents;
    private JPopupMenu popupMenu;
    private VarArray<SaveCarGarageSlot> aSaveGarageSlot;
    private StoredCar selectedStoredCar;
    
    public GaragesPage()
    {
        super("Garages", Visibility.VISIBLE_WHEN_FILE_LOADED_ONLY);
        
        initComponents();
        initVariableComponentParameters();
        initVehicleComponents();
        initSafehouseComboBox();
        initStoredCarListPopupMenu();
        initStoredCarMouseListener();
        initStoredCarListSelectionListener();
        initVehicleComboBox();
        initColorPanels();
        initRadioStationComboBox();
        initBombTypeComboBox();
    }
    
    private void initVariableComponentParameters()
    {
        vehicleComboBox.setValueOffset(90);
        bulletproofCheckBox.setMask(
                GameConstants.VehicleImmunity.BULLETPROOF.getMask());
        collisionproofCheckBox.setMask(
                GameConstants.VehicleImmunity.COLLISIONPROOF.getMask());
        explosionproofCheckBox.setMask(
                GameConstants.VehicleImmunity.EXPLOSIONPROOF.getMask());
        fireproofCheckBox.setMask(
                GameConstants.VehicleImmunity.FIREPROOF.getMask());
    }
    
    private void initVehicleComponents()
    {
        vehicleComponents = new JComponent[] {
            vehicleLabel, vehicleComboBox, immunitiesPanel, bulletproofCheckBox,
            fireproofCheckBox, collisionproofCheckBox, explosionproofCheckBox,
            colorsPanel, primaryColorLabel, primaryColorPanel,
            secondaryColorLabel, secondaryColorPanel, bombTypePanel,
            bombTypeComboBox, bombArmedCheckBox, radioStationPanel,
            radioStationComboBox
        };
        
        for (JComponent comp : vehicleComponents) {
            comp.setEnabled(false);
        }
    }
    
    private void initSafehouseComboBox()
    {
        DefaultComboBoxModel<String> safehouseComboBoxModel
                = new DefaultComboBoxModel<>();
        for (GameConstants.Island i : GameConstants.Island.values()) {
            safehouseComboBoxModel.addElement(i.getFriendlyName());
        }
        
        safehouseComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                safehouseComboBoxAction(e);
            }
        });
        safehouseComboBox.setModel(safehouseComboBoxModel);
    }
    
    private void initStoredCarListPopupMenu()
    {
        popupMenu = new JPopupMenu();
        
        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteVehicle(selectedStoredCar);
            }
        });
        
        popupMenu.add(deleteMenuItem);
    }
    
    private void initStoredCarMouseListener()
    {
        storedCarList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int index = storedCarList.locationToIndex(e.getPoint());
                    storedCarList.setSelectedIndex(index);
                    popupMenu.show(storedCarList, e.getX(), e.getY());
                }
            }
        });
    }
    
    private void initStoredCarListSelectionListener()
    {
        ListSelectionListener lsl = new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                storedCarListItemSelectedAction(e);
            }
        };
        storedCarList.addListSelectionListener(lsl);
    }
    
    private void initColorPanels()
    {
//        primaryColorPanel.setFocusable(true);
//        secondaryColorPanel.setFocusable(true);
        
        primaryColorPanel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (!primaryColorPanel.isEnabled()
                        || selectedStoredCar == null) {
                    return;
                }
                
                chooseCarColor(primaryColorPanel,
                        selectedStoredCar.nPrimaryColorID);
            }
        });
        
        secondaryColorPanel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (!secondaryColorPanel.isEnabled()
                        || selectedStoredCar == null) {
                    return;
                }
                
                chooseCarColor(secondaryColorPanel,
                        selectedStoredCar.nSecondaryColorID);
            }
        });
    }
    
    private void initVehicleComboBox()
    {
        DefaultComboBoxModel<String> vehicleComboBoxModel
                = new DefaultComboBoxModel<>();
        
        for (GameConstants.Vehicle v : GameConstants.Vehicle.values()) {
            vehicleComboBoxModel.addElement(v.getFriendlyName());
        }
        
        vehicleComboBox.setModel(vehicleComboBoxModel);
        
        vehicleComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                storedCarList.repaint();
            }
        });
    }
    
    private void initRadioStationComboBox()
    {
        DefaultComboBoxModel<String> radioStationComboBoxModel
                = new DefaultComboBoxModel<>();
        for (GameConstants.RadioStation r
                : GameConstants.RadioStation.values()) {
            radioStationComboBoxModel.addElement(r.getFriendlyName());
        }
        radioStationComboBox.setModel(radioStationComboBoxModel);
    }
    
    private void initBombTypeComboBox()
    {
        DefaultComboBoxModel<String> bombTypeComboBoxModel
                = new DefaultComboBoxModel<>();
        for (GameConstants.CarBomb cb
                : GameConstants.CarBomb.values()) {
            bombTypeComboBoxModel.addElement(cb.getFriendlyName());
        }
        bombTypeComboBox.setModel(bombTypeComboBoxModel);
    }
    
    private void deleteVehicle(StoredCar sc)
    {
        if (sc == null) {
            return;
        }

        sc.nModelID.setValue(0);
        Logger.debug("Variable updated: " + sc.nModelID);
        notifyChange(sc.nModelID);
        ((DefaultListModel)storedCarList.getModel()).removeElement(sc);
    }
    
    private void chooseCarColor(JPanel colorPanel, VarByte colorVar)
    {
        Window parent = SwingUtilities.getWindowAncestor(this);
        CarColorSelectorDialog ccs = new CarColorSelectorDialog(parent);
        GameConstants.CarColor cc = ccs.showColorSelectionDialog();
        
        if (cc != null) {
            colorPanel.setBackground(cc.getColor());
            colorVar.setValue((byte)cc.getID());
            Logger.debug("Variable updated: " + colorVar);
            notifyChange(colorVar);
        }
    }
    
    private void safehouseComboBoxAction(ActionEvent e)
    {
        int selectedIndex = safehouseComboBox.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        
        DefaultListModel storedCarListModel
                = new DefaultListModel();
        for (SaveCarGarageSlot slot : aSaveGarageSlot) {
            StoredCar sc = slot.aStoredCar.getElementAt(selectedIndex);
            if (sc.nModelID.getValue() != 0) {
                storedCarListModel.addElement(sc);
            }
        }
        
        if (storedCarListModel.isEmpty()) {
            storedCarListModel.addElement("(no vehicles)");
            storedCarList.setCellRenderer(new DefaultListCellRenderer());
            storedCarList.setEnabled(false);
        } else {
            storedCarList.setCellRenderer(new StoredCarListCellRenderer());
            storedCarList.setEnabled(true);
        }
        
        storedCarList.setModel(storedCarListModel);
    }
    
    private void storedCarListItemSelectedAction(ListSelectionEvent e)
    {
        selectedStoredCar = (StoredCar)storedCarList.getSelectedValue();
        for (JComponent comp : vehicleComponents) {
            comp.setEnabled(selectedStoredCar != null);
        }
        primaryColorPanel.setBackground(DEFAULT_BACKGROUND);
        secondaryColorPanel.setBackground(DEFAULT_BACKGROUND);
        if (selectedStoredCar == null) {
            return;
        }

        vehicleComboBox.setVariable(selectedStoredCar.nModelID);
        bulletproofCheckBox.setVariable(selectedStoredCar.nImmunities);
        collisionproofCheckBox.setVariable(selectedStoredCar.nImmunities);
        explosionproofCheckBox.setVariable(selectedStoredCar.nImmunities);
        fireproofCheckBox.setVariable(selectedStoredCar.nImmunities);
        radioStationComboBox.setVariable(selectedStoredCar.nRadioStationID);
        bombTypeComboBox.setVariable(selectedStoredCar.nBombID);
//        bombArmedCheckBox.setVariable(selectedStoredCar.nBombID);
        
        for (GameConstants.CarColor cc : GameConstants.CarColor.values()) {
            if (selectedStoredCar.nPrimaryColorID.getValue() == cc.getID()) {
                primaryColorPanel.setBackground(cc.getColor());
            }
            if (selectedStoredCar.nSecondaryColorID.getValue() == cc.getID()) {
                secondaryColorPanel.setBackground(cc.getColor());
            }
        }
    }
    
    @Override
    public void loadPage()
    {
        Logger.debug("Loading page: %s...\n", getPageTitle());
        
        aSaveGarageSlot
                = SaveFileNew.getCurrentSaveFile().garages.aSaveCarGarageSlot;
        
        safehouseComboBox.setSelectedIndex(-1);
        safehouseComboBox.setSelectedIndex(0);
        
//        vars = SaveFile.getCurrentlyLoadedFile().getVariables();
//        
//        DefaultComboBoxModel safehouseComboBoxModel = new DefaultComboBoxModel();
//        for (GameConstants.Island i : GameConstants.Island.values()) {
//            safehouseComboBoxModel.addElement(i.getFriendlyName());
//        }
//        safehouseComboBox.setModel(safehouseComboBoxModel);
//        
//        DefaultComboBoxModel<GameConstants.Vehicle> vehicleComboBoxModel = new DefaultComboBoxModel();
//        for (GameConstants.Vehicle v : GameConstants.Vehicle.values()) {
//            vehicleComboBoxModel.addElement(v);
//        }
//        vehicleComboBox.setRenderer(new VehicleListCellRenderer());
//        vehicleComboBox.setModel(vehicleComboBoxModel);
//        
//        DefaultComboBoxModel radioStationComboBoxModel = new DefaultComboBoxModel();
//        for (GameConstants.RadioStation r : GameConstants.RadioStation.values()) {
//            radioStationComboBoxModel.addElement(r.getFriendlyName());
//        }
//        radioStationComboBox.setModel(radioStationComboBoxModel);
//        
//        DefaultComboBoxModel bombTypeComboBoxModel = new DefaultComboBoxModel();
//        for (GameConstants.CarBomb b : GameConstants.CarBomb.values()) {
//            bombTypeComboBoxModel.addElement(b.getFriendlyName());
//        }
//        bombTypeComboBox.setModel(bombTypeComboBoxModel);
//        
//        primaryColorComboBox.setRenderer(new CarColorListCellRenderer());
//        secondaryColorComboBox.setRenderer(new CarColorListCellRenderer());
//        DefaultComboBoxModel primaryColorComboBoxModel = new DefaultComboBoxModel();
//        DefaultComboBoxModel secondaryColorComboBoxModel = new DefaultComboBoxModel();
//        for (GameConstants.CarColor c : GameConstants.CarColor.values()) {
//            primaryColorComboBoxModel.addElement(c);
//            secondaryColorComboBoxModel.addElement(c);
//        }
//        primaryColorComboBox.setModel(primaryColorComboBoxModel);
//        secondaryColorComboBox.setModel(secondaryColorComboBoxModel);
//        
//        setSlotEditComponentsEnabled(false);
//        updateGarageSlots(GameConstants.Island.PORTLAND);
    }
    
    //    private void initSelectionListener()
//    {
//        garageSlotList.addListSelectionListener(new ListSelectionListener()
//        {
//            @Override
//            public void valueChanged(ListSelectionEvent e)
//            {
//                StoredCar storedCar = getSelectedGarageSlot();
//                if (storedCar == null) {
//                    setSlotEditComponentsEnabled(false);
//                    return;
//                }
//                setSlotEditComponentsEnabled(true);
//                
//                for (GameConstants.Vehicle v : GameConstants.Vehicle.values()) {
//                    if (storedCar.getModelIDAsVariable().getValue().intValue() == v.getID()) {
//                        vehicleComboBox.setSelectedItem(v);
//                        break;
//                    }
//                }
//                
//                int immunities = storedCar.getImmunitiesAsVariable().getValue().intValue();
//                bulletproofCheckBox.setSelected((immunities & BP_MASK) == BP_MASK);
//                fireproofCheckBox.setSelected((immunities & FP_MASK) == FP_MASK);
//                collisionProofCheckBox.setSelected((immunities & CP_MASK) == CP_MASK);
//                explosionProofCheckBox.setSelected((immunities & EP_MASK) == EP_MASK);
//                
//                int primaryColorID = storedCar.getPrimaryColorIDAsVariable().getValue().byteValue();
//                for (GameConstants.CarColor col : GameConstants.CarColor.values()) {
//                    if (col.getID() == primaryColorID) {
//                        primaryColorComboBox.setSelectedItem(col);
//                        break;
//                    }
//                }
//                int secondaryColorID = storedCar.getSecondaryColorIDAsVariable().getValue().byteValue();
//                for (GameConstants.CarColor col : GameConstants.CarColor.values()) {
//                    if (col.getID() == secondaryColorID) {
//                        secondaryColorComboBox.setSelectedItem(col);
//                        break;
//                    }
//                }
//                
//                int radioStationID = storedCar.getRadioStationIDAsVariable().getValue().byteValue();
//                for (GameConstants.RadioStation r : GameConstants.RadioStation.values()) {
//                    if (r.getID() == radioStationID) {
//                        radioStationComboBox.setSelectedItem(r.getFriendlyName());
//                        break;
//                    }
//                }
//                
//                int bombType = storedCar.getBombTypeAsVariable().getValue().byteValue();
//                for (GameConstants.CarBomb b : GameConstants.CarBomb.values()) {
//                    if (b.getID() == bombType) {
//                        bombTypeComboBox.setSelectedItem(b.getFriendlyName());
//                        break;
//                    }
//                }
//            }
//        });
//    }
    
//    private StoredCar getSelectedGarageSlot()
//    {
//        if (storedCarList.getSelectedIndex() == -1) {
//            return null;
//        }
//        return (StoredCar)storedCarList.getSelectedValue();
//    }
//    
//    private void setSlotEditComponentsEnabled(boolean enabled)
//    {
//        vehicleLabel.setEnabled(enabled);
//        vehicleComboBox.setEnabled(enabled);
//        
//        immunitiesPanel.setEnabled(enabled);
//        bulletproofCheckBox.setEnabled(enabled);
//        fireproofCheckBox.setEnabled(enabled);
//        collisionProofCheckBox.setEnabled(enabled);
//        explosionProofCheckBox.setEnabled(enabled);
//        
//        colorsPanel.setEnabled(enabled);
//        primaryColorLabel.setEnabled(enabled);
//        primaryColorComboBox.setEnabled(enabled);
//        secondaryColorLabel.setEnabled(enabled);
//        secondaryColorComboBox.setEnabled(enabled);
//        
//        radioStationPanel.setEnabled(enabled);
//        radioStationComboBox.setEnabled(enabled);
//        
//        bombTypePanel.setEnabled(enabled);
//        bombTypeComboBox.setEnabled(enabled);
//        
//        if (!enabled) {
//            vehicleComboBox.setSelectedIndex(-1);
//            bulletproofCheckBox.setSelected(false);
//            fireproofCheckBox.setSelected(false);
//            collisionProofCheckBox.setSelected(false);
//            explosionProofCheckBox.setSelected(false);
//            primaryColorComboBox.setSelectedIndex(-1);
//            secondaryColorComboBox.setSelectedIndex(-1);
//            radioStationComboBox.setSelectedIndex(-1);
//            bombTypeComboBox.setSelectedIndex(-1);
//        }
//    }
//    
//    private void setImmunity(JCheckBox checkBox, int immunityMask)
//    {
//        StoredCar storedCar = getSelectedGarageSlot();
//        if (storedCar == null) {
//            return;
//        }
//        int immunities = storedCar.getImmunitiesAsVariable().getValue().intValue();
//        if (checkBox.isSelected()) {
//            immunities |= immunityMask;
//        } else {
//            immunities &= ~immunityMask;
//        }
//        storedCar.getImmunitiesAsVariable().setValue(new GTAInteger(immunities));
//    }
//    
//    @SuppressWarnings("unchecked")
//    private void setColor(JComboBox comboBox, Variable colorVar)
//    {
//        GameConstants.CarColor newColor = (GameConstants.CarColor)comboBox.getSelectedItem();
//        colorVar.setValue(new GTAByte((byte)newColor.getID()));
//    }
//    
//    @SuppressWarnings("unchecked")
//    private void updateGarageSlots(GameConstants.Island safehouse)
//    {
//        storedCarList.setCellRenderer(new StoredCarListCellRenderer());
//        DefaultListModel garageSlotListModel = new DefaultListModel();
//        
//        for (int i = 0; i < 6; i++) {
//            StoredCar storedCar = vars.aSaveGarageSlots.getValueAt(i).getStoredCars()[safehouse.getID() - 1];
//            if (storedCar.getModelIDAsVariable().getValue().intValue() == 0) {
//                continue;
//            }
//            garageSlotListModel.addElement(storedCar);
//        }
//        storedCarList.setModel(garageSlotListModel);
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        scrollPane = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        safehouseLabel = new javax.swing.JLabel();
        safehouseComboBox = new javax.swing.JComboBox();
        saveGaragePanel = new javax.swing.JPanel();
        storedCarScrollPane = new javax.swing.JScrollPane();
        storedCarList = new javax.swing.JList();
        vehicleLabel = new javax.swing.JLabel();
        vehicleComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox();
        immunitiesPanel = new javax.swing.JPanel();
        bulletproofCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.BitmaskVariableCheckBox();
        collisionproofCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.BitmaskVariableCheckBox();
        explosionproofCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.BitmaskVariableCheckBox();
        fireproofCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.BitmaskVariableCheckBox();
        colorsPanel = new javax.swing.JPanel();
        primaryColorLabel = new javax.swing.JLabel();
        primaryColorPanel = new javax.swing.JPanel();
        secondaryColorLabel = new javax.swing.JLabel();
        secondaryColorPanel = new javax.swing.JPanel();
        radioStationPanel = new javax.swing.JPanel();
        radioStationComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox();
        bombTypePanel = new javax.swing.JPanel();
        bombTypeComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox();
        bombArmedCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableCheckBox();

        safehouseLabel.setText("Safehouse:");
        safehouseLabel.setToolTipText("");

        safehouseComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<safehouse_name>" }));
        safehouseComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                safehouseComboBoxActionPerformed(evt);
            }
        });

        saveGaragePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save Garage"));

        storedCarList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        storedCarScrollPane.setViewportView(storedCarList);

        vehicleLabel.setText("Vehicle:");

        vehicleComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<vehicle_name>" }));

        immunitiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Immunities"));

        bulletproofCheckBox.setText("Bulletproof");

        collisionproofCheckBox.setText("Collisionproof");

        explosionproofCheckBox.setText("Explosionproof");

        fireproofCheckBox.setText("Fireproof");

        javax.swing.GroupLayout immunitiesPanelLayout = new javax.swing.GroupLayout(immunitiesPanel);
        immunitiesPanel.setLayout(immunitiesPanelLayout);
        immunitiesPanelLayout.setHorizontalGroup(
            immunitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(immunitiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(immunitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bulletproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(collisionproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(explosionproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fireproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        immunitiesPanelLayout.setVerticalGroup(
            immunitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(immunitiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bulletproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collisionproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(explosionproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fireproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        colorsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Colors"));

        primaryColorLabel.setText("Color 1");

        primaryColorPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        primaryColorPanel.setPreferredSize(new java.awt.Dimension(48, 48));

        javax.swing.GroupLayout primaryColorPanelLayout = new javax.swing.GroupLayout(primaryColorPanel);
        primaryColorPanel.setLayout(primaryColorPanelLayout);
        primaryColorPanelLayout.setHorizontalGroup(
            primaryColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );
        primaryColorPanelLayout.setVerticalGroup(
            primaryColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        secondaryColorLabel.setText("Color 2");
        secondaryColorLabel.setToolTipText("");

        secondaryColorPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        secondaryColorPanel.setPreferredSize(new java.awt.Dimension(48, 48));

        javax.swing.GroupLayout secondaryColorPanelLayout = new javax.swing.GroupLayout(secondaryColorPanel);
        secondaryColorPanel.setLayout(secondaryColorPanelLayout);
        secondaryColorPanelLayout.setHorizontalGroup(
            secondaryColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );
        secondaryColorPanelLayout.setVerticalGroup(
            secondaryColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorsPanelLayout = new javax.swing.GroupLayout(colorsPanel);
        colorsPanel.setLayout(colorsPanelLayout);
        colorsPanelLayout.setHorizontalGroup(
            colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(primaryColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(primaryColorLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(secondaryColorLabel)
                    .addComponent(secondaryColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        colorsPanelLayout.setVerticalGroup(
            colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, colorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(primaryColorLabel)
                    .addComponent(secondaryColorLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(primaryColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(secondaryColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        radioStationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Radio Station"));

        radioStationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<radiostation_name>" }));

        javax.swing.GroupLayout radioStationPanelLayout = new javax.swing.GroupLayout(radioStationPanel);
        radioStationPanel.setLayout(radioStationPanelLayout);
        radioStationPanelLayout.setHorizontalGroup(
            radioStationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(radioStationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioStationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        radioStationPanelLayout.setVerticalGroup(
            radioStationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(radioStationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioStationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bombTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Car Bomb"));

        bombTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<bombtype_name>" }));

        bombArmedCheckBox.setText("Armed");

        javax.swing.GroupLayout bombTypePanelLayout = new javax.swing.GroupLayout(bombTypePanel);
        bombTypePanel.setLayout(bombTypePanelLayout);
        bombTypePanelLayout.setHorizontalGroup(
            bombTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bombTypePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bombTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bombTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bombTypePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(bombArmedCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bombTypePanelLayout.setVerticalGroup(
            bombTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bombTypePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bombTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(bombArmedCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout saveGaragePanelLayout = new javax.swing.GroupLayout(saveGaragePanel);
        saveGaragePanel.setLayout(saveGaragePanelLayout);
        saveGaragePanelLayout.setHorizontalGroup(
            saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveGaragePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(storedCarScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(saveGaragePanelLayout.createSequentialGroup()
                        .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(radioStationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(immunitiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bombTypePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(colorsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(saveGaragePanelLayout.createSequentialGroup()
                        .addComponent(vehicleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vehicleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        saveGaragePanelLayout.setVerticalGroup(
            saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveGaragePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(saveGaragePanelLayout.createSequentialGroup()
                        .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(vehicleLabel)
                            .addComponent(vehicleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(colorsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(immunitiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bombTypePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioStationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(storedCarScrollPane))
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveGaragePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(safehouseLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(safehouseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(safehouseLabel)
                    .addComponent(safehouseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveGaragePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scrollPane.setViewportView(mainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void safehouseComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_safehouseComboBoxActionPerformed
    {//GEN-HEADEREND:event_safehouseComboBoxActionPerformed
//        int islandID = safehouseComboBox.getSelectedIndex() + 1;
//        for (GameConstants.Island i : GameConstants.Island.values()) {
//            if (i.getID() == islandID) {
//                updateGarageSlots(i);
//                return;
//            }
//        }
    }//GEN-LAST:event_safehouseComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableCheckBox bombArmedCheckBox;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox bombTypeComboBox;
    private javax.swing.JPanel bombTypePanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.BitmaskVariableCheckBox bulletproofCheckBox;
    private thehambone.gtatools.gta3savefileeditor.gui.component.BitmaskVariableCheckBox collisionproofCheckBox;
    private javax.swing.JPanel colorsPanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.BitmaskVariableCheckBox explosionproofCheckBox;
    private thehambone.gtatools.gta3savefileeditor.gui.component.BitmaskVariableCheckBox fireproofCheckBox;
    private javax.swing.JPanel immunitiesPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel primaryColorLabel;
    private javax.swing.JPanel primaryColorPanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox radioStationComboBox;
    private javax.swing.JPanel radioStationPanel;
    private javax.swing.JComboBox safehouseComboBox;
    private javax.swing.JLabel safehouseLabel;
    private javax.swing.JPanel saveGaragePanel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel secondaryColorLabel;
    private javax.swing.JPanel secondaryColorPanel;
    private javax.swing.JList storedCarList;
    private javax.swing.JScrollPane storedCarScrollPane;
    private thehambone.gtatools.gta3savefileeditor.gui.component.IntegerVariableComboBox vehicleComboBox;
    private javax.swing.JLabel vehicleLabel;
    // End of variables declaration//GEN-END:variables
}