package view;

import controller.WeaponController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AddWeaponDialog extends JDialog {

    private final WeaponController controller;
    private boolean confirmed = false;
    private String weaponName = "";

    private JComboBox<String> typeCombo;
    private JTextField nameField;
    private JSpinner damageSpinner;
    private JSpinner speedSpinner;
    private JSpinner strengthSpinner;
    private JSpinner lengthSpinner;
    private JSpinner ammoSpinner;
    private JSpinner accuracySpinner;
    private JPanel specificPanel;

    public AddWeaponDialog(Frame parent, WeaponController controller) {
        super(parent, "Добавить оружие", true);
        this.controller = controller;
        buildUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);
        root.add(buildCommonPanel(),   BorderLayout.NORTH);
        root.add(buildSpecificPanel(), BorderLayout.CENTER);
        root.add(buildButtonRow(),     BorderLayout.SOUTH);
    }

    private JPanel buildCommonPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new TitledBorder("Общие параметры"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.fill   = GridBagConstraints.HORIZONTAL;
        addRow(p, c, 0, "Тип оружия",    typeCombo       = buildTypeCombo());
        addRow(p, c, 1, "Название",       nameField       = new JTextField(16));
        addRow(p, c, 2, "Урон",           damageSpinner   = spinner(10, 1, 9999));
        addRow(p, c, 3, "Скорость атаки", speedSpinner    = spinner(1,  1, 100));
        addRow(p, c, 4, "Прочность",      strengthSpinner = spinner(10, 1, 9999));
        return p;
    }

    private JComboBox<String> buildTypeCombo() {
        JComboBox<String> box = new JComboBox<>(new String[]{"Ближнего боя", "Дальнобойное"});
        box.addActionListener(e -> switchPanel(box.getSelectedIndex() == 0));
        return box;
    }

    private JPanel buildSpecificPanel() {
        specificPanel = new JPanel(new CardLayout());
        specificPanel.add(buildMeleePanel(), "melee");
        specificPanel.add(buildRangePanel(), "range");
        return specificPanel;
    }

    private JPanel buildMeleePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new TitledBorder("Параметры ближнего боя"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.fill   = GridBagConstraints.HORIZONTAL;
        lengthSpinner = spinner(10, 1, 200);
        addRow(p, c, 0, "Длина оружия", lengthSpinner);
        return p;
    }

    private JPanel buildRangePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new TitledBorder("Параметры дальнего боя"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.fill   = GridBagConstraints.HORIZONTAL;
        ammoSpinner     = spinner(20, 0, 9999);
        accuracySpinner = spinner(50, 1, 100);
        addRow(p, c, 0, "Боеприпасы",  ammoSpinner);
        addRow(p, c, 1, "Точность (%)", accuracySpinner);
        return p;
    }

    private JPanel buildButtonRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        JButton ok = new JButton("Добавить");
        ok.addActionListener(e -> confirm());
        JButton cancel = new JButton("Отмена");
        cancel.addActionListener(e -> dispose());
        p.add(ok);
        p.add(cancel);
        return p;
    }

    private void confirm() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите название оружия!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }
        int dmg = (int) damageSpinner.getValue();
        int spd = (int) speedSpinner.getValue();
        int str = (int) strengthSpinner.getValue();
        if (typeCombo.getSelectedIndex() == 0) {
            controller.addMeleeWeapon(name, dmg, spd, str, (int) lengthSpinner.getValue());
        } else {
            controller.addRangeWeapon(name, dmg, spd, str,
                    (int) ammoSpinner.getValue(), (int) accuracySpinner.getValue());
        }
        weaponName = name;
        confirmed  = true;
        dispose();
    }

    private void switchPanel(boolean melee) {
        CardLayout cl = (CardLayout) specificPanel.getLayout();
        if (melee) {
            cl.show(specificPanel, "melee");
        } else {
            cl.show(specificPanel, "range");
        }
    }

    private static void addRow(JPanel p, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        p.add(new JLabel(label), c);
        c.gridx = 1;              c.weightx = 1;
        p.add(field, c);
    }

    private static JSpinner spinner(int value, int min, int max) {
        return new JSpinner(new SpinnerNumberModel(value, min, max, 1));
    }

    public boolean isConfirmed()  { return confirmed; }
    public String  getWeaponName(){ return weaponName; }
}
