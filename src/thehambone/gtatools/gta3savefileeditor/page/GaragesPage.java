package thehambone.gtatools.gta3savefileeditor.page;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import thehambone.gtatools.gta3savefileeditor.CarColorSelectorDialog;
import thehambone.gtatools.gta3savefileeditor.cellrenderer.StoredCarListCellRenderer;
import thehambone.gtatools.gta3savefileeditor.savefile.SaveFile;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.SaveCarGarageSlot;
import thehambone.gtatools.gta3savefileeditor.savefile.struct.StoredCar;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarArray;
import thehambone.gtatools.gta3savefileeditor.savefile.var.VarByte;
import thehambone.gtatools.gta3savefileeditor.savefile.var.component.VariableComboBoxItem;
import thehambone.gtatools.gta3savefileeditor.util.Logger;

/**
 * This page contains features for editing the properties of the vehicles stored
 * in the player's three garages.
 * <p>
 * Created on Mar 31, 2015.
 * 
 * @author thehambone
 */
public class GaragesPage extends Page
{
    private static final Color DEFAULT_BACKGROUND
            = UIManager.getColor("Panel.background");
    
    private JComponent[] vehicleComponents;
    private JPopupMenu popupMenu;
    private VarArray<SaveCarGarageSlot> aSaveGarageSlot;
    private StoredCar selectedStoredCar;
    
    /**
     * Creates a new {@code GaragesPage} instance.
     */
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
    
    /**
     * Sets parameters for certain VariableComponents.
     */
    private void initVariableComponentParameters()
    {
        // Set the bitmasks to apply when the checkboxes are selected
        bulletproofCheckBox.setMask(
                GameConstants.VehicleImmunity.BULLETPROOF.getMask());
        collisionproofCheckBox.setMask(
                GameConstants.VehicleImmunity.COLLISIONPROOF.getMask());
        explosionproofCheckBox.setMask(
                GameConstants.VehicleImmunity.EXPLOSIONPROOF.getMask());
        fireproofCheckBox.setMask(
                GameConstants.VehicleImmunity.FIREPROOF.getMask());
    }
    
    /**
     * Creates an array of all components related to vehicle property editing.
     */
    private void initVehicleComponents()
    {
        vehicleComponents = new JComponent[] {
            vehicleLabel, vehicleComboBox, immunitiesPanel, bulletproofCheckBox,
            fireproofCheckBox, collisionproofCheckBox, explosionproofCheckBox,
            colorsPanel, primaryColorLabel, primaryColorPanel,
            secondaryColorLabel, secondaryColorPanel, bombTypePanel,
            bombTypeComboBox, radioStationPanel, radioStationComboBox
        };
        
        for (JComponent comp : vehicleComponents) {
            comp.setEnabled(false);
        }
    }
    
    /**
     * Sets up the safehouse selection combo box.
     */
    @SuppressWarnings("unchecked")
    private void initSafehouseComboBox()
    {
        DefaultComboBoxModel<String> safehouseComboBoxModel
                = new DefaultComboBoxModel<>();
        for (GameConstants.Island i : GameConstants.Island.values()) {
            safehouseComboBoxModel.addElement(i.getFriendlyName());
        }
        
        safehouseComboBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                safehouseComboBoxItemStateChanged();
            }
        });
        safehouseComboBox.setModel(safehouseComboBoxModel);
    }
    
    /**
     * Sets up the stored car list right-click menu.
     */
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
    
    /**
     * Defines the item selection listener for the stored car list.
     */
    private void initStoredCarListSelectionListener()
    {
        ListSelectionListener lsl = new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                storedCarListItemSelectedAction();
            }
        };
        storedCarList.addListSelectionListener(lsl);
    }
    
    /**
     * Defines the mouse listener for the stored car list.
     */
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
    
    /**
     * Sets up the vehicle color boxes.
     */
    private void initColorPanels()
    {
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
    
    /**
     * Populates the vehicle selection combo box.
     */
    private void initVehicleComboBox()
    {
        List<VariableComboBoxItem> vehicles = new ArrayList<>();
        for (GameConstants.Vehicle v : GameConstants.Vehicle.values()) {
            vehicles.add(
                    new VariableComboBoxItem(v.getID(), v.getFriendlyName()));
        }
        Collections.sort(vehicles);
        
        DefaultComboBoxModel<VariableComboBoxItem> vehicleComboBoxModel
                = new DefaultComboBoxModel<>(
                        vehicles.toArray(new VariableComboBoxItem[0]));
        
        vehicleComboBox.setModel(vehicleComboBoxModel);
        
        vehicleComboBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        storedCarList.repaint();
                    }
                });
            }
        });
    }
    
    /**
     * Populates the radio station selection combo box.
     */
    private void initRadioStationComboBox()
    {
        DefaultComboBoxModel<VariableComboBoxItem> radioStationComboBoxModel
                = new DefaultComboBoxModel<>();
        
        for (GameConstants.RadioStation r
                : GameConstants.RadioStation.values()) {
            radioStationComboBoxModel.addElement(
                    new VariableComboBoxItem(r.getID(), r.getFriendlyName()));
        }
        
        radioStationComboBox.setModel(radioStationComboBoxModel);
    }
    
    /**
     * Populates the car bomb selection combo box.
     */
    private void initBombTypeComboBox()
    {
        List<VariableComboBoxItem> bombTypes = new ArrayList<>();
        for (GameConstants.CarBomb cb
                : GameConstants.CarBomb.values()) {
            if (cb == GameConstants.CarBomb.TIMEDACTIVE) {
                continue;
            }
            
            bombTypes.add(
                    new VariableComboBoxItem(cb.getID(), cb.getFriendlyName()));
        }
        Collections.sort(bombTypes);
        
        DefaultComboBoxModel<VariableComboBoxItem> bombTypeComboBoxModel
                = new DefaultComboBoxModel<>(
                        bombTypes.toArray(new VariableComboBoxItem[0]));
        
        bombTypeComboBox.setModel(bombTypeComboBoxModel);
    }
    
    /**
     * Marks a StoredCar disabled in the file, which effectively deletes the
     * vehicle from the garage.
     * 
     * @param sc the StoredCar to delete
     */
    private void deleteVehicle(StoredCar sc)
    {
        if (sc == null) {
            return;
        }

        sc.nModelID.setValue(0);
        notifyVariableChange(sc.nModelID);
        
        ((DefaultListModel)storedCarList.getModel()).removeElement(sc);
    }
    
    /**
     * Opens a CarColorSelectorDialog and updates a car color variable based on
     * the color chosen.
     * 
     * @param colorPanel the panel that was clicked to open the dialog
     * @param colorVar the variable to write the selected color ID into
     */
    private void chooseCarColor(JPanel colorPanel, VarByte colorVar)
    {
        Window parent = SwingUtilities.getWindowAncestor(this);
        CarColorSelectorDialog ccs = new CarColorSelectorDialog(parent);
        GameConstants.CarColor cc = ccs.showColorSelectionDialog();
        
        if (cc != null) {
            colorPanel.setBackground(cc.getColor());
            colorVar.setValue((byte)cc.getID());
            notifyVariableChange(colorVar);
        }
    }
    
    /**
     * Defines the action to perform when a new item is selected in the
     * safehouse combo box.
     */
    @SuppressWarnings("unchecked")
    private void safehouseComboBoxItemStateChanged()
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
    
    /**
     * Defines the action to perform when an item is selected in the stored car
     * list.
     */
    private void storedCarListItemSelectedAction()
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
        
        // Reload variables
        vehicleComboBox.setVariable(selectedStoredCar.nModelID);
        bulletproofCheckBox.setVariable(selectedStoredCar.nImmunities);
        collisionproofCheckBox.setVariable(selectedStoredCar.nImmunities);
        explosionproofCheckBox.setVariable(selectedStoredCar.nImmunities);
        fireproofCheckBox.setVariable(selectedStoredCar.nImmunities);
        radioStationComboBox.setVariable(selectedStoredCar.nRadioStationID);
        bombTypeComboBox.setVariable(selectedStoredCar.nBombID);
        
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
                = SaveFile.getCurrentSaveFile().garages.aSaveCarGarageSlot;
        
        // Fire savehouse combo box selection listener
        safehouseComboBox.setSelectedIndex(-1);
        safehouseComboBox.setSelectedIndex(0);
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
        storedCarScrollPane = new javax.swing.JScrollPane();
        storedCarList = new javax.swing.JList();
        vehicleLabel = new javax.swing.JLabel();
        vehicleComboBox = new thehambone.gtatools.gta3savefileeditor.savefile.var.component.IntegerVariableComboBox();
        immunitiesPanel = new javax.swing.JPanel();
        bulletproofCheckBox = new thehambone.gtatools.gta3savefileeditor.savefile.var.component.BitmaskVariableCheckBox();
        collisionproofCheckBox = new thehambone.gtatools.gta3savefileeditor.savefile.var.component.BitmaskVariableCheckBox();
        explosionproofCheckBox = new thehambone.gtatools.gta3savefileeditor.savefile.var.component.BitmaskVariableCheckBox();
        fireproofCheckBox = new thehambone.gtatools.gta3savefileeditor.savefile.var.component.BitmaskVariableCheckBox();
        colorsPanel = new javax.swing.JPanel();
        primaryColorLabel = new javax.swing.JLabel();
        primaryColorPanel = new javax.swing.JPanel();
        secondaryColorLabel = new javax.swing.JLabel();
        secondaryColorPanel = new javax.swing.JPanel();
        radioStationPanel = new javax.swing.JPanel();
        radioStationComboBox = new thehambone.gtatools.gta3savefileeditor.savefile.var.component.IntegerVariableComboBox();
        bombTypePanel = new javax.swing.JPanel();
        bombTypeComboBox = new thehambone.gtatools.gta3savefileeditor.savefile.var.component.IntegerVariableComboBox();

        safehouseLabel.setText("Safehouse:");
        safehouseLabel.setToolTipText("");

        safehouseComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<safehouse_name>" }));

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

        javax.swing.GroupLayout bombTypePanelLayout = new javax.swing.GroupLayout(bombTypePanel);
        bombTypePanel.setLayout(bombTypePanelLayout);
        bombTypePanelLayout.setHorizontalGroup(
            bombTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bombTypePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bombTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bombTypePanelLayout.setVerticalGroup(
            bombTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bombTypePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bombTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private thehambone.gtatools.gta3savefileeditor.savefile.var.component.IntegerVariableComboBox bombTypeComboBox;
    private javax.swing.JPanel bombTypePanel;
    private thehambone.gtatools.gta3savefileeditor.savefile.var.component.BitmaskVariableCheckBox bulletproofCheckBox;
    private thehambone.gtatools.gta3savefileeditor.savefile.var.component.BitmaskVariableCheckBox collisionproofCheckBox;
    private javax.swing.JPanel colorsPanel;
    private thehambone.gtatools.gta3savefileeditor.savefile.var.component.BitmaskVariableCheckBox explosionproofCheckBox;
    private thehambone.gtatools.gta3savefileeditor.savefile.var.component.BitmaskVariableCheckBox fireproofCheckBox;
    private javax.swing.JPanel immunitiesPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel primaryColorLabel;
    private javax.swing.JPanel primaryColorPanel;
    private thehambone.gtatools.gta3savefileeditor.savefile.var.component.IntegerVariableComboBox radioStationComboBox;
    private javax.swing.JPanel radioStationPanel;
    private javax.swing.JComboBox safehouseComboBox;
    private javax.swing.JLabel safehouseLabel;
    private javax.swing.JPanel saveGaragePanel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel secondaryColorLabel;
    private javax.swing.JPanel secondaryColorPanel;
    private javax.swing.JList storedCarList;
    private javax.swing.JScrollPane storedCarScrollPane;
    private thehambone.gtatools.gta3savefileeditor.savefile.var.component.IntegerVariableComboBox vehicleComboBox;
    private javax.swing.JLabel vehicleLabel;
    // End of variables declaration//GEN-END:variables
}
