import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*; 


public class Manager extends Applet
implements ActionListener
{
	TextField Naam,EMail, Groep_Kiezen;
	Button Aanmelden, GroepAanmaken, lootjes;
	String Naam_, EMail_, Groep_,KiesGroep;
	boolean GROEP, Mensen, Gelukt;
	JComboBox Groepen;
	Mens[] mensen;

	public void init()
	{
		setLayout( null );
		Naam=new TextField ("Naam");
		Naam.addActionListener( this );
		Naam.setBounds(10, 100, 175, 20 );
		EMail=new TextField ("E-Mail");
		EMail.addActionListener( this );
		EMail.setBounds(10, 125, 175, 20 );
		Groep_Kiezen=new TextField ("");
		Groep_Kiezen.addActionListener( this );
		Groep_Kiezen.setBounds(10, 35, 175, 20 );

		Aanmelden=new Button("Aanmelden");
		Aanmelden.addActionListener( this );
		Aanmelden.setBounds(50, 150, 80, 20 );
		GroepAanmaken=new Button("Groep aanmaken");
		GroepAanmaken.addActionListener( this );
		GroepAanmaken.setBounds(40, 65, 100, 20 );
		lootjes=new Button("lootjes trekken");
		lootjes.addActionListener( this );
		lootjes.setBounds(40, 185, 100, 20 );


		Groepen = new JComboBox();
		Groepen.setEditable(true);
		Groepen.setBounds(10, 10, 175, 20);
		Groepen.addItem("kies een groep");
		Leesgroepen();

		add(Naam);
		add(EMail);
		add(Aanmelden);
		add(GroepAanmaken);
		add(lootjes);
		add(Groepen);

		GROEP=false;
		Mensen=false;
		Gelukt=true;
		
		mensen = new Mens[50];
	}
	public void actionPerformed( ActionEvent e)
	{
		if(e.getSource()==GroepAanmaken)
		{
			if(!GROEP&&!Mensen)
			{
				Groep_Kiezen.setText("groepsnaam");
				add(Groep_Kiezen);
				GROEP=true;
			}
			else
			{
				if(!Mensen)
				{
					Groep_=Groep_Kiezen.getText();
					remove(Groep_Kiezen);
					add(Naam);
					add(EMail);
					Groepen.addItem(Groep_);
					if(Groep_.equals("groepsnaam"));
					{
					Schrijfgroep(Groep_);
					}
					GROEP=false;
				}
			}

		}
		if(e.getSource()==Aanmelden)
		{
			Groep_=Groepen.getSelectedItem().toString();
			if(!Groep_.contentEquals("kies een groep"))
			{
				Naam_=Naam.getText();
				EMail_=EMail.getText();
				Schrijfmens(Naam_, EMail_, Groep_);
				Naam.setText("");
				EMail.setText("");
			}
			else
			{
				error();
			}
			
		}
		if(e.getSource()==lootjes)
		{
			Groep_=Groepen.getSelectedItem().toString();
			if(!Groep_.contentEquals("kies een groep"))
			{
				Leesmensen(Groep_);
			}
			else
			{
				error();
			}
		}
	}
	public void Schrijfgroep(String regeltje)
	{
		try { 
			BufferedWriter out = new BufferedWriter(new FileWriter("groepdata.txt", true)); 
			out.write(regeltje); 
			out.newLine();
			out.close(); 
		} catch (IOException e) { 
		} 
	}

	public void Leesgroepen()
	{
		try { 
			BufferedReader in = new BufferedReader(new FileReader("groepdata.txt"));
			String str;
			while ((str = in.readLine()) != null) {
				Groepen.addItem(str);
			}
			in.close();
		} catch (IOException e) {
		}
	} 

	public void Schrijfmens(String naam,String email,String groep)
	{
		try { 
			BufferedWriter out = new BufferedWriter(new FileWriter("mensdata.txt", true)); 
			out.write(groep);
			out.write(","); 
			out.write(naam); 
			out.write(","); 
			out.write(email); 
			out.newLine();
			out.close(); 
		} catch (IOException e) { 
		} 
	}

	public void Leesmensen(String groep)
	{
		int nummer = 0;
		Random generator = new Random(System.currentTimeMillis() );

		try { 
			BufferedReader in = new BufferedReader(new FileReader("mensdata.txt"));
			String str;
			String gesplitste_velden[];

			while ((str = in.readLine()) != null) {
				gesplitste_velden = str.split(",");
				if (groep.equals(gesplitste_velden[0])){
					mensen[nummer]=new Mens();
					mensen[nummer].groep = gesplitste_velden[0];
					mensen[nummer].naam = gesplitste_velden[1];
					mensen[nummer].email = gesplitste_velden[2];
					nummer = nummer+1;
				}
			}
			in.close();

		} catch (IOException e) {
		}
		if (nummer>2) {
			for(int i=nummer-1;i>=0;i--)
			{
				int naam1 = generator.nextInt( nummer );
				if  ( (naam1!=i) &&
						(mensen[naam1].is_getrokken_door==-1) &&
						((nummer<=2) || (mensen[i].is_getrokken_door != naam1))
				)
				{
					mensen[naam1].is_getrokken_door=i;
					mensen[i].heeft_getrokken=naam1;
				}
				else
				{
					i++;
				}
			}
			String[] recipients = new String[1];
			for(int i=nummer-1;i>=0;i--)
			{
				recipients[0] = mensen[i].email;
				String message = "Lieve "+mensen[i].naam+",\n\nJe hebt "+ mensen[mensen[i].heeft_getrokken].naam +
				" getrokken om een surprise voor te maken voor Sinterklaas. \nen is er nog geen nieuwe update van het lootjes-programma en website.\n\nGroetjes van de lootjestrek-fabrikant.\nPowered by Adventure-Industries.";           
				//String message = mensen[i].naam+" heeft "+ mensen[mensen[i].heeft_getrokken].naam + " getrokken.";
				try {
					postMail(recipients, "Dit is je lootje voor Sinterklaas!", message,"noreply@ziggo.nl" );
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("klaar");
		} else {
			JOptionPane.showMessageDialog(null, "Geen lootjes getrokken. Er moeten minstens 3 deelnemers zijn.");

		}
	} 


	public void error()
	{		
		JOptionPane.showMessageDialog(null, "Kies een aangemaakte groep!");
	}

	public void postMail( String recipients[ ], String subject, String message , String from) throws MessagingException
	{
	    boolean debug = false;

	     //Set the host smtp address
	     Properties props = new Properties();
	     props.put("mail.smtp.host", "smtp.ziggo.nl");

	    // create some properties and get the default Session
	    Session session = Session.getDefaultInstance(props, null);
	    session.setDebug(debug);

	    // create a message
	    Message msg = new MimeMessage(session);

	    // set the from and to address
	    InternetAddress addressFrom = new InternetAddress(from);
	    msg.setFrom(addressFrom);

	    InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
	    for (int i = 0; i < recipients.length; i++)
	    {
	        addressTo[i] = new InternetAddress(recipients[i]);
	    }
	    msg.setRecipients(Message.RecipientType.TO, addressTo);
	   

	    // Optional : You can also set your custom headers in the Email if you Want
	    // msg.addHeader("MyHeaderName", "myHeaderValue");

	    // Setting the Subject and Content Type
	    msg.setSubject(subject);
	    msg.setContent(message, "text/plain");
	    Transport.send(msg);
	}

}

