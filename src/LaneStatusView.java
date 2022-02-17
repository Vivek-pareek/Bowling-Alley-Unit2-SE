/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class LaneStatusView implements EventObserver {

	private JPanel jp;

	private JLabel curBowler, foul, pinsDown;
	private JButton viewLane, viewPinSetter, maintenance;

	private PinSetterView psv;
	private LaneView lv;
	private Lane lane;
	int laneNum;

	boolean laneShowing;
	boolean psShowing;

	public LaneStatusView(Lane lane, int laneNum ) {

		this.lane = lane;
		this.laneNum = laneNum;

		laneShowing=false;
		psShowing=false;

		psv = new PinSetterView( laneNum );
		Pinsetter ps = lane.getPinsetter();
		ps.subscribe(psv);

		lv = new LaneView( lane, laneNum );
		lane.subscribe(lv);


		jp = new JPanel();
		jp.setLayout(new FlowLayout());
		JLabel cLabel = new JLabel( "Now Bowling: " );
		curBowler = new JLabel( "(no one)" );
		JLabel fLabel = new JLabel( "Foul: " );
		foul = new JLabel( " " );
		JLabel pdLabel = new JLabel( "Pins Down: " );
		pinsDown = new JLabel( "0" );

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		viewLane = new JButton("View Lane");
		JPanel viewLanePanel = new JPanel();
		viewLanePanel.setLayout(new FlowLayout());
		viewLane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if ( lane.isPartyAssigned() ) {
					if ( laneShowing == false ) {
						lv.show();
						laneShowing=true;
					} else if ( laneShowing == true ) {
						lv.hide();
						laneShowing=false;
					}
				}
			}
		});
		viewLanePanel.add(viewLane);

		viewPinSetter = new JButton("Pinsetter");
		JPanel viewPinSetterPanel = new JPanel();
		viewPinSetterPanel.setLayout(new FlowLayout());
		viewPinSetter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if ( lane.isPartyAssigned() ) {

						if ( psShowing == false ) {
							psv.show();
							psShowing=true;
						} else if ( psShowing == true ) {
							psv.hide();
							psShowing=false;
						}

				}
			}
		});
		viewPinSetterPanel.add(viewPinSetter);

		maintenance = new JButton("     ");
		maintenance.setBackground( Color.GREEN );
		JPanel maintenancePanel = new JPanel();
		maintenancePanel.setLayout(new FlowLayout());
		maintenance.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if ( lane.isPartyAssigned() ) {
					lane.unPauseGame();
					maintenance.setBackground( Color.GREEN );
				}
			}
		});
		maintenancePanel.add(maintenance);

		viewLane.setEnabled( false );
		viewPinSetter.setEnabled( false );


		buttonPanel.add(viewLanePanel);
		buttonPanel.add(viewPinSetterPanel);
		buttonPanel.add(maintenancePanel);

		jp.add( cLabel );
		jp.add( curBowler );
//		jp.add( fLabel );
//		jp.add( foul );
		jp.add( pdLabel );
		jp.add( pinsDown );
		
		jp.add(buttonPanel);

	}

	public JPanel showLane() {
		return jp;
	}


	public void receiveEvent(Object eventObject){
		if(eventObject instanceof Pinsetter){
			pinsDown.setText(Integer.toString(((Pinsetter) eventObject).totalPinsDown()));
		}
		else if(eventObject instanceof Lane){
			curBowler.setText( ( ((Lane)eventObject).getBowler()).getNick() );
			if ( ((Lane)eventObject).isMechanicalProblem() ) {
				maintenance.setBackground( Color.RED );
			}
			if (!lane.isPartyAssigned()) {
				viewLane.setEnabled( false );
				viewPinSetter.setEnabled( false );
			} else {
				viewLane.setEnabled( true );
				viewPinSetter.setEnabled( true );
			}
		}
	}

}
