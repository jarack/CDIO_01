package dice;
import java.util.Scanner;
import dice.Player;
public class Main {

	public static void main(String[] args) {

		Scanner keyb = new Scanner(System.in);		
		//Velkomst information
		String info = ("Hej. Velkommen til spillet.");
		String info2 = ("Tast \"Hjælp\" for at læse spillet regler eller  \"Start\" for at starte spillet ");

		//Introduktion
		System.out.println(info);
		System.out.println(info2);

		//Instanser der bestemmer om spillet skal spilles
		boolean start = false;
		boolean slut = false;

		//Start valg.
		do {
			String Input = keyb.nextLine();
			Input.toLowerCase();

			switch (Input) {
			case "hjælp":
				showRules();
				break;
			case "start":
				System.out.println("Du har trykket start.");
				start = true;
				break;

			case "slut" :
				System.out.println("Tak fordi du brugte spillet - Farvel");
				slut = true;
				break;
			default :
				System.out.println("Ugyldig Indtastning");
			}
		}
		while ((start || slut) == false);



		//Selve spillet
		while (start) 
		{
			/*Noter
			 * Skal have et loop, der repræsenterer starten af spillet
			 */
			System.out.println("Velkommen til spillet");
			//Spillere

			int numOfPlayers = 2;
			Player.resetPlayerNum();
			Player[] players = initPlayers(numOfPlayers);

			//Terninger
			Dice d1 = new Dice();
			Dice d2 = new Dice();

			//Instanser
			int turn = 0;	//Vælger hvilken spillers tur det er.
			int ekstratur = 1;
			String extraTurnStr = "EKSTRA TUR!";
			boolean lastTurn = false; // så den der tabte kan få en sidste tur
			boolean onlyOneLastTurn = false;
			boolean askToRestart = false;
			boolean genstart = false;


			//Udskriver antallet af point spillerne har


			//Start tekst
			System.out.println("Spiller 1 starter.");



			String gameCommand = "";

			while (start && !genstart ) {

				ekstratur = 1;		// Tæller hvor mange ture spilleren har
				genstart = false;
				//Loop for en spillers runde
				while(ekstratur > 0) 
				{
					System.out.println("\t\t"+players[turn].getowner()+"'s tur\r");
					System.out.println(players[0].getowner() + " har " + players[0].getpoints() + " points");
					System.out.println(players[1].getowner() + " har " + players[1].getpoints() + " points\r");
					System.out.println("Tryk enter for at rafle");
					gameCommand = keyb.nextLine();
					d1.roll();
					d2.roll();
					players[turn].incTimesRolled(); // antallet af spillerens kast gennem hele spillet +1

					System.out.println(players[turn].getowner()+" slog: "+d1.getFaceValue()+" og "+d2.getFaceValue());

					int resultat = d1.getFaceValue()+d2.getFaceValue();
					//Spørger om spilleren har mere end 40 point
					if (players[turn].getpoints() >= 40) 
					{

						//Spørger om terningernes øjne er ens
						if (d1.isEqual(d2)) 
						{

							//Spørger om terningernes øjne er 1'ere
							if (d1.getFaceValue() == 1) 
							{
								players[turn].setpoints(0);
								System.out.println(extraTurnStr);
								System.out.println("Du mistede alle dine point desværre");
								ekstratur++;
							}	

							//Spørger om terningernes øjne er andet end 1'ere
							else if (d1.getFaceValue() != 1) 
							{

								// NOTE, SYSTEMET MANGLER AT UDSKRIVE POINT TIL DETTE STEP
								System.out.println(players[turn].getowner() + " har vundet med "+players[turn].getTimesRolled()+" kast");
								lastTurn = true;
							}			// Slut else if sætning 
						}// slut d1.isEqual(d2) if sætning
						else 
						{
							players[turn].addpoints(resultat);
						}				
					}// slut players points >=40
					else 
					{
						players[turn].addpoints(resultat);
						//Spørger om terningernes øjne er ens
						if (d1.isEqual(d2)) 
						{


							//Spørger om terningernes øjne er 1'ere
							if (d1.getFaceValue() == 1) 
							{
								players[turn].setpoints(0);
								System.out.println(extraTurnStr);
								System.out.println("Du mistede alle dine point desværre");
								ekstratur++;
								//syso? "Du har mistet alle dine points"
							}	

							//Spørger om terningernes øjne er andet end 1'ere
							else if (d1.getFaceValue() == 6) 
							{
								//Checker om sidste kast for spilleren var to seksere
								if(players[turn].lastThrowEqual(d1.getFaceValue(), d2.getFaceValue()))
								{
									System.out.println( players[turn].getowner()+" har vundet med "+ players[turn].getTimesRolled());
									lastTurn = true;
									//
								}
								else
								{
									System.out.println(extraTurnStr);
									ekstratur++;
								}
							}
							else
							{
								System.out.println(extraTurnStr);
								ekstratur++;
							}
						} 


					}
					players[turn].setLastThrow(d1.getFaceValue(),d2.getFaceValue());

					ekstratur--;
					System.out.println("____________________________________________________\r");

					//Slutning af tur.
					//Turen skiftes


				}
				if (ekstratur==0)
				{
					ekstratur = lastTurn? 1:0;

					if(onlyOneLastTurn)
					{
						/// sikrer at, hvis taberen vinder, så bliver turen ikke ved med at skifte
						ekstratur = 0; 
					}
					else if(lastTurn)
					{
						//Sidste chance for at den anden spiller kan få uafgjort
						lastTurn=false;
						askToRestart=true;
						onlyOneLastTurn=true;
						turn++;
					}
					else
					{
						turn++;
					}

				}
				if (turn == numOfPlayers) 
				{
					turn = 0;
				}

				//Spørger om man vil genstarte, eller afslutte spillet
				if(askToRestart && (ekstratur==0))
				{
					System.out.println("---------------------------------");
					boolean validCommand = false;
					while (!validCommand)
					{
						System.out.println("tast 'genstart' for at starte et nyt spil, eller tast 'slut' for at lukke spillet");
						String input = keyb.nextLine();
						input.toLowerCase();
						switch(input)
						{
						case "slut":
							start = false;
							validCommand = true;
							break;
						case "genstart":
							genstart = true;
							validCommand = true;
							break;
						default: 
							System.out.println("Ugyldig indtastning");
							break;

						}
					}

				}		
				// Runder						
			}
		}//Selve spillets while loop.
		keyb.close();
	}


	private static Player[] initPlayers(int numOfPlayers)
	{
		/// opretter spillere med selvvalgte navne, returnere en spiller array.
		/// er static så man ikke behøver at instantiere et objekt for at bruge metoden
		Player players[] = new Player[numOfPlayers];
		Scanner keyb = new Scanner(System.in);
		String name = "";
		for(int i = 0 ; i<numOfPlayers; i++)
		{
			System.out.println("Indtast navn på Spiller_"+(i+1));
			name = keyb.nextLine();
			if(name.length()<1)
			{
				name = "Spiller_"+Player.getPlayerNum();
			}
			while(nameTaken(players,name,i))
			{
				System.out.println("Navnet er taget - indtast venligst et nyt");
				name = keyb.nextLine();
				if(name.length()<1)
				{
					name="Spiller_"+Player.getPlayerNum();
				}

			}
			players[i] = new Player(name);			
		}
		return players;
	}

	private static boolean nameTaken(Player[] playerArr, String newName, int numOfElements)
	{
		//Checker om et navn er taget, hvor numOfElements er hvor mange spillere -
		//- der er i spiller arrayet indtil videre
		for(int i = 0; i<numOfElements;i++)
		{
			if(newName.equals(playerArr[i].getowner()))
			{
				return true;
			}
		}
		return false;

	}

	private static void showRules()
	{
		// Regler er vigtige
		System.out.println();
		System.out.println("Velkommen til terningspil 2000 supreme deluxe edition");
		System.out.println("Reglerne er følgende:");
		System.out.println("\t 1. Slår du fire 6'ere i streg vinder du");
		System.out.println("\t 2. Slår du to ens, mens dine point er 40 eller over vinder du");
		System.out.println("\t 3. Slår du to 1'ere, mister du alle dine point, også selvom du har 40 point eller derover");
		System.out.println("\t 4. Slår du to ens på nogen andre tidspunkter, får du en ekstratur");
		System.out.println();
	}

}



