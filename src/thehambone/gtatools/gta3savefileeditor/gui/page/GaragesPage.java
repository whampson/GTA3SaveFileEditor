package thehambone.gtatools.gta3savefileeditor.gui.page;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import thehambone.gtatools.gta3savefileeditor.game.GameConstants;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAByte;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.GTAInteger;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.typedefs.gtaobjdefs.StoredCar;
import thehambone.gtatools.gta3savefileeditor.savefile.variable.Variable;
import thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer.CarColorListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer.StoredCarListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.gui.component.cellrenderer.VehicleListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.io.IO;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * 
 * @author thehambone
 * @version 0.1
 * @since 0.1, March 31, 2015
 */
public class GaragesPage extends Page
{
    private static final int BP_MASK = 0x01;
    private static final int FP_MASK = 0x02;
    private static final int EP_MASK = 0x04;
    private static final int CP_MASK = 0x08;
    
    public GaragesPage()
    {
        super("Garages", Visibility.VISIBLE_WHEN_GAMESAVE_LOADED_ONLY);
        initComponents();
        addNotifiersToComponents(mainPanel, safehouseComboBox);
        initSelectionListener();
    }
    
    private void initSelectionListener()
    {
        garageSlotList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                isPageInitializing = true;
                
                StoredCar storedCar = getSelectedGarageSlot();
                if (storedCar == null) {
                    setSlotEditComponentsEnabled(false);
                    return;
                }
                setSlotEditComponentsEnabled(true);
                
                for (GameConstants.Vehicle v : GameConstants.Vehicle.values()) {
                    if (storedCar.getModelIDAsVariable().getValue().intValue() == v.getID()) {
                        vehicleComboBox.setSelectedItem(v);
                        break;
                    }
                }
                
                int immunities = storedCar.getImmunitiesAsVariable().getValue().intValue();
                bulletproofCheckBox.setSelected((immunities & BP_MASK) == BP_MASK);
                fireproofCheckBox.setSelected((immunities & FP_MASK) == FP_MASK);
                collisionProofCheckBox.setSelected((immunities & CP_MASK) == CP_MASK);
                explosionProofCheckBox.setSelected((immunities & EP_MASK) == EP_MASK);
                
                int primaryColorID = storedCar.getPrimaryColorIDAsVariable().getValue().byteValue();
                for (GameConstants.CarColor col : GameConstants.CarColor.values()) {
                    if (col.getID() == primaryColorID) {
                        primaryColorComboBox.setSelectedItem(col);
                        break;
                    }
                }
                int secondaryColorID = storedCar.getSecondaryColorIDAsVariable().getValue().byteValue();
                for (GameConstants.CarColor col : GameConstants.CarColor.values()) {
                    if (col.getID() == secondaryColorID) {
                        secondaryColorComboBox.setSelectedItem(col);
                        break;
                    }
                }
                
                int radioStationID = storedCar.getRadioStationIDAsVariable().getValue().byteValue();
                for (GameConstants.RadioStation r : GameConstants.RadioStation.values()) {
                    if (r.getID() == radioStationID) {
                        radioStationComboBox.setSelectedItem(r.getFriendlyName());
                        break;
                    }
                }
                
                int bombType = storedCar.getBombTypeAsVariable().getValue().byteValue();
                for (GameConstants.BombType b : GameConstants.BombType.values()) {
                    if (b.getID() == bombType) {
                        bombTypeComboBox.setSelectedItem(b.getFriendlyName());
                        break;
                    }
                }
                
                isPageInitializing = false;
            }
        });
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void loadPage()
    {
        Logger.debug("Loading page: %s...\n", getTitle());
        
        isPageInitializing = true;
        vars = SaveFile.getCurrentlyLoadedFile().getVariables();
        
        DefaultComboBoxModel safehouseComboBoxModel = new DefaultComboBoxModel();
        for (GameConstants.Island i : GameConstants.Island.values()) {
            safehouseComboBoxModel.addElement(i.getFriendlyName());
        }
        safehouseComboBox.setModel(safehouseComboBoxModel);
        
        DefaultComboBoxModel<GameConstants.Vehicle> vehicleComboBoxModel = new DefaultComboBoxModel();
        for (GameConstants.Vehicle v : GameConstants.Vehicle.values()) {
            vehicleComboBoxModel.addElement(v);
        }
        vehicleComboBox.setRenderer(new VehicleListCellRenderer());
        vehicleComboBox.setModel(vehicleComboBoxModel);
        
        DefaultComboBoxModel radioStationComboBoxModel = new DefaultComboBoxModel();
        for (GameConstants.RadioStation r : GameConstants.RadioStation.values()) {
            radioStationComboBoxModel.addElement(r.getFriendlyName());
        }
        radioStationComboBox.setModel(radioStationComboBoxModel);
        
        DefaultComboBoxModel bombTypeComboBoxModel = new DefaultComboBoxModel();
        for (GameConstants.BombType b : GameConstants.BombType.values()) {
            bombTypeComboBoxModel.addElement(b.getFriendlyName());
        }
        bombTypeComboBox.setModel(bombTypeComboBoxModel);
        
        primaryColorComboBox.setRenderer(new CarColorListCellRenderer());
        secondaryColorComboBox.setRenderer(new CarColorListCellRenderer());
        DefaultComboBoxModel primaryColorComboBoxModel = new DefaultComboBoxModel();
        DefaultComboBoxModel secondaryColorComboBoxModel = new DefaultComboBoxModel();
        for (GameConstants.CarColor c : GameConstants.CarColor.values()) {
            primaryColorComboBoxModel.addElement(c);
            secondaryColorComboBoxModel.addElement(c);
        }
        primaryColorComboBox.setModel(primaryColorComboBoxModel);
        secondaryColorComboBox.setModel(secondaryColorComboBoxModel);
        
        setSlotEditComponentsEnabled(false);
        updateGarageSlots(GameConstants.Island.PORTLAND);
        
        isPageInitializing = false;
    }
    
    private StoredCar getSelectedGarageSlot()
    {
        if (garageSlotList.getSelectedIndex() == -1) {
            return null;
        }
        return (StoredCar)garageSlotList.getSelectedValue();
    }
    
    private void setSlotEditComponentsEnabled(boolean enabled)
    {
        vehicleLabel.setEnabled(enabled);
        vehicleComboBox.setEnabled(enabled);
        
        immunitiesPanel.setEnabled(enabled);
        bulletproofCheckBox.setEnabled(enabled);
        fireproofCheckBox.setEnabled(enabled);
        collisionProofCheckBox.setEnabled(enabled);
        explosionProofCheckBox.setEnabled(enabled);
        
        colorsPanel.setEnabled(enabled);
        primaryColorLabel.setEnabled(enabled);
        primaryColorComboBox.setEnabled(enabled);
        secondaryColorLabel.setEnabled(enabled);
        secondaryColorComboBox.setEnabled(enabled);
        
        radioStationPanel.setEnabled(enabled);
        radioStationComboBox.setEnabled(enabled);
        
        bombTypePanel.setEnabled(enabled);
        bombTypeComboBox.setEnabled(enabled);
        
        if (!enabled) {
            vehicleComboBox.setSelectedIndex(-1);
            bulletproofCheckBox.setSelected(false);
            fireproofCheckBox.setSelected(false);
            collisionProofCheckBox.setSelected(false);
            explosionProofCheckBox.setSelected(false);
            primaryColorComboBox.setSelectedIndex(-1);
            secondaryColorComboBox.setSelectedIndex(-1);
            radioStationComboBox.setSelectedIndex(-1);
            bombTypeComboBox.setSelectedIndex(-1);
        }
    }
    
    private void setImmunity(JCheckBox checkBox, int immunityMask)
    {
        StoredCar storedCar = getSelectedGarageSlot();
        if (storedCar == null) {
            return;
        }
        int immunities = storedCar.getImmunitiesAsVariable().getValue().intValue();
        if (checkBox.isSelected()) {
            immunities |= immunityMask;
        } else {
            immunities &= ~immunityMask;
        }
        storedCar.getImmunitiesAsVariable().setValue(new GTAInteger(immunities));
    }
    
    @SuppressWarnings("unchecked")
    private void setColor(JComboBox comboBox, Variable colorVar)
    {
        GameConstants.CarColor newColor = (GameConstants.CarColor)comboBox.getSelectedItem();
        colorVar.setValue(new GTAByte((byte)newColor.getID()));
    }
    
    @SuppressWarnings("unchecked")
    private void updateGarageSlots(GameConstants.Island safehouse)
    {
        garageSlotList.setCellRenderer(new StoredCarListCellRenderer());
        DefaultListModel garageSlotListModel = new DefaultListModel();
        
        for (int i = 0; i < 6; i++) {
            StoredCar storedCar = vars.aSaveGarageSlots.getValueAt(i).getStoredCars()[safehouse.getID() - 1];
            if (storedCar.getModelIDAsVariable().getValue().intValue() == 0) {
                continue;
            }
            garageSlotListModel.addElement(storedCar);
        }
        garageSlotList.setModel(garageSlotListModel);
    }

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
        garageSlotScrollPane = new javax.swing.JScrollPane();
        garageSlotList = new javax.swing.JList();
        vehicleLabel = new javax.swing.JLabel();
        vehicleComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox();
        immunitiesPanel = new javax.swing.JPanel();
        bulletproofCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox();
        fireproofCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox();
        collisionProofCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox();
        explosionProofCheckBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox();
        colorsPanel = new javax.swing.JPanel();
        primaryColorLabel = new javax.swing.JLabel();
        secondaryColorLabel = new javax.swing.JLabel();
        primaryColorComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox();
        secondaryColorComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox();
        radioStationPanel = new javax.swing.JPanel();
        radioStationComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox();
        bombTypePanel = new javax.swing.JPanel();
        bombTypeComboBox = new thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox();

        safehouseLabel.setText("Safehouse:");
        safehouseLabel.setToolTipText("");

        safehouseComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        safehouseComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                safehouseComboBoxActionPerformed(evt);
            }
        });

        saveGaragePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save Garage"));

        garageSlotList.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "<empty>" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        garageSlotList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        garageSlotScrollPane.setViewportView(garageSlotList);

        vehicleLabel.setText("Vehicle:");

        vehicleComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                vehicleComboBoxActionPerformed(evt);
            }
        });

        immunitiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Immunities"));

        bulletproofCheckBox.setText("Bulletproof");
        bulletproofCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bulletproofCheckBoxActionPerformed(evt);
            }
        });

        fireproofCheckBox.setText("Fireproof");
        fireproofCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fireproofCheckBoxActionPerformed(evt);
            }
        });

        collisionProofCheckBox.setText("Collision-proof");
        collisionProofCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                collisionProofCheckBoxActionPerformed(evt);
            }
        });

        explosionProofCheckBox.setText("Explosion-proof");
        explosionProofCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                explosionProofCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout immunitiesPanelLayout = new javax.swing.GroupLayout(immunitiesPanel);
        immunitiesPanel.setLayout(immunitiesPanelLayout);
        immunitiesPanelLayout.setHorizontalGroup(
            immunitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(immunitiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(immunitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bulletproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fireproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(collisionProofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(explosionProofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        immunitiesPanelLayout.setVerticalGroup(
            immunitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(immunitiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bulletproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fireproofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collisionProofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(explosionProofCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        colorsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Colors"));

        primaryColorLabel.setText("Primary:");

        secondaryColorLabel.setText("Secondary:");

        primaryColorComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                primaryColorComboBoxActionPerformed(evt);
            }
        });

        secondaryColorComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                secondaryColorComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout colorsPanelLayout = new javax.swing.GroupLayout(colorsPanel);
        colorsPanel.setLayout(colorsPanelLayout);
        colorsPanelLayout.setHorizontalGroup(
            colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(secondaryColorLabel)
                    .addComponent(primaryColorLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(primaryColorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(secondaryColorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        colorsPanelLayout.setVerticalGroup(
            colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(primaryColorLabel)
                    .addComponent(primaryColorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(secondaryColorLabel)
                    .addComponent(secondaryColorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        radioStationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Radio Station"));

        radioStationComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                radioStationComboBoxActionPerformed(evt);
            }
        });

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

        bombTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Bomb Type"));

        bombTypeComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bombTypeComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bombTypePanelLayout = new javax.swing.GroupLayout(bombTypePanel);
        bombTypePanel.setLayout(bombTypePanelLayout);
        bombTypePanelLayout.setHorizontalGroup(
            bombTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bombTypePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bombTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );
        bombTypePanelLayout.setVerticalGroup(
            bombTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bombTypePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bombTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout saveGaragePanelLayout = new javax.swing.GroupLayout(saveGaragePanel);
        saveGaragePanel.setLayout(saveGaragePanelLayout);
        saveGaragePanelLayout.setHorizontalGroup(
            saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveGaragePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(garageSlotScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(saveGaragePanelLayout.createSequentialGroup()
                        .addComponent(vehicleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vehicleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(saveGaragePanelLayout.createSequentialGroup()
                        .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(immunitiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioStationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(colorsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bombTypePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        saveGaragePanelLayout.setVerticalGroup(
            saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveGaragePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(garageSlotScrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, saveGaragePanelLayout.createSequentialGroup()
                        .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(vehicleLabel)
                            .addComponent(vehicleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(saveGaragePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(saveGaragePanelLayout.createSequentialGroup()
                                .addComponent(immunitiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(radioStationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(saveGaragePanelLayout.createSequentialGroup()
                                .addComponent(colorsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bombTypePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        int islandID = safehouseComboBox.getSelectedIndex() + 1;
        for (GameConstants.Island i : GameConstants.Island.values()) {
            if (i.getID() == islandID) {
                updateGarageSlots(i);
                return;
            }
        }
    }//GEN-LAST:event_safehouseComboBoxActionPerformed

    private void vehicleComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_vehicleComboBoxActionPerformed
    {//GEN-HEADEREND:event_vehicleComboBoxActionPerformed
        StoredCar storedCar = getSelectedGarageSlot();
        if (storedCar == null || vehicleComboBox.getSelectedIndex() == -1) {
            return;
        }
        GameConstants.Vehicle newVehicle = (GameConstants.Vehicle)vehicleComboBox.getSelectedItem();
        storedCar.getModelIDAsVariable().setValue(new GTAInteger(newVehicle.getID()));
        garageSlotList.repaint();
        notifyObservers("change.made");
    }//GEN-LAST:event_vehicleComboBoxActionPerformed

    private void primaryColorComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_primaryColorComboBoxActionPerformed
    {//GEN-HEADEREND:event_primaryColorComboBoxActionPerformed
        StoredCar storedCar = getSelectedGarageSlot();
        if (storedCar == null || primaryColorComboBox.getSelectedIndex() == -1) {
            return;
        }
        setColor(primaryColorComboBox, storedCar.getPrimaryColorIDAsVariable());
        notifyObservers("change.made");
    }//GEN-LAST:event_primaryColorComboBoxActionPerformed

    private void secondaryColorComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_secondaryColorComboBoxActionPerformed
    {//GEN-HEADEREND:event_secondaryColorComboBoxActionPerformed
        StoredCar storedCar = getSelectedGarageSlot();
        if (storedCar == null || secondaryColorComboBox.getSelectedIndex() == -1) {
            return;
        }
        setColor(secondaryColorComboBox, storedCar.getSecondaryColorIDAsVariable());
        notifyObservers("change.made");
    }//GEN-LAST:event_secondaryColorComboBoxActionPerformed

    private void bulletproofCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bulletproofCheckBoxActionPerformed
    {//GEN-HEADEREND:event_bulletproofCheckBoxActionPerformed
        setImmunity(bulletproofCheckBox, BP_MASK);
        notifyObservers("change.made");
    }//GEN-LAST:event_bulletproofCheckBoxActionPerformed

    private void fireproofCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fireproofCheckBoxActionPerformed
    {//GEN-HEADEREND:event_fireproofCheckBoxActionPerformed
        setImmunity(fireproofCheckBox, FP_MASK);
        notifyObservers("change.made");
    }//GEN-LAST:event_fireproofCheckBoxActionPerformed

    private void collisionProofCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_collisionProofCheckBoxActionPerformed
    {//GEN-HEADEREND:event_collisionProofCheckBoxActionPerformed
        setImmunity(collisionProofCheckBox, CP_MASK);
        notifyObservers("change.made");
    }//GEN-LAST:event_collisionProofCheckBoxActionPerformed

    private void explosionProofCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_explosionProofCheckBoxActionPerformed
    {//GEN-HEADEREND:event_explosionProofCheckBoxActionPerformed
        setImmunity(explosionProofCheckBox, EP_MASK);
        notifyObservers("change.made");
    }//GEN-LAST:event_explosionProofCheckBoxActionPerformed

    private void radioStationComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_radioStationComboBoxActionPerformed
    {//GEN-HEADEREND:event_radioStationComboBoxActionPerformed
        StoredCar storedCar = getSelectedGarageSlot();
        if (storedCar == null) {
            return;
        }
        int newRadioStationID = 0;
        for (GameConstants.RadioStation r : GameConstants.RadioStation.values()) {
            if (r.getFriendlyName().equals((String)radioStationComboBox.getSelectedItem())) {
                newRadioStationID = r.getID();
                break;
            }
        }
        storedCar.getRadioStationIDAsVariable().setValue(new GTAByte((byte)newRadioStationID));
        notifyObservers("change.made");
    }//GEN-LAST:event_radioStationComboBoxActionPerformed

    private void bombTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bombTypeComboBoxActionPerformed
    {//GEN-HEADEREND:event_bombTypeComboBoxActionPerformed
        StoredCar storedCar = getSelectedGarageSlot();
        if (storedCar == null) {
            return;
        }
        int newBombType = 0;
        for (GameConstants.BombType b : GameConstants.BombType.values()) {
            if (b.getFriendlyName().equals((String)bombTypeComboBox.getSelectedItem())) {
                newBombType = b.getID();
                break;
            }
        }
        storedCar.getBombTypeAsVariable().setValue(new GTAByte((byte)newBombType));
        notifyObservers("change.made");
    }//GEN-LAST:event_bombTypeComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox bombTypeComboBox;
    private javax.swing.JPanel bombTypePanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox bulletproofCheckBox;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox collisionProofCheckBox;
    private javax.swing.JPanel colorsPanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox explosionProofCheckBox;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueCheckBox fireproofCheckBox;
    private javax.swing.JList garageSlotList;
    private javax.swing.JScrollPane garageSlotScrollPane;
    private javax.swing.JPanel immunitiesPanel;
    private javax.swing.JPanel mainPanel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox primaryColorComboBox;
    private javax.swing.JLabel primaryColorLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox radioStationComboBox;
    private javax.swing.JPanel radioStationPanel;
    private javax.swing.JComboBox safehouseComboBox;
    private javax.swing.JLabel safehouseLabel;
    private javax.swing.JPanel saveGaragePanel;
    private javax.swing.JScrollPane scrollPane;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox secondaryColorComboBox;
    private javax.swing.JLabel secondaryColorLabel;
    private thehambone.gtatools.gta3savefileeditor.gui.component.VariableValueComboBox vehicleComboBox;
    private javax.swing.JLabel vehicleLabel;
    // End of variables declaration//GEN-END:variables
}