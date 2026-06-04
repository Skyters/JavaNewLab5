package view;

import controller.WeaponController;
import model.MeleeWeapon;
import model.RangeWeapon;
import model.Weapon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {

    private static final int DIVIDER = 260;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final WeaponController controller = new WeaponController();

    private DefaultListModel<String> listModel;
    private JList<String> weaponList;
    private JTextArea infoArea;
    private JButton   attackBtn;
    private JButton   statusBtn;
    private JButton   removeBtn;
    private JTextArea logArea;

    public MainFrame() {
        super("Полигон оружия — MVC");
        buildUI();
        refreshList();
        setVisible(true);
    }

    private void buildUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(820, 580));
        setLocationRelativeTo(null);
        setJMenuBar(buildMenuBar());
        JPanel content = (JPanel) getContentPane();
        content.setLayout(new BorderLayout(8, 8));
        content.setBorder(new EmptyBorder(8, 8, 8, 8));
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, buildListPanel(), buildDetailPanel());
        split.setDividerLocation(DIVIDER);
        split.setResizeWeight(0.3);
        content.add(split,           BorderLayout.CENTER);
        content.add(buildLogPanel(), BorderLayout.SOUTH);
        pack();
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar  = new JMenuBar();
        JMenu    menu = new JMenu("Оружие");
        JMenuItem addItem = new JMenuItem("Добавить оружие...");
        addItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        addItem.addActionListener(e -> openAddDialog());
        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> System.exit(0));
        menu.add(addItem);
        menu.addSeparator();
        menu.add(exitItem);
        bar.add(menu);
        return bar;
    }

    private JPanel buildListPanel() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBorder(new TitledBorder("Арсенал"));
        listModel  = new DefaultListModel<>();
        weaponList = new JList<>(listModel);
        weaponList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        weaponList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        weaponList.setCellRenderer(new WeaponCellRenderer());
        weaponList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && weaponList.getSelectedIndex() >= 0) {
                controller.selectWeapon(weaponList.getSelectedIndex());
                refreshInfo();
                setActionButtonsEnabled(true);
            }
        });
        JButton addBtn = new JButton("+ Добавить");
        addBtn.addActionListener(e -> openAddDialog());
        p.add(new JScrollPane(weaponList), BorderLayout.CENTER);
        p.add(addBtn,                       BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildDetailPanel() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBorder(new TitledBorder("Информация об оружии"));
        infoArea = new JTextArea("Выберите оружие из списка слева");
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        infoArea.setBackground(new Color(248, 249, 250));
        infoArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoArea.setLineWrap(true);
        p.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        p.add(buildActionRow(),           BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildActionRow() {
        JPanel p = new JPanel(new GridLayout(1, 3, 6, 0));
        p.setBorder(new EmptyBorder(6, 0, 0, 0));
        attackBtn = styledButton("Атаковать", new Color(192, 57, 43), Color.WHITE);
        attackBtn.setEnabled(false);
        attackBtn.addActionListener(e -> performAttack());
        statusBtn = new JButton("Обновить статус");
        statusBtn.setEnabled(false);
        statusBtn.addActionListener(e -> refreshInfo());
        removeBtn = styledButton("Удалить", new Color(108, 117, 125), Color.WHITE);
        removeBtn.setEnabled(false);
        removeBtn.addActionListener(e -> removeSelected());
        p.add(attackBtn);
        p.add(statusBtn);
        p.add(removeBtn);
        return p;
    }

    private JPanel buildLogPanel() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBorder(new TitledBorder("Журнал атак"));
        p.setPreferredSize(new Dimension(0, 180));
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(new Color(80, 220, 100));
        logArea.setCaretColor(new Color(80, 220, 100));
        JButton clearBtn = new JButton("Очистить");
        clearBtn.addActionListener(e -> logArea.setText(""));
        p.add(new JScrollPane(logArea), BorderLayout.CENTER);
        p.add(clearBtn,                  BorderLayout.EAST);
        return p;
    }

    private void openAddDialog() {
        AddWeaponDialog dlg = new AddWeaponDialog(this, controller);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            refreshList();
            log("Добавлено оружие — " + dlg.getWeaponName());
        }
    }

    private void performAttack() {
        String result = controller.attack();
        log(result);
        refreshInfo();
    }

    private void removeSelected() {
        int idx = weaponList.getSelectedIndex();
        if (idx < 0) return;
        String name = controller.getWeapons().get(idx).getName();
        int answer = JOptionPane.showConfirmDialog(this,
                "Удалить оружие «" + name + "»?", "Подтверждение удаления",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            controller.removeWeapon(idx);
            refreshList();
            infoArea.setText("Выберите оружие из списка слева");
            setActionButtonsEnabled(false);
            log("Удалено оружие — " + name);
        }
    }

    private void refreshList() {
        int prev = weaponList.getSelectedIndex();
        listModel.clear();
        for (Weapon w : controller.getWeapons()) {
            String tag;
            if (w instanceof MeleeWeapon) {
                tag = "[Б] ";
            } else {
                tag = "[Д] ";
            }
            listModel.addElement(tag + w.getName());
        }
        if (prev >= 0 && prev < listModel.size()) {
            weaponList.setSelectedIndex(prev);
        }
    }

    private void refreshInfo() {
        Weapon w = controller.getSelectedWeapon();
        if (w == null) {
            infoArea.setText("Оружие не выбрано");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Название — ").append(w.getName()).append("\n\n");
        sb.append(w.getInfo()).append("\n\n");
        if (w.isBroken()) {
            sb.append("Состояние — СЛОМАНО");
        } else {
            sb.append("Состояние — Исправно");
        }
        infoArea.setText(sb.toString());
        infoArea.setCaretPosition(0);
    }

    private void setActionButtonsEnabled(boolean enabled) {
        attackBtn.setEnabled(enabled);
        statusBtn.setEnabled(enabled);
        removeBtn.setEnabled(enabled);
    }

    private void log(String message) {
        String time = LocalTime.now().format(TIME_FMT);
        logArea.append("[" + time + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private static JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    private static class WeaponCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String text = value.toString();
            if (!isSelected && text.contains("СЛОМАНО")) {
                setForeground(new Color(180, 50, 50));
            }
            return this;
        }
    }
}