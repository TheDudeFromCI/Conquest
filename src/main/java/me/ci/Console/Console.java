package me.ci.Console;

import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.GridLayout;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.border.BevelBorder;

import me.ci.Community.Save;

@SuppressWarnings("serial")
public class Console extends JFrame
{
	private static HashMap<LogType, ArrayList<Log>> logs = new HashMap<>();

	public Console()
	{
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit()
							.getImage(Console.class.getResource("/Spiral Logo.png")));
		setTitle("Wraithaven Console");
		setSize(500, 500);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Kingdoms", null, panel, null);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Server", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6, BorderLayout.NORTH);
		panel_6.setLayout(new GridLayout(0, 3, 0, 0));

		JLabel lblWhitelist = new JLabel("Whitelist");
		panel_6.add(lblWhitelist);

		JRadioButton rdbtnTrue = new JRadioButton("True");
		panel_6.add(rdbtnTrue);

		JRadioButton rdbtnFalse = new JRadioButton("False");
		panel_6.add(rdbtnFalse);

		JLabel lblMaxPlayers = new JLabel("Max Players");
		panel_6.add(lblMaxPlayers);

		JSpinner spinner = new JSpinner();
		panel_6.add(spinner);

		JLabel label = new JLabel("");
		panel_6.add(label);

		JLabel lblRestartTime = new JLabel("Restart Time");
		panel_6.add(lblRestartTime);

		JSpinner spinner_1 = new JSpinner();
		panel_6.add(spinner_1);

		JLabel label_1 = new JLabel("");
		panel_6.add(label_1);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Players", null, panel_2, null);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Wiki", null, panel_3, null);

		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Events", null, panel_4, null);

		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Traffic Log", null, panel_5, null);
		panel_5.setLayout(new BorderLayout(0, 0));

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_5.add(panel_8, BorderLayout.WEST);
		panel_8.setLayout(new GridLayout(0, 1, 0, 0));

		JCheckBox chckbxLogins = new JCheckBox("Logins");
		panel_8.add(chckbxLogins);

		JCheckBox chckbxLoginAttempts = new JCheckBox("Login Attempts");
		panel_8.add(chckbxLoginAttempts);

		JCheckBox chckbxNewPlayers = new JCheckBox("New Players");
		panel_8.add(chckbxNewPlayers);

		JCheckBox chckbxPlayTime = new JCheckBox("Play Time");
		panel_8.add(chckbxPlayTime);

		JSeparator separator = new JSeparator();
		panel_8.add(separator);

		JCheckBox chckbxDonations = new JCheckBox("Donations");
		panel_8.add(chckbxDonations);

		JCheckBox chckbxDonationAmount = new JCheckBox("Donation Amount");
		panel_8.add(chckbxDonationAmount);

		JSeparator separator_1 = new JSeparator();
		panel_8.add(separator_1);

		JCheckBox chckbxKingdomCreations = new JCheckBox("Kingdom Creations");
		panel_8.add(chckbxKingdomCreations);

		JCheckBox chckbxKingdomDestructions = new JCheckBox("Kingdom Destructions");
		panel_8.add(chckbxKingdomDestructions);

		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_5.add(panel_9, BorderLayout.SOUTH);
		panel_9.setLayout(new GridLayout(0, 1, 0, 0));

		JTextArea textPane = new JTextArea();
		textPane.setBackground(new Color(255, 255, 255));
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textPane.setWrapStyleWord(true);
		textPane.setLineWrap(true);
		textPane.setRows(7);
		textPane.setTabSize(4);
		textPane.setEditable(false);
		panel_9.add(textPane);

		JPanel panel_10 = new JPanel();
		panel_5.add(panel_10, BorderLayout.CENTER);

		JPanel panel_7 = new JPanel();
		tabbedPane.addTab("Help Videos", null, panel_7, null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmReloadConfig = new JMenuItem("Reload Config");
		mnFile.add(mntmReloadConfig);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmHelp = new JMenuItem("Help (?)");
		mnHelp.add(mntmHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		setVisible(true);
	}

	public static void log(final LogType type, final Log log)
	{
		final ArrayList<Log> l;
		if (logs.containsKey(type))
			l = logs.get(type);
		else
			l = new ArrayList<>();
		l.add(log);
		logs.put(type, l);
	}

	public static void load()
	{
		final Map<String, String> paths = Save.getAllPaths("Resources", "Log");
		LogType type;
		String[] s1;
		for (String s : paths.keySet())
		{
			s1 = s.split("-");
			type = LogType.getById(Integer.valueOf(s1[0]));
			log(type, Log.getLogByType(type, paths.get(s)));
		}
	}

	public static void save()
	{
		for (LogType type : logs.keySet())
		{
			for (Log l : logs.get(type))
			{
				Save.set("Resources", "Log", type.getId() + "-" + logs	.get(type)
																		.indexOf(l),
						l.save());
			}
		}
	}
}
