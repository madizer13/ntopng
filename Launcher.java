import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.net.URI;;

public class Launcher {
	private JFrame frmNtopngGraphicalLauncher;
	private JTextField txtListenIP;
	private JTextField txtListenOnPort;
	private JTextField txtLocalNetworkList;
	private JTextField txtMaxFlows;
	private JTextField txtMaxHosts;
	private String pcapAddress = null;
	private String dpiAddress = null;
	private final ButtonGroup buttonGroupInterface = new ButtonGroup();
	private final ButtonGroup buttonGroupPromiscuousMode = new ButtonGroup();
	private final ButtonGroup buttonGroupDumpHosts = new ButtonGroup();
	private final ButtonGroup buttonGroupCaptureDirection = new ButtonGroup();
	private final ButtonGroup buttonGroupTaps = new ButtonGroup();
	private final ButtonGroup buttonGroupFlowActivity = new ButtonGroup();
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
		try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception e) {
            e.printStackTrace();
        }
		frmNtopngGraphicalLauncher = new JFrame();
		frmNtopngGraphicalLauncher.setFont(new Font("Harrington", Font.PLAIN, 12));
		frmNtopngGraphicalLauncher.setTitle("NTOPNG Graphical Launcher");
		frmNtopngGraphicalLauncher.setBounds(100, 100, 800, 493);
		frmNtopngGraphicalLauncher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNtopngGraphicalLauncher.getContentPane().setLayout(null);
		
		final JRadioButton rdbtnAnalyseNetworkInterface = new JRadioButton("Analyse Network Interface");
		rdbtnAnalyseNetworkInterface.setBounds(38, 16, 200, 23);
		buttonGroupInterface.add(rdbtnAnalyseNetworkInterface);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnAnalyseNetworkInterface);
		
		final JComboBox<String> interfaceBox = new JComboBox<String>();
		interfaceBox.setBounds(265, 17, 140, 20);
		interfaceBox.addItem("ALL (DEFAULT)");
		interfaceBox.setMaximumRowCount(50);
		frmNtopngGraphicalLauncher.getContentPane().add(interfaceBox);
		
		JLabel lblOr = new JLabel("OR");
		lblOr.setBounds(6, 33, 34, 14);
		lblOr.setFont(new Font("Tahoma", Font.PLAIN, 16));
		frmNtopngGraphicalLauncher.getContentPane().add(lblOr);
		
		final JRadioButton rdbtnAnalysePCAP = new JRadioButton("Analyse already captured PCAP");
		rdbtnAnalysePCAP.setBounds(38, 48, 223, 23);
		buttonGroupInterface.add(rdbtnAnalysePCAP);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnAnalysePCAP);
		
		JButton btnBrowsePcap = new JButton("Browse PCAP");
		btnBrowsePcap.setBounds(265, 48, 140, 23);
		btnBrowsePcap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser pcapDirectory = new JFileChooser();
				int returnVal = pcapDirectory.showDialog(frmNtopngGraphicalLauncher, "Choose");
				if (returnVal == pcapDirectory.APPROVE_OPTION) {
				    File selectedFile = pcapDirectory.getSelectedFile();
				    pcapAddress = selectedFile.getAbsolutePath();
				}
			}
		});
		frmNtopngGraphicalLauncher.getContentPane().add(btnBrowsePcap);

		JLabel lblDnsMode = new JLabel("DNS Mode");
		lblDnsMode.setBounds(10, 83, 69, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblDnsMode);

		String[] DNS = {"0","1","2","3"};		
		final JComboBox dnsBox = new JComboBox(DNS);
		dnsBox.setBounds(89, 80, 112, 20);
		dnsBox.setMaximumRowCount(4);
		frmNtopngGraphicalLauncher.getContentPane().add(dnsBox);
		JLabel lbldecodeDnsResolve = new JLabel("0-Decode DNS Resolve Local IP    1-Decode DNS Resolve All IP     2-Decode DNS Only    3-Don't Decode or Resolve");
		lbldecodeDnsResolve.setBounds(211, 83, 563, 14);
		lbldecodeDnsResolve.setFont(new Font("Tahoma", Font.PLAIN, 9));
		frmNtopngGraphicalLauncher.getContentPane().add(lbldecodeDnsResolve);
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

		JLabel lblPromiscuisMode = new JLabel("Promiscuous Mode");
		lblPromiscuisMode.setBounds(10, 109, 121, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblPromiscuisMode);

		JRadioButton rdbtnPMYes = new JRadioButton("Yes");
		rdbtnPMYes.setBounds(137, 105, 64, 23);
		rdbtnPMYes.setSelected(true);
		buttonGroupPromiscuousMode.add(rdbtnPMYes);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnPMYes);

		final JRadioButton rdbtnPMNo = new JRadioButton("No");
		rdbtnPMNo.setBounds(210, 105, 49, 23);
		buttonGroupPromiscuousMode.add(rdbtnPMNo);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnPMNo);

		JLabel lblListenIP = new JLabel("Listen for Specific IP");
		lblListenIP.setBounds(10, 137, 133, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblListenIP);

		final JTextField txtListenIP = new JTextField();
		txtListenIP.setBounds(136, 134, 129, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtListenIP);
		txtListenIP.setColumns(10);

		JLabel lblListenPort = new JLabel("Listen on Specific Port");
		lblListenPort.setBounds(314, 137, 137, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblListenPort);

		txtListenOnPort = new JTextField();
		txtListenOnPort.setBounds(461, 134, 86, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtListenOnPort);
		txtListenOnPort.setColumns(10);

		JLabel lblLocalNetworkLists = new JLabel("Local Network Lists");
		lblLocalNetworkLists.setBounds(10, 172, 133, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblLocalNetworkLists);

		txtLocalNetworkList = new JTextField();
		txtLocalNetworkList.setBounds(137, 169, 479, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtLocalNetworkList);
		txtLocalNetworkList.setColumns(10);
		
		JLabel lblseperateIpsWith = new JLabel("(Seperate IPs with a Comma)");
		lblseperateIpsWith.setBounds(626, 172, 148, 14);
		lblseperateIpsWith.setFont(new Font("Tahoma", Font.PLAIN, 9));
		frmNtopngGraphicalLauncher.getContentPane().add(lblseperateIpsWith);

		JLabel lblSpecifyNdpiProtocols = new JLabel("Specify NDPI Protocol");
		lblSpecifyNdpiProtocols.setBounds(10, 208, 151, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblSpecifyNdpiProtocols);

		JButton btnNdpiBrowse = new JButton("Browse");
		btnNdpiBrowse.setBounds(153, 204, 89, 23);
		btnNdpiBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser dpiProtocol = new JFileChooser();
				int returnVal = dpiProtocol.showDialog(frmNtopngGraphicalLauncher, "Choose");
				if (returnVal == dpiProtocol.APPROVE_OPTION) {
				    File selectedFile = dpiProtocol.getSelectedFile();
				    dpiAddress = selectedFile.getAbsolutePath();
				}
			}
		});
		frmNtopngGraphicalLauncher.getContentPane().add(btnNdpiBrowse);

		JLabel lblDisableLoginFor = new JLabel("Disable Login For:");
		lblDisableLoginFor.setBounds(10, 238, 121, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblDisableLoginFor);

		JCheckBox chckbxLocalHost = new JCheckBox("Local Host");
		chckbxLocalHost.setBounds(125, 234, 101, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(chckbxLocalHost);

		JCheckBox chckbxAllHosts = new JCheckBox("All Hosts");
		chckbxAllHosts.setBounds(241, 234, 97, 23);
		frmNtopngGraphicalLauncher.getContentPane().add(chckbxAllHosts);

		JLabel lblMemoryManagement = new JLabel("Memory Management:");
		lblMemoryManagement.setBounds(10, 266, 133, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblMemoryManagement);

		JLabel lblMaxFlows = new JLabel("Maximum Number of Flows");
		lblMaxFlows.setBounds(155, 267, 164, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblMaxFlows);

		txtMaxFlows = new JTextField();
		txtMaxFlows.setBounds(323, 263, 112, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtMaxFlows);
		txtMaxFlows.setColumns(10);

		JLabel lblMaxHosts = new JLabel("Maximum Number of Hosts");
		lblMaxHosts.setBounds(445, 266, 155, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblMaxHosts);

		txtMaxHosts = new JTextField();
		txtMaxHosts.setBounds(610, 263, 112, 20);
		frmNtopngGraphicalLauncher.getContentPane().add(txtMaxHosts);
		txtMaxHosts.setColumns(10);

		JLabel lblDumpHosts = new JLabel("Dump Hosts ");
		lblDumpHosts.setBounds(10, 291, 79, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblDumpHosts);

		JRadioButton rdbtnAll = new JRadioButton("All");
		rdbtnAll.setBounds(89, 287, 54, 23);
		buttonGroupDumpHosts.add(rdbtnAll);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnAll);

		JRadioButton rdbtnLocal = new JRadioButton("Local");
		rdbtnLocal.setBounds(153, 288, 63, 23);
		buttonGroupDumpHosts.add(rdbtnLocal);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnLocal);

		JRadioButton rdbtnRemote = new JRadioButton("Remote");
		rdbtnRemote.setBounds(228, 288, 79, 23);
		buttonGroupDumpHosts.add(rdbtnRemote);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnRemote);

		JRadioButton rdbtnNone = new JRadioButton("None");
		rdbtnNone.setBounds(310, 288, 79, 23);
		buttonGroupDumpHosts.add(rdbtnNone);
		rdbtnNone.setSelected(true);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnNone);

		JLabel lblCaptureDirection = new JLabel("Capture Direction");
		lblCaptureDirection.setBounds(10, 316, 121, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblCaptureDirection);

		JRadioButton rdbtnBoth = new JRadioButton("Both");
		rdbtnBoth.setBounds(127, 312, 74, 23);
		buttonGroupCaptureDirection.add(rdbtnBoth);
		rdbtnBoth.setSelected(true);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnBoth);

		final JRadioButton rdbtnReceiveOnly = new JRadioButton("Receive Only");
		rdbtnReceiveOnly.setBounds(201, 312, 106, 23);
		buttonGroupCaptureDirection.add(rdbtnReceiveOnly);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnReceiveOnly);

		final JRadioButton rdbtnTransmitOnly = new JRadioButton("Transmit Only");
		rdbtnTransmitOnly.setBounds(308, 312, 127, 23);
		buttonGroupCaptureDirection.add(rdbtnTransmitOnly);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnTransmitOnly);

		JLabel lblTaps = new JLabel("Enable Taps");
		lblTaps.setBounds(10, 341, 79, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblTaps);

		final JRadioButton rdbtnTapsYes = new JRadioButton("Yes");
		rdbtnTapsYes.setBounds(92, 337, 69, 23);
		buttonGroupTaps.add(rdbtnTapsYes);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnTapsYes);

		JRadioButton rdbtnTapsNo = new JRadioButton("No");
		rdbtnTapsNo.setBounds(160, 337, 56, 23);
		rdbtnTapsNo.setSelected(true);
		buttonGroupTaps.add(rdbtnTapsNo);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnTapsNo);

		JLabel lblFlowActivity = new JLabel("Enable Flow Activity");
		lblFlowActivity.setBounds(10, 369, 121, 14);
		frmNtopngGraphicalLauncher.getContentPane().add(lblFlowActivity);

		final JRadioButton rdbtnFlowActivityYes = new JRadioButton("Yes");
		rdbtnFlowActivityYes.setBounds(127, 365, 53, 23);
		buttonGroupFlowActivity.add(rdbtnFlowActivityYes);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnFlowActivityYes);

		JRadioButton rdbtnFlowActivityNo = new JRadioButton("No");
		rdbtnFlowActivityNo.setBounds(183, 365, 59, 23);
		rdbtnFlowActivityNo.setSelected(true);
		buttonGroupFlowActivity.add(rdbtnFlowActivityNo);
		frmNtopngGraphicalLauncher.getContentPane().add(rdbtnFlowActivityNo);

		JButton btnLaunchNtopng = new JButton("Launch NTOPNG");
		btnLaunchNtopng.setBounds(137, 400, 200, 30);
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
					if (rdbtnAnalyseNetworkInterface.isSelected()==true)
					{
						setInterface(String.valueOf(interfaceBox.getSelectedItem()));
					}else if(rdbtnAnalysePCAP.isSelected()==true)
					{
						setInterface(pcapAddress);
					}					
					setDNSMode(String.valueOf(dnsBox.getSelectedItem()));
					setPromiscuousMode(rdbtnPMNo.isSelected());
					setnDPIProtocols(dpiAddress);
					setListenIPPort(txtListenIP.getText(), txtListenOnPort.getText());
					setLocalNetworkList(txtLocalNetworkList.getText());
					setMaxFlows(txtMaxFlows.getText());
					setMaxHosts(txtMaxHosts.getText());
					setCaptureDirection(rdbtnTransmitOnly.isSelected(), rdbtnReceiveOnly.isSelected());
					setEnableTaps(rdbtnTapsYes.isSelected());
					setFlowActivity(rdbtnFlowActivityYes.isSelected());
					runNTOP();	
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLaunchNtopng.setFont(new Font("Tahoma", Font.ITALIC, 18));
		frmNtopngGraphicalLauncher.getContentPane().add(btnLaunchNtopng);
		
		JButton btnNewButton = new JButton("Stop NTOPNG");
		btnNewButton.setFont(new Font("Tahoma", Font.ITALIC, 18));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					stopNTOP();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(442, 399, 201, 31);
		frmNtopngGraphicalLauncher.getContentPane().add(btnNewButton);
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
	
	public void setnDPIProtocols(String protocol) throws IOException
	{
		if(protocol != null){
			commands.writetoFile("--ndpi-protocols=" + protocol);
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
	
	public void runNTOP() throws IOException, URISyntaxException
	{
		Path currentRelativePath = Paths.get("");
		String fileDirectory = currentRelativePath.toAbsolutePath().toString();
		Process proc = Runtime.getRuntime().exec("ntopng " + fileDirectory + "ntopng.conf");
		Desktop.getDesktop().browse(new  URI("http://127.0.0.1:3000/"));
	}
	
	public void stopNTOP() throws IOException
	{
		Process proc = Runtime.getRuntime().exec("ntopng stop");
	}
}
