package src.main.java;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.myproject.mymodel.domain.Scratch;
import service.InAndOutHandle;
import service.impl.InAndOutHandleImpl;
import src.main.resources.GlobalControl;
import src.main.resources.GlobalControlImpl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MyFrame extends JFrame {

    private IBTextPanel textPanel = new IBTextPanel("Information Panel", false);

    MyFrame(){
        JPanel scrollingWindowDisplayPanel = new JPanel( new GridLayout( 0, 1) );
        scrollingWindowDisplayPanel.add(textPanel);
        JPanel buttonPanel = createButtonPanel();

        getContentPane().add( scrollingWindowDisplayPanel, BorderLayout.CENTER);
        getContentPane().add( buttonPanel, BorderLayout.EAST);
        setSize( 900, 800);
        setTitle( "Sample");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JPanel createButtonPanel(){
        JPanel buttonPanel = new JPanel( new GridLayout( 0, 1) );
        JButton butUpdate15MinEigenScratch = new JButton( "Update 15Min EigenScratch");
        butUpdate15MinEigenScratch.addActionListener(e -> updateEigenScratch("15mins"));
        JButton butShow15MinEigenScratchList = new JButton( "Show 15Min EigenScratch List");
        butShow15MinEigenScratchList.addActionListener(e -> showEigenScratchList("15mins"));
        JButton butUpdate1MinEigenScratch = new JButton( "Update 1Min EigenScratch");
        butUpdate1MinEigenScratch.addActionListener(e -> updateEigenScratch("1min"));
        JButton butShow1MinEigenScratchList = new JButton( "Show 1Min EigenScratch List");
        butShow1MinEigenScratchList.addActionListener(e -> showEigenScratchList("1min"));

        buttonPanel.add( new JPanel() );
        buttonPanel.add(butUpdate15MinEigenScratch);
        buttonPanel.add(butShow15MinEigenScratchList);
        buttonPanel.add(butUpdate1MinEigenScratch);
        buttonPanel.add(butShow1MinEigenScratchList);
        return buttonPanel;
    }

    private void updateEigenScratch(String priceBar){
        EWrapperImpl wrapper = new EWrapperImpl();
        final EClientSocket m_client = wrapper.getClient();
        final EReaderSignal m_signal = wrapper.getSignal();
        m_client.eConnect("localhost", 7496, 0);//Or port=4001 for IBGate and port=7496 for TWS;
        final EReader reader = new EReader(m_client, m_signal);
        reader.start();
        new Thread(() -> {
            while (m_client.isConnected()) {
                m_signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception e) {
                    System.out.println("Exception: "+e.getMessage());
                }
            }
        }).start();
        //Thread.sleep(1000);
        GlobalControl globalControl=new GlobalControlImpl();
        List<String> symbols=new ArrayList<>();

        symbols.add("ES");
        symbols.add("NQ");
        symbols.add("YM");
        symbols.add("RTY");

        String duration="";
        String barSize="";
        if(priceBar=="1min"){
          barSize="1 min";
          duration="1 D";
        }else if(priceBar=="15mins"){
          barSize="15 mins";
          duration="1 M";
        }
        globalControl.dataUpdate(m_client,symbols,duration,barSize,priceBar);
    }
    private void showEigenScratchList(String priceBar){
        InAndOutHandle inAndOutHandle=new InAndOutHandleImpl();
        List<Scratch> eigenScratchList = inAndOutHandle.readScratchFromCSV("E:\\Data\\allEigenScratches"+priceBar+".csv");
        for (Scratch scratch:eigenScratchList){
            textPanel.add(scratch.toString());
        }
    }
}
