import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import static java.lang.System.out;

public class Launcher {
	private JFrame frmNtopngGraphicalLauncher;
	private JTextField txtListenIP;
	private JTextField txtListenOnPort;
	private JTextField txtLocalNetworkList;
	private JTextField txtIPAddress;
	private JTextField txtPassword;
	private JTextField txtDatabaseID;
	private JTextField txtMaxFlows;
	private JTextField txtMaxHosts;
	private final ButtonGroup buttonGroupPromiscuousMode = new ButtonGroup();
	private final ButtonGroup buttonGroupDumpHosts = new ButtonGroup();
	private final ButtonGroup buttonGroupCaptureDirection = new ButtonGroup();
	private final ButtonGroup buttonGroupTaps = new ButtonGroup();
	private final ButtonGroup buttonGroupFlowActivity = new ButtonGroup();
	private final ButtonGroup buttonGroupOS = new ButtonGroup();
	private Enumeration<NetworkInterface> nets;
	WriteFile commands = new WriteFile("ntopng.conf", true);


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Launcher window = new Launcher();
					window.frmNtopngGraphicalLauncher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Launcher() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @return 
	 * @return 
	 */
	public void initialize() {
		frmNtopngGraphicalLauncher = new JFrame();
		frmNtopngGraphicalLauncher.setFont(new Font("Harrington", Font.PLAIN, 12));
		frmNtopngGraphicalLauncher.setTitle("NTOPNG Graphical Launcher");
		frmNtopngGraphicalLauncher.setBounds(100, 100, 800, 493);
		frmNtopngGraphicalLauncher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNtopngGraphicalLauncher.getContentPane().setLayout(null);

		JLabel lblDnsMode = new JLabel("DNS Mode");
		lblDnsMode.setBounds(10, 11, 69, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblDnsMode);

		String[] DNS = {"0","1","2","3"};		
		JLabel lbldecodeDnsResolve = new JLabel("0-Decode DNS Resolve Local IP    1-Decode DNS Resolve All IP     2-Decode DNS Only    3-Don't Decode or Resolve");
		lbldecodeDnsResolve.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lbldecodeDnsResolve.setBounds(211, 11, 563, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lbldecodeDnsResolve);
		JComboBox dnsBox = new JComboBox(DNS);
		dnsBox.setMaximumRowCount(4);
		dnsBox.setBounds(89, 8, 112, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(dnsBox);

		JLabel lblNetworkInterfacr = new JLabel("Network Interface");
		lblNetworkInterfacr.setBounds(10, 36, 133, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblNetworkInterfacr);

		JComboBox interfaceBox = new JComboBox();
		interfaceBox.addItem("ALL (DEFAULT)");
		try {
			nets = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        for (NetworkInterface netint : Collections.list(nets))
        {
            interfaceBox.addItem(netint.getName());
        }
		interfaceBox.setMaximumRowCount(50);
		interfaceBox.setBounds(153, 33, 112, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(interfaceBox);

		JLabel lblDataDirectory = new JLabel("Data Directory");
		lblDataDirectory.setBounds(10, 61, 107, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblDataDirectory);

		JButton btnDirectoryBrowse = new JButton("Browse");
		btnDirectoryBrowse.setBounds(127, 57, 89, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(btnDirectoryBrowse);

		JLabel lblPromiscuisMode = new JLabel("Promiscuous Mode");
		lblPromiscuisMode.setBounds(10, 86, 121, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblPromiscuisMode);

		JRadioButton rdbtnPMYes = new JRadioButton("Yes");
		rdbtnPMYes.setSelected(true);
		buttonGroupPromiscuousMode.add(rdbtnPMYes);
		rdbtnPMYes.setBounds(137, 82, 64, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnPMYes);

		JRadioButton rdbtnPMNo = new JRadioButton("No");
		buttonGroupPromiscuousMode.add(rdbtnPMNo);
		rdbtnPMNo.setBounds(210, 82, 49, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnPMNo);

		JLabel lblListenIP = new JLabel("Listen on Specific IP");
		lblListenIP.setBounds(10, 111, 133, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblListenIP);

		JTextField txtListenIP = new JTextField();
		txtListenIP.setBounds(136, 108, 129, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtListenIP);
		txtListenIP.setColumns(10);

		JLabel lblListenPort = new JLabel("Listen on Specific Port");
		lblListenPort.setBounds(314, 111, 137, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblListenPort);

		txtListenOnPort = new JTextField();
		txtListenOnPort.setBounds(461, 108, 86, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtListenOnPort);
		txtListenOnPort.setColumns(10);

		JLabel lblLocalNetworkLists = new JLabel("Local Network Lists");
		lblLocalNetworkLists.setBounds(10, 136, 133, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblLocalNetworkLists);

		txtLocalNetworkList = new JTextField();
		txtLocalNetworkList.setBounds(137, 133, 479, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtLocalNetworkList);
		txtLocalNetworkList.setColumns(10);

		JLabel lblSpecifyNdpiProtocols = new JLabel("Specify NDPI Protocol");
		lblSpecifyNdpiProtocols.setBounds(10, 161, 151, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblSpecifyNdpiProtocols);

		JButton btnNdpiBrowse = new JButton("Browse");
		btnNdpiBrowse.setBounds(153, 157, 89, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(btnNdpiBrowse);

		JLabel lblRedisDetails = new JLabel("Redis Server Details:");
		lblRedisDetails.setBounds(10, 186, 133, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblRedisDetails);

		JLabel lblIPAddress = new JLabel("IP Address");
		lblIPAddress.setBounds(137, 186, 89, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblIPAddress);

		txtIPAddress = new JTextField();
		txtIPAddress.setBounds(209, 183, 129, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtIPAddress);
		txtIPAddress.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(348, 186, 69, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblPassword);

		txtPassword = new JTextField();
		txtPassword.setBounds(418, 183, 129, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtPassword);
		txtPassword.setColumns(10);

		JLabel lblDatabaseId = new JLabel("Database ID");
		lblDatabaseId.setBounds(565, 186, 89, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblDatabaseId);

		txtDatabaseID = new JTextField();
		txtDatabaseID.setColumns(10);
		txtDatabaseID.setBounds(645, 183, 129, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtDatabaseID);

		JLabel lblDisableLoginFor = new JLabel("Disable Login For:");
		lblDisableLoginFor.setBounds(10, 211, 121, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblDisableLoginFor);

		JCheckBox chckbxLocalHost = new JCheckBox("Local Host");
		chckbxLocalHost.setBounds(141, 207, 101, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(chckbxLocalHost);

		JCheckBox chckbxAllHosts = new JCheckBox("All Hosts");
		chckbxAllHosts.setBounds(241, 207, 97, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(chckbxAllHosts);

		JLabel lblMemoryManagement = new JLabel("Memory Management:");
		lblMemoryManagement.setBounds(10, 236, 133, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblMemoryManagement);

		JLabel lblMaxFlows = new JLabel("Maximum Number of Flows");
		lblMaxFlows.setBounds(155, 237, 164, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblMaxFlows);

		txtMaxFlows = new JTextField();
		txtMaxFlows.setBounds(323, 233, 112, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtMaxFlows);
		txtMaxFlows.setColumns(10);

		JLabel lblMaxHosts = new JLabel("Maximum Number of Hosts");
		lblMaxHosts.setBounds(445, 236, 155, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblMaxHosts);

		txtMaxHosts = new JTextField();
		txtMaxHosts.setBounds(610, 233, 112, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtMaxHosts);
		txtMaxHosts.setColumns(10);

		JLabel lblDumpHosts = new JLabel("Dump Hosts ");
		lblDumpHosts.setBounds(10, 261, 79, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblDumpHosts);

		JRadioButton rdbtnAll = new JRadioButton("All");
		buttonGroupDumpHosts.add(rdbtnAll);
		rdbtnAll.setBounds(89, 257, 54, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnAll);

		JRadioButton rdbtnLocal = new JRadioButton("Local");
		buttonGroupDumpHosts.add(rdbtnLocal);
		rdbtnLocal.setBounds(153, 258, 63, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnLocal);

		JRadioButton rdbtnRemote = new JRadioButton("Remote");
		buttonGroupDumpHosts.add(rdbtnRemote);
		rdbtnRemote.setBounds(228, 258, 79, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnRemote);

		JRadioButton rdbtnNone = new JRadioButton("None");
		buttonGroupDumpHosts.add(rdbtnNone);
		rdbtnNone.setSelected(true);
		rdbtnNone.setBounds(310, 258, 79, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnNone);

		JLabel lblCaptureDirection = new JLabel("Capture Direction");
		lblCaptureDirection.setBounds(10, 286, 121, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblCaptureDirection);

		JRadioButton rdbtnBoth = new JRadioButton("Both");
		buttonGroupCaptureDirection.add(rdbtnBoth);
		rdbtnBoth.setSelected(true);
		rdbtnBoth.setBounds(127, 282, 74, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnBoth);

		JRadioButton rdbtnReceiveOnly = new JRadioButton("Receive Only");
		buttonGroupCaptureDirection.add(rdbtnReceiveOnly);
		rdbtnReceiveOnly.setBounds(201, 282, 106, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnReceiveOnly);

		JRadioButton rdbtnTransmitOnly = new JRadioButton("Transmit Only");
		buttonGroupCaptureDirection.add(rdbtnTransmitOnly);
		rdbtnTransmitOnly.setBounds(308, 282, 127, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnTransmitOnly);

		JLabel lblTaps = new JLabel("Enable Taps");
		lblTaps.setBounds(10, 311, 79, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblTaps);

		JRadioButton rdbtnTapsYes = new JRadioButton("Yes");
		buttonGroupTaps.add(rdbtnTapsYes);
		rdbtnTapsYes.setBounds(92, 307, 69, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnTapsYes);

		JRadioButton rdbtnTapsNo = new JRadioButton("No");
		rdbtnTapsNo.setSelected(true);
		buttonGroupTaps.add(rdbtnTapsNo);
		rdbtnTapsNo.setBounds(160, 307, 56, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnTapsNo);

		JLabel lblFlowActivity = new JLabel("Enable Flow Activity");
		lblFlowActivity.setBounds(10, 336, 121, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblFlowActivity);

		JRadioButton rdbtnFlowActivityYes = new JRadioButton("Yes");
		buttonGroupFlowActivity.add(rdbtnFlowActivityYes);
		rdbtnFlowActivityYes.setBounds(127, 332, 53, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnFlowActivityYes);

		JRadioButton rdbtnFlowActivityNo = new JRadioButton("No");
		rdbtnFlowActivityNo.setSelected(true);
		buttonGroupFlowActivity.add(rdbtnFlowActivityNo);
		rdbtnFlowActivityNo.setBounds(183, 332, 59, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnFlowActivityNo);

		JLabel lblOperatingSystem = new JLabel("Operating System:");
		lblOperatingSystem.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblOperatingSystem.setBounds(163, 358, 162, 19);
		frmNtopngGraphicalLauncher.getContentPane().add(lblOperatingSystem);

		JRadioButton rdbtnLinux = new JRadioButton("Linux");
		rdbtnLinux.setSelected(true);
		buttonGroupOS.add(rdbtnLinux);
		rdbtnLinux.setFont(new Font("Tahoma", Font.BOLD, 16));
		rdbtnLinux.setBounds(332, 358, 109, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnLinux);

		JRadioButton rdbtnWindows = new JRadioButton("Windows");
		buttonGroupOS.add(rdbtnWindows);
		rdbtnWindows.setFont(new Font("Tahoma", Font.BOLD, 16));
		rdbtnWindows.setBounds(443, 358, 129, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnWindows);

		JButton btnLaunchNtopng = new JButton("Launch NTOPNG");
		btnLaunchNtopng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WriteFile clear = new WriteFile("ntopng.conf");
				try {
					clear.writetoFile("#NTOPNG Configuration File");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					setDNSMode(String.valueOf(dnsBox.getSelectedItem()));
					setInterface(String.valueOf(interfaceBox.getSelectedItem()));
					setPromiscuousMode(rdbtnPMNo.isSelected());
					setListenIPPort(txtListenIP.getText(), txtListenOnPort.getText());
					setLocalNetworkList(txtLocalNetworkList.getText());
					setMaxFlows(txtMaxFlows.getText());
					setMaxHosts(txtMaxHosts.getText());
					setCaptureDirection(rdbtnTransmitOnly.isSelected(), rdbtnReceiveOnly.isSelected());
					setEnableTaps(rdbtnTapsYes.isSelected());
					setFlowActivity(rdbtnFlowActivityYes.isSelected());
					if(rdbtnLinux.isSelected())
					{
						runOnLinux();
					}else{runOnWindows();}
					




				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLaunchNtopng.setFont(new Font("Tahoma", Font.ITALIC, 18));
		btnLaunchNtopng.setBounds(300, 400, 200, 30);
		frmNtopngGraphicalLauncher.getContentPane().add(btnLaunchNtopng);

		JLabel lblseperateIpsWith = new JLabel("(Seperate IPs with a Comma)");
		lblseperateIpsWith.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblseperateIpsWith.setBounds(626, 136, 148, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblseperateIpsWith);
	}

	public void setDNSMode(String DNSMode) throws IOException
	{
		commands.writetoFile("--dns-mode=" + DNSMode);
	}
	
	public void setInterface(String networkInterface) throws IOException
	{
		if(networkInterface!="ALL (DEFAULT)"){
			commands.writetoFile("--interface=" + networkInterface);
		}
	}
	
	public void setPromiscuousMode(boolean nopromiscuous) throws IOException
	{
		if(nopromiscuous==true){
			commands.writetoFile("--no-promisc");
		}
	}

	public void setListenIPPort(String IP, String Port) throws IOException
	{
		if(IP.length()!=0 && Port.length()!=0){
			commands.writetoFile("--http-port=" + IP + ":" + Port);
		} else if(IP.length()==0 && Port.length()!=0){
			commands.writetoFile("--http-port=:" + Port);
		} else if(IP.length()!=0 && Port.length()==0){
			commands.writetoFile("--http-port=" + IP);
		}
	}
	
	public void setLocalNetworkList(String LocalNetworks) throws IOException
	{
		if(LocalNetworks.length()!=0){
			commands.writetoFile("--local-networks=" + LocalNetworks);
		}
	}
	
	public void setMaxFlows(String Flows) throws IOException
	{
		if(Flows.length()!=0){
			commands.writetoFile("--max-flows=" + Flows);
		}
	}
	
	public void setMaxHosts(String Hosts) throws IOException
	{
		if(Hosts.length()!=0){
			commands.writetoFile("--max-hosts=" + Hosts);
		}
	}
	
	public void setCaptureDirection(boolean TXonly, boolean RXonly) throws IOException
	{
		if(TXonly==true){
			commands.writetoFile("--capture-direction=2");
		}		
		if(RXonly==true){
			commands.writetoFile("--capture-direction=1");
		}
		
	}
	
	public void setEnableTaps(boolean enableTaps) throws IOException
	{
		if(enableTaps==true)
		{
			commands.writetoFile("--enable-taps");
		}
	}
	
	public void setFlowActivity(boolean flowActivity) throws IOException
	{
		if(flowActivity==true)
		{
			commands.writetoFile("--enable-flow-activity");
		}
	}
	
	public void runOnLinux() throws IOException
	{
		Path currentRelativePath = Paths.get("");
		String fileDirectory = currentRelativePath.toAbsolutePath().toString();
		//Process proc = Runtime.getRuntime().exec("ntopng " + fileDirectory + "ntopng.conf");
		System.out.println("ntopng " + fileDirectory + "/ntopng.conf");
	}
	
	public void runOnWindows()
	{
		
	}
}
